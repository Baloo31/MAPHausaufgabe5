package Model;

import java.util.List;

public class Course implements Comparable<Course>{
    private String name;
    private long teacherId;
    private int maxEnrollment;
    private List<Long> studentsEnrolled;
    private int credits;
    private long courseId;


    /**
     * constructor for a course
     * @param name : name of a course
     * @param teacherId : name of the teacher teaching the course
     * @param maxEnrollment : maximum number of students that can join the course
     * @param credits : course credits
     */
    public Course(String name, long teacherId, int maxEnrollment, int credits, long courseId, List<Long> students){
        this.name = name;
        this.teacherId = teacherId;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = students;
        this.credits = credits;
        this.courseId = courseId;
    }


    /**
     * string representation for a course
     * @return : String
     */
    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", maxEnrollment=" + maxEnrollment +
                ", enrolledStudents=" + studentsEnrolled +
                ", credits=" + credits +
                ", courseId=" + courseId +
                '}';
    }


    /**
     * adds a student to the list of enrolled students
     * @param student : student to add
     */
    public void addStudent(long student) {
        studentsEnrolled.add(student);
    }


    /**
     * deletes a student from the list of enrolled students
     * @param student : student to delete
     */
    public void deleteStudent(long student) {
        studentsEnrolled.remove(student);
    }


    /**
     * getter for the number of enrolled students
     * @return number of enrolled students (int)
     */
    public int getNumberOfStudents(){
        return studentsEnrolled.size();
    }


    /**
     * getter for the name of a course
     * @return name of the course (String)
     */
    public String getName() {
        return name;
    }


    /**
     * setter for the name of a course
     * @param name : name of a course
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * getter for the teacher of a course
     * @return Teacher
     */
    public long getTeacher() {
        return teacherId;
    }


    /**
     * setter for the teacher of a course
     * @param teacherId : teacher that teaches the course
     */
    public void setTeacher(long teacherId) {
        //deleteTeacherCourse(this.teacherId, courseId);
        this.teacherId = teacherId;
        //addTeacherCourse(teacherId, courseId);
    }


    /**
     * getter for the maximum number of students enrolled to a course
     * @return max number (int)
     */
    public int getMaxEnrollment() {
        return maxEnrollment;
    }


    /**
     * setter for the maximum number of students enrolled to a course
     * @param maxEnrollment : max number of students
     */
    public void setMaxEnrollment(int maxEnrollment) {
        if (this.getNumberOfStudents() > maxEnrollment) {
            this.maxEnrollment = maxEnrollment;
        }
    }


    /**
     * getter for the number of credits of the course
     * @return credits (int)
     */
    public int getCredits() {
        return credits;
    }


    /**
     * setter for the number of credits of a course
     * @param credits : number of credits
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }


    /**
     * getter for the students enrolled for a course
     * @return the list of students (List<Student>)
     */
    public List<Long> getStudentsEnrolled() {
        return studentsEnrolled;
    }


    /**
     * setter fot the students enrolled for a course
     * @param studentsEnrolled list of students ids
     */
    public void setStudentsEnrolled(List<Long> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    /**
     * getter for the course id
     * @return course id (long)
     */
    public long getCourseId() {
        return courseId;
    }

    @Override
    public int compareTo(Course o) {
        if (courseId == o.getCourseId()) {
            return 1;
        }
        return 0;
    }
}
