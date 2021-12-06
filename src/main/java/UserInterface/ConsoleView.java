package UserInterface;

import Controller.RegistrationSystem;
import Exceptions.*;
import Model.Course;
import Model.Student;
import Model.Teacher;

import java.util.Scanner;

/**
 * User interface
 */
public class ConsoleView {
    private RegistrationSystem registrationSystem;
    private Scanner inputScanner;


    /**
     * Constructor for the user interface
     */
    public ConsoleView() {
        registrationSystem = new RegistrationSystem("course.json", "student.json", "teacher.json");
        inputScanner = new Scanner(System.in);
    }


    /**
     * Starts the application (the option menu)
     */
    public void start(){

        // data from json files is read
        registrationSystem.readAllData();

        int option = -1;
        while (option != 0) {
            this.showMenu();
            System.out.print("Please choose an Option : ");
            option = inputScanner.nextInt();
            if (option > 13 || option < 0){
                System.out.println("This Option does not exist, please try again !");
            }

            if (option == 1) {
                this.addStudent();
            } else if (option == 2) {
                this.addTeacher();
            } else if (option == 3) {
                this.addCourse();
            } else if (option == 4) {
                this.register();
            } else if (option == 5) {
                this.retrieveFree();
            } else if (option == 6) {
                this.retrieveAll();
            } else if (option == 7) {
                this.teacherDeleteCourse();
            } else if (option == 8) {
                this.showAllTeachers();
            } else if (option == 9) {
                this.showAllStudents();
            } else if (option == 10) {
                this.showStudentsSortedById();
            } else if (option == 11) {
                this.showCoursesSortedByName();
            } else if (option == 12) {
                this.filterStudentsEnrolled();
            } else if (option == 13) {
                this.filterCoursesWithStudents();
            }
        }

        // The new data is written to the json files
        registrationSystem.writeAllData();

        System.out.println("The Application was closed !\s");
    }


    /**
     * gets from user the input for adding a student
     */
    public void addStudent() {
        inputScanner.nextLine();

        System.out.print("Enter first name : ");
        String firstName = inputScanner.nextLine();

        System.out.print("Enter last name : ");
        String lastName = inputScanner.nextLine();

        System.out.print("Enter id : ");
        long id = inputScanner.nextLong();

        try {
            registrationSystem.addStudent(firstName, lastName, id);
            System.out.println("Student added successfully !");
        } catch (AlreadyExistsException e) {
            System.out.println("Student already exists !");
        }
    }


    /**
     * gets from user the input for adding a teacher
     */
    public void addTeacher(){
        inputScanner.nextLine();

        System.out.print("Enter first name : ");
        String firstName = inputScanner.nextLine();

        System.out.print("Enter last name : ");
        String lastName = inputScanner.nextLine();

        System.out.print("Enter id : ");
        long id = inputScanner.nextLong();

        try {
            registrationSystem.addTeacher(firstName, lastName, id);
            System.out.println("Teacher added successfully !");
        } catch (AlreadyExistsException e) {
            System.out.println("Teacher already exists !");
        }
    }


    /**
     * gets from user the input for adding a course
     */
    public void addCourse(){
        inputScanner.nextLine();

        System.out.print("Enter course name : ");
        String name = inputScanner.nextLine();

        System.out.print("Enter a teacher id (that actually exists) : ");
        long teacherId = inputScanner.nextLong();

        System.out.print("Enter max enrollment : ");
        int maxEnrollment = inputScanner.nextInt();

        System.out.print("Enter number of credits : ");
        int credits = inputScanner.nextInt();

        System.out.print("Enter course id : ");
        long courseId = inputScanner.nextLong();


        try {
            registrationSystem.addCourse(name, teacherId, maxEnrollment, credits, courseId);
            System.out.println("Course added successfully !");
        } catch (AlreadyExistsException e) {
            System.out.println("Course already exists !");
        } catch (ElementDoesNotExistException e) {
            System.out.println("That teacher does not exist !");
        }
    }


    /**
     * gets from user the input for registering a student to a course
     */
    public void register(){
        inputScanner.nextLine();

        System.out.print("Enter course id : ");
        long courseId = inputScanner.nextLong();

        System.out.print("Enter student id : ");
        long studentId = inputScanner.nextLong();

        try {
            registrationSystem.register(courseId, studentId);
            System.out.println("Successfully registered to the course !");
        } catch (ElementDoesNotExistException e) {
            System.out.println("The Course or the Student could not be found !");
        } catch (MaxCreditsSurpassedException e) {
            System.out.println("Can't perform the operation ! The student will have more than 30 credits !");
        } catch (MaxEnrollmentSurpassedException e) {
            System.out.println("This course has no available places !");
        } catch (AlreadyExistsException e) {
            System.out.println("Student is already registered to this course !");
        }
    }


    /**
     * shows the courses with free places
     */
    public void retrieveFree(){
        for (Course course : registrationSystem.retrieveCoursesWithFreePlaces()){
            System.out.println(course);
        }
    }

    /**
     * shows all courses
     */
    public void retrieveAll(){
        for (Course course : registrationSystem.getAllCourses()){
            System.out.println(course);
        }
    }


    /**
     * gets from user the input for deleting a course
     */
    public void teacherDeleteCourse(){
        inputScanner.nextLine();

        System.out.print("Enter teacher id : ");
        long teacherId = inputScanner.nextLong();

        System.out.print("Enter course id : ");
        long courseId = inputScanner.nextLong();

        try {
            registrationSystem.deleteTeacherCourse(courseId, teacherId);
            System.out.println("Course successfully deleted !");
        } catch (ElementDoesNotExistException e) {
            System.out.println("Course or teacher does not exist !");
        } catch (NotTeachingTheCourseException e) {
            System.out.println("The specified teacher is not teaching this course !");
        }
    }


    /**
     * shows all teachers
     */
    public void showAllTeachers(){
        for (Teacher teacher : registrationSystem.retrieveAllTeachers()){
            System.out.println(teacher);
        }
    }


    /**
     * shows all students
     */
    public void showAllStudents(){
        for (Student student : registrationSystem.retrieveAllStudents()){
            System.out.println(student);
        }
    }


    /**
     * shows all students sorted ascending by their id
     */
    public void showStudentsSortedById(){
        for (Student student : registrationSystem.sortStudentsById()){
            System.out.println(student);
        }
    }


    /**
     * shows all courses sorted alphabetically by their name
     */
    public void showCoursesSortedByName(){
        for (Course course : registrationSystem.sortCoursesByName()){
            System.out.println(course);
        }
    }


    /**
     * filters the students enrolled for at least one course
     */
    public void filterStudentsEnrolled(){
        for (Student student : registrationSystem.filterStudentsEnrolled()){
            System.out.println(student);
        }
    }


    /**
     * filters the courses with at least one student enrolled for
     */
    public void filterCoursesWithStudents(){
        for (Course course : registrationSystem.filterCoursesWithStudents()){
            System.out.println(course);
        }
    }


    /**
     * shows the user menu
     */
    public void showMenu(){
        System.out.print("""
                0. Exit\s
                1. Add student\s
                2. Add teacher\s
                3. Add course\s
                4. Register a student to a course\s
                5. Retrieve courses with free places\s
                6. Retrieve all available courses\s
                7. Delete a Teachers course\s
                8. Show all teachers\s
                9. Show all students\s
                10. Show students sorted by id\s
                11. Show courses sorted by name\s
                12. Filter students enrolled for at least a course\s
                13. Filter courses with at least one student enrolled for
                """);
    }
}
