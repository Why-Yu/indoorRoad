package com.indoor.navigation.entity.database;

import com.indoor.navigation.util.ChangeType;

import javax.persistence.*;

/**
 * @author HaoYu
 * @description 存储拓扑图的额外边缘
 * @date 2021/06/22
 */
@Entity
@Table(name = "change_vertex")
public class ChangeVertex {
    @Id
    private String globalIndex;
    private String upGlobalIndex;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ChangeType changeType;
    private String buildId;

    public ChangeVertex(){}
    public ChangeVertex(String globalIndex, String upGlobalIndex, ChangeType changeType, String buildId) {
        this.globalIndex = globalIndex;
        this.upGlobalIndex = upGlobalIndex;
        this.changeType = changeType;
        this.buildId = buildId;
    }
    public String getGlobalIndex() {
        return globalIndex;
    }

    public void setGlobalIndex(String globalIndex) {
        this.globalIndex = globalIndex;
    }

    public String getUpGlobalIndex() {
        return upGlobalIndex;
    }

    public void setUpGlobalIndex(String upGlobalIndex) {
        this.upGlobalIndex = upGlobalIndex;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    @Override
    public String toString() {
        return "ChangeVertex{" +
                "globalIndex='" + globalIndex + '\'' +
                ", upGlobalIndex='" + upGlobalIndex + '\'' +
                ", changeType=" + changeType +
                ", buildId='" + buildId + '\'' +
                '}';
    }
}
