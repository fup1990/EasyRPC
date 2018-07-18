package com.gome.fup.easy.rpc.nameserver.data;

/**
 * Created by fupeng-ds on 2018/7/16.
 */
public class Node {

    //节点id
    private long zxid;

    //节点数据
    private byte[] value;

    //父节点
    private Node parent;

    //左子节点
    private Node left;

    //右子节点
    private Node right;

    //节点颜色，true-red,false-black
    private boolean color = true;

    public Node() {
    }

    public long getZxid() {
        return zxid;
    }

    public void setZxid(long zxid) {
        this.zxid = zxid;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getUncle() {
        if (parent != null) {
            Node ancestor = parent.getParent();
            if (ancestor != null) {
                if (parent == ancestor.getLeft()) {
                    return ancestor.getRight();
                } else {
                    return ancestor.getLeft();
                }
            }
        }
        return null;
    }

    public Node getRoot() {
        if (parent != null) {
            return parent.getRoot();
        } else {
            return this;
        }
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public boolean isRed() {
        return color;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    boolean isLeaf(){
        return left == null && right == null;
    }

    boolean isRoot(){
        return parent == null;
    }
}
