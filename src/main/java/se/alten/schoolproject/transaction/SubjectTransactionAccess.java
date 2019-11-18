package se.alten.schoolproject.transaction;


import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List listAllSubjects();
    int addSubject(Subject subject);
    List<Subject> getSubjectsByName(List<String> subject);
    int addStudentToSubject(Subject subject, Student student);

    int removeSubject(String subject);

    int RemoveStudentFromSubject(Subject subject, Student studentToRemove);

    int addTeacherToSubject(Subject subjectToAdd, Teacher teacherToAdd);

    int RemoveTeacherFromSubject(Subject subject, Teacher teacherToRemove);
}