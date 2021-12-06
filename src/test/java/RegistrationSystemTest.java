import Controller.RegistrationSystem;
import Exceptions.*;
import Model.Course;
import Model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        registrationSystem = new RegistrationSystem("", "", "");
        try {
            registrationSystem.addStudent("Alin", "Goga", 1);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        // Adding some students
        try {
            registrationSystem.addStudent("Mihai", "Avram", 2);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Flavius", "Ioan", 3);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Andrei", "Balu", 4);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Emil", "Deac", 5);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Nicolae", "Craciun", 6);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        //Adding some teachers
        try {
            registrationSystem.addTeacher("Radu", "Dragan", 1);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addTeacher("Florin", "Dragomirescu", 2);
        } catch (AlreadyExistsException e) {
            Assertions.fail();
        }

        // Adding some courses

        try {
            registrationSystem.addCourse("Baze de date", 2, 10, 5, 1);
        } catch (AlreadyExistsException | ElementDoesNotExistException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addCourse("Unjoinable course", 2, 100, 31, 2);
        } catch (AlreadyExistsException | ElementDoesNotExistException e) {
            Assertions.fail();
        }

        try {
            registrationSystem.addCourse("Analiza matematica", 1, 5, 5,3);
        } catch (AlreadyExistsException | ElementDoesNotExistException e) {
            Assertions.fail();
        }
    }

    @Test
    void register() {
        // No student can join the second course beacuse of the number of credits
        for (Student student : registrationSystem.retrieveAllStudents()){
            try {
                registrationSystem.register(2, student.getStudentId());
            } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
                Assertions.fail();
            } catch (MaxCreditsSurpassedException e) {
                Assertions.assertTrue(true);
            }
        }

        // All students join the course
        for (Student student : registrationSystem.retrieveAllStudents()){
            try {
                registrationSystem.register(1, student.getStudentId());
            } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                Assertions.fail();
            }
        }

        // The course will be full and the last student (id 6) will not join
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
    }

    @Test
    void retrieveCoursesWithFreePlaces() {
        // The course will be full and the last student (id 6) will not join
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

        // There are only 2 courses with free places now
        Assertions.assertEquals(2, registrationSystem.retrieveCoursesWithFreePlaces().size());
    }

    @Test
    void retrieveStudentsEnrolledForACourse() {
        // All students join the course
        for (Student student : registrationSystem.retrieveAllStudents()){
            try {
                registrationSystem.register(1, student.getStudentId());
            } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                Assertions.fail();
            }
        }

        // Check if the method returns the correct students
        List<Student> studentsEnrolledForThisCourse = registrationSystem.retrieveStudentsEnrolledForACourse(1);
        for (Student student : registrationSystem.retrieveAllStudents()){
            Assertions.assertTrue(studentsEnrolledForThisCourse.contains(student));
        }

        // There are no students enrolled to the second course
        Assertions.assertTrue(registrationSystem.retrieveStudentsEnrolledForACourse(2).isEmpty());
    }

    @Test
    void deleteTeacherCourse() {
        // All students join the course
        for (Student student : registrationSystem.retrieveAllStudents()){
            try {
                registrationSystem.register(1, student.getStudentId());
            } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                Assertions.fail();
            }
        }

        for (Student student : registrationSystem.retrieveAllStudents()){
            Assertions.assertNotEquals(0, student.getNumberOfCourses());
        }

        // teacher deletes the course
        try {
            registrationSystem.deleteTeacherCourse(1, 2);
        } catch (ElementDoesNotExistException | NotTeachingTheCourseException e) {
            fail();
        }

        for (Student student : registrationSystem.retrieveAllStudents()){
            Assertions.assertEquals(0, student.getNumberOfCourses());
        }

        // the course should exist and be teached by the specified teacher
        try {
            registrationSystem.deleteTeacherCourse(10, 1);
            fail();
        } catch (ElementDoesNotExistException | NotTeachingTheCourseException e) {
            Assertions.assertTrue(true);
        }

        // Both exist, but this teacher is not teaching this ocurse
        try {
            registrationSystem.deleteTeacherCourse(2, 1);
            fail();
        } catch (ElementDoesNotExistException e) {
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
        }

        // added
        try {
            registrationSystem.addTeacher("Raul", "Ion", 5);
        } catch (AlreadyExistsException e) {
            fail();
        }

    }

    @Test
    void addStudent() {
        // added
        try {
            registrationSystem.addStudent("Flaviu", "Pop", 7);
        } catch (AlreadyExistsException e) {
            fail();
        }

        // already exists
        try {
            registrationSystem.addStudent("Flavius", "Ioan", 3);
            fail();
        } catch (AlreadyExistsException e) {
            assertTrue(true);
        }
    }

    @Test
    void addCourse() {
        // Added
        try {
            registrationSystem.addCourse("Algebra", 1, 10, 5, 4);
        } catch (AlreadyExistsException | ElementDoesNotExistException e) {
            fail();
        }

        // Teacher exists, but course already exists
        try {
            registrationSystem.addCourse("Algebra", 1, 10, 5, 4);
            fail();
        } catch (AlreadyExistsException e) {
            assertTrue(true);
        } catch (ElementDoesNotExistException e) {
            fail();
        }

        // The course does not already exist, but the teacher can't teach this course because he does not exist
        try {
            registrationSystem.addCourse("Algebra", 10, 10, 5, 10);
            fail();
        } catch (AlreadyExistsException e) {
            fail();
        } catch (ElementDoesNotExistException e) {
            assertTrue(true);
        }

    }

    @Test
    void calculateStudentCredits() {
        // All the students are not registered and they will all be registered to the first course
        for (Student student : registrationSystem.retrieveAllStudents()){
            try {
                registrationSystem.register(1, student.getStudentId());
            } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
                fail();
            }

            // Cheching the credits
            assertEquals(5, registrationSystem.calculateStudentCredits(student));

        }

        // All the students have now 5 credits. Some of them will be enrolled to another 5 credit course
        List<Student> students  = registrationSystem.retrieveAllStudents();
        for (int idx = 0; idx < 3; idx++){
            try {
                registrationSystem.register(3, students.get(idx).getStudentId());
            } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
                fail();
            }

            // Check if this Student will have the number of credits 10
            assertEquals(10, registrationSystem.calculateStudentCredits(students.get(idx)));
        }


    }

    @Test
    void sortStudentsById() {
        List<Student> studentsSortedById = registrationSystem.sortStudentsById();

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
        List<Course> coursesSortedByName = registrationSystem.sortCoursesByName();

        // correct order of the courses
        assertEquals(3, coursesSortedByName.get(0).getCourseId());
        assertEquals(1, coursesSortedByName.get(1).getCourseId());
        assertEquals(2, coursesSortedByName.get(2).getCourseId());
    }


    @Test
    void filterStudentsEnrolled() {
        List<Student> studentsEnrolled = registrationSystem.filterStudentsEnrolled();
        assertEquals(0, studentsEnrolled.size());

        try {
            registrationSystem.register(3, 1);
        } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
            fail();
        }

        studentsEnrolled = registrationSystem.filterStudentsEnrolled();
        assertEquals(1, studentsEnrolled.size());
        assertEquals(1, studentsEnrolled.get(0).getStudentId());

        // All students join the course
        for (Student student : registrationSystem.retrieveAllStudents()){
            try {
                registrationSystem.register(1, student.getStudentId());
            } catch (ElementDoesNotExistException | MaxEnrollmentSurpassedException | AlreadyExistsException | MaxCreditsSurpassedException e) {
                Assertions.fail();
            }
        }

        studentsEnrolled = registrationSystem.filterStudentsEnrolled();
        assertEquals(6, studentsEnrolled.size());

    }

    @Test
    void filterCoursesWithStudents() {
        List<Course> coursesWithStudents = registrationSystem.filterCoursesWithStudents();

        assertEquals(0, coursesWithStudents.size());

        try {
            registrationSystem.register(3, 1);
        } catch (ElementDoesNotExistException | MaxCreditsSurpassedException | MaxEnrollmentSurpassedException | AlreadyExistsException e) {
            fail();
        }

        coursesWithStudents = registrationSystem.filterCoursesWithStudents();
        assertEquals(1, coursesWithStudents.size());
        assertEquals(3, coursesWithStudents.get(0).getCourseId());

    }
}
