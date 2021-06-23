package com.indoor.navigation.entity.util;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author HaoYu
 * @description get information from client<br>
 *         navMode表示导航模式，其中1表示室内外一体化导航，2表示纯室内导航
 * @date 2021/05/30
 */
public class TransmissionNode {
    @ApiModelProperty("起始楼层")
    private int startFloor;
    @ApiModelProperty("终止楼层")
    private int endFloor;
    @ApiModelProperty("起始x")
    private double startX;
    @ApiModelProperty("终止y")
    private double endX;
    @ApiModelProperty("起始y")
    private double startY;
    @ApiModelProperty("终止y")
    private double endY;
    @ApiModelProperty("导航模式")
    private int navMode;

    public int getStartFloor() {
        return startFloor;
    }

    public void setStartFloor(int startFloor) {
        this.startFloor = startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public void setEndFloor(int endFloor) {
        this.endFloor = endFloor;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public int getNavMode() {
        return navMode;
    }

    public void setNavMode(int navMode) {
        this.navMode = navMode;
    }

    @Override
    public String toString() {
        return "TransmissionNode{" +
                "startFloor=" + startFloor +
                ", endFloor=" + endFloor +
                ", startX=" + startX +
                ", endX=" + endX +
                ", startY=" + startY +
                ", endY=" + endY +
                ", navMode=" + navMode +
                '}';
    }
}
