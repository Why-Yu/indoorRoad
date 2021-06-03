package com.indoor.navigation.Interfaces;

public interface TrimShapeModel {
    Integer getModelId();
    void setModelId(Integer modelId);
    String getBeginId();
    void setBeginId(String beginId);
    Double getBeginX();
    void setBeginX(Double beginX);
    Double getBeginY();
    void setBeginY(Double beginY);
    String getEndId();
    void setEndId(String endId);
    Double getEndX();
    void setEndX(Double endX);
    Double getEndY();
    void setEndY(Double endY);
    String getFloor();
    void setFloor(String floor);
}
