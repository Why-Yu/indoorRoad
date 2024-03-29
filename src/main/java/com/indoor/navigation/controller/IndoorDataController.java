package com.indoor.navigation.controller;

import com.alibaba.fastjson.JSONObject;
import com.indoor.navigation.entity.database.*;
import com.indoor.navigation.entity.util.LonLat;
import com.indoor.navigation.entity.util.ResultShapeModel;
import com.indoor.navigation.service.*;
import com.indoor.navigation.util.ExcelReader;
import com.indoor.navigation.util.MercatorToLonLat;
import com.indoor.navigation.util.ShapeReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author HaoYu
 * @description 和数据有关的接口
 * @date 2021/05/24
 */
@Api(tags = "IndoorDataController", description = "数据测试接口")
@RestController
@RequestMapping("/data")
public class IndoorDataController {
    private final static Logger logger = LoggerFactory.getLogger(IndoorDataController.class);
    @Autowired
    IndoorVertexService vertexService;
    @Autowired
    IndoorEdgeService edgeService;
    @Autowired
    IndoorModelService modelService;
    @Autowired
    IndoorChangeVertexService changeVertexService;
    @Autowired
    IndoorBoundingBoxService boundingBoxService;
    @Autowired
    ShapeReader shpReader;


    /**
     * description: 将shapefile文件中的属性导入库，以后可以考虑利用the_geom的WKT格式的数据导入postGis中
     * 再利用矩形查找，获得离用户输入点最近的那条线的垂点作为计算的起终点 <br>
     * date: 2021/5/28 21:24 <br>
     * author: HaoYu <br>
     *
     * @param jsonParam 储存shapeFile文件的目录，并且子目录的编写需要符合一定格式
     *                  必须包含buildId(楼栋编号)以及filePath(文件的目录)
     * @return java.lang.String
     */
    @ApiOperation("shapefile文件导入库，直接输入目录即可")
    @RequestMapping(value = "/saveShp", method = RequestMethod.POST)
    @CrossOrigin
    public String saveShp(@ApiParam(name = "filePath", value = "文件目录")
                          @RequestBody JSONObject jsonParam) {
        File folder = new File(jsonParam.getString("filePath"));
        int globalBuildId = 1;
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file: files) {
                    ArrayList<ShapeModel> modelList;
                    String buildId = Integer.toString(globalBuildId);
                    globalBuildId += 1;
                    if (jsonParam.getString("floor") == null) {
                        modelList = shpReader.readShapeFile(file.getPath());
                    } else {
                        modelList = shpReader.readSingleShapeFile(file.getPath(), jsonParam.getString("floor"));
                    }
                    ArrayList<Vertex> vertexList = new ArrayList<>();
                    ArrayList<Edge> edgeList = new ArrayList<>();
                    StringBuilder startGlobalIndex = new StringBuilder();
                    StringBuilder endGlobalIndex = new StringBuilder();
                    Integer floor;

                    for (ShapeModel model : modelList) {
                        startGlobalIndex.append(buildId).append("-").append(model.getFloor()).append("-").append(model.getBeginId());
                        endGlobalIndex.append(buildId).append("-").append(model.getFloor()).append("-").append(model.getEndId());
                        model.setBeginId(startGlobalIndex.toString());
                        model.setEndId(endGlobalIndex.toString());
                        model.setBuildId(buildId);
                        // 读取的shapeModel中的floor是String这里转换为Integer
                        floor = Integer.parseInt(model.getFloor());

                        Vertex vertexBegin = new Vertex(startGlobalIndex.toString(), buildId,
                                floor, model.getBeginX(), model.getBeginY());
                        vertexList.add(vertexBegin);

                        Vertex vertexEnd = new Vertex(endGlobalIndex.toString(), buildId,
                                floor, model.getEndX(), model.getEndY());
                        vertexList.add(vertexEnd);
                        double diffX = Math.abs(model.getBeginX() - model.getEndX());
                        double diffY = Math.abs(model.getBeginY() - model.getEndY());
                        Edge edge = new Edge(buildId, startGlobalIndex.toString(), endGlobalIndex.toString(),
                                Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)));
                        edgeList.add(edge);
                        // 每次需要把StringBuilder清空，以备下次使用
                        startGlobalIndex.setLength(0);
                        endGlobalIndex.setLength(0);
                    }
                    // 有重复的没关系，insert的时候主键一致，数据库应该会自动覆盖
                    modelService.saveAll(modelList);
                    vertexService.saveAll(vertexList);
                    edgeService.saveAll(edgeList);

                    ArrayList<Double> lonList = new ArrayList<>();
                    ArrayList<Double> latList = new ArrayList<>();
                    for (Vertex vertex : vertexService.findByBuildId(buildId)) {
                        lonList.add(vertex.getX());
                        latList.add(vertex.getY());
                    }
                    Double maxLon = Collections.max(lonList);
                    Double minLon = Collections.min(lonList);
                    Double maxLat = Collections.max(latList);
                    Double minLat = Collections.min(latList);

                    boundingBoxService.save(new BoundingBox(buildId, minLon, maxLon, minLat, maxLat));

                    ArrayList<ChangeVertex> CVList = new ArrayList<>();
                    if (file.isDirectory()) {
                        File[] interiorFiles = file.listFiles();
                        if (interiorFiles != null){
                            for (File interiorFile : interiorFiles) {
                                if (interiorFile.getName().endsWith(".xlsx")) {
                                    try {
                                        CVList = ExcelReader.read(interiorFile.getPath(), buildId);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    changeVertexService.saveAll(CVList);
                }
            }
        }
        logger.info("成功导入目录下的所有road.shp以及xlsx，并把相关数据处理后入库");
        return "处理完成";
    }


    @ApiOperation("获取室内数据接口")
    @GetMapping(value = "/shapeFindAll")
    @CrossOrigin
    public List<ResultShapeModel> getTrimShape() {
        return modelService.findAllTrimModel();
    }

    @ApiOperation("分页获取室内数据的接口")
    @GetMapping(value = "/shapeFindAll/{page}/{size}")
    @CrossOrigin
    public Page<ShapeModel> getPageShape(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        // 使用Jpa封装好的page方法page-1是因为数组从0开始的,前端传过来第1页实际上是数组的第0页
        Pageable pageable = PageRequest.of(page - 1, size);
        return modelService.findAll(pageable);
    }

    /**
     * 查询用户分页并且绑定id字段来进行正序逆序排序
     *
     * @param page
     * @param size
     * @param sortType
     * @param sortableFields
     * @return
     */
    @ApiOperation("分页且排序获取室内数据的接口")
    @GetMapping("/shapeFindAllSort/{page}/{size}/{sortType}/{sortableFields}")
    @CrossOrigin
    public Page<ShapeModel> getPageShapeSortable(
            @PathVariable("page") Integer page, // 第几页
            @PathVariable("size") Integer size, // 显示多少条
            @PathVariable("sortType") String sortType, // 正序还是逆序
            @PathVariable("sortableFields") String sortableFields //需要按照哪一个字段域来排序
    ) {
        //判断排序类型及排序字段
        Sort sort = "ascending".equals(sortType) ? Sort.by(Sort.Direction.ASC, sortableFields) : Sort.by(Sort.Direction.DESC, sortableFields);
        //获取pageable
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return modelService.findAll(pageable);
    }

    /**
     * description: saveExtra存储转换点以及门等额外边缘信息 <br>
     * date: 2021/6/22 22:14 <br>
     * author: HaoYu <br>
     *
     * @param
     * @return java.lang.String
     */
    @ApiOperation("存储额外边缘信息")
    @RequestMapping(value = "/saveExtra", method = RequestMethod.POST)
    @CrossOrigin
    public String saveExtra(@RequestBody List<ChangeVertex> changeVertexList) {
        // 添加楼层之间的额外边缘
//        for (int i = 8; i < 22; i++) {
//            String floor = Integer.toString(i);
//            String floorUp = Integer.toString(i + 1);
//            changeVertexList.add(new ChangeVertex("1-" + floor + "-35", "1-" + floorUp + "-35",
//                    ChangeType.elevator, "1"));
//            changeVertexList.add(new ChangeVertex("1-" + floor + "-38", "1-" + floorUp + "-38",
//                    ChangeType.elevator, "1"));
//        }
//        // 添加建筑物的出入口信息
//        changeVertexList.add(new ChangeVertex("1-8-2", null, ChangeType.door, "1"));
//        changeVertexList.add(new ChangeVertex("1-8-9", null, ChangeType.door, "1"));
//        changeVertexService.saveAll(changeVertexList);
        return "接口已废弃，无需调用";
    }

    @ApiOperation("测试接口，测试部分功能时使用")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @CrossOrigin
    public String test(@RequestBody JSONObject jsonParam) {
        // List<Edge> edgeList = edgeService.findByStartIndex(jsonParam.getString("startIndex"));
//        List<ResultShapeModel> trimModelList = modelService.findAllTrimModel();
//        return JSON.toJSONString(trimModelList);
        LonLat lonLat = MercatorToLonLat.mercatorToLonLat(jsonParam.getDouble("x"), jsonParam.getDouble("y"));
        return lonLat.toString();
//        Mercator mercator = MercatorToLonLat.lonLatToMercator(jsonParam.getDouble("x"), jsonParam.getDouble("y"));
//        return mercator.toString();
//        String buildId = "1";
//        List<ChangeType> changeTypes = Arrays.asList(ChangeType.elevator, ChangeType.escalator, ChangeType.stairs);
//        for(ChangeVertex cv : changeVertexService.findByChangeTypeInAndBuildId(changeTypes, buildId)) {
//                System.out.println(cv.getGlobalIndex());
//        }
//        return "ok";
    }
}
