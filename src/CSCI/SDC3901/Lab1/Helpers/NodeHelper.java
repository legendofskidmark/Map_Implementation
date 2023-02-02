package CSCI.SDC3901.Lab1.Helpers;

import CSCI.SDC3901.Lab1.Node;

/**
 * <h2> NodeHelper </h2>
 *
 * <p>
 *     This helper class aids the Node class.
 * </p>
 *
 * @author Boon
 * @author Abhinav
 * @version 1.0
 * @since   2023-01-13
 **/
public class NodeHelper {

    /**
     * Creates a data store node with the given {@code key}, {@code value} pairs.
     * @return The new node which is created with the given {@code key}, {@code value} pairs.
     *
     **/
    public static <T, G> Node<T, G> createDataStoreNode(T key, G value) {
        Node<T, G> newNode = new Node<>(key, value); // Create a Node object with the given key, value pairs.
        return newNode;
    }

}
