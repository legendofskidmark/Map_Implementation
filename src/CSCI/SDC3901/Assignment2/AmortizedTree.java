package CSCI.SDC3901.Assignment2;

import CSCI.SDC3901.Assignment2.Interfaces.Searchable;
import CSCI.SDC3901.Assignment2.Interfaces.TreeDebug;
import CSCI.SDC3901.Assignment2.Model.TreeNode;

import java.util.Arrays;
import java.util.Comparator;

public class AmortizedTree implements Searchable, TreeDebug {

    private String[] keysArray = new String[1];
    private TreeNode root = null;

    //gives the number of nodes in the tree
    private int treeSize = 0;

    //indicates the current empty index in the array
    private int arrayIndex = 0;

    private String printTreeStr = "";
    private boolean insertionError = false;

    private int orderedIndex = 0;

    @Override
    public boolean add(String key) {
        orderedIndex = 0;

        if (arrayIndex < keysArray.length) {
            keysArray[arrayIndex] = key;
            incrementArrayIndex();
        }

        if (arrayIndex == keysArray.length) {
            sortArray();
            String[] keysCopy = keysArray.clone();
            int[] orderedIndices = new int[keysCopy.length];
            insertArrayIntoTree(0, arrayIndex - 1, orderedIndices);

            for(int i = 0 ; i < orderedIndices.length ; i++) {
                root = insert(root, keysCopy[orderedIndices[i]]);
            }
            resetArrayParameters();
        }

        return false;
    }

    private void resetArrayParameters() {
        for(int i = 0 ; i < keysArray.length ; i++) {
            if (keysArray[i] == null) {
                arrayIndex = i;
                break;
            }
        }
    }

    private void incrementArrayIndex() {
        arrayIndex++;
    }

    private void sortArray() { //src: https://stackoverflow.com/questions/14514467/sorting-array-with-null-values
        //todo: can use this sort library or should i implement my own
        Arrays.sort(keysArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return o1.compareTo(o2);
            }});
    }

    private void increaseArraySize() {
        int newSize = (int)Math.ceil(Math.log(treeSize) / Math.log(2)); //src : https://www.geeksforgeeks.org/how-to-calculate-log-base-2-of-an-integer-in-java/
        if (newSize == 0) newSize = 1;
        String[] keysCopy = keysArray.clone();
        keysArray = new String[newSize];

        //copy old elements into the newly resized array
        if (keysArray.length >= keysCopy.length) {
            for(int i = 0 ; i < keysCopy.length ; i++) keysArray[i] = keysCopy[i];
        }
    }


    private int indexOfElement(String key) {
        for(int i = 0 ; i < keysArray.length ; i++) {
            if (keysArray[i] != null && keysArray[i].compareToIgnoreCase(key) == 0) return i;
        }
        return -1;
    }

    private void deleteElementAtIndex(int index) {
        for(int i = index ; i+1 < keysArray.length ; i++) {
            keysArray[i] = keysArray[i+1];
        }
        keysArray[keysArray.length - 1] = null;
    }

    private void insertArrayIntoTree(int start, int end, int[] orderToBeAdded) {
        //loop through selecting mid and call insert on each element

        if (start <= end) {
            int mid = (int) Math.ceil((start + end) * 1.0/2.0);
            orderToBeAdded[orderedIndex] = mid;
            orderedIndex++;
            insertArrayIntoTree(start, mid - 1, orderToBeAdded);
            insertArrayIntoTree(mid + 1, end, orderToBeAdded);
        }
    }

    private TreeNode insert(TreeNode root, String key) {

        if (root == null) {
            root = new TreeNode(key);
            treeSize++;
            int indexInArray = indexOfElement(key);
            if (indexInArray != -1) {
                deleteElementAtIndex(indexInArray);
                increaseArraySize();
            }
            return root;
        }

        if (root.getKey().compareToIgnoreCase(key) > 0) {
            root.setLeft(insert(root.getLeft(), key));
        } else if (root.getKey().compareToIgnoreCase(key) < 0) {
            root.setRight(insert(root.getRight(), key));
        }

        if (root.getKey().compareToIgnoreCase(key) == 0) insertionError = true;
        return root;
    }


    @Override
    public String printTree() {

        printTreeHelper(root);
        return printTreeStr;
    }

    private void printTreeHelper(TreeNode root) {
        if (root != null) {
            printTreeHelper(root.getLeft());
            printTreeStr += root.getKey() + " ";
            printTreeHelper(root.getRight());
        }
    }
}
