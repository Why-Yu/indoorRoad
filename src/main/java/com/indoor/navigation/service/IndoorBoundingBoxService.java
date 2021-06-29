package com.indoor.navigation.service;

import com.indoor.navigation.entity.database.BoundingBox;
import com.indoor.navigation.repo.IndoorBoundingBoxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Date 2021/6/29 9:52
 * @author YH
 */
@Service
public class IndoorBoundingBoxService {
    @Autowired
    IndoorBoundingBoxRepo indoorBoundingBoxRepo;

    public BoundingBox save(BoundingBox boundingBox) {
        return indoorBoundingBoxRepo.save(boundingBox);
    }

    public List<BoundingBox> findAll() {
        return indoorBoundingBoxRepo.findAll();
    }
}
