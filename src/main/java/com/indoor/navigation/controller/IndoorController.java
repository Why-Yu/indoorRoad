package com.indoor.navigation.controller;

import com.indoor.navigation.entity.IndoorRoad;
import com.indoor.navigation.entity.ShapeModel;
import com.indoor.navigation.service.IndoorMapService;
import com.indoor.navigation.service.IndoorRoadService;
import com.indoor.navigation.util.ShapeReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author HaoYu
 * @description Controller
 * @date 2021/05/24
 */
@RestController
public class IndoorController {
    @Autowired
    IndoorMapService service;
    @Autowired
    ShapeReader shpReader;

    @RequestMapping(value="/saveShp")
    public String saveShp() {
        String filePath = "F:\\indoorData\\indoorMap\\10F\\road.shp";
        ArrayList<ShapeModel> modelList = shpReader.readShapeFile(filePath);
        service.saveAll(modelList);
        return "ok";
    }

}
