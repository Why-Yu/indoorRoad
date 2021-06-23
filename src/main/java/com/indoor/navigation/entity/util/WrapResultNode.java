package com.indoor.navigation.entity.util;

import java.util.ArrayList;

/**
 * @author HaoYu
 * @description 除了结果路径还需要包装上一些文字信息
 * @date 2021/06/23
 */
public class WrapResultNode {
    private ArrayList<ResultNode> resultNodeList;
    private String message;

    public ArrayList<ResultNode> getResultNodeList() {
        return resultNodeList;
    }

    public void setResultNodeList(ArrayList<ResultNode> resultNodeList) {
        this.resultNodeList = resultNodeList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WrapResultNode{" +
                "resultNodeList=" + resultNodeList +
                ", message='" + message + '\'' +
                '}';
    }
}
