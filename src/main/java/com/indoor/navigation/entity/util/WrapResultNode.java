package com.indoor.navigation.entity.util;

import java.util.ArrayList;

/**
 * @author HaoYu
 * @description 除了结果路径还需要包装上一些文字信息
 * @date 2021/06/23
 */
public class WrapResultNode {
    private ArrayList<ResultNode> resultNodes;
    public int floor;
    private String message;

    public WrapResultNode() {
        this.resultNodes = new ArrayList<>();
        this.floor = 0;
        this.message = "";
    }
    public ArrayList<ResultNode> getResultNodes() {
        return resultNodes;
    }

    public void setResultNodes(ArrayList<ResultNode> resultNodes) {
        this.resultNodes = resultNodes;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addMessage(String message) {
        this.message += message;
    }

    @Override
    public String toString() {
        return "WrapResultNode{" +
                "resultNodes=" + resultNodes +
                ", floor=" + floor +
                ", message='" + message + '\'' +
                '}';
    }
}
