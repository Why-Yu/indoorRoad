import com.indoor.navigation.algorithm.FindPath;
import com.indoor.navigation.algorithm.StupidFindPath;
import com.indoor.navigation.algorithm.datastructure.MinHeap;
import com.indoor.navigation.algorithm.datastructure.Node;
import com.indoor.navigation.algorithm.datastructure.TopologyNetwork;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FindPathTest {

//    @Test
//    public void testReference(){
//        MinHeap<Node> minHeap = new MinHeap<>(Node.class);
//        Node node1 = new Node(1, 1, 1, 1, 0);
//        minHeap.add(node1);
//        Map<Integer, Node> openMap = new HashMap<>();
//        openMap.put(1, node1);
//        minHeap.get(1).gCost = 100;
//        System.out.println(openMap.get(1).gCost);
//    }

    /**
     * 只是想通过生成随机的网络，测试一下按现在计算思路写的与按大四思路写的，
     * 基准测试的差距
     */
    @Test
    public void testAlgorithm() {
        long findPathTime = 0;
        long stupidFindPathTime = 0;
        long startTime;
        long endTime;
        for (int round = 0; round < 5; round++) {
            TopologyNetwork network = createNetwork(10, 300);
            FindPath findPath = new FindPath();
            findPath.changeNetwork(network);
            findPath.setStartNode(1, 20, 20);
            findPath.setEndNode(10, 60, 60);

            startTime = System.currentTimeMillis();
            List<Node> pathList = findPath.getShortestPath();
            StringBuilder result = new StringBuilder();
            for (Node pathNode : pathList)
            {
                result.append(pathNode.dataIndex);
                result.append("   ");
            }
            System.out.println(result.toString());
            endTime = System.currentTimeMillis();
            findPathTime += endTime - startTime;

//            System.out.println("start" + findPath.startNode);
//            System.out.println("end" + findPath.endNode);
//            for (Node node : pathList) {
//                System.out.println(node.dataIndex);
//            }

            StupidFindPath stupidFindPath = new StupidFindPath();
            stupidFindPath.changeNetwork(network);
            stupidFindPath.setStartNode(1, 20, 20);
            stupidFindPath.setEndNode(10, 60, 60);

            startTime = System.currentTimeMillis();
            List<Node> pathList2 = stupidFindPath.getShortestPath();
            endTime = System.currentTimeMillis();
            stupidFindPathTime += endTime - startTime;

//            System.out.println("start" + stupidFindPath.startNode);
//            System.out.println("end" + stupidFindPath.endNode);
//            for (Node node : pathList2) {
//                System.out.println(node.dataIndex);
//            }
        }
        System.out.println(findPathTime);
        System.out.println(stupidFindPathTime);
    }

    //生成一个随机网络
    public TopologyNetwork createNetwork(int floors, int verticesInFloor) {
        TopologyNetwork network = new TopologyNetwork();
        for (int i = 1; i < floors + 1; i++) {
            for (int j = 0; j < verticesInFloor; j++) {
                String dataIndex = Integer.toString(i) + "-" + Integer.toString(j);
                network.insertVertex(dataIndex, i, Math.random() * 100, Math.random() * 100);
            }
        }

        Random rm = new Random();
        for (int i = 1; i < floors + 1; i++) {
            for (int j = 0; j < verticesInFloor; j++) {
                String dataIndexFrom = Integer.toString(i) + "-" + Integer.toString(j);
                for (int k = 0; k < 3; k++) {  //修改这个参数，理论上能使路径更加长，每一步可选的路径减少
                    String dataIndexTo = Integer.toString(i) + "-" + Integer.toString(rm.nextInt(verticesInFloor));
                    network.insertEdge(dataIndexFrom, dataIndexTo, Math.random() * 10);
                }
            }
        }

        for (int i = 1; i < floors + 1; i++) {
            String dataIndexFrom = Integer.toString(i) + "-0";
            String dataIndexTo = Integer.toString(i + 1) + "-0";
            network.insertEdge(dataIndexFrom, dataIndexTo, 5);
        }

        return network;
    }
}

