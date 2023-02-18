package CSCI.SDC3901.Assignment2;

public class BoonMain {

    public static void main(String[] args) {

        AmortizedTree tree = new AmortizedTree();

//        tree.add("ball");
//        tree.add("cat");
//        tree.add("apple");
//        tree.add("dog");
//        tree.add("fish");
//        tree.add("egg");
//        tree.add("hut");
//        tree.add("gun");
//        tree.add("moon");
//        tree.add("net");
//        tree.add("sun");
//        tree.add("jeep");
//        tree.add("rain");
//        tree.add("ink");
//        tree.add("owl");
//        tree.add("queen");
//        tree.add("jeep");
//        tree.add("king");
//        tree.add("lion");
//        tree.add("pen");
//        tree.add("pen");
//        tree.add("pen");
//        tree.add("tea");
//        tree.add("uno");
//        tree.add("watch");
//        tree.add("van");
//        tree.add("xray");
//        tree.add("zebra");
//        tree.add("you");

        for(int i = 1 ; i <= 25 ; i++) {
            tree.add(String.valueOf(i));
        }

        System.out.println(tree.printTree());

        printMy(tree.awaitingInsertion());
    }

    static void printMy(String [] arr) {
        for(int i = 0 ; i < arr.length ; i++) System.out.println(arr[i]);
//        System.out.println("------------");
    }
}
