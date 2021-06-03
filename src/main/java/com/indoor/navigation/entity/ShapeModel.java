package com.indoor.navigation.entity;

import com.indoor.navigation.Interfaces.TrimShapeModel;

import javax.persistence.*;

/**
 * @author HaoYu
 * @description shpFile Entity
 * the_geom是shp内部存储的地理实体数据转化为字符串
 * @date 2021/05/24
 */

@Entity
@Table(name = "shape_model")
public class ShapeModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="modelSeq")
    @SequenceGenerator(name = "modelSeq",initialValue = 1, allocationSize = 1,sequenceName = "modelSeq")
    private Integer modelId;
    private Integer id;
    private String beginId;
    private Double beginX;
    private Double beginY;
    private String endId;
    private Double endX;
    private Double endY;
    private String floor;
    private String the_geom;

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBeginId() {
        return beginId;
    }

    public void setBeginId(String beginId) {
        this.beginId = beginId;
    }

    public Double getBeginX() {
        return beginX;
    }

    public void setBeginX(Double beginX) {
        this.beginX = beginX;
    }

    public Double getBeginY() {
        return beginY;
    }

    public void setBeginY(Double beginY) {
        this.beginY = beginY;
    }

    public String getEndId() {
        return endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }

    public Double getEndX() {
        return endX;
    }

    public void setEndX(Double endX) {
        this.endX = endX;
    }

    public Double getEndY() {
        return endY;
    }

    public void setEndY(Double endY) {
        this.endY = endY;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getThe_geom() {
        return the_geom;
    }

    public void setThe_geom(String the_geom) {
        this.the_geom = the_geom;
    }

    @Override
    public String toString() {
        return "ShapeModel{" +
                "modelId=" + modelId +
                ", id=" + id +
                ", beginId='" + beginId + '\'' +
                ", beginX=" + beginX +
                ", beginY=" + beginY +
                ", endId='" + endId + '\'' +
                ", endX=" + endX +
                ", endY=" + endY +
                ", floor='" + floor + '\'' +
                ", the_geom='" + the_geom + '\'' +
                '}';
    }
}
