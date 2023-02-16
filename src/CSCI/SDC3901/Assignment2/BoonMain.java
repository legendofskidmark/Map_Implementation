package CSCI.SDC3901.Assignment2;

public class BoonMain {

    public static void main(String[] args) {

        AmortizedTree tree = new AmortizedTree();

        tree.add("ball");
        tree.add("cat");
        tree.add("apple");
        tree.add("dog");
        tree.add("fish");
        tree.add("egg");
        tree.add("hut");
        tree.add("gun");
        tree.add("moon");
        tree.add("net");
        tree.add("sun");
        tree.add("jeep");
        tree.add("rain");
        tree.add("ink");
        tree.add("owl");
        tree.add("queen");
        tree.add("king");
        tree.add("pen");
        tree.add("lion");


        System.out.println(tree.printTree());
    }
}
