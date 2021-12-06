package Repository;


import Model.Student;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * StudentRepository : extends in-memory repository
 */
public class StudentRepository extends InMemoryRepository<Student> implements FileRepository<Student>{
    private ObjectMapper objectMapper;
    private String fileName;

    /**
     * constructor for a student repository
     */
    public StudentRepository(String fileName){
        super();
        repoList = new LinkedList<>();
        objectMapper = new ObjectMapper();
        this.fileName = fileName;
    }


    /**
     * Writes the data (students) to a json file
     * @throws IOException if writing did not work
     */
    @Override
    public void writeData() throws IOException {
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(fileName), repoList);
    }


    /**
     * Reads the data from the json file that stores the students
     * @return the extracted students
     * @throws IOException if input was wrong
     */
    @Override
    public List<Student> readData() throws IOException {
        Reader reader = new BufferedReader(new FileReader(fileName));
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode node : parser){
            String firstName = node.path("firstName").asText();
            String lastName = node.path("lastName").asText();
            long  studentId = node.path("studentId").asLong();

            List<Long> listeCourse = new LinkedList<>();
            for (JsonNode courseId : node.path("enrolledCourses")){
                listeCourse.add(courseId.asLong());
            }

            Student studentFromFile = new Student(firstName, lastName, listeCourse, studentId);
            repoList.add(studentFromFile);
        }

        return repoList;
    }

    /**
     * updates a student
     * @param obj : student to update
     * @return updated student (Student)
     */
    @Override
    public Student update(Student obj) {
        Student studentToUpdate = this.repoList.stream()
                .filter(student -> student.getStudentId() == obj.getStudentId())
                .findFirst()
                .orElseThrow();

        studentToUpdate.setFirstName(obj.getFirstName());
        studentToUpdate.setLastName(obj.getLastName());
        studentToUpdate.setEnrolledCourses(obj.getEnrolledCourses());
        return studentToUpdate;
    }
}
