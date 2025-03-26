package KiemTraJsonLopHoc.utils;

import KiemTraJsonLopHoc.entities.Address;
import KiemTraJsonLopHoc.entities.Clazz;
import KiemTraJsonLopHoc.entities.Student;
import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ClazzStream {
    public static List<Clazz> fromJsonArray(String fileName) {
        List<Clazz> clazzList = new ArrayList<>();
        String keyName = "";
        Clazz clazz = null;
        Student student = null;
        List<Student> studentList = null;
        Address address = null;

        try (JsonParser jsonParser = Json.createParser(new FileReader(fileName))) {
            while (jsonParser.hasNext()) {
                JsonParser.Event event = jsonParser.next();

                switch (event) {
                    case KEY_NAME:
                        keyName = jsonParser.getString();
                        break;

                    case START_ARRAY:
                        if ("students".equals(keyName) && clazz != null) {
                            studentList = new ArrayList<>();
                        }
                        break;

                    case START_OBJECT:
                        if ("address".equals(keyName)) {
                            address = new Address();
                        } else if (clazz == null) {
                            clazz = new Clazz();
                        } else {
                            student = new Student();
                        }
                        break;

                    case VALUE_STRING:
                        if (student != null && address == null) {
                            switch (keyName) {
                                case "student_id": student.setStudent_id(jsonParser.getString()); break;
                                case "name": student.setName(jsonParser.getString()); break;
                            }
                        } else if (clazz != null && student == null) {
                            switch (keyName) {
                                case "name": clazz.setName(jsonParser.getString()); break;
                                case "teacher": clazz.setTeacher(jsonParser.getString()); break;
                                case "room": clazz.setRoom(jsonParser.getString()); break;
                                case "start_time": clazz.setStart_time(jsonParser.getString()); break;
                                case "end_time": clazz.setEnd_time(jsonParser.getString()); break;
                            }
                        } else if (address != null) {
                            switch (keyName) {
                                case "street": address.setStreet(jsonParser.getString()); break;
                                case "city": address.setCity(jsonParser.getString()); break;
                                case "state": address.setState(jsonParser.getString()); break;
                                case "zip": address.setZip(jsonParser.getString()); break;
                            }
                        }
                        break;

                    case VALUE_NUMBER:
                        if (student != null && address == null) {
                            switch (keyName) {
                                case "age": student.setAge(jsonParser.getInt()); break;
                                case "gpa": student.setGpa(jsonParser.getBigDecimal().floatValue()); break;
                            }
                        }
                        break;

                    case END_OBJECT:
                        if (address != null) {
                            if (student != null) {
                                student.setAddress(address);
                            }
                            address = null;
                        } else if (student != null) {
                            if (studentList != null) {
                                studentList.add(student);
                            }
                            student = null;
                        } else if (clazz != null) {
                            clazzList.add(clazz);
                            clazz = null;
                        }
                        break;

                    case END_ARRAY:
                        if (studentList != null && clazz != null) {
                            clazz.setStudents(studentList);
                            studentList = null;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazzList;
    }

    public static void toJsonArray(List<Clazz> clazzList, File fileName) {
        Map<String, Object> conf = new HashMap<>();
        conf.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGeneratorFactory factory = Json.createGeneratorFactory(conf);
        try(JsonGenerator writer = factory.createGenerator(new FileWriter(fileName))){
            writer.writeStartArray();
            for (Clazz clazz : clazzList) {
                writer.writeStartObject();
                writer.write("name", clazz.getName())
                        .write("teacher", clazz.getTeacher())
                        .write("room", clazz.getRoom())
                        .write("start_time", clazz.getStart_time())
                        .write("end_time", clazz.getEnd_time());
                writer.writeStartArray("students");
                for (Student student : clazz.getStudents()) {
                    writer.writeStartObject();
                    writer.write("student_id", student.getStudent_id())
                            .write("name", student.getName())
                            .write("age", student.getAge())
                            .write("gpa", student.getGpa());
                    writer.writeStartObject("address");
                    writer.write("street", student.getAddress().getStreet())
                            .write("city", student.getAddress().getCity())
                            .write("state", student.getAddress().getState())
                            .write("zip", student.getAddress().getZip());
                    writer.writeEnd();
                    writer.writeEnd();
                }
                writer.writeEnd();
            }
            writer.writeEnd();
        }
    }

    public List<Student> listStudent(String name) {
        List<Clazz> clazzList = ClazzStream.fromJsonArray("json/LopHoc.json");
        for (Clazz clazz : clazzList) {
            if(clazz.getName().equals(name)) {
                return clazz.getStudents();
            }
        }

        return new ArrayList<>();
    }



    public static void main(String[] args) {
        List<Clazz> clazzList = ClazzStream.fromJsonArray("json/LopHoc.json");
        for (Clazz clazz : clazzList) {
            System.out.println(clazz);
        }
    }
}
