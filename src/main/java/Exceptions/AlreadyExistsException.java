package Exceptions;

/**
 * Thrown when an object already exists
 */
public class AlreadyExistsException extends Exception{
    public AlreadyExistsException(String message){
        super(message);
    }
}
