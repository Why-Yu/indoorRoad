package com.indoor.navigation.database.service;

import com.indoor.navigation.database.entity.IndoorRoad;
import com.indoor.navigation.database.repo.IndoorRoadRepo;
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
