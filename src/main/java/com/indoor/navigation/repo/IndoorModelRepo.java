package com.indoor.navigation.repo;

import com.indoor.navigation.entity.util.ResultShapeModel;
import com.indoor.navigation.entity.database.ShapeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndoorModelRepo extends JpaRepository<ShapeModel, Integer> {
    // nativeQuery = true时, sql可以直接放到数据库里面跑，表名和字段是完全和数据库一一对应的
    // 而nativeQuery = false时， sql表名和字段名其实和实体类对应，jpa会帮忙进行转换
    // ------查询部分列有几种写法
    /* 1
    @Query(value = "select modelId, beginId, beginX, beginY, endId, endX, endY, floor from ShapeModel ")
    List<Object> findAllTrimModel();
    2
    TrimShapeModel只拥有部分实体类的get方法，实体类作为具体实现类
    @Query(value = "select modelId, beginId, beginX, beginY, endId, endX, endY, floor from ShapeModel ")
    Iterable<TrimShapeModel> findAllTrimModel();
    3
    创建新的实体类
    @Query(value = "select new com.indoor.navigation.entity.util.ResultShapeModel(modelId, beginId, " +
        "beginX, beginY, endId, endX, endY, floor) from ShapeModel ")
    List<ResultShapeModel> findAllTrimModel();
    但第一第二种只会传value丢失了key值，主要用一三这两个方法吧，一简便，三不丢key
     */
    @Query(value = "select new com.indoor.navigation.entity.util.ResultShapeModel(modelId, beginId, " +
            "beginX, beginY, endId, endX, endY, floor) from ShapeModel ")
    List<ResultShapeModel> findAllTrimModel();

    List<ShapeModel> findByBuildId(String buildId);
}
