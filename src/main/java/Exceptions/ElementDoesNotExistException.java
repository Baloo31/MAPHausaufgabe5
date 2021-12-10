package Exceptions;

/**
 * Thrown when an object does not exist
 */
public class ElementDoesNotExistException extends Exception{

    public ElementDoesNotExistException(String message){
        super(message);
    }
}
