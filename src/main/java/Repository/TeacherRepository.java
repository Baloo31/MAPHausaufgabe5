package Repository;

import Model.Teacher;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * TeacherRepository : extends in-memory repository
 */
public class TeacherRepository extends InMemoryRepository<Teacher> implements FileRepository<Teacher> {
    private ObjectMapper objectMapper;
    private String fileName;


    /**
     * Writes the data (teachers) to a json file
     * @throws IOException if writing did not work
     */
    @Override
    public void writeData() throws IOException {
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(fileName), repoList);
    }


    /**
     * Reads the data from the json file that stores the teachers
     * @return the extracted teachers
     * @throws IOException if input was wrong
     */
    @Override
    public List<Teacher> readData() throws IOException {
        Reader reader = new BufferedReader(new FileReader(fileName));
        JsonNode parser = objectMapper.readTree(reader);

        for (JsonNode node : parser){
            String firstName = node.path("firstName").asText();
            String lastName = node.path("lastName").asText();
            long  teacherId = node.path("teacherId").asLong();

            List<Long> listeCourse = new LinkedList<>();
            for (JsonNode courseId : node.path("courses")){
                listeCourse.add(courseId.asLong());
            }

            Teacher teacherFromFile = new Teacher(firstName, lastName, listeCourse, teacherId);
            repoList.add(teacherFromFile);
        }

        return repoList;
    }

    /**
     * constructor for a teacher repository
     */
    public TeacherRepository(String fileName){
        super();
        repoList = new LinkedList<>();
        objectMapper = new ObjectMapper();
        this.fileName = fileName;
    }


    /**
     * updates a teacher
     * @param obj : teacher to update
     * @return updated teacher (Teacher)
     */
    @Override
    public Teacher update(Teacher obj) {
        Teacher teacherToUpdate = this.repoList.stream()
                .filter(teacher -> teacher.getTeacherId() == obj.getTeacherId())
                .findFirst()
                .orElseThrow();

        teacherToUpdate.setFirstName(obj.getFirstName());
        teacherToUpdate.setLastName(obj.getLastName());
        teacherToUpdate.setCourses(obj.getCourses());
        return teacherToUpdate;
    }
}
