package CSCI.SDC3901.Lab1;

import CSCI.SDC3901.Lab1.Interfaces.MapInterface;
import CSCI.SDC3901.Lab1.Helpers.MapHelper;
import java.util.LinkedList;


/**
 * <h2> Map </h2>
 *
 * This class implements the {@link MapInterface}.
 *
 * @author Boon
 * @author Abhinav
 * @version 1.0
 * @since   2023-01-13
 **/
public class Map<T, G> implements MapInterface<T, G> {

    private LinkedList<Node<T, G>> mapDataStore;


    /**
     * The default constructor of the Map Class.
     * <p>
     * This Constructor will initialize the datastore object.
     * </p>
     */
    public Map() {
        initMapDataStore();
    }


    /**
     * The parameterized Constructor of the Map Class.
     * <p>
     * The 'key' argument is the first object of the pair that is stored and is used to look up the data in the Map.
     * The keys are always unique in the Map data store.
     * </p>
     * <p>
     * The 'value' argument is the second object of the pair that is being stored. The values need not be unique.
     * </p>
     *
     * @param key used to identify an entry in the datastore.
     * @param value an item associated with the given 'key'
     *
     **/
    public Map(T key, G value) {
        initMapDataStore(); //initialise the dataStore object to be able to use it after the object creation.
        MapHelper.addEntryInTheMap(mapDataStore, key, value);
    }


    /**
     * Inserts a key, value pair into the data store.
     * <p>
     *     This method ensure we are inserting only those entries with an unique key. If there exists a key already it is be overwritten.
     * </p>
     *
     * @param key used to identify an entry in the datastore.
     * @param value an item associated with the given 'key'
     *
     **/
    @Override
    public void put(T key, G value) {
        MapHelper.addEntryInTheMap(mapDataStore, key, value);
    }


    /**
     * Retrieves an entry from the data store with the given 'key'.
     *<p>
     *     If the 'key' item is not present in the data store, a {@code null} will be returned.
     *</p>
     *
     * @param key used to identify an entry in the datastore.
     * @return an Object if an entry exists with the given 'key', or {@code null} is returned.
     **/
    @Override
    public G get(T key) {
        /*
         * Search the data store with the given key item.
         * If it exists, the entire Node object is returned.
         * Else a null is returned.
         */
        Node<T, G> nodeWithGivenKey = MapHelper.findNodeWithGivenKeyInTheMap(mapDataStore, key); //find if there's an entry with the given key
        if (nodeWithGivenKey != null) {
            return nodeWithGivenKey.getValue(); //if it exists, return the value
        }
        return null; //else return null
    }


    /**
     * An overridden version of toString method with custom formatting of the map data items.
     * @return String a formatted String with the Map data store items
     */
    @Override
    public String toString() {
        String result = "";
        for(Node node: mapDataStore) {
            result += "{ key: " + node.getKey() + ", value: " + node.getValue() + "}\n"; //format the node items in a string format.
        }
        return result;
    }

    /**
     *  Initialize the datastore object of the Map class.
     *
     **/
    private void initMapDataStore() {
        mapDataStore = new LinkedList<Node<T, G>>();
    }


}

