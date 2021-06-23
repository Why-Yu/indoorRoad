package com.indoor.navigation.algorithm;

import com.indoor.navigation.algorithm.datastructure.MinHeap;
import com.indoor.navigation.algorithm.datastructure.Node;
import com.indoor.navigation.algorithm.datastructure.TopologyNetwork;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YH
 */
@Component
@Scope("prototype")
public class FindPath {

    public TopologyNetwork network;
    public MinHeap<Node> minHeap;
    public Map<String, Node> openMap;
    public Map<String, Node> closeMap;

    public Node startNode;
    public Node endNode;
    private Node currentNode;
    private double dx;
    private double dy;
    private double basicsH;

    public FindPath(){
        openMap = new HashMap<>(50);
        closeMap = new HashMap<>();
        minHeap = new MinHeap<>(Node.class);
    }

    /**
     * 让此类和具体的网络实例剥离开来，计算最短路径类可以在任何一个network上进行计算
     * @param network 具体的拓扑网络数据
     */
    public void changeNetwork(TopologyNetwork network){
        this.network = network;
    }

    public void setStartNode(int floor , double x, double y){
        this.startNode = network.findNearNode(floor, x, y);
    }

    public void setEndNode(int floor, double x, double y){
        this.endNode = network.findNearNode(floor, x, y);
    }

    /**
     * h估计函数，计算点到目标点之间的曼哈顿距离 + 向量的叉积(保证路径尽可能成一条直线)
     * @param node 需要估值的某个结点
     * @return 估计出来的还需要的花费
     */
    public double getHeuristics(Node node){
        basicsH = Math.abs(node.x - endNode.x) + Math.abs(node.y - endNode.y);
        return basicsH + 0.001 * Math.abs((node.x - endNode.x) * dy - dx * (node.y - endNode.y));
    }

    private void getVector(){
        dx = startNode.x - endNode.x;
        dy = startNode.y - endNode.y;
    }

    /**
     * 在数据以及起终点设置完成后，获取最短路径
     * @return 由一系列Node表示的最短路径，供前端生成路径
     */
    public List<Node> getShortestPath() throws NullPointerException{
        if(network == null || startNode == null || endNode == null) {
            throw new NullPointerException("请先设置拓扑网络和起终点");
        }
        //初始化
        getVector();
        currentNode = startNode;
        closeMap.put(startNode.dataIndex, startNode);

        //算法
        while(!currentNode.dataIndex.equals(endNode.dataIndex)){
            for(Node node : network.getLinkedNode(currentNode)){
                if(!closeMap.containsKey(node.dataIndex)){
                    if(!openMap.containsKey(node.dataIndex)){
                        node.heuristics = getHeuristics(node);
                        node.total = node.gCost + node.heuristics;
                        node.parent = currentNode;
                        openMap.put(node.dataIndex, node);
                        minHeap.add(node);
                    }else {
                        if(openMap.get(node.dataIndex).gCost > node.gCost){
                            Node revisedNode = openMap.get(node.dataIndex);
                            revisedNode.gCost = node.gCost;
                            revisedNode.total = revisedNode.gCost + revisedNode.heuristics;
                            revisedNode.parent = currentNode;
                            minHeap.swim(revisedNode);
                        }
                    }
                }
            }
            if(!openMap.isEmpty()){
                currentNode = minHeap.delMin();
                closeMap.put(currentNode.dataIndex, currentNode);
                openMap.remove(currentNode.dataIndex);
            }else {
                return null;
            }
        }
        List<Node> resultList = new ArrayList<>();
        while (currentNode != null){
            resultList.add(currentNode);
            currentNode = currentNode.parent;
        }
        return resultList;
    }
}
