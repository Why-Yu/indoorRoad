package com.indoor.navigation.algorithm.datastructure;

/**
 *  Node是对拓扑网络中结点的抽象
 */
public class Node implements Comparable<Node>{

    /**
     * dataIndex:在数据库中的唯一id
     * arrayIndex:在有序集合实现的最小堆中的index
     * parent:在最短路径中到达这个结点的上一个结点
     * x,y,floor(可替换为z):描述点的具体空间位置
     * total,gCost,heuristics:计算最短路径的f=g+h
     */
    public int dataIndex;
    public int arrayIndex;

    public Node parent;
    public double x;
    public double y;
    public int floor;

    public double total;
    public double gCost;
    public double heuristics;

    /**
     * 获得关于Node的键值
     * @return floor_x_y形式的String
     */
    public String getKey(){
        StringBuilder result = new StringBuilder();
        result.append(floor);
        result.append("_");
        result.append(x);
        result.append("_");
        result.append(y);
        return result.toString();
    }

    /**
     * 这个无参构造是必须的哦，不然MinHeap里就不能用Node类对象创建Node实例了
     */
    public Node(){

    }

    public Node(int dataIndex, int floor, double x, double y, double gCost){
        this.dataIndex = dataIndex;
        this.floor = floor;
        this.x = x;
        this.y = y;
        this.gCost = gCost;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.total, o.total);
    }

    @Override
    public String toString() {
        return "Node{" +
                "dataIndex=" + dataIndex +
                ", arrayIndex=" + arrayIndex +
                ", parent=" + parent +
                ", x=" + x +
                ", y=" + y +
                ", floor=" + floor +
                ", total=" + total +
                ", gCost=" + gCost +
                ", heuristics=" + heuristics +
                '}';
    }
}
