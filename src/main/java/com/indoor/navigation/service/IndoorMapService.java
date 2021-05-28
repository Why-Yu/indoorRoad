package com.indoor.navigation.service;

import com.indoor.navigation.entity.ShapeModel;
import com.indoor.navigation.repo.IndoorMapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HaoYu
 * @description service
 * @date 2021/05/24
 */
@Service
public class IndoorMapService {
    @Autowired
    IndoorMapRepo indoorMapRepo;

    public ShapeModel save(ShapeModel data) {
        return indoorMapRepo.save(data);
    }

    public List<ShapeModel> findById(int id) {
        return  indoorMapRepo.findById(id);
    }

    public List<ShapeModel> saveAll(Iterable<ShapeModel> entities) {
        return indoorMapRepo.saveAll(entities);
    }

}
