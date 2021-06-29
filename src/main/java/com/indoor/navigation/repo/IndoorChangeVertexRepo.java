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
    /**
     * description: 按转换点类别寻找关键点 <br>
     * date: 2021/6/29 9:41 <br>
     * author: HaoYu <br>
     * @param changeTypeList
     * @return java.util.List<com.indoor.navigation.entity.database.ChangeVertex>
     */
    List<ChangeVertex> findByChangeTypeIn(List<ChangeType> changeTypeList);
    List<ChangeVertex> findByChangeTypeInAndBuildId(List<ChangeType> changeTypeList, String buildId);
    ChangeVertex findByGlobalIndex(String globalIndex);
}
