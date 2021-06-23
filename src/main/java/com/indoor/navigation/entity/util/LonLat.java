package com.indoor.navigation.entity.util;

/**
 * @author HaoYu
 * @description 经纬度坐标
 * @date 2021/06/23
 */
public class LonLat {
    public double lon;
    public double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "LonLat{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
