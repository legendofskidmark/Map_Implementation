package CSCI.SDC3901.Lab1.Interfaces;

/**
 * <h2> MapInterface </h2>
 *
 * This interface consists of {@code get(T key)}, {@code put(T key, G value)} methods.
 *
 * @author Boon
 * @author Abhinav
 * @version 1.0
 * @since   2023-01-13
 **/
public interface MapInterface<T, G> {
    public G get(T key);
    public void put(T key, G value);
}
