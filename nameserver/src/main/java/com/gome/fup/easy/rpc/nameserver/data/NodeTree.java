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

    private Node get(long zxid, Node node) {
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
        if (root == null) {
            node.setColor(BLACK);
            root = node;
        } else {
            put(root, node);
            fixInsert(node);
        }
        size.incrementAndGet();
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
        }
    }

    // TODO 删除节点
    public Node remove(long zxid) {
        Node node = get(zxid);
        fixRemove(node);
        size.decrementAndGet();
        return node;
    }

    // TODO 添加之后修复树结构
    private void fixInsert(Node node) {
        Node parent = node.getParent();
        if (parent.isRed()) {     //只有当父节点是红色时，才需要进行修复
            Node uncle = node.getUncle();
            Node ancestor = parent.getParent();
            if (uncle != null && uncle.isRed()) {                    //叔叔节点是红色
                parent.setColor(BLACK);
                uncle.setColor(BLACK);
                ancestor.setColor(RED);
                fixInsert(ancestor);
            } else {                                //叔叔节点是黑色的
                if (node == parent.getRight()) {     //当前节点是父节点的右节点
                    rotatingLeft(parent);
                    fixInsert(parent);
                } else {
                    parent.setColor(BLACK);
                    ancestor.setColor(RED);
                    rotatingRight(ancestor);
                    node.getRoot().setColor(BLACK);
                }
            }
        }
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
