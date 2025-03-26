package KiemTraJsonLopHoc.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Clazz {
    private String name;
    private String teacher;
    private String room;
    private String start_time;
    private String end_time;

    private List<Student> students;
}
