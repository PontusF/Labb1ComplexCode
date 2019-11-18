package se.alten.schoolproject.dao;

import javax.ejb.Local;
import javax.ws.rs.core.Response;
import java.util.List;

@Local //hit vår inject böna hamnar
public interface SchoolAccessLocal {

    List listAllStudents() throws Exception;

    Response getStudent(String email);

    Response addStudent(String studentModel);

    boolean removeStudent(String student);

    Response updateStudent(String studentModel);

    Response updateStudentPartial(String studentModel);

    List listAllSubjects();

    Response addSubject(String subjectModel);

    Response addStudentToSubject(String studentEmail, String subjectName);

    Response addTeacherToSubject(String teacherEmail, String subjectName);

    Response getSubjects(List<String> name);

    boolean removeSubject(String subject);

    Response removeStudentFromSubject(String studentEmail, String subjectName);

    List listAllTeachers();

    Response getTeacher(String email);

    Response addTeacher(String teacherModel);

    boolean removeTeacher(String email);

    Response updateTeacher(String teacherModel);

    Response updateTeacherPartial(String teacherModel);

    Response removeTeacherFromSubject(String teacherEmail, String subject);

    Response addStudentToTeacher(String studentEmail, String teacherEmail);

    Response removeStudentFromTeacher(String studentEmail, String teacherEmail);
}
