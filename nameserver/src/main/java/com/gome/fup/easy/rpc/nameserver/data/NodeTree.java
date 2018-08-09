package com.gome.fup.easy.rpc.nameserver.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
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
        put(create(zxid, value));
    }

    private Node create(long zxid, byte[] value) {
        Node node = new Node();
        node.setZxid(zxid);
        node.setValue(value);
        return node;
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

    public Node remove(long zxid) {
        Node node = get(zxid);
        if (node != null) {
            //根据二叉树特性，删除节点
            delete(node);
            fixRemove(node);
            size.decrementAndGet();
            return node;
        }
        return null;
    }

    private void delete(Node node) {
        Node parent = node.getParent();
        if (isLeaf(node)) {    //叶子节点，则直接删除
            if (parent != null) {
                if (parent.getLeft() == node) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
            }
        } else {
            if (node.getLeft() != null && node.getRight() != null) {    //有两个子节点
                //获取右子树中最小的元素，并设置到node位置
                Node n = node.getRight();
                while (n.getLeft() != null) {
                    n = n.getLeft();
                }
                Node newNode = create(n.getZxid(), n.getValue());
                newNode.setColor(n.getColor());
                newNode.setParent(parent);
                newNode.setLeft(node.getLeft());
                newNode.setRight(node.getRight());
                if (parent.getLeft() == node) {
                    parent.setLeft(newNode);
                } else {
                    parent.setRight(newNode);
                }
                //递归的删除n节点
                delete(n);
            } else {    //有一个子节点
                Node child;
                if (node.getLeft() != null) {
                    child = node.getLeft();
                } else {
                    child = node.getRight();
                }
                if (parent.getLeft() == node) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
                child.setParent(parent);
            }
        }
    }

    private void fixInsert(Node node) {
        Node parent = node.getParent();
        if (parent != null && parent.isRed()) {                               //父节点是否是红色，若是黑色，则不用修复结构
            Node ancestor = parent.getParent();
            if (ancestor == null) {
                return;
            } else {
                Node uncle = getUncle(node);
                if (uncle != null && uncle.isRed()) {
                    parent.setColor(BLACK);
                    uncle.setColor(BLACK);
                    ancestor.setColor(RED);
                    fixInsert(ancestor);                        //已祖父节点继续修复结构
                } else {
                    if (isBothSides(node)) {                    //一字型
                        parent.setColor(BLACK);
                        ancestor.setColor(RED);
                        if (ancestor.getRight() == parent) {    //对祖父节点进行左旋
                            rotatingLeft(ancestor);
                        } else {
                            rotatingRight(ancestor);            //对组件节点进行右旋
                        }
                    } else {                                    //之字形
                        node.setColor(BLACK);
                        ancestor.setColor(RED);
                        if (parent.getLeft() == node) {         //右-左双旋
                            rotatingRight(parent);
                            rotatingLeft(ancestor);
                        } else {                                //左-右双旋
                            rotatingLeft(parent);
                            rotatingRight(ancestor);
                        }
                    }
                }
                resetRoot();
            }
        }
    }

    private void fixRemove(Node node) {
        if (!node.isRed() && node != root) {
            Node parent = node.getParent();
            Node brother;
            if (node == parent.getLeft()) {
                brother = parent.getRight();
            } else {
                brother = parent.getLeft();
            }
            if (null != brother && brother.isRed()) {   //兄弟节点是红色的
                if (brother == parent.getRight()) {
                    rotatingLeft(parent);
                } else {
                    rotatingRight(parent);
                }
                brother.setColor(BLACK);
                parent.setColor(RED);
                fixRemove(node);
            } else {
                Node left = brother.getLeft();
                Node right = brother.getRight();
            }
        }
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

        if (parent != null) {
            if (parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
        }

        if (left != null) {
            left.setParent(node);
        }
        return node;
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

        if (parent != null) {
            if (parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
        }

        if (right != null) {
            right.setParent(node);
        }
        return node;
    }

    private Node getUncle(Node node) {
        Node parent = node.getParent();
        Node ancestor = parent.getParent();
        if (ancestor == null) {
            return null;
        } else {
            if (ancestor.getLeft() == parent) {
                return ancestor.getRight();
            } else {
                return ancestor.getLeft();
            }
        }
    }

    /**
     * 判断节点node与其父节点和祖父节点是否成一字型
     * @param node
     * @return true:一字型，false:之字型
     */
    private boolean isBothSides(Node node) {
        Node parent = node.getParent();
        Node ancestor = parent.getParent();
        boolean left = ancestor.getLeft() == parent && parent.getLeft() == node;
        boolean right = ancestor.getRight() == parent && parent.getRight() == node;
        return left || right;
    }

    /**
     * 前序遍历
     * @return
     */
    public List<Node> preOrder() {
        List<Node> list = new ArrayList<Node>(size.get());
        preOrder(list, root);
        return list;
    }

    /**
     * 中序遍历
     * @return
     */
    public List<Node> middleOrder() {
        List<Node> list = new ArrayList<Node>(size.get());
        middleOrder(list, root);
        return list;
    }

    /**
     * 后序遍历
     * @return
     */
    public List<Node> postOrder() {
        List<Node> list = new ArrayList<Node>(size.get());
        postOrder(list, root);
        return list;
    }

    private void preOrder(List<Node> list, Node node) {
        if (node != null) {
            list.add(node);
            preOrder(list, node.getLeft());
            preOrder(list, node.getRight());
        }
    }

    private void middleOrder(List<Node> list, Node node) {
        if (node != null) {
            middleOrder(list, node.getLeft());
            list.add(node);
            middleOrder(list, node.getRight());
        }
    }

    private void postOrder(List<Node> list, Node node) {
        if (node != null) {
            postOrder(list, node.getLeft());
            postOrder(list, node.getRight());
            list.add(node);
        }
    }

    public int size() {
        return size.get();
    }

    public Node getRoot() {
        return root;
    }

    private void resetRoot() {
        Node node = this.root;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        this.root = node;
    }

    private boolean isLeaf(Node node){
        return node.getLeft() == null && node.getRight() == null;
    }

}
