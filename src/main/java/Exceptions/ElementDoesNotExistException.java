package Exceptions;

/**
 * Thrown when an object does not exists
 */
public class ElementDoesNotExistException extends Exception{

    public ElementDoesNotExistException(String message){
        super(message);
    }
}
