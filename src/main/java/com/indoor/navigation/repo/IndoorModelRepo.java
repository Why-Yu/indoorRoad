package com.indoor.navigation.repo;

import com.indoor.navigation.entity.ShapeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndoorModelRepo extends JpaRepository<ShapeModel, Integer> {

}
