package CSCI.SDC3901.Assignment2.Model;

public class TreeNode {

    String key;
    TreeNode left;
    TreeNode right;


    public TreeNode(String key, TreeNode left, TreeNode right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    public TreeNode(String key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
