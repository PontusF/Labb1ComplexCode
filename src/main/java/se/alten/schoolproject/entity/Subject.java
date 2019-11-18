package se.alten.schoolproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="subject")
@NoArgsConstructor
@AllArgsConstructor
public class Subject implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @ManyToMany(mappedBy = "subject", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "subject", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Teacher> teachers = new HashSet<>();



    public Subject toEntity(String subjectModel) {
        Set<Student> tempStudents = new HashSet<>();
        Set<Teacher> tempTeachers = new HashSet<>();
        JsonReader reader = Json.createReader(new StringReader(subjectModel));

        JsonObject jsonObject = reader.readObject();

        Subject subject = new Subject();

        if ( jsonObject.containsKey("subject")) {

            subject.setTitle(jsonObject.getString("subject"));
        } else {
            subject.setTitle("");
        }

        if (jsonObject.containsKey("student")) {
            JsonArray jsonArray = jsonObject.getJsonArray("student");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                tempStudents.add(Student.class.cast(jsonArray.get(i)));
                subject.setStudents(tempStudents);
            }
        } else {
            subject.setStudents(null);
        }
        if (jsonObject.containsKey("teacher")) {
            JsonArray jsonArray = jsonObject.getJsonArray("teacher");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                tempTeachers.add(Teacher.class.cast(jsonArray.get(i)));
                subject.setTeachers(tempTeachers);
            }
        } else {
            subject.setTeachers(null);
        }
        return subject;
    }
}
