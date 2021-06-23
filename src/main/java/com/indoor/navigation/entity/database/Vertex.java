package com.indoor.navigation.entity.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HaoYu
 * @description Vertex
 * @date 2021/05/28
 */
@Entity
@Table(name = "Vertex")
public class Vertex {
    @Id
    private String globalIndex;
    private String dataIndex;
    private Integer floor;
    private Double x;
    private Double y;

    public Vertex(){}

    public Vertex(String globalIndex, String dataIndex, Integer floor, Double x, Double y) {
        this.globalIndex = globalIndex;
        this.dataIndex = dataIndex;
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

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
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
                ", dataIndex='" + dataIndex + '\'' +
                ", floor='" + floor + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
