package com.indoor.navigation.entity;

import javax.persistence.*;

@Entity
@Table(name = "indoorRoad")
public class IndoorRoad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="indoorRoadSeq")
    @SequenceGenerator(name = "indoorRoadSeq",initialValue = 1, allocationSize = 1,sequenceName = "indoorRoadSeq")
    private int roadId;
    private int beginId;
    private double beginX;
    private double beginY;
    private int endId;
    private double endX;
    private double endY;
    private double length;

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getBeginId() {
        return beginId;
    }

    public void setBeginId(int beginId) {
        this.beginId = beginId;
    }

    public double getBeginX() {
        return beginX;
    }

    public void setBeginX(double beginX) {
        this.beginX = beginX;
    }

    public double getBeginY() {
        return beginY;
    }

    public void setBeginY(double beginY) {
        this.beginY = beginY;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
