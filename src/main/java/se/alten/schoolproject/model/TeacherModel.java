package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Teacher;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherModel {

    private String forename;
    private String lastname;
    private String email;
    private Set<String> subjects = new HashSet<>();
    private Set<String> students = new HashSet<>();

    public TeacherModel toModel(Teacher teacher) {
        TeacherModel teacherModel = new TeacherModel();
        switch (teacher.getForename()) {
            case "empty":
                teacherModel.setForename("empty");
                return teacherModel;
            case "duplicate":
                teacherModel.setForename("duplicate");
                return teacherModel;
            default:
                teacherModel.setForename(teacher.getForename());
                teacherModel.setLastname(teacher.getLastname());
                teacherModel.setEmail(teacher.getEmail());

                teacher.getSubject().forEach(subject -> {
                    teacherModel.subjects.add(subject.getTitle());
                });
                teacher.getStudent().forEach(student -> {
                    teacherModel.students.add(student.getForename()+" "+ student.getLastname() +" "+  student.getEmail());
                });

                return teacherModel;
        }
    }
    public List<TeacherModel> toModelList(List<Teacher> teachers) {

        List<TeacherModel> teacherModels = new ArrayList<>();

        teachers.forEach(teacher -> {
            TeacherModel sm = new TeacherModel();
            sm.forename = teacher.getForename();
            sm.lastname = teacher.getLastname();
            sm.email = teacher.getEmail();
            teacher.getSubject().forEach(subject -> {
                sm.subjects.add(subject.getTitle());
            });

            teacherModels.add(sm);
        });
        return teacherModels;
    }
}
