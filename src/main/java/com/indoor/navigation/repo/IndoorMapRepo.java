package com.indoor.navigation.repo;

import com.indoor.navigation.entity.ShapeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndoorMapRepo extends JpaRepository<ShapeModel, Integer> {
    List<ShapeModel> findById(int id);
}
