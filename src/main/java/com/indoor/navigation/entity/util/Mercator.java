package com.indoor.navigation.entity.util;

/**
 * @author HaoYu
 * @description 墨卡托坐标
 * @date 2021/06/23
 */
public class Mercator {
    public double x;
    public double y;

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
        return "Mercator{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
