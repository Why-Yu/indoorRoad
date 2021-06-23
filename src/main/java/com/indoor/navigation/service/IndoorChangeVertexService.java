package com.indoor.navigation.service;

import com.indoor.navigation.entity.database.ChangeVertex;
import com.indoor.navigation.repo.IndoorChangeVertexRepo;
import com.indoor.navigation.util.ChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HaoYu
 * @description ChangeVertexService
 * @date 2021/06/22
 */
@Service
public class IndoorChangeVertexService {
    @Autowired
    IndoorChangeVertexRepo indoorChangeVertexRepo;

    public ChangeVertex save(ChangeVertex changeVertex){
        return indoorChangeVertexRepo.save(changeVertex);
    }

    public List<ChangeVertex> saveAll(Iterable<ChangeVertex> entities) {
        return indoorChangeVertexRepo.saveAll(entities);
    }

    public List<ChangeVertex> findByChangeType(ChangeType changeType) {
        return indoorChangeVertexRepo.findByChangeType(changeType);
    }

    public ChangeVertex findByGlobalIndex(String globalIndex) {
        return indoorChangeVertexRepo.findByGlobalIndex(globalIndex);
    }
}
