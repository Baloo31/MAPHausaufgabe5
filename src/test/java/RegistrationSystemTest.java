import Controller.RegistrationSystem;
import Exceptions.*;
import Model.Course;
import Model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * Tests the registration system
 *
 */
class RegistrationSystemTest {
    private RegistrationSystem registrationSystem;

    @BeforeEach
    void setUp() {
            // Creating the registration system
            registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/university", "root", "password31");

            // Removing old data
            Connection connection;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "password31");
                Statement statement1 = connection.createStatement();
                statement1.execute("DELETE FROM enrolled");
                Statement statement2 = connection.createStatement();
                statement2.execute("DELETE FROM student");
                Statement statement3 = connection.createStatement();
                statement3.execute("DELETE FROM course");
                Statement statement4 = connection.createStatement();
                statement4.execute("DELETE FROM teacher");
            } catch (SQLException e) {
                fail();
            }

            // Adding data
            try {
                registrationSystem.addStudent("Alin", "Goga", 1);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            // Adding some students
            try {
                registrationSystem.addStudent("Mihai", "Avram", 2);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addStudent("Flavius", "Ioan", 3);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addStudent("Andrei", "Balu", 4);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addStudent("Emil", "Deac", 5);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addStudent("Nicolae", "Craciun", 6);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            //Adding some teachers
            try {
                registrationSystem.addTeacher("Radu", "Dragan", 1);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addTeacher("Florin", "Dragomirescu", 2);
            } catch (AlreadyExistsException | SQLException e) {
                Assertions.fail();
            }

            // Adding some courses

            try {
                registrationSystem.addCourse("Baze de date", 2, 10, 5, 1);
            } catch (AlreadyExistsException | ElementDoesNotExistException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addCourse("Unjoinable course", 2, 100, 31, 2);
            } catch (AlreadyExistsException | ElementDoesNotExistException | SQLException e) {
                Assertions.fail();
            }

            try {
                registrationSystem.addCourse("Analiza matematica", 1, 5, 5, 3);
            } catch (AlreadyExistsException | ElementDoesNotExistException | SQLException e) {
                Assertions.fail();
            }

    }

    @Test
    void register() {
        // No student can join the second course because of the number of credits
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(2, student.getStudentId());
                } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
                    Assertions.fail();
                } catch (MaxCreditsSurpassedException e) {
                    Assertions.assertTrue(true);
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // All students join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(1, student.getStudentId());
                } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                    Assertions.fail();
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // The course will be full and the last student (id 6) will not join
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(3, student.getStudentId());
                } catch (ElementDoesNotExistException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                    Assertions.fail();
                } catch (MaxEnrollmentSurpassedException e) {
                    if (student.getStudentId() != 6) {
                        Assertions.fail();
                    }
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void retrieveCoursesWithFreePlaces() {
        // The course will be full and the last student (id 6) will not join
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(3, student.getStudentId());
                } catch (ElementDoesNotExistException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                    Assertions.fail();
                } catch (MaxEnrollmentSurpassedException e) {
                    if (student.getStudentId() != 6) {
                        Assertions.fail();
                    }
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // There are only 2 courses with free places now
        try {
            Assertions.assertEquals(2, registrationSystem.retrieveCoursesWithFreePlaces().size());
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void retrieveStudentsEnrolledForACourse() {
        // All students join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(1, student.getStudentId());
                } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                    Assertions.fail();
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // Check if the method returns the correct students
        List<Student> studentsEnrolledForThisCourse = null;
        try {
            studentsEnrolledForThisCourse = registrationSystem.retrieveStudentsEnrolledForACourse(1);
        } catch (SQLException e) {
            Assertions.fail();
        }

        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                boolean contained = false;
                for (Student studentEnrolled : studentsEnrolledForThisCourse) {
                    if (student.getStudentId() == studentEnrolled.getStudentId()) {
                        contained = true;
                        break;
                    }
                }
                if (!contained){
                    fail();
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // There are no students enrolled to the second course
        try {
            Assertions.assertTrue(registrationSystem.retrieveStudentsEnrolledForACourse(2).isEmpty());
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void deleteTeacherCourse() {
        // All students join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(1, student.getStudentId());
                } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                    Assertions.fail();
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                Assertions.assertNotEquals(0, student.getNumberOfCourses());
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // teacher deletes the course
        try {
            registrationSystem.deleteTeacherCourse(1, 2);
        } catch (ElementDoesNotExistException | NotTeachingTheCourseException | SQLException e) {
            fail();
        }

        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                Assertions.assertEquals(0, student.getNumberOfCourses());
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        // the course should exist and be teached by the specified teacher
        try {
            registrationSystem.deleteTeacherCourse(10, 1);
            fail();
        } catch (ElementDoesNotExistException | NotTeachingTheCourseException e) {
            Assertions.assertTrue(true);
        } catch (SQLException throwables) {
            Assertions.fail();
        }

        // Both exist, but this teacher is not teaching this ocurse
        try {
            registrationSystem.deleteTeacherCourse(2, 1);
            fail();
        } catch (ElementDoesNotExistException | SQLException e) {
            fail();
        } catch (NotTeachingTheCourseException e) {
            Assertions.assertTrue(true);
        }

    }

    @Test
    void addTeacher() {
        // already exists
        try {
            registrationSystem.addTeacher("Radu", "Dragan", 1);
            fail();
        } catch (AlreadyExistsException e) {
            assertTrue(true);
        } catch (SQLException throwable) {
            fail();
        }

        // added
        try {
            registrationSystem.addTeacher("Raul", "Ion", 5);
        } catch (AlreadyExistsException | SQLException e) {
            fail();
        }

    }

    @Test
    void addStudent() {
        // added
        try {
            registrationSystem.addStudent("Flaviu", "Pop", 7);
        } catch (AlreadyExistsException | SQLException e) {
            fail();
        }

        // already exists
        try {
            registrationSystem.addStudent("Flavius", "Ioan", 3);
            fail();
        } catch (AlreadyExistsException | SQLException e) {
            assertTrue(true);
        }
    }

    @Test
    void addCourse() {
        // Added
        try {
            registrationSystem.addCourse("Algebra", 1, 10, 5, 4);
        } catch (AlreadyExistsException | ElementDoesNotExistException | SQLException e) {
            fail();
        }

        // Teacher exists, but course already exists
        try {
            registrationSystem.addCourse("Algebra", 1, 10, 5, 4);
            fail();
        } catch (AlreadyExistsException e) {
            assertTrue(true);
        } catch (ElementDoesNotExistException | SQLException e) {
            fail();
        }

        // The course does not already exist, but the teacher can't teach this course because he does not exist
        try {
            registrationSystem.addCourse("Algebra", 10, 10, 5, 10);
            fail();
        } catch (AlreadyExistsException | SQLException e) {
            fail();
        } catch (ElementDoesNotExistException e) {
            assertTrue(true);
        }

    }

    @Test
    void calculateStudentCredits() {
        // All the students are not registered and they will all be registered to the first course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(1, student.getStudentId());
                } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
                    fail();
                }
            }

            for (Student student : registrationSystem.retrieveAllStudents()) {
                // Checking the credits
                assertEquals(5, registrationSystem.calculateStudentCredits(student));
            }
        } catch (SQLException e) {
            fail();
        }

        // All the students have now 5 credits. Some of them will be enrolled to another 5 credit course
        List<Student> students  = null;
        try {
            students = registrationSystem.retrieveAllStudents();
        } catch (SQLException e) {
            fail();
        }
        for (int idx = 0; idx < 3; idx++){
            try {
                registrationSystem.register(3, students.get(idx).getStudentId());
            } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException | SQLException e) {
                fail();
            }
        }

        try {
            students = registrationSystem.retrieveAllStudents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int idx = 0; idx < 3; idx++){
            // Check if this Student will have the number of credits 10
            try {
                assertEquals(10, registrationSystem.calculateStudentCredits(students.get(idx)));
            } catch (SQLException e) {
                fail();
            }
        }


    }

    @Test
    void sortStudentsById() {
        List<Student> studentsSortedById = null;
        try {
            studentsSortedById = registrationSystem.sortStudentsById();
        } catch (SQLException e) {
            fail();
        }

        // check if the students were sorted ascending by their id
        long prevId = -1;
        for (Student student : studentsSortedById){
            if (student.getStudentId() < prevId){
                fail();
            }
        }
    }

    @Test
    void sortCoursesByName() {
        List<Course> coursesSortedByName = null;
        try {
            coursesSortedByName = registrationSystem.sortCoursesByName();
        } catch (SQLException e) {
            fail();
        }

        // correct order of the courses
        assertEquals(3, coursesSortedByName.get(0).getCourseId());
        assertEquals(1, coursesSortedByName.get(1).getCourseId());
        assertEquals(2, coursesSortedByName.get(2).getCourseId());
    }


    @Test
    void filterStudentsEnrolled() {
        List<Student> studentsEnrolled = null;
        try {
            studentsEnrolled = registrationSystem.filterStudentsEnrolled();
        } catch (SQLException e) {
            fail();
        }
        assertEquals(0, studentsEnrolled.size());

        try {
            registrationSystem.register(3, 1);
        } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException | SQLException e) {
            fail();
        }

        try {
            studentsEnrolled = registrationSystem.filterStudentsEnrolled();
        } catch (SQLException e) {
            fail();
        }
        assertEquals(1, studentsEnrolled.size());
        assertEquals(1, studentsEnrolled.get(0).getStudentId());

        // All students join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(1, student.getStudentId());
                } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                    Assertions.fail();
                }
            }
        } catch (SQLException e) {
            fail();
        }

        try {
            studentsEnrolled = registrationSystem.filterStudentsEnrolled();
        } catch (SQLException e) {
            fail();
        }
        assertEquals(6, studentsEnrolled.size());

    }

    @Test
    void filterCoursesWithStudents() {
        List<Course> coursesWithStudents = null;
        try {
            coursesWithStudents = registrationSystem.filterCoursesWithStudents();
        } catch (SQLException e) {
            fail();
        }

        assertEquals(0, coursesWithStudents.size());

        try {
            registrationSystem.register(3, 1);
        } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException | SQLException e) {
            fail();
        }

        try {
            coursesWithStudents = registrationSystem.filterCoursesWithStudents();
        } catch (SQLException e) {
            fail();
        }
        assertEquals(1, coursesWithStudents.size());
        assertEquals(3, coursesWithStudents.get(0).getCourseId());

    }
}
