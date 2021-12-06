package Repository;

import java.io.IOException;
import java.util.List;

public interface FileRepository<T> extends ICrudRepository<T>
{

    /**
     * Stores data in json files
     * @throws IOException writing failed
     */
    void writeData() throws IOException;



    /**
     * Reads the data inside a json file
     * @return list of created objects
     * @throws IOException reading failed
     */
    List<T> readData() throws IOException;


}
