package com.indoor.navigation.service;

import com.indoor.navigation.entity.database.Vertex;
import com.indoor.navigation.repo.IndoorVertexRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HaoYu
 * @description service
 * @date 2021/05/28
 */
@Service
public class IndoorVertexService {
    @Autowired
    IndoorVertexRepo indoorVertexRepo;

    public Vertex save(Vertex vertex) {
        return indoorVertexRepo.save(vertex);
    }

    public Vertex findByGlobalIndex(String globalIndex) {
        return indoorVertexRepo.findByGlobalIndex(globalIndex);
    }

    public List<Vertex> saveAll(Iterable<Vertex> entities) {
        return indoorVertexRepo.saveAll(entities);
    }

    public List<Vertex> findAll(){
        return indoorVertexRepo.findAll();
    }

    public List<Vertex> findByBuildId(String buildId) {
        return indoorVertexRepo.findByBuildId(buildId);
    }
}
