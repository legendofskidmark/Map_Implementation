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


        System.out.println(tree.depth("1"));

        tree.add("1");

        tree.add("2");

        tree.add("3");

        tree.add("4");
        tree.add("5");

        tree.add("6");
        tree.add("7");
        tree.add("8");
//        tree.remove("1");
//        tree.remove("3");
//        tree.remove("2");

        tree.add("9");
        tree.add("10");
        tree.add("11");
        tree.add("12");
        tree.add("13");
        tree.add("14");


        System.out.println(tree.depth("100"));


//        tree.remove("4");
//        tree.remove("");
//        tree.remove(null);
//        tree.remove("40");

        tree.add("15");
        tree.add("16");
        tree.add("17");
        tree.add("18");
        tree.add("19");
        tree.add("20");
        tree.add("21");
        tree.add("22");
        tree.add("23");
        tree.add("24");
        tree.add("25");
        tree.add("26");

        System.out.println(tree.depth("1"));

        System.out.println(tree.depth("10"));

        System.out.println(tree.depth("9"));
        System.out.println(tree.depth("2400"));




    }

    static void printMy(String [] arr) {
        for(int i = 0 ; i < arr.length ; i++) System.out.println(arr[i]);
        System.out.println("------------");
    }
}
