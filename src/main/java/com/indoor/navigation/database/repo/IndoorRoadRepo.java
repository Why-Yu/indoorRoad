package com.indoor.navigation.database.repo;

import com.indoor.navigation.database.entity.IndoorRoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndoorRoadRepo extends JpaRepository<IndoorRoad, Integer> {

}
