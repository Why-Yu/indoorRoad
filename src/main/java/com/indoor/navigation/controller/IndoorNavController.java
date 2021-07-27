package com.indoor.navigation.controller;

import com.alibaba.fastjson.JSON;
import com.indoor.navigation.algorithm.FindPath;
import com.indoor.navigation.algorithm.datastructure.MinHeap;
import com.indoor.navigation.algorithm.datastructure.Node;
import com.indoor.navigation.algorithm.datastructure.TopologyNetwork;
import com.indoor.navigation.entity.database.BoundingBox;
import com.indoor.navigation.entity.database.ChangeVertex;
import com.indoor.navigation.entity.database.Edge;
import com.indoor.navigation.entity.database.Vertex;
import com.indoor.navigation.entity.util.*;
import com.indoor.navigation.service.IndoorBoundingBoxService;
import com.indoor.navigation.service.IndoorChangeVertexService;
import com.indoor.navigation.service.IndoorEdgeService;
import com.indoor.navigation.service.IndoorVertexService;
import com.indoor.navigation.util.ChangeType;
import com.indoor.navigation.util.MercatorToLonLat;
import com.indoor.navigation.util.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author HaoYu
 * @description 导航有关的接口
 * @date 2021/06/23
 */
@Api(tags = "IndoorNavController", description = "导航测试接口")
@RestController
@RequestMapping("/nav")
public class IndoorNavController {
    private final static Logger logger = LoggerFactory.getLogger(IndoorNavController.class);
    @Autowired
    IndoorVertexService vertexService;
    @Autowired
    IndoorEdgeService edgeService;
    @Autowired
    IndoorChangeVertexService changeVertexService;
    @Autowired
    IndoorBoundingBoxService boundingBoxService;

    /**
     * description: 为了能与室外导航整合，例如输入两个位置，获得两个POI的坐标，
     * TransmissionNode里navFromOut表示方向
     * true表示室外到室内，false表示室内到室外<br>
     * 例{
     * "startFloor": 1, (室外坐标，楼层统一用1表示，室外POI的经纬度)
     * "startX": 114.45,
     * "startY": 30.30,
     * "endFloor": 18,  (室内坐标，如泛海写字楼POI的经纬度，和需要去往的楼层)
     * "endX": 114.24,
     * "endY": 30.60,
     * "navFromOut":true
     * }<br>
     * 每一次访问都重新构建拓扑网络，目的是为了之后能对不同建筑物进行导航，
     * 而不是Springboot默认的单例模式，重复在一个network上添加<br>
     * date: 2021/5/28 22:10 <br>
     * author: HaoYu <br>
     *
     * @return java.lang.String
     */
    @ApiOperation("获取室内外一体化路径的室内路径部分")
    @RequestMapping(value = "/getPathAll", method = RequestMethod.POST)
    @CrossOrigin
    public String getPathAll(@RequestBody TransmissionNode paramsNode) {
        //每次都从spring容器中拿出一个新的FindPath和TopologyNetwork的实例,因为springboot默认是单例
        FindPath findPath = SpringContextUtil.getBean(FindPath.class);
        TopologyNetwork network = SpringContextUtil.getBean(TopologyNetwork.class);

        Mercator outdoorMercator;
        Mercator indoorMercator;
        // 室内点落在哪个建筑物的范围内,则buildId就是此建筑物的序号
        String buildId = "1";
        boolean hasFindBuildId = false;
        // 从室外到室内
        if (paramsNode.isNavFromOut()) {
            outdoorMercator = MercatorToLonLat.lonLatToMercator(paramsNode.getStartX(), paramsNode.getStartY());
            indoorMercator = MercatorToLonLat.lonLatToMercator(paramsNode.getEndX(), paramsNode.getEndY());
        } else { // 从室内到室外
            outdoorMercator = MercatorToLonLat.lonLatToMercator(paramsNode.getEndX(), paramsNode.getEndY());
            indoorMercator = MercatorToLonLat.lonLatToMercator(paramsNode.getStartX(), paramsNode.getStartY());
        }
        for (BoundingBox boundingBox : boundingBoxService.findAll()) {
            if (indoorMercator.mercatorInBox(boundingBox)) {
                buildId = boundingBox.getBuildId();
                hasFindBuildId = true;
                break;
            }
        }
        if (!hasFindBuildId) {
            logger.info("输入的室内坐标对应不上数据库中的任何一个建筑物范围");
            return "室内坐标无法对应到建筑物";
        }

        // !!!!!! 其实使用一张shape_model表就好了，不需要两张表，不知道当初怎么想的，但暂时先不改吧
        for (Vertex vertex : vertexService.findByBuildId(buildId)) {
            network.insertVertex(vertex.getGlobalIndex(), vertex.getFloor(), vertex.getX(), vertex.getY());
        }
        for (Edge edge : edgeService.findByBuildId(buildId)) {
            network.insertEdge(edge.getStartIndex(), edge.getEndIndex(), edge.getWeight());
        }
        // ******层与层之间的联通关系，在change_vertex表中只存储了向上的部分
        // 又由于此，导致wrapPath时，处于最顶层的那个转换节点没有记录，就很尴尬
        List<ChangeType> changeTypes = Arrays.asList(ChangeType.elevator, ChangeType.escalator, ChangeType.stairs);
        for (ChangeVertex cv : changeVertexService.findByChangeTypeInAndBuildId(changeTypes, buildId)) {
            if (cv.getUpGlobalIndex() != null) {
                network.insertEdge(cv.getGlobalIndex(), cv.getUpGlobalIndex(), cv.getChangeType().ordinal() * 2);
            }
        }
        // ******
        // 导入室内拓扑网络
        findPath.changeNetwork(network);

        List<Node> pathList = new ArrayList<>();
        MinHeap<Node> minHeap = findDoorVertex(outdoorMercator, buildId);
        while (pathList.isEmpty() && minHeap.size() > 0) {
            Node doorNode = minHeap.delMin();
            if (paramsNode.isNavFromOut()) {
                // 从室外到室内
                // 最后返回的路径是从后追溯到前的，所以这里反过来注入，最后返回的路径就是正确顺序的
                findPath.setStartNode(paramsNode.getEndFloor(), indoorMercator.getX(), indoorMercator.getY());
                findPath.setEndNode(paramsNode.getStartFloor(), doorNode.x, doorNode.y);
            } else {
                // 从室内到室外
                findPath.setStartNode(paramsNode.getEndFloor(), doorNode.x, doorNode.y);
                findPath.setEndNode(paramsNode.getStartFloor(), indoorMercator.getX(), indoorMercator.getY());
            }
            pathList = findPath.getShortestPath();
            findPath.refresh();
        }
        // 对最短路径进行分段，并添加信息
        if (pathList.isEmpty() && minHeap.size() == 0) {
            return "{\"message\": \"输入的室内点应该是孤立点，故无法找到路径\"}";
        }
        ArrayList<WrapResultNode> wrapResultNodes = wrapPath(pathList, paramsNode.isNavFromOut());

        logger.info("成功获得室内最短路径");
        return JSON.toJSONString(wrapResultNodes);
    }

    @ApiOperation("单纯的只在室内导航，比如从第二层到第五层")
    @RequestMapping(value = "/getPathOnlyIndoor", method = RequestMethod.POST)
    @CrossOrigin
    public String getPathOnlyIndoor(@RequestBody TransmissionNode paramsNode) {
        //每次都从spring容器中拿出一个新的FindPath和TopologyNetwork的实例,因为springboot默认是单例
        FindPath findPath = SpringContextUtil.getBean(FindPath.class);
        TopologyNetwork network = SpringContextUtil.getBean(TopologyNetwork.class);

        Mercator startMercator = MercatorToLonLat.lonLatToMercator(paramsNode.getStartX(), paramsNode.getStartY());
        Mercator endMercator = MercatorToLonLat.lonLatToMercator(paramsNode.getEndX(), paramsNode.getEndY());
        String buildId = "1"; // 室内点落在哪个建筑物的范围内,则buildId就是此建筑物的序号
        boolean hasFindBuildId = false;

        for (BoundingBox boundingBox : boundingBoxService.findAll()) {
            if (startMercator.mercatorInBox(boundingBox)) {
                buildId = boundingBox.getBuildId();
                hasFindBuildId = true;
                break;
            }
        }
        if (!hasFindBuildId) {
            logger.info("输入的室内坐标对应不上数据库中的任何一个建筑物范围");
            return "室内坐标无法对应到建筑物";
        }

        for (Vertex vertex : vertexService.findByBuildId(buildId)) {
            network.insertVertex(vertex.getGlobalIndex(), vertex.getFloor(), vertex.getX(), vertex.getY());
        }
        for (Edge edge : edgeService.findByBuildId(buildId)) {
            network.insertEdge(edge.getStartIndex(), edge.getEndIndex(), edge.getWeight());
        }

        List<ChangeType> changeTypes = Arrays.asList(ChangeType.elevator, ChangeType.escalator, ChangeType.stairs);
        for (ChangeVertex cv : changeVertexService.findByChangeTypeInAndBuildId(changeTypes, buildId)) {
            if (cv.getUpGlobalIndex() != null) {
                network.insertEdge(cv.getGlobalIndex(), cv.getUpGlobalIndex(), cv.getChangeType().ordinal() * 2);
            }
        }
        // ******
        // 导入室内拓扑网络
        findPath.changeNetwork(network);

        // 最后返回的路径是从后追溯到前的，所以这里反过来注入，最后返回的路径就是正确顺序的
        findPath.setStartNode(paramsNode.getEndFloor(), endMercator.getX(), endMercator.getY());
        findPath.setEndNode(paramsNode.getStartFloor(), startMercator.getX(), startMercator.getY());

        List<Node> pathList = findPath.getShortestPath();
        if (pathList.isEmpty()) {
            return "{\"message\":\"输入的起始点或者终止点是一个孤立点\"}";
        }
        ArrayList<WrapResultNode> wrapResultNodes = wrapPath(pathList, paramsNode.isNavFromOut());

        logger.info("成功获得纯室内最短路径");
        return JSON.toJSONString(wrapResultNodes);
    }

