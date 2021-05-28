package com.indoor.navigation.controller;

import com.indoor.navigation.entity.IndoorRoad;
import com.indoor.navigation.service.IndoorRoadService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    public static void main(String[] args) {
        IndoorRoadService service = new IndoorRoadService();
        IndoorRoad road = new IndoorRoad();
        road.setRoadId(1);
        road.setBeginId(100);
        road.setBeginX(20);
        road.setBeginY(30);
        road.setEndId(101);
        road.setEndX(40);
        road.setEndY(50);
        service.save(road);
    }
}
