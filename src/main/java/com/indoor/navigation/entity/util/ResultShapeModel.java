package com.indoor.navigation.entity.util;

/**
 * @author HaoYu
 * @description for dataManagement
 * @date 2021/06/03
 */
public class ResultShapeModel {
    private Integer modelId;
    private String beginId;
    private Double beginX;
    private Double beginY;
    private String endId;
    private Double endX;
    private Double endY;
    private String floor;

    public ResultShapeModel() {}

    public ResultShapeModel(Integer modelId, String beginId, Double beginX, Double beginY, String endId, Double endX, Double endY, String floor) {
        this.modelId = modelId;
        this.beginId = beginId;
        this.beginX = beginX;
        this.beginY = beginY;
        this.endId = endId;
        this.endX = endX;
        this.endY = endY;
        this.floor = floor;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
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
}
