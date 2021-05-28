package com.indoor.navigation.service;

import com.indoor.navigation.entity.IndoorRoad;
import com.indoor.navigation.repo.IndoorRoadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndoorRoadService {
    @Autowired
    private IndoorRoadRepo indoorRoadRepo;

    public IndoorRoad save(IndoorRoad data){
        return indoorRoadRepo.save(data);
    }
}
