package com.indoor.navigation.service;

import com.indoor.navigation.entity.ShapeModel;
import com.indoor.navigation.repo.IndoorModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HaoYu
 * @description service
 * @date 2021/06/02
 */
@Service
public class IndoorModelService {
    @Autowired
    private IndoorModelRepo indoorModelRepo;

    public List<ShapeModel> saveAll(Iterable<ShapeModel> entities) {
        return indoorModelRepo.saveAll(entities);
    }

}
