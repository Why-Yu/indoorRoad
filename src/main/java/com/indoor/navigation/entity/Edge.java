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
    private Integer startIndex;
    private Integer endIndex;
    private Double weight;
}
