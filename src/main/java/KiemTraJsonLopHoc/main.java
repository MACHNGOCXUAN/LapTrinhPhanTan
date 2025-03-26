package KiemTraJsonLopHoc;

import KiemTraJsonLopHoc.entities.Clazz;
import KiemTraJsonLopHoc.entities.Student;
import KiemTraJsonLopHoc.utils.ClassUtils;
import jakarta.json.JsonArray;

import java.util.List;

public class main {
    public static void main(String[] args) {
        ClassUtils classUtils = new ClassUtils();
//        List<Clazz> clazzes = classUtils.fromJsonArray("json/LopHoc.json");
//        clazzes.forEach(clazz -> {
//            System.out.println(clazz);
//        });

        System.out.println("Math");
        List<Student> students = classUtils.listStudent("Math");
        for (Student student : students) {
            System.out.println(student);
        }

        JsonArray ja = ClassUtils.toJsonArray(students);
        ClassUtils.writeJsonToFile(ja, "json/22656961-xuan.json");
    }
}
