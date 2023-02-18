package CSCI.SDC3901.Assignment2;

import CSCI.SDC3901.Assignment2.Interfaces.Searchable;
import CSCI.SDC3901.Assignment2.Interfaces.TreeDebug;
import CSCI.SDC3901.Assignment2.Model.NodeMetaData;
import CSCI.SDC3901.Assignment2.Model.TreeNode;

import java.util.Arrays;
import java.util.Comparator;

public class AmortizedTree implements Searchable, TreeDebug {

    // DEPTH_NOT_FOUND_FACTOR is added to the size of tree when the depth of an element is asked which is non-existent in the tree.
    private static final int DEPTH_NOT_FOUND_FACTOR = 100;

    private String[] keysArray = new String[1];

    // root of the amortized tree
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
    private int reBalanceIndex = 0;

    private NodeMetaData[] treeMetaData;


    @Override
    public boolean add(String key) {

        // input validation
        if (key == null) return false;
        if (key.isEmpty()) return false;
        if (findInTree(key, root) != null) return false;

        orderedIndex = 0;

        // if the array has space to hold the incoming element
        if (arrayIndex < keysArray.length) {
            if (indexOfElement(key) == -1) {
                keysArray[arrayIndex] = key;
                incrementArrayIndex();
            } else {
                return false;
            }
            // if the array has no space left, then we are emptying the current array elements by adding them into the tree in an efficient order
            // which makes the tree less unbalanced
        } else if (arrayIndex == keysArray.length) {
            sortArray();

            // having a copy of the array of elements so that we can modify the actual size of the array and not worry about indices and mis-indexing the elements
            String[] keysCopy = keysArray.clone();
            int[] orderedIndices = new int[keysCopy.length];

            // returns the order in which we have to insert the array elements into tree
            orderOfIndiciesToBeAdded(0, arrayIndex - 1, orderedIndices);

            //insert the elements in the order taken from "orderOfIndiciesToBeAdded()"
            for(int i = 0 ; i < orderedIndices.length ; i++) {
                root = insert(root, keysCopy[orderedIndices[i]]);
            }

            // reset the array index pointers and nullify the new resized array
            resetArrayParameters();

            //insert the incoming element into the array
            keysArray[arrayIndex] = key;
            incrementArrayIndex();
        }

        // if we have an error while adding any node, this flag is going to be turned true.
        if (insertionError) {
            //reset to its original state for next addition check
            insertionError = false;
            return false;
        }
        return true;
    }

    @Override
    public boolean find(String key) {
        // input validation
        if (key == null) return false;
        if (key.isEmpty()) return false;

        // check if the element is in the array
        int index = indexOfElement(key);

        // check if the element is in the tree
        TreeNode node = findInTree(key, root);


        // if its in either of them, return true
        if (index != -1 || node != null) return true;

        //else it means element is not found
        return false;
    }

    @Override
    public boolean remove(String key) {

        // input validation
        if (key == null) return false;
        if (key.isEmpty()) return false;

        // if the element is in array, we will remove it
        if(removeFromArray(key)) {
            return true;
        }

        // else if the element is in the tree, remove from there
        root = removeFromTree(root, key);

        if (removedNodeFromTree) {
            // A node is removed from the tree, so decrease the tree size
            treeSize--;

            // reset the boolean flags for the next removal method calls
            removedNodeFromTree = false;
            return true;
        }

        return false;
    }

    @Override
    public int size() {
        // return the size of array and tree
        return arrayIndex + treeSize;
    }


