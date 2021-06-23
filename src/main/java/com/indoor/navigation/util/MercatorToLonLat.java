package com.indoor.navigation.util;

import com.indoor.navigation.entity.util.LonLat;
import com.indoor.navigation.entity.util.Mercator;

/**
 * @author HaoYu
 * @description 坐标转换
 * @date 2021/06/23
 */
public class MercatorToLonLat {
    /**
     * description: 墨卡托转为WGS84经纬度 <br>
     * date: 2021/6/23 12:02 <br>
     * author: HaoYu <br>
     * @param mercatorX
     * @param mercatorY
     * @return LonLat
     */
    public static LonLat mercatorToLonLat(double mercatorX, double mercatorY) {
        LonLat lonLat = new LonLat();
        double x = mercatorX / 20037508.34 * 180;
        double y = mercatorY / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        lonLat.lon = x;
        lonLat.lat = y;
        return lonLat;
    }

    /**
     * description: 经纬度转为墨卡托 <br>
     * date: 2021/6/23 12:02 <br>
     * author: HaoYu <br>
     * @param lon
     * @param lat
     * @return com.indoor.navigation.entity.util.Mercator
     */
    public static Mercator lonLatToMercator(double lon, double lat) {
        Mercator mercator = new Mercator();
        double x = lon * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        mercator.x = x;
        mercator.y = y;
        return mercator;
    }
}
