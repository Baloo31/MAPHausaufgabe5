package Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * CRUD operation repository interface
 * @param <T>
 */
public interface ICrudRepository<T> {


    /**
     * adds an object
     * @param obj : an object to add (T)
     */
    void create(T obj) throws SQLException;


    /**
     * @return all objects
     */
    List<T> getAll() throws SQLException;


    /**
     * updates an object
     * @param obj : object to update
     */
    void update(T obj) throws SQLException;


    /**
     * deletes and object
     * @param obj : object to delete
     */
    void delete(T obj) throws SQLException;

}