    //todo: remove unwanted collection imports
    @Override
    public boolean rebalance() {

        int targetCompleteBSTSize = size();

        // tree is empty
        if (targetCompleteBSTSize == 0) return true; //todo: true/false for empty tree

        TreeNode newRoot = new TreeNode("");
        TreeNode newRootCopy = newRoot;

        int currentCompleteBSTNodeCount = targetCompleteBSTSize;

        int queueArrayIndex = 0;
        int queueTopPointer = 0;

        // assuming array of TreeNode as Queue and "queueTopPointer" will always point to the first element to be dequeued from the queue.
        TreeNode[] myQueue = new TreeNode[100000000];
        myQueue[queueArrayIndex] = newRootCopy;

        currentCompleteBSTNodeCount--;

        // construct a complete BST with total number of nodes required when merging unbalanced tree and array
        while(currentCompleteBSTNodeCount > 0 && myQueue[queueTopPointer] != null) {
            newRootCopy = myQueue[queueTopPointer];
            newRootCopy.setLeft(new TreeNode(String.valueOf(currentCompleteBSTNodeCount)));
            currentCompleteBSTNodeCount--;
            if (currentCompleteBSTNodeCount == 0) break;
            queueArrayIndex++;
            myQueue[queueArrayIndex] = newRootCopy.getLeft();
            newRootCopy.setRight(new TreeNode(String.valueOf(currentCompleteBSTNodeCount)));
            currentCompleteBSTNodeCount--;
            if (currentCompleteBSTNodeCount == 0) break;
            queueArrayIndex++;
            myQueue[queueArrayIndex] = newRootCopy.getRight();

            queueTopPointer++;
        }

        // A complete BST structure is ready, now copy values to Complete BST from bottom up so that the BST property is retained
        String[] allValuesOfAmortizedTree = getAllValuesInAmortizedTree(targetCompleteBSTSize);
        InsertElementsInInorderFashion(allValuesOfAmortizedTree, newRoot);

        // reset the index variables
        reBalanceIndex = 0;

        // assign the newly rebalanced to the root
        root = newRoot;
        resetParamsAfterRebalancing(allValuesOfAmortizedTree);

        return true;
    }

    // Insert values into an already structured Complete BST in an inorder fashion
    private void InsertElementsInInorderFashion(String[] allValuesOfAmortizedTree, TreeNode newRoot) {
        if (newRoot != null) {
            InsertElementsInInorderFashion(allValuesOfAmortizedTree, newRoot.getLeft());
            newRoot.setKey(allValuesOfAmortizedTree[reBalanceIndex]);
            reBalanceIndex++;
            InsertElementsInInorderFashion(allValuesOfAmortizedTree, newRoot.getRight());
        }
    }

    @Override
    public boolean rebalanceValue(String key) {
        //todo: testcase handling

        // input validation
        if (key == null) return false;
        if (key.isEmpty()) return false;
        if (size() == 0) return false;

        // if a node is not in the tree and in the array
        if (findInTree(key, root) == null && indexOfElement(key) == -1) return false;

        int targetCompleteBSTSize = size();

        if (targetCompleteBSTSize == 0) return false;

        // get all values in the tree and the array and store it in array
        String[] allValuesOfAmortizedTree = getAllValuesInAmortizedTree(targetCompleteBSTSize);

        // delete the new root value from the array
        int i = 0;
        for( ; i < allValuesOfAmortizedTree.length ; i++) {
            if (allValuesOfAmortizedTree[i].compareToIgnoreCase(key) == 0) break;
        }

        for( ; i+1 < allValuesOfAmortizedTree.length ; i++) allValuesOfAmortizedTree[i] = allValuesOfAmortizedTree[i+1];

        orderedIndex = 0;

        int[] orderToBeInserted = new int[allValuesOfAmortizedTree.length - 1];

        // get the order to be inserted to mitigate imbalances in the tree
        orderOfIndiciesToBeAdded(0, allValuesOfAmortizedTree.length - 2, orderToBeInserted);

        TreeNode newRoot = new TreeNode(key);
        root = newRoot;

        for(i = 0 ; i < orderToBeInserted.length ; i++) {
            root = insert(root, allValuesOfAmortizedTree[orderToBeInserted[i]]);
        }

        resetParamsAfterRebalancing(allValuesOfAmortizedTree);
        return true;
    }

    private void resetParamsAfterRebalancing(String[] allValuesOfAmortizedTree) {
        // reset the count and index variables after performing any operation on the amortized tree
        treeSize = allValuesOfAmortizedTree.length;
        increaseArraySize();
        resetArrayParameters();
    }


