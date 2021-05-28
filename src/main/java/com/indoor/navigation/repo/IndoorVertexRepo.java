package com.indoor.navigation.repo;

import com.indoor.navigation.entity.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndoorVertexRepo extends JpaRepository<Vertex, String> {
    Vertex findByGlobalIndex(String globalIndex);
}
