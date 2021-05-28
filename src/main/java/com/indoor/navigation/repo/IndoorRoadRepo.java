package com.indoor.navigation.repo;

import com.indoor.navigation.entity.IndoorRoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndoorRoadRepo extends JpaRepository<IndoorRoad, Integer> {

}
