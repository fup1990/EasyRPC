package com.gome.fup.easy.rpc.nameserver.data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 红黑树实现
 * 特性：
 * 1、每个节点都有颜色之分，或者是红色，或者是黑色
 * 2、根节点是黑色
 * 3、如果一个节点是红色的，则它的子节点必须是黑色
 * 4、一个节点，到它的子孙叶子节点的每一条路径必须包含相同数目的黑色节点
 * Created by fupeng-ds on 2018/7/16.
 */
public class NodeTree {

    private static final boolean RED = true;

    private static final boolean BLACK = false;

    private AtomicInteger size = new AtomicInteger(0);

    //根节点
    private Node root;

    public NodeTree() {
    }

    public Node get(long zxid) {
        return get(zxid, root);
    }

    public Node get(long zxid, Node node) {
        if (node == null) {
            return null;
        }
        Node n;
        long id = node.getZxid();
        if (id == zxid) {
            return node;
        } else if (id < zxid) {
            n = node.getRight();
        } else {
            n = node.getLeft();
        }
        return get(zxid, n);
    }

    public void put(long zxid, byte[] value) {
        Node node = new Node();
        node.setZxid(zxid);
        node.setValue(value);
        put(node);
    }

    public void put(Node node) {
        if (node != null) {
            if (root == null) {
                node.setColor(BLACK);
                root = node;
            } else {
                put(root, node);
            }
        }
    }

    private void put(Node parent, Node node) {
        if (parent.getZxid() == node.getZxid()) {
            parent.setValue(node.getValue());
        } else {
            if (parent.getZxid() > node.getZxid()) {
                Node left = parent.getLeft();
                if (left == null) {
                    parent.setLeft(node);
                    node.setParent(parent);
                } else {
                    put(left, node);
                }
            } else {
                Node right = parent.getRight();
                if (right == null) {
                    parent.setRight(node);
                    node.setParent(parent);
                } else {
                    put(right, node);
                }
            }
            fixInsert(node);
        }
        size.incrementAndGet();
    }

    // TODO 删除节点
    public Node remove(long zxid) {
        return null;
    }

    // TODO 添加之后修复树结构
    private void fixInsert(Node node) {
    }

    // TODO 删除之后修复树结构
    private void fixRemove(Node node) {
    }

    /**
     * 对node进行左旋
     * @param node
     */
    private Node rotatingLeft(Node node) {
        Node parent = node.getParent();
        Node right = node.getRight();
        Node left = right.getLeft();

        right.setParent(parent);
        right.setLeft(node);
        node.setParent(right);
        node.setRight(left);

        return right;
    }

    /**
     * 对node进行右旋
     * @param node
     */
    private Node rotatingRight(Node node) {
        Node parent = node.getParent();
        Node left = node.getLeft();
        Node right = left.getRight();

        left.setParent(parent);
        left.setRight(node);
        node.setParent(left);
        node.setLeft(right);

        return left;
    }

    /**
     * 转换颜色
     * @param node
     */
    private void flipColors(Node node) {
        node.setColor(RED);
        Node left = node.getLeft();
        if (left != null) {
            left.setColor(BLACK);
        }
        Node right = node.getRight();
        if (right != null) {
            right.setColor(BLACK);
        }
    }

    public int size() {
        return size.get();
    }

}