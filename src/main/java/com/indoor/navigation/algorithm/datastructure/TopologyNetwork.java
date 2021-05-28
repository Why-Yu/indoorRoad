package com.indoor.navigation.algorithm.datastructure;

import com.indoor.navigation.Interfaces.Graph;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拓扑网络结构
 * 把对应数据库中的数据就转换成这个类
 */
@Component
@Scope("prototype")
public class TopologyNetwork implements Graph {

    /**
     * 初始容量300，减少储存空间的频繁调整
     */
    public TopologyNetwork(){
        vertices = new ArrayList<>(300);
        dataIndexToVerticesIndex = new HashMap<>();
    }

    public class Vertex{

        Vertex(){}

        Vertex(String dataIndex, int floor, double x, double y){
            this.dataIndex = dataIndex;
            this.floor = floor;
            this.x = x;
            this.y = y;
        }
        String dataIndex;
        int floor;
        double x;
        double y;
        NextNode nextNode;

        @Override
        public String toString() {
            return "Vertex{" +
                    "dataIndex=" + dataIndex +
                    ", floor=" + floor +
                    ", x=" + x +
                    ", y=" + y +
                    ", nextNode=" + nextNode +
                    '}' + '\n';
        }
    }

    private class NextNode{

        NextNode(String dataIndex, double weight){
            this.dataIndex = dataIndex;
            this.weight = weight;
        }
        String dataIndex;
        double weight;
        NextNode nextNode;

        @Override
        public String toString() {
            return "NextNode{" +
                    "dataIndex=" + dataIndex +
                    ", weight=" + weight +
                    ", nextNode=" + nextNode +
                    '}';
        }
    }

    private List<Vertex> vertices;
    private Map<String, Integer> dataIndexToVerticesIndex;

    @Override
    public void insertVertex(String dataIndex, int floor, double x, double y) {
        if(!dataIndexToVerticesIndex.containsKey(dataIndex)){
            dataIndexToVerticesIndex.put(dataIndex, vertices.size());
            Vertex vertex = new Vertex(dataIndex, floor, x, y);
            vertices.add(vertex);
        }
    }

    @Override
    public void deleteVertex(String dataIndex) {
        //在构建的时候其实是用不上的，但写着玩,但添加操作简单，删除操作一般都很麻烦
        if(dataIndexToVerticesIndex.containsKey(dataIndex)){
            int verticesIndex = dataIndexToVerticesIndex.get(dataIndex);
            vertices.set(verticesIndex, new Vertex());

            NextNode previous;
            NextNode current;
            for(Vertex vertex : vertices){
                NextNode nextNode = vertex.nextNode;
                if(nextNode == null) continue;
                if(nextNode.dataIndex.equals(dataIndex) && nextNode.nextNode == null){
                    vertex.nextNode = null;
                    continue;
                }
                //这时nextNode等于链条中的第一个点
                current = nextNode;
                while (current != null){
                    previous = current;
                    current = current.nextNode;
                    if(current.dataIndex.equals(dataIndex)){
                        previous.nextNode = current.nextNode;
                        break;
                    }
                }
            }
            dataIndexToVerticesIndex.remove(dataIndex);
        }
    }

    //插入新边时，直接插在Vertex后面，而不是一行链表的最后面
    //不能插入环
    //代码简化，不再需要对nextNode是否为null做检查
    @Override
    public void insertEdge(String dataIndex1, String dataIndex2, double weight) {
        if(dataIndexToVerticesIndex.containsKey(dataIndex1) &&
        dataIndexToVerticesIndex.containsKey(dataIndex2) && !dataIndex1.equals(dataIndex2)){
            //-----这一块测试之后删掉,功能是生成随机网络时，拒绝重复添加边
            int tempVerticesIndex = dataIndexToVerticesIndex.get(dataIndex1);
            NextNode tempNextNode = vertices.get(tempVerticesIndex).nextNode;
            while (tempNextNode != null){
                if(tempNextNode.dataIndex.equals(dataIndex2)) return;
                tempNextNode = tempNextNode.nextNode;
            }
            //-----
            int verticesIndex1 = dataIndexToVerticesIndex.get(dataIndex1);
            NextNode newNode = new NextNode(dataIndex2, weight);
            newNode.nextNode = vertices.get(verticesIndex1).nextNode;
            vertices.get(verticesIndex1).nextNode = newNode;

            int verticesIndex2 = dataIndexToVerticesIndex.get(dataIndex2);
            newNode = new NextNode(dataIndex1, weight);
            newNode.nextNode = vertices.get(verticesIndex2).nextNode;
            vertices.get(verticesIndex2).nextNode = newNode;
        }
    }

