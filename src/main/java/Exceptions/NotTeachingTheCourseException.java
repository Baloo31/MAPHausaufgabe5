package Exceptions;

/**
 * Thrown when a specified teacher is not the one teaching the specified course
 */
public class NotTeachingTheCourseException extends Exception{
    public NotTeachingTheCourseException(String message){
        super(message);
    }
}
