package CSCI.SDC3901.Lab1;

/**
 * <h2> Node </h2>
 *
 * <p>
 *     This class has the getters and setters for the Node class.
 * </p>
 *
 * @author Boon
 * @author Abhinav
 * @version 1.0
 * @since   2023-01-13
 **/
public class Node<T, G> {

    private T key;
    private G value;


    /**
     * <p>
     *     Parameterized constructor of the Node class.
     * </p>
     *
     * @param key first item of the Node entry
     * @param value second item of the Node entry
     *
     *
     **/
    public Node(T key, G value) {
        this.key = key;
        this.value = value;
    }




    /**
     *
     * Returns the current {@code key} object.
     *
     **/
    public T getKey() {
        return this.key;
    }


    /**
     *
     * Returns the current {@code value} object.
     *
     **/
    public G getValue() {
        return this.value;
    }


    /**
     *
     * Sets the {@code value} object with the object in the function parameter.
     *
     **/
    public void setValue(G value) {
        this.value = value;
    }


    /**
     * Prints the current Node object data in a String format.
     *
     **/
    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