    @Override
    public void deleteEdge(String dataIndex1, String dataIndex2) {
        if(dataIndexToVerticesIndex.containsKey(dataIndex1) &&
                dataIndexToVerticesIndex.containsKey(dataIndex2)){
            int verticesIndex1 = dataIndexToVerticesIndex.get(dataIndex1);
            NextNode current = vertices.get(verticesIndex1).nextNode;
            NextNode previous = null;
            //这步的判断是必须的，此时恰好第一个nextNode就是想要的点，单独处理，防止我们引用空指针previous,下同
            if(current.dataIndex.equals(dataIndex2)) {
                vertices.get(verticesIndex1).nextNode = current.nextNode;
                current = null;
            }
            while(current != null && !current.dataIndex.equals(dataIndex2)){
                previous = current;
                current = current.nextNode;
            }
            if(current != null) previous.nextNode = current.nextNode;

            int verticesIndex2 = dataIndexToVerticesIndex.get(dataIndex2);
            current = vertices.get(verticesIndex2).nextNode;
            previous = null;
            if(current.dataIndex.equals(dataIndex1)) {
                vertices.get(verticesIndex2).nextNode = current.nextNode;
                current = null;
            }
            while(current != null && !current.dataIndex.equals(dataIndex1)){
                previous = current;
                current = current.nextNode;
            }
            if(current != null) previous.nextNode = current.nextNode;
        }
    }


    @Override
    public List<Node> getLinkedNode(Node node) {
        int verticesIndex = dataIndexToVerticesIndex.get(node.dataIndex);
        NextNode current = vertices.get(verticesIndex).nextNode;
        int tempVerticesIndex;
        Vertex tempVertex;
        List<Node> nodeList = new ArrayList<>();
        while(current != null){
            tempVerticesIndex = dataIndexToVerticesIndex.get(current.dataIndex);
            tempVertex = vertices.get(tempVerticesIndex);
            Node tempNode = new Node(tempVertex.dataIndex, tempVertex.floor,
                    tempVertex.x, tempVertex.y, node.gCost + current.weight);
            nodeList.add(tempNode);
            current = current.nextNode;
        }
        return nodeList;
    }

    /**
     * 用户输入坐标后，获得网络中对应的起止点
     * @param floor
     * @param x
     * @param y
     * @return 与输入最近的Node
     */
    public Node findNearNode(int floor, double x, double y){
        List<Vertex> vertexList = getVerticesInFloor(floor);
        double tempMin = 99999;
        double absolute;
        String tempDataIndex = floor + "-0";

        for(Vertex vertex : vertexList){
            absolute = Math.abs(x - vertex.x) + Math.abs(y -vertex.y);
            if(absolute <= tempMin){
                tempMin = absolute;
                tempDataIndex = vertex.dataIndex;
            }
        }
        int verticesIndex = dataIndexToVerticesIndex.get(tempDataIndex);
        return new Node(tempDataIndex, floor, vertices.get(verticesIndex).x, vertices.get(verticesIndex).y, 0);
    }

    public List<Vertex> getVerticesInFloor(int floor){
        List<Vertex> vertexList = new ArrayList<>();
        for(Vertex vertex : vertices){
            if(vertex.floor == floor)
                vertexList.add(vertex);
        }
        return vertexList;
    }


    @Override
    public int getNumOfVertices() {
        return vertices.size();
    }

    @Override
    public String toString() {
        return "TopologyNetwork{" +
                "vertices=" + vertices +
                '}';
    }
}
