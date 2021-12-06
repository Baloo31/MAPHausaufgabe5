package Model;

import java.util.List;

public class Student extends Person implements Comparable<Student>{
    private long studentId;
    private List<Long> enrolledCourses;

    /**
     * constructor for a student
     * @param firstname : first name
     * @param lastName : last name
     */
    public Student(String firstname, String lastName, List<Long> enrolledCourses, long studentId) {
        super(firstname, lastName);
        this.enrolledCourses = enrolledCourses;
        this.studentId = studentId;
    }


    /**
     * string representation for a student
     * @return String
     */
    @Override
    public String toString() {
        return "Student{" +
                "firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", studentId=" + studentId +
                ", nrEnrolledCourses=" + getEnrolledCourses() +
                '}';
    }


    /**
     * adds a course to the list of courses of a student
     * @param courseId : course to add
     */
    public void addCourse(long courseId){
        this.enrolledCourses.add(courseId);
    }


    /**
     * deletes a course from the list of courses of a student
     * @param courseId : course to delete
     */
    public void deleteCourse(long courseId){
        this.enrolledCourses.remove(courseId);
    }


    /**
     * getter for the id of a student
     * @return student id (long)
     */
    public long getStudentId() {
        return studentId;
    }



    /**
     * getter for the courses a student is enrolled to
     * @return list of courses ids (List<Long>)
     */
    public List<Long> getEnrolledCourses() {
        return enrolledCourses;
    }



    /**
     *setter for the courses a student is enrolled to
     * @param enrolledCourses list of courses ids
     */
    public void setEnrolledCourses(List<Long> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }



    /**
     * getter for the number of courses a student is enrolled to
     * @return number of enrolled courses
     */
    public int getNumberOfCourses(){
        return enrolledCourses.size();
    }

    @Override
    public int compareTo(Student o) {
        if (studentId == o.getStudentId()) {
            return 1;
        }
        return 0;
    }
}
