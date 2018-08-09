package com.gome.fup.easy.rpc.nameserver.test;

import com.gome.fup.easy.rpc.nameserver.data.Node;
import com.gome.fup.easy.rpc.nameserver.data.NodeTree;

import java.util.List;

/**
 * Created by fupeng-ds on 2018/7/19.
 */
public class NodeTreeTest {

    private static NodeTree tree = new NodeTree();

    public static void main(String[] args) {
        for (int i = 1; i < 10; i++) {
            tree.put(i, ("node_" + i).getBytes());
        }
        List<Node> nodes = tree.preOrder();
        for (Node node : nodes) {
            System.out.println("id is : " + node.getZxid() + ", color is : " + node.getColor());
        }
        System.out.println("=============================");
        tree.remove(6);
        List<Node> list = tree.preOrder();
        for (Node node : list) {
            System.out.println("id is : " + node.getZxid() + ", color is : " + node.getColor());
        }
    }

}
