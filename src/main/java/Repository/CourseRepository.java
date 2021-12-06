package Repository;

import Model.Course;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * CourseRepository : extends in-memory repository
 */
public class CourseRepository extends InMemoryRepository<Course> implements FileRepository<Course> {
    private ObjectMapper objectMapper;
    private String fileName;

    /**
     * Writes the data (courses) to a json file
     * @throws IOException if writing did not work
     */
    @Override
    public void writeData() throws IOException {
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(fileName), repoList);
    }


    /**
     * Reads the data from the json file that stores the courses
     * @return the extracted courses
     * @throws IOException if input was wrong
     */
    @Override
    public List<Course> readData() throws IOException {
        Reader reader = new BufferedReader(new FileReader(fileName));
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode node : parser){
            String name = node.path("name").asText();
            long teacherId = node.path("teacher").asLong();
            int  maxEnrollment = node.path("maxEnrollment").asInt();
            int credits = node.path("credits").asInt();
            long courseId = node.path("courseId").asLong();

            List<Long> listeStudenten = new LinkedList<>();
            for (JsonNode studentId : node.path("studentsEnrolled")){
                listeStudenten.add(studentId.asLong());
            }

            repoList.add(new Course(name, teacherId, maxEnrollment, credits, courseId, listeStudenten));
        }

        return repoList;
    }

    /**
     * constructor for a course repository
     */
    public CourseRepository(String fileName){
        super();
        repoList = new LinkedList<>();
        objectMapper = new ObjectMapper();
        this.fileName = fileName;
    }


    /**
     * updates a course
     * @param obj : course to update
     * @return updated course (Course)
     */
    @Override
    public Course update(Course obj) {
        Course courseToUpdate = this.repoList.stream()
                .filter(course -> course.getCourseId() == obj.getCourseId())
                .findFirst()
                .orElseThrow();

        courseToUpdate.setName(obj.getName());
        courseToUpdate.setMaxEnrollment(obj.getMaxEnrollment());
        courseToUpdate.setCredits(obj.getCredits());
        courseToUpdate.setTeacher(obj.getTeacher());
        courseToUpdate.setStudentsEnrolled(obj.getStudentsEnrolled());
        return courseToUpdate;
    }
}
