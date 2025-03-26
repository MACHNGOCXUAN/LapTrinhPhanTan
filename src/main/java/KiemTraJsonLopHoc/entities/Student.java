package KiemTraJsonLopHoc.entities;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String student_id;
    private String name;
    private int age;
    private float gpa;
    private Address address;
}
