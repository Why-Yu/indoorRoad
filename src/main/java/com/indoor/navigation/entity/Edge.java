package com.indoor.navigation.entity;

import javax.persistence.*;

/**
 * @author HaoYu
 * @description Edge
 * @date 2021/05/28
 */
@Entity
@Table(name = "Edge")
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="edgeSeq")
    @SequenceGenerator(name = "edgeSeq",initialValue = 1, allocationSize = 1,sequenceName = "edgeSeq")
    private Integer edgeId;
    private String startIndex;
    private String endIndex;
    private Double weight;

    public Edge(){}
    public Edge(String startIndex, String endIndex, Double weight) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.weight = weight;
    }

    public Integer getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "edgeId=" + edgeId +
                ", startIndex='" + startIndex + '\'' +
                ", endIndex='" + endIndex + '\'' +
                ", weight=" + weight +
                '}';
    }
}
