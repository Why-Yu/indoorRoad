package com.indoor.navigation.repo;

import com.indoor.navigation.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndoorEdgeRepo extends JpaRepository<Edge, Integer> {
    Edge findByEdgeId(int id);
    List<Edge> findByStartIndex(String startIndex);
}
