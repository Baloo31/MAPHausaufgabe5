package Exceptions;

/**
 * Thrown when the max number of credits is surpassed
 */
public class MaxCreditsSurpassedException extends Exception{

    public MaxCreditsSurpassedException(String message){
        super(message);
    }
}