    /**
     * description: 寻找室内外的转换点，这里选择离室外目标尽可能近但又同时能抵达目的地的那个门 <br>
     * date: 2021/6/23 17:46 <br>
     * author: HaoYu <br>
     *
     * @param mercator 墨卡托坐标
     * @return com.indoor.navigation.entity.database.Vertex
     */
    private MinHeap<Node> findDoorVertex(Mercator mercator, String buildId) {
        MinHeap<Node> minHeap = new MinHeap<>(Node.class);
        Vertex tempDoorVertex;
        double absolute;
        List<ChangeType> changeTypes = Collections.singletonList(ChangeType.door);
        for (ChangeVertex cv : changeVertexService.findByChangeTypeInAndBuildId(changeTypes, buildId)) {
            tempDoorVertex = vertexService.findByGlobalIndex(cv.getGlobalIndex());
            absolute = Math.abs(mercator.getX() - tempDoorVertex.getX()) + Math.abs(mercator.getY() - tempDoorVertex.getY());
            Node tempNode = new Node(cv.getGlobalIndex(), 1, tempDoorVertex.getX(), tempDoorVertex.getY(), absolute);
            tempNode.total = absolute;
            minHeap.add(tempNode);
        }
        return minHeap;
    }

    @Deprecated
    private ArrayList<WrapResultNode> splitPath(List<Node> pathList) {
        // 必须用List这样能保证输出结果的顺序的正确
        ArrayList<WrapResultNode> wrapResultNodes = new ArrayList<>();
        int startFloor = pathList.get(0).floor;
        // 保证列表中始终有一个，防止空指针
        wrapResultNodes.add(new WrapResultNode());
        wrapResultNodes.get(wrapResultNodes.size() - 1).setFloor(startFloor);
        WrapResultNode wrapResultNode;
        LonLat lonLat;
        ChangeVertex cv;
        for (Node pathNode : pathList) {
            cv = changeVertexService.findByGlobalIndex(pathNode.dataIndex);
            lonLat = MercatorToLonLat.mercatorToLonLat(pathNode.x, pathNode.y);
            if (cv != null) {
                if (cv.getChangeType() != ChangeType.door) {
                    // 检测到是转换点，并且是电梯扶梯之类
                    wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
                    wrapResultNode.getResultNodes().add(
                            new ResultNode(pathNode.dataIndex, pathNode.floor, lonLat.getLon(), lonLat.getLat()));
                    wrapResultNode.addMessage("  to:" + cv.getChangeType().toString() + pathNode.dataIndex);

                    // 开启下一段路径，但要注意避免在第一层中重复添加
                    if (pathNode.floor != startFloor) {
                        wrapResultNodes.add(new WrapResultNode());
                        wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
                        wrapResultNode.getResultNodes().add(
                                new ResultNode(pathNode.dataIndex, pathNode.floor, lonLat.getLon(), lonLat.getLat()));
                        wrapResultNode.setFloor(pathNode.floor);
                        wrapResultNode.addMessage("from:" + cv.getChangeType().toString() + pathNode.dataIndex);
                    }
                } else {
                    // 检测到是转换点，并且是建筑物出入门
                    wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
                    wrapResultNode.getResultNodes().add(
                            new ResultNode(pathNode.dataIndex, pathNode.floor, lonLat.getLon(), lonLat.getLat()));
                    wrapResultNode.setFloor(pathNode.floor);
                    wrapResultNode.addMessage("from:" + cv.getChangeType().toString() + pathNode.dataIndex);
                }
            } else {
                wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
                wrapResultNode.getResultNodes().add(
                        new ResultNode(pathNode.dataIndex, pathNode.floor, lonLat.getLon(), lonLat.getLat()));
            }
        }
        return wrapResultNodes;
    }

