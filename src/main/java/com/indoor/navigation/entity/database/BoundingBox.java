package com.indoor.navigation.entity.database;

import javax.persistence.*;

/**
 * @description: 存储建筑物的轮廓，以判断输入坐标是哪个建筑物
 * @date: 2021/6/28 22:47
 * @author: HaoYu
 */
@Entity
@Table(name = "bounding_box")
public class BoundingBox {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="boundingBoxSeq")
    @SequenceGenerator(name = "boundingBoxSeq",initialValue = 1, allocationSize = 1,sequenceName = "boundingBoxSeq")
    private Integer boundingBoxId;
    private String buildId;
    private Double minLon;
    private Double maxLon;
    private Double minLat;
    private Double maxLat;

    public BoundingBox(){};

    public BoundingBox(String buildId, Double minLon, Double maxLon, Double minLat, Double maxLat) {
        this.buildId = buildId;
        this.minLon = minLon;
        this.maxLon = maxLon;
        this.minLat = minLat;
        this.maxLat = maxLat;
    }

    public Integer getBoundingBoxId() {
        return boundingBoxId;
    }

    public void setBoundingBoxId(Integer boundingBoxId) {
        this.boundingBoxId = boundingBoxId;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public Double getMinLon() {
        return minLon;
    }

    public void setMinLon(Double minLon) {
        this.minLon = minLon;
    }

    public Double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(Double maxLon) {
        this.maxLon = maxLon;
    }

    public Double getMinLat() {
        return minLat;
    }

    public void setMinLat(Double minLat) {
        this.minLat = minLat;
    }

    public Double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(Double maxLat) {
        this.maxLat = maxLat;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "boundingBoxId=" + boundingBoxId +
                ", buildId='" + buildId + '\'' +
                ", minLon=" + minLon +
                ", maxLon=" + maxLon +
                ", minLat=" + minLat +
                ", maxLat=" + maxLat +
                '}';
    }
}
