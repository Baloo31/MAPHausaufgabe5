package Repository;

import java.util.List;

/**
 * CRUD operations repository interface
 * @param <T>
 */
public interface ICrudRepository<T> {


    /**
     * adds an object
     * @param obj : an object to add (T)
     * @return the added object
     */
    T create(T obj);


    /**
     * @return all objects
     */
    List<T> getAll();


    /**
     * updates an object
     * @param obj : object to update
     * @return updated object
     */
    T update(T obj);


    /**
     * deletes and object
     * @param obj : object to delete
     */
    void delete(T obj);

}
