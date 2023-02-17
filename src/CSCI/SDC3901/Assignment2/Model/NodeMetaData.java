package CSCI.SDC3901.Assignment2.Model;

public class NodeMetaData {
    private String key;
    private int depth;

    public NodeMetaData(String key, int depth) {
        this.key = key;
        this.depth = depth;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
