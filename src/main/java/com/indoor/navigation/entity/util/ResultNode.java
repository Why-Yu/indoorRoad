package com.indoor.navigation.entity.util;

/**
 * @author HaoYu
 * @description used to result List for changing to jsonString
 * 必须要有getter & setter，因为fastjson要根据这个操作private field
 * @date 2021/05/30
 */
public class ResultNode {
    private String dataIndex;
    private int floor;
    private double x;
    private double y;

    public ResultNode(String dataIndex, int floor, double x, double y) {
        this.dataIndex = dataIndex;
        this.floor = floor;
        this.x = x;
        this.y = y;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ResultNode{" +
                "dataIndex='" + dataIndex + '\'' +
                ", floor=" + floor +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
