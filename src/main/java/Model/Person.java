package Model;

/**
 * Person
 */
public class Person {
    private String firstName;
    private String lastName;


    /**
     * returns a string representation of an object Person
     * @return String
     */
    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


    /**
     * default constructor for an object Person
     */
    public Person(){
        this.firstName="";
        this.lastName="";
    }


    /**
     * constructor for an object Person
     * @param firstName : first name
     * @param lastName : last name
     */
    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }


    /**
     * getter for first name
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * getter for last name
     * @return String
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * setter for first name
     * @param firstName : first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * setter for last name
     * @param lastName : last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