    // Returns all the values in the Amortized tree
    private String[] getAllValuesInAmortizedTree(int targetCompleteBSTSize) {
        String[] allValuesOfAmortizedTree = new String[targetCompleteBSTSize];

        String[] awaitingInsertionValues = awaitingInsertion();

        int i = 0;
        for (; i < awaitingInsertionValues.length && awaitingInsertionValues[i] != null; i++) {
            allValuesOfAmortizedTree[i] = awaitingInsertionValues[i];
        }

        String treeValueDepthsStr = printTree();

        if (treeValueDepthsStr != null && !treeValueDepthsStr.isEmpty()) {
            String[] valuesInTree = treeValueDepthsStr.split("\n");
            for (int j = 0; j < valuesInTree.length; j++) {
                allValuesOfAmortizedTree[i] = valuesInTree[j].split(" ")[0];
                i++;
            }
        }

        Arrays.sort(allValuesOfAmortizedTree);
        return allValuesOfAmortizedTree;
    }

    private boolean removeFromArray(String key) {

        // deletes the given value from the array
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
                // key to be deleted has both children. So get the minimum value from the right subtree and make it the new root of that subtree
                String minValueInRightSubTree = minimumOfSubtree(root.getRight());
                root.setKey(minValueInRightSubTree);

                // now remove the minimum value from the right subtree to avoid duplicates.
                root.setRight(removeFromTree(root.getRight(), minValueInRightSubTree));
            }
        }
        return root;
    }

    private String minimumOfSubtree(TreeNode root) {
        // loop till we reach the leftmost node in the subtree rooted with "root" as key
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

        // if the size is less than 3, then the array size must be 1.
        if (newSize == 0) newSize = 1;

        String[] keysCopy = keysArray.clone();
        keysArray = new String[newSize];

        //copy old elements into the newly resized array
        if (keysArray.length >= keysCopy.length) {
            for(int i = 0 ; i < keysCopy.length ; i++) keysArray[i] = keysCopy[i];
        }
    }


    private int indexOfElement(String key) {
        // finds the index of an element in the keys array
        for(int i = 0 ; i < keysArray.length ; i++) {
            if (keysArray[i] != null && keysArray[i].compareToIgnoreCase(key) == 0) return i;
        }

        // return -1 if element is not found
        return -1;
    }

    private void deleteElementAtIndex(int index) {
        // removes an element at a given index by copying its right neighbours to the left
        for(int i = index ; i+1 < keysArray.length ; i++) {
            keysArray[i] = keysArray[i+1];
        }
        keysArray[keysArray.length - 1] = null;
    }

    private void orderOfIndiciesToBeAdded(int start, int end, int[] orderToBeAdded) {
        //recursively traverse and selecting mid and collecting the indices of the insertion order of elements

        if (start <= end) {
            int mid = (int) Math.floor((start + end) * 1.0/2.0);
            orderToBeAdded[orderedIndex] = mid;
            orderedIndex++;
            orderOfIndiciesToBeAdded(start, mid - 1, orderToBeAdded);
            orderOfIndiciesToBeAdded(mid + 1, end, orderToBeAdded);
        }
    }

    private TreeNode insert(TreeNode root, String key) {
    // inserts elements into the unabalnced tree
        if (root == null) {
            root = new TreeNode(key);
            treeSize++;
            // as soon as an element is added in the tree, check if we have to resize the array
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

        // if the element is already in the tree, turn the insertionError flag to be true
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

        if (key == null) return size() + DEPTH_NOT_FOUND_FACTOR;
        if (key.isEmpty()) return size() + DEPTH_NOT_FOUND_FACTOR;
        int depth = depthHelper(root, key, 1);

        if (depth == 0) {
            //key is not in tree
            //search in array
            int index = indexOfElement(key);
            if (index != -1) return -1 * index;
            else return size() + DEPTH_NOT_FOUND_FACTOR;
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
