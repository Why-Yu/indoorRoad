package com.indoor.navigation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indoor.navigation.algorithm.FindPath;
import com.indoor.navigation.algorithm.datastructure.Node;
import com.indoor.navigation.algorithm.datastructure.TopologyNetwork;
import com.indoor.navigation.entity.*;
import com.indoor.navigation.service.IndoorEdgeService;
import com.indoor.navigation.service.IndoorVertexService;
import com.indoor.navigation.util.ShapeReader;
import com.indoor.navigation.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HaoYu
 * @description Controller
 * @date 2021/05/24
 */
@RestController
public class IndoorController {
    private final static Logger logger = LoggerFactory.getLogger(IndoorController.class);
    @Autowired
    IndoorVertexService vertexService;
    @Autowired
    IndoorEdgeService edgeService;
    @Autowired
    ShapeReader shpReader;
    @Autowired
    FindPath findPath;


    /*
     * description: saveShp <br>
     * date: 2021/5/28 21:24 <br>
     * author: HaoYu <br>
     * @param filePath 储存shapeFile文件的目录，并且子目录的编写需要符合一定格式
     * @return java.lang.String
     */
    @RequestMapping(value="/saveShp")
    @CrossOrigin
    public String saveShp(@RequestBody JSONObject jsonParam) {
        ArrayList<ShapeModel> modelList = new ArrayList<>();
        if (jsonParam.getString("floor") == null) {
            modelList = shpReader.readShapeFile(jsonParam.getString("filePath"));
        } else {
            modelList = shpReader.readSingleShapeFile(jsonParam.getString("filePath"), jsonParam.getString("floor"));
        }
        ArrayList<Vertex> vertexList = new ArrayList<>();
        ArrayList<Edge> edgeList = new ArrayList<>();
        String startGlobalIndex;
        String endGlobalIndex;
        Integer floor;
        for (ShapeModel model : modelList) {
            startGlobalIndex = model.getFloor() + "-" + model.getBeginId();
            endGlobalIndex = model.getFloor() + "-" + model.getEndId();
            // 读取的shapeModel中的floor是String这里转换为Integer
            floor = Integer.parseInt(model.getFloor());
            Vertex vertexBegin = new Vertex(startGlobalIndex, model.getBeginId(),
                    floor, model.getBeginX(), model.getBeginY());
            vertexList.add(vertexBegin);
            Vertex vertexEnd = new Vertex(endGlobalIndex, model.getEndId(),
                    floor, model.getEndX(), model.getEndY());
            vertexList.add(vertexEnd);
            double diffX = Math.abs(model.getBeginX() - model.getEndX());
            double diffY = Math.abs(model.getBeginY() - model.getEndY());
            Edge edge = new Edge(startGlobalIndex, endGlobalIndex, Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)));
            edgeList.add(edge);
        }
        // 有重复的没关系，insert的时候主键一致，数据库应该会自动覆盖
        vertexService.saveAll(vertexList);
        edgeService.saveAll(edgeList);
        logger.info("成功导入目录下的所有road.shp");
        return "ok";
    }

    /**
     * description: getShortestPath
     * 每一次访问都重新构建拓扑网络，目的是为了之后能对不同建筑物进行导航，
     * 而不是Springboot默认的单例模式，重复在一个network上添加<br>
     * date: 2021/5/28 22:10 <br>
     * author: HaoYu <br>
     * @return java.lang.String
     */
    @RequestMapping(value="/getShortestPath")
    @CrossOrigin
    public String getShortestPath(@RequestBody TransmissionNode paramsNode){
        //每次都从spring容器中拿出一个新的TopologyNetwork的实例
        TopologyNetwork network = SpringContextUtil.getBean(TopologyNetwork.class);
        for (Vertex vertex : vertexService.findAll()) {
            network.insertVertex(vertex.getGlobalIndex(), vertex.getFloor(), vertex.getX(), vertex.getY());
        }
        for (Edge edge : edgeService.findAll()) {
            network.insertEdge(edge.getStartIndex(), edge.getEndIndex(), edge.getWeight());
        }
        // ******层与层之间的联通关系现在只能手动添加
        for (int i = 8; i < 22; i++) {
            String floorDown = Integer.toString(i);
            String floorUp = Integer.toString(i + 1);
            network.insertEdge(floorDown + "-0", floorUp + "-0", 5);
        }
        // ******

        findPath.changeNetwork(network);
        findPath.setStartNode(paramsNode.getStartFloor(), paramsNode.getStartX(), paramsNode.getStartY());
        findPath.setEndNode(paramsNode.getEndFloor(), paramsNode.getEndX(), paramsNode.getEndY());
        List<Node> pathList = findPath.getShortestPath();
        // 必须用List这样能保证输出结果的顺序的正确
        ArrayList<ResultNode> resultNodeList = new ArrayList<>();
        for (Node pathNode : pathList)
        {
            resultNodeList.add(new ResultNode(pathNode.dataIndex, pathNode.floor, pathNode.x, pathNode.y)) ;
        }
        logger.info("成功获得最短路径");
        return JSON.toJSONString(resultNodeList);
    }

    @RequestMapping(value = "/test")
    @CrossOrigin
    public String test (@RequestBody JSONObject jsonParam) {
        if (jsonParam.getString("floor") == null) {
            return "null";
        } else {
            return "ok";
        }
    }
}
