package com.indoor.navigation.repo;

import com.indoor.navigation.entity.database.BoundingBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Classname IndoorBoundingBoxRepo
 * @Description
 * @Date 2021/6/29 9:51
 * @Created by HaoYu
 */
@Repository
public interface IndoorBoundingBoxRepo extends JpaRepository<BoundingBox, Integer> {
}
