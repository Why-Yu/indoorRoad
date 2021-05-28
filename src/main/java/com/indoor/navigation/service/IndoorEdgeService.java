package com.indoor.navigation.service;

import com.indoor.navigation.entity.Edge;
import com.indoor.navigation.repo.IndoorEdgeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HaoYu
 * @description service
 * @date 2021/05/28
 */
@Service
public class IndoorEdgeService {
    @Autowired
    IndoorEdgeRepo indoorEdgeRepo;

    public Edge save(Edge edge){
        return indoorEdgeRepo.save(edge);
    }

    public Edge findByEdgeId(int id) {
        return indoorEdgeRepo.findByEdgeId(id);
    }

    public List<Edge> findByStartIndex(String startIndex) {
        return indoorEdgeRepo.findByStartIndex(startIndex);
    }

    public List<Edge> saveAll(Iterable<Edge> entities) {
        return indoorEdgeRepo.saveAll(entities);
    }

    public List<Edge> findAll() {
        return indoorEdgeRepo.findAll();
    }
}

