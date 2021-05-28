package com.indoor.navigation.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HaoYu
 * @description Vertex
 * @date 2021/05/28
 */
@Entity
@Table(name = "Vertex")
public class Vertex {
    @Id
    private Integer dataIndex;
    private Integer floor;
    private Double x;
    private Double y;
}
