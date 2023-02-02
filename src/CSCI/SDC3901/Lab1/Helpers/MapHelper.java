package CSCI.SDC3901.Lab1.Helpers;

import CSCI.SDC3901.Lab1.Node;
import java.util.LinkedList;

/**
 * <h2> MapHelper </h2>
 *
 * <p>
 *     This helper class aids the Map class.
 * </p>
 *
 * @author Boon
 * @author Abhinav
 * @version 1.0
 * @since   2023-01-13
 **/
public class MapHelper {

    /**
     * A helper method to find a data entry on the Map object with the given {@code key}.
     *
     * @return If there exists an entry in the data store with the given key, that {@code Node} object is returned, else {@code null} is returned.
     **/
    public static <T, G> Node<T, G> findNodeWithGivenKeyInTheMap(LinkedList<Node<T,G>> mapDataStore, T key) {
        // Loop through the entire data store and check if an entry exists with the given key.
        for (Node<T, G> node: mapDataStore) {
            if (node.getKey() == null) { // For null key items in the data store, we cannot use equal() method on it.
                if (node.getKey() == key) return node; // if the given key we have to find is 'null'
            } else if (node.getKey().equals(key)) { // If the entry is found with the given key, that Node object is returned.
                return node;
            }
        }
        return null; // If the key item does not exist, a null is returned.
    }


    /**
     *  Inserts the data node into the Map data store object.
     *
     **/
    public static <T, G> void addEntryInTheMap(LinkedList<Node<T,G>> mapDataStore, T key, G value) {
        Node<T, G> potentialNode;
        potentialNode = MapHelper.findNodeWithGivenKeyInTheMap(mapDataStore, key); //Find a node with the given key in the data store.

        if (potentialNode != null) { //If there exists a key, then we overwrite with the new 'value' item we got in the function parameter.
            potentialNode.setValue(value);
        } else { //Else a new entry is added with the given 'key' and 'value'.
            potentialNode = new Node<T, G>(key, value);
            mapDataStore.add(potentialNode);
        }
    }

}
