package Model;

import java.util.List;

public class Teacher extends Person implements Comparable<Teacher>{
    private long teacherId;
    private List<Long> courses;

    /**
     * constructor for a teacher
     * @param firstName : first name
     * @param lastName : last name
     */
    public Teacher(String firstName, String lastName, List<Long> courses, long teacherId){
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.courses = courses;
    }


    /**
     * string representation of a teacher
     * @return String
     */
    @Override
    public String toString() {
        return "Teacher{" +
                "firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", teacherId=" + teacherId +
                ", courses=" + getCourses() +
                '}';
    }


    /**
     * adds a course to the list of courses that the teacher is teaching
     * @param courseId : a course that the teacher will teach
     */
    public void addCourse(long courseId){
        courses.add(courseId);
    }


    /**
     * deletes a course from the list of courses of the teacher
     * @param courseId : a course that the teacher is teaching
     */
    public void deleteCourse(long courseId) {
        courses.remove(courseId);
    }


    /**
     * getter for the number of courses a teacher is teaching
     * @return number of courses
     */
    public int getNrOfCourses() {
        return courses.size();
    }


    /**
     * getter for the id of a teacher
     * @return teacher id (long)
     */
    public long getTeacherId() {
        return teacherId;
    }


    /**
     * getter for the courses of a teacher
     * @return list of courses (List<Courses>)
     */
    public List<Long> getCourses() {
        return courses;
    }


    /**
     * setter for the courses a teacher is teaching
     * @param courses list of courses ids
     */
    public void setCourses(List<Long> courses) {
        this.courses = courses;
    }


    @Override
    public int compareTo(Teacher o) {
        if (teacherId == o.getTeacherId()) {
            return 1;
        }
        return 0;
    }
}
