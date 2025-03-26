package KiemTraJsonLopHoc.utils;

import KiemTraJsonLopHoc.entities.Address;
import KiemTraJsonLopHoc.entities.Clazz;
import KiemTraJsonLopHoc.entities.Student;
import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassUtils {

    public static List<Clazz> fromJsonArray(String fileName){
        List<Clazz> clazzList = new ArrayList<>();
        try(JsonReader jsonReader = Json.createReader(new FileReader(fileName))){
            JsonArray jsonArray = jsonReader.readArray();
            for(JsonValue jsonValue : jsonArray){
                JsonObject jsonClazz = jsonValue.asJsonObject();
                Clazz clazz = new Clazz();

                clazz.setName(jsonClazz.getString("name"));
                clazz.setTeacher(jsonClazz.getString("teacher"));
                clazz.setRoom(jsonClazz.getString("room"));
                clazz.setStart_time(jsonClazz.getString("start_time"));
                clazz.setEnd_time(jsonClazz.getString("end_time"));

                JsonArray jaStudents = jsonClazz.getJsonArray("students");
                List<Student> studentList = new ArrayList<>();
                for(JsonValue studentJsonValue : jaStudents){
                    JsonObject joStudent = studentJsonValue.asJsonObject();
                    Student student = new Student();

                    student.setStudent_id(joStudent.getString("student_id"));
                    student.setName(joStudent.getString("name"));
                    student.setAge(joStudent.getInt("age"));
                    student.setGpa(joStudent.getJsonNumber("gpa").numberValue().floatValue());


                    JsonObject joAddress = joStudent.getJsonObject("address");
                    Address address = new Address();

                    address.setStreet(joAddress.getString("street"));
                    address.setCity(joAddress.getString("city"));
                    address.setState(joAddress.getString("state"));
                    address.setZip(joAddress.getString("zip"));

                    student.setAddress(address);
                    studentList.add(student);
                }
                clazz.setStudents(studentList);
                clazzList.add(clazz);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return clazzList;
    }


    public List<Student> listStudent(String name){
        List<Clazz> clazzes = fromJsonArray("json/LopHoc.json");

        for(Clazz clazz : clazzes){
            if(clazz.getName().equalsIgnoreCase(name)){
                return clazz.getStudents();
            }
        }
        return new ArrayList<>();
    }

    public static void writeJsonToFile(JsonArray ja, String fileName){
        Map<String, Boolean> conf = new HashMap<>();
        conf.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonWriterFactory factory = Json.createWriterFactory(conf);

        try(JsonWriter writer = factory.createWriter(new FileWriter(fileName))){
            writer.write(ja);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JsonArray toJsonArray(List<Student> students){
        JsonArrayBuilder jaBuilder = Json.createArrayBuilder();

        students.forEach(student -> {
            JsonObjectBuilder joBuilder = Json.createObjectBuilder();
            joBuilder.add("student_id", student.getStudent_id());
            joBuilder.add("name", student.getName());
            joBuilder.add("age", student.getAge());
            joBuilder.add("gpa", student.getGpa());

            if(student.getAddress() != null){
                JsonObjectBuilder joAddressBuilder = Json.createObjectBuilder();
                joAddressBuilder.add("street", student.getAddress().getStreet());
                joAddressBuilder.add("city", student.getAddress().getCity());
                joAddressBuilder.add("state", student.getAddress().getState());
                joAddressBuilder.add("zip", student.getAddress().getZip());

                joBuilder.add("address", joAddressBuilder);
            }

            jaBuilder.add(joBuilder.build());
        });

        return jaBuilder.build();
    }
}
