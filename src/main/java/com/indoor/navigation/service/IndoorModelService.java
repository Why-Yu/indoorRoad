package com.indoor.navigation.service;

import com.indoor.navigation.entity.util.ResultShapeModel;
import com.indoor.navigation.entity.database.ShapeModel;
import com.indoor.navigation.repo.IndoorModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public List<ResultShapeModel> findAllTrimModel() {
        return indoorModelRepo.findAllTrimModel();
    }

    public Page<ShapeModel> findAll(Pageable pageable) {
        return indoorModelRepo.findAll(pageable);
    }

    public List<ShapeModel> findByBuildId(String buildId) {
        return indoorModelRepo.findByBuildId(buildId);
    }

}
