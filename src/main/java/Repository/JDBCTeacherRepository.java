package Repository;

import Model.Teacher;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

/**
 * JDBCTeacherRepository
 */
public class JDBCTeacherRepository implements ICrudRepository<Teacher>{
    private String DB_URL;
    private String USER;
    private String PASS;


    /**
     * Constructor
     * @param url the database url
     * @param user the user to access
     * @param password the password for the user
     */
    public JDBCTeacherRepository(String url, String user, String password){
        DB_URL = url;
        USER = user;
        PASS = password;
    }


    /**
     * Adds a new teacher to the database
     * @param obj : an object to add (Teacher)
     * @throws SQLException if a query is incorrect
     */
    @Override
    public void create(Teacher obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        String insertTeacher = String.format("INSERT INTO teacher(firstName, lastName, teacherId) VALUES (\"%s\", \"%s\", %2d)",
                obj.getFirstName(), obj.getLastName(), obj.getTeacherId());
        statement.execute(insertTeacher);

        statement.close();
        connection.close();
    }


    /**
     * Returns all teachers
     * @return list of teachers
     * @throws SQLException if a query is incorrect
     */
    @Override
    public List<Teacher> getAll() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        List<Teacher> teachers = new LinkedList<>();

        String selectAllTeachers = "SELECT * FROM teacher";
        ResultSet resultSet = statement.executeQuery(selectAllTeachers);
        while (resultSet.next()){
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            long teacherId = resultSet.getLong("teacherId");

            List<Long> courses = new LinkedList<>();
            String selectTeachingCourses = String
                    .format("SELECT course.courseId FROM course INNER JOIN teacher ON course.teacher=teacher.teacherId WHERE teacher=%2d",teacherId);
            Statement statement1 = connection.createStatement();
            ResultSet teachingCourses = statement1.executeQuery(selectTeachingCourses);
            while (teachingCourses.next()){
                courses.add(teachingCourses.getLong("courseId"));
            }
            statement1.close();

            teachers.add(new Teacher(firstName, lastName, courses, teacherId));
        }

        statement.close();
        connection.close();
        return teachers;
    }


    /**
     * Updates a teacher in the database
     * @param obj : teacher to update
     * @throws SQLException if a query is incorrect
     */
    @Override
    public void update(Teacher obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        String updateTeacher = String.format("UPDATE teacher SET firstName=\"%s\", lastName=\"%s\" WHERE teacherId=%2d",
                obj.getFirstName(), obj.getLastName(), obj.getTeacherId());
        statement.execute(updateTeacher);

        statement.close();
        connection.close();
    }


    /**
     * Deletes a teacher from the database
     * @param obj : teacher to delete
     * @throws SQLException if a query is incorrect
     */
    @Override
    public void delete(Teacher obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        // Un-enroll students from all courses taught by the teacher
        String deleteEnrollment = String.format("DELETE e FROM enrolled e INNER JOIN course ON course.courseId=e.courseId WHERE course.teacher=%2d", obj.getTeacherId());
        statement.execute(deleteEnrollment);
        statement.close();

        // Delete all courses taught by the teacher
        String deleteCourses = String.format("DELETE FROM course WHERE teacher=%2d", obj.getTeacherId());
        Statement statement1 = connection.createStatement();
        statement1.execute(deleteCourses);
        statement1.close();

        // Delete the teacher
        String deleteTeacher = String.format("DELETE FROM teacher WHERE teacherId=%2d", obj.getTeacherId());
        Statement statement2 = connection.createStatement();
        statement2.execute(deleteTeacher);
        statement2.close();

        connection.close();
    }
}
