package com.indoor.navigation.repo;

import com.indoor.navigation.entity.database.ChangeVertex;
import com.indoor.navigation.util.ChangeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YH
 */
@Repository
public interface IndoorChangeVertexRepo extends JpaRepository<ChangeVertex, String> {
    List<ChangeVertex> findByChangeTypeIn(List<ChangeType> changeTypeList);
    ChangeVertex findByGlobalIndex(String globalIndex);
}
