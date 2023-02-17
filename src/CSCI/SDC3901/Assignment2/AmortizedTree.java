package CSCI.SDC3901.Assignment2;

import CSCI.SDC3901.Assignment2.Interfaces.Searchable;
import CSCI.SDC3901.Assignment2.Interfaces.TreeDebug;
import CSCI.SDC3901.Assignment2.Model.NodeMetaData;
import CSCI.SDC3901.Assignment2.Model.TreeNode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class AmortizedTree implements Searchable, TreeDebug {

    private String[] keysArray = new String[1];
    private TreeNode root = null;

    //gives the number of nodes in the tree
    private int treeSize = 0;

    //indicates the current empty index in the array
    private int arrayIndex = 0;

    private String printTreeStr = "";
    private boolean insertionError = false;
    private boolean removedNodeFromTree = false;

    private int orderedIndex = 0;
    private int treeMetaDataIndex = 0;

    private NodeMetaData[] treeMetaData;


    @Override
    public boolean add(String key) {

        if (key == null) return false;
        if (key.isEmpty()) return false;
        if (findInTree(key, root) != null) return false;

        orderedIndex = 0;

        if (arrayIndex < keysArray.length) {
            if (indexOfElement(key) == -1) {
                keysArray[arrayIndex] = key;
                incrementArrayIndex();
            } else {
                return false;
            }
        } else if (arrayIndex == keysArray.length) {
            sortArray();
            String[] keysCopy = keysArray.clone();
            int[] orderedIndices = new int[keysCopy.length];
            insertArrayIntoTree(0, arrayIndex - 1, orderedIndices);

            for(int i = 0 ; i < orderedIndices.length ; i++) {
                root = insert(root, keysCopy[orderedIndices[i]]);
            }
            resetArrayParameters();
            keysArray[arrayIndex] = key;
            incrementArrayIndex();
        }

        if (insertionError) {
            insertionError = false;
            return false;
        }
        return true;
    }

    @Override
    public boolean find(String key) {
        if (key == null) return false;
        if (key.isEmpty()) return false;

        int index = indexOfElement(key);
        TreeNode node = findInTree(key, root);

        if (index != -1 || node != null) return true;
        return false;
    }

    @Override
    public boolean remove(String key) {
        if (key == null) return false;
        if (key.isEmpty()) return false;

        if(removeFromArray(key)) {
            return true;
        }

        root = removeFromTree(root, key);
        if (removedNodeFromTree) {
            treeSize--;
            removedNodeFromTree = false;
            return true;
        }

        return false;
    }

    @Override
    public int size() {
        return arrayIndex + treeSize;
    }

    private boolean removeFromArray(String key) {
        for(int i = 0 ; i < arrayIndex ; i++) {
            if (keysArray[i].compareToIgnoreCase(key) == 0) {
                deleteElementAtIndex(i);
                decrementArrayIndex();
                return true;
            }
        }
        return false;
    }

    private TreeNode removeFromTree(TreeNode root, String key) {
        if (root == null) return null;

        if (root.getKey().compareToIgnoreCase(key) < 0) {
            root.setRight(removeFromTree(root.getRight(), key));
        } else if (root.getKey().compareToIgnoreCase(key) > 0) {
            root.setLeft(removeFromTree(root.getLeft(), key));
        } else {
            //boolean flag to indicate if a node deletion has happened
            removedNodeFromTree = true;
            // if the key to be deleted is a leaf
            if (root.getLeft() == null && root.getRight() == null) {
                return null;
            } else if (root.getLeft() == null) { // key to be deleted has only right subtree
                return root.getRight();
            } else if (root.getRight() == null) { // key to be deleted has only left subtree
                return root.getLeft();
            } else {
                // key to be deleted has both children
                String minValueInRightSubTree = minimumOfSubtree(root.getRight());
                root.setKey(minValueInRightSubTree);
                root.setRight(removeFromTree(root.getRight(), minValueInRightSubTree));
            }
        }
        return root;
    }

    private String minimumOfSubtree(TreeNode root) {
        while (root.getLeft() != null) {
            root = root.getLeft();
        }
        return root.getKey();
    }

    private TreeNode findInTree(String key, TreeNode root) {
        if (key == null || root == null) return null;
        if (root.getKey().compareToIgnoreCase(key) == 0) return root;
        if (root.getKey().compareToIgnoreCase(key) < 0) return findInTree(key, root.getRight());
        return findInTree(key, root.getLeft());
    }

    private void resetArrayParameters() {
        for(int i = 0 ; i < keysArray.length ; i++) {
            keysArray[i] = null;
        }
        arrayIndex = 0;
    }

    private void incrementArrayIndex() {
        arrayIndex++;
    }

    private void decrementArrayIndex() {
        arrayIndex--;
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

        //todo: return null in case of error, like for empty tree ?
        treeMetaData = new NodeMetaData[treeSize];
        printTreeStr = "";
        treeMetaDataIndex = 0;
        printTreeHelper(root, 1);
        for(int i = 0 ; i < treeMetaDataIndex ; i++) {
            printTreeStr += treeMetaData[i].getKey() + " " + treeMetaData[i].getDepth() + "\n";
        }
        //reset the index
        treeMetaDataIndex = 0;
        return printTreeStr;
    }

    @Override
    public String[] awaitingInsertion() {
        //todo: return null, when array is empty (size 0) or when some or all elements are null ?
        String[] keysCopy = keysArray.clone();
        Arrays.sort(keysCopy, new Comparator<String>() {
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

        return keysCopy; //todo: clarification from Prof
    }

    @Override
    public String[] treeValues() {
        //todo: return null, when ?
        treeMetaData = new NodeMetaData[treeSize];
        String[] result = new String[treeSize];
        treeMetaDataIndex = 0;
        printTreeHelper(root, 1);
        for(int i = 0 ; i < treeMetaDataIndex ; i++) {
            result[i] = treeMetaData[i].getKey();
        }
        treeMetaDataIndex = 0;
        return result;
    }

    @Override
    public int depth(String key) {
        if (key == null) return size() + 1;
        if (key.isEmpty()) return size() + 1;
        int depth = depthHelper(root, key, 1);

        if (depth == 0) {
            //key is not in tree
            //search in array
            int index = indexOfElement(key);
            if (index != -1) return -1 * index;
            else return size() + 1;
        } else {
            return depth;
        }
    }

    private int depthHelper(TreeNode root, String key, int depth) {
        if (root == null) return 0;
        if (root.getKey().compareToIgnoreCase(key) == 0) return depth;

        if (root.getKey().compareToIgnoreCase(key) < 0) {
            return depthHelper(root.getRight(), key, depth + 1);
        }
        return depthHelper(root.getLeft(), key, depth + 1);
    }

    private void printTreeHelper(TreeNode root, int depth) {
        if (root != null) {
            printTreeHelper(root.getLeft(), depth + 1);
            treeMetaData[treeMetaDataIndex] = new NodeMetaData(root.getKey(), depth);
            treeMetaDataIndex++;
            printTreeHelper(root.getRight(), depth + 1);
        }
    }
}
