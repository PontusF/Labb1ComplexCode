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
}