    private ArrayList<WrapResultNode> wrapPath(List<Node> pathList, boolean navFromOut) {
        ArrayList<WrapResultNode> wrapResultNodes = new ArrayList<>();
        int currentFloor = pathList.get(0).floor;
        LonLat lonLat;
        String toIndex;
        ChangeVertex cv;
        String currentChange;

        wrapResultNodes.add(new WrapResultNode());
        WrapResultNode wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
        wrapResultNode.setFloor(currentFloor);
        cv = changeVertexService.findByGlobalIndex(pathList.get(0).dataIndex);
        wrapResultNode.addMessage(messageBuilder("from:", cv, pathList.get(0).dataIndex) + ";");

        for (Node pathNode : pathList) {
            lonLat = MercatorToLonLat.mercatorToLonLat(pathNode.x, pathNode.y);
            if (pathNode.floor != currentFloor) {
                // 先对上一层的末尾进行处理
                toIndex = wrapResultNode.getResultNodes().get(wrapResultNode.getResultNodes().size() - 1).getDataIndex();
                cv = changeVertexService.findByGlobalIndex(toIndex);
                wrapResultNode.addMessage(messageBuilder("to:", cv, toIndex));
                // 然后再对下一层楼开头
                wrapResultNodes.add(new WrapResultNode());
                wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
                wrapResultNode.setFloor(pathNode.floor);
                cv = changeVertexService.findByGlobalIndex(pathNode.dataIndex);
                wrapResultNode.addMessage(messageBuilder("from:", cv,  pathNode.dataIndex) + ";");
                // 更新currentFloor
                currentFloor = pathNode.floor;
            }
            wrapResultNode.getResultNodes().add(
                    new ResultNode(pathNode.dataIndex, pathNode.floor, lonLat.getLon(), lonLat.getLat()));
        }

        toIndex = wrapResultNode.getResultNodes().get(wrapResultNode.getResultNodes().size() - 1).getDataIndex();
        cv = changeVertexService.findByGlobalIndex(toIndex);
        wrapResultNode.addMessage(messageBuilder("to:", cv,  toIndex));

        //*** 这一块代码的目的是解决建筑物最顶层数据的转换点缺失，导致只能输出normal的问题(只当路径经过最顶层才会出现)
        //*** 所以在这里依据navFromOut强制重新把顶层到次顶层的那个message修改一下(但要注意路径起码要经过两层)
        if (wrapResultNodes.size() > 1) {
            if (navFromOut) {
                wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 2);
                String message = wrapResultNode.getMessage();
                currentChange = message.substring(message.lastIndexOf(":"), message.lastIndexOf("|"));

                wrapResultNode = wrapResultNodes.get(wrapResultNodes.size() - 1);
                StringBuilder builder = new StringBuilder(wrapResultNode.getMessage());
                builder.replace(builder.indexOf(":"), builder.indexOf("|"), currentChange);
                wrapResultNode.setMessage(builder.toString());

            } else {
                wrapResultNode = wrapResultNodes.get(1);
                String message = wrapResultNode.getMessage();
                currentChange = message.substring(message.indexOf(":"), message.indexOf("|"));

                wrapResultNode = wrapResultNodes.get(0);
                StringBuilder builder = new StringBuilder(wrapResultNode.getMessage());
                builder.replace(builder.lastIndexOf(":"), builder.lastIndexOf("|"), currentChange);
                wrapResultNode.setMessage(builder.toString());
            }
        }
        return wrapResultNodes;
    }

    private String messageBuilder(String direction, ChangeVertex cv, String dataIndex) {
        StringBuilder builder = new StringBuilder();
        if (cv != null) {
            builder.append(direction)
                    .append(cv.getChangeType().toString())
                    .append("|")
                    .append(dataIndex);
        } else {
            builder.append(direction)
                    .append("normal|")
                    .append(dataIndex);
        }
        return builder.toString();
    }
}
