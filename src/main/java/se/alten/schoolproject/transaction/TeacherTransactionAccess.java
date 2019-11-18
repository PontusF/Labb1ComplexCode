package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Local;
import java.util.List;

@Local
public interface TeacherTransactionAccess {
    List listAllTeachers();
    Teacher getTeacher(String email);
    int addTeacher(Teacher teacherToAdd);
    int removeTeacher(String teacher);
    int updateTeacher(Teacher teacherToUpdate);
    int updateTeacherPartial(Teacher teacherToUpdate);
    int addStudentToTeacher(Teacher teacherToAdd, Student studentToAdd);

    int RemoveStudentFromTeacher(Teacher teacher, Student studentToRemove);
}
