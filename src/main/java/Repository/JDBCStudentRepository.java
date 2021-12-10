package Repository;

import Model.Student;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

/**
 * JDBCStudentRepository
 */
public class JDBCStudentRepository implements ICrudRepository<Student>{
    private String DB_URL;
    private String USER;
    private String PASS;

    /**
     * Constructor
     * @param url the database url
     * @param user the user to access
     * @param password the password for the user
     */
    public JDBCStudentRepository(String url, String user, String password){
        DB_URL = url;
        USER = user;
        PASS = password;
    }


    /**
     * Adds a new Student to the database
     * @param obj : an object to add (Student)
     * @throws SQLException if a query is incorrect
     */
    @Override
    public void create(Student obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        String insertStudent = String.format("INSERT INTO student(firstName, lastName, studentId) VALUES (\"%s\", \"%s\", %2d)",
                obj.getFirstName(), obj.getLastName(), obj.getStudentId());
        statement.execute(insertStudent);

        statement.close();
        connection.close();
    }


    /**
     * Returns all students
     * @return list of students
     * @throws SQLException if a query is incorrect
     */
    @Override
    public List<Student> getAll() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        List<Student> students = new LinkedList<>();

        String selectAllStudents = "SELECT * FROM student";
        ResultSet resultSet = statement.executeQuery(selectAllStudents);
        while (resultSet.next()){
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            long studentId = resultSet.getLong("studentId");

            List<Long> courses = new LinkedList<>();
            String selectCoursesEnrolled = String.format("SELECT courseId FROM enrolled WHERE studentId=%2d",studentId);
            Statement statement1 = connection.createStatement();
            ResultSet enrolledCourses = statement1.executeQuery(selectCoursesEnrolled);
            while (enrolledCourses.next()){
                courses.add(enrolledCourses.getLong("courseId"));
            }
            statement1.close();

            students.add(new Student(firstName, lastName, courses, studentId));
        }

        statement.close();
        connection.close();
        return students;
    }


    /**
     * Updates a student from the database
     * @param obj : Student to update
     * @throws SQLException if a query is incorrect
     */
    @Override
    public void update(Student obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        String updateStudent = String.format("UPDATE student SET firstName=\"%s\", lastName=\"%s\" WHERE studentId=%2d",
                obj.getFirstName(), obj.getLastName(), obj.getStudentId());
        statement.execute(updateStudent);

        List<Long> updatedEnrolledCourses = obj.getEnrolledCourses();

        String getEnrollment = String.format("SELECT courseId FROM enrolled WHERE studentId=%2d", obj.getStudentId());
        Statement statement1 = connection.createStatement();
        ResultSet enrolledCourses = statement1.executeQuery(getEnrollment);

        // find the deleted enrollment and deleting it from the database
        while (enrolledCourses.next()){
            long courseId = enrolledCourses.getLong("courseId");
            if (!updatedEnrolledCourses.contains(courseId)){
                Statement statement2 = connection.createStatement();
                statement2.execute(String.format("DELETE FROM enrolled WHERE studentId=%2d AND courseId=%2d", obj.getStudentId(), courseId));
                statement2.close();
            } else {
                updatedEnrolledCourses.remove(courseId);
            }
        }

        // add the new enrollment (the one remaining after the previous while)
        if (!updatedEnrolledCourses.isEmpty()) {
            Statement statement3 = connection.createStatement();
            statement3.execute(String.format("INSERT INTO enrolled(studentId, courseId) VALUES (%2d, %2d)", obj.getStudentId(), updatedEnrolledCourses.get(0)));
            statement3.close();
        }

        statement.close();
        connection.close();
    }

    /**
     * Deletes a student from the database
     * @param obj : Student to delete
     * @throws SQLException if a query is incorrect
     */
    @Override
    public void delete(Student obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        // Un-enroll student from all courses
        String deleteEnrollment = String.format("DELETE FROM enrolled WHERE studentId=%2d", obj.getStudentId());
        statement.execute(deleteEnrollment);

        // Delete the student
        String deleteStudent = String.format("DELETE FROM student WHERE studentId=%2d", obj.getStudentId());
        Statement statement1 = connection.createStatement();
        statement1.execute(deleteStudent);

        statement.close();
        connection.close();
    }
}
