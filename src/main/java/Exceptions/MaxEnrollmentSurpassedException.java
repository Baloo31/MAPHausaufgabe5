package Exceptions;

/**
 * Thrown when the max enrollment is surpassed
 */
public class MaxEnrollmentSurpassedException extends Exception{
    public MaxEnrollmentSurpassedException(String message){
        super(message);
    }
}
