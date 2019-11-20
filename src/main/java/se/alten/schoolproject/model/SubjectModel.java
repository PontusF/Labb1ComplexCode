package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Subject;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectModel {

    private String title;
    private Set<String> students = new HashSet<>();
    private Set<String> teachers = new HashSet<>();
    public SubjectModel toModel(Subject subjectToAdd) {
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setTitle(subjectToAdd.getTitle());

        subjectToAdd.getStudents().forEach(student  -> {
            subjectModel.students.add(student.getForename() + " " + student.getLastname() + " " + student.getEmail());
        });
        subjectToAdd.getTeachers().forEach(teacher  -> {
            subjectModel.teachers.add(teacher.getForename() + " " + teacher.getLastname()+ " " + teacher.getEmail());
        });


        return subjectModel;
    }
}