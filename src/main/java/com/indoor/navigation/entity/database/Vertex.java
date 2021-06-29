package com.indoor.navigation.entity.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HaoYu
 * @description 存储网络拓扑中的结点信息
 * @date 2021/05/28
 */
@Entity
@Table(name = "Vertex")
public class Vertex {
    @Id
    private String globalIndex;
    private String buildId;
    private Integer floor;
    private Double x;
    private Double y;

    public Vertex() {
    }

    public Vertex(String globalIndex, String buildId, Integer floor, Double x, Double y) {
        this.globalIndex = globalIndex;
        this.buildId = buildId;
        this.floor = floor;
        this.x = x;
        this.y = y;
    }

    public String getGlobalIndex() {
        return globalIndex;
    }

    public void setGlobalIndex(String globalIndex) {
        this.globalIndex = globalIndex;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "globalIndex='" + globalIndex + '\'' +
                ", buildId='" + buildId + '\'' +
                ", floor=" + floor +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
