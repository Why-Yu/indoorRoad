import com.indoor.navigation.algorithm.datastructure.MinHeap;
import com.indoor.navigation.algorithm.datastructure.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {
//    @Test
//    public void testGetKey(){
//        Node node = new Node(5, 123, 456);
//        System.out.println(node.getKey());
//    }
//
//    @Test
//    public void testCompare(){
//        Node node1 = new Node();
//        Node node2 = new Node();
//        node1.total = 100;
//        node2.total = 200;
//        System.out.println(node1.compareTo(node2));
//    }
//
//    @Test
//    public void testList(){
//        List<Node> list = new ArrayList<>();
//        list.add(new Node());
//        list.add(new Node(5, 123, 456));
//        System.out.println(list.remove(0));
//        System.out.println(list);
//    }

    @Test
    public void testMinHeap(){
        MinHeap<Node> minHeap = new MinHeap<>(Node.class);
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        Node node5 = new Node();
        Node node6 = new Node();
        Node node7 = new Node();
        node1.total = 60;
        node2.total = 10;
        node3.total = 30;
        node4.total = 90;
        node5.total = 20;
        node6.total = 100;
        node7.total = 5;
        minHeap.add(node1);
        minHeap.add(node2);
        minHeap.add(node3);
        minHeap.add(node4);
        minHeap.add(node5);
        minHeap.add(node6);
        minHeap.add(node7);
        System.out.println(minHeap);
        System.out.println(minHeap.delMin().total);
        System.out.println(minHeap);
        System.out.println(minHeap.delMin().total);
        System.out.println(minHeap);
    }
}
