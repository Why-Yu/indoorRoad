package com.indoor.navigation.repo;

import com.indoor.navigation.entity.database.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndoorVertexRepo extends JpaRepository<Vertex, String> {
    Vertex findByGlobalIndex(String globalIndex);
}
