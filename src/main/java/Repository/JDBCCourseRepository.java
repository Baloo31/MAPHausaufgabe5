package Repository;

import Model.Course;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

public class JDBCCourseRepository implements ICrudRepository<Course>{
    private String DB_URL;
    private String USER;
    private String PASS;

    public JDBCCourseRepository(String url, String user, String password){
        DB_URL = url;
        USER = user;
        PASS = password;
    }

    @Override
    public void create(Course obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        String insertCourse = String.format("INSERT INTO course(name, maxEnrollment, credits, courseId, teacher) VALUES (\"%s\", %2d, %2d, %2d, %2d)",
                obj.getName(), obj.getMaxEnrollment(), obj.getCredits(), obj.getCourseId(), obj.getTeacher());
        statement.execute(insertCourse);

        statement.close();
        connection.close();
    }

    @Override
    public List<Course> getAll() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        List<Course> courses = new LinkedList<>();

        String selectAllCourses = "SELECT * FROM course";
        ResultSet resultSet = statement.executeQuery(selectAllCourses);
        while (resultSet.next()){
            String name = resultSet.getString("name");
            int maxEnrollment = resultSet.getInt("maxEnrollment");
            int credits = resultSet.getInt("credits");
            long courseId = resultSet.getLong("courseId");
            long teacher = resultSet.getLong("teacher");

            List<Long> students = new LinkedList<>();
            String selectStudentsEnrolled = String.format("SELECT studentId FROM enrolled WHERE courseId=%2d",courseId);
            Statement statement1 = connection.createStatement();
            ResultSet enrolledStudents = statement1.executeQuery(selectStudentsEnrolled);
            while (enrolledStudents.next()){
                students.add(enrolledStudents.getLong("studentId"));
            }
            statement1.close();

            courses.add(new Course(name, teacher, maxEnrollment, credits, courseId, students));
        }

        statement.close();
        connection.close();
        return courses;
    }

    @Override
    public void update(Course obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        String updateCourse = String.format("UPDATE course SET name=\"%s\", maxEnrollment=%2d, credits=%2d, teacher=%2d WHERE courseId=%2d",
                obj.getName(), obj.getMaxEnrollment(), obj.getCredits(), obj.getTeacher(), obj.getCourseId());
        statement.execute(updateCourse);

        List<Long> updatedEnrolledStudents = obj.getStudentsEnrolled();

        String getEnrollment = String.format("SELECT studentId FROM enrolled WHERE courseId=%2d", obj.getCourseId());
        Statement statement1 = connection.createStatement();
        ResultSet enrolledStudents = statement1.executeQuery(getEnrollment);

        // find the deleted enrollment and deleting it from the database
        while (enrolledStudents.next()){
            long studentId = enrolledStudents.getLong("studentId");
            if (!updatedEnrolledStudents.contains(studentId)){
                Statement statement2 = connection.createStatement();
                statement2.execute(String.format("DELETE FROM enrolled WHERE studentId=%2d AND courseId=%2d", studentId, obj.getCourseId()));
                statement2.close();
            } else {
                updatedEnrolledStudents.remove(studentId);
            }
        }

        // add the new enrollment (the one remaining after the previous while)
        if (!updatedEnrolledStudents.isEmpty()) {
            Statement statement3 = connection.createStatement();
            statement3.execute(String.format("INSERT INTO enrolled(studentId, courseId) VALUES (%2d, %2d)", updatedEnrolledStudents.get(0), obj.getCourseId()));
            statement3.close();
        }

        statement.close();
        connection.close();
    }

    @Override
    public void delete(Course obj) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        // Unenroll all students from the course
        String deleteEnrollment = String.format("DELETE FROM enrolled WHERE courseId=%2d", obj.getCourseId());
        statement.execute(deleteEnrollment);

        // Delete the course
        Statement statement1 = connection.createStatement();
        String deleteCourse = String.format("DELETE FROM course WHERE courseId=%2d", obj.getCourseId());
        statement1.execute(deleteCourse);

        statement.close();
        connection.close();
    }
}
