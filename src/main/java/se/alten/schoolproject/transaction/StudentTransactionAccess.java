package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    Student getStudent(String email);
    int addStudent(Student studentToAdd);
    int removeStudent(String student);
    int updateStudent(Student studentToUpdate);
    int updateStudentPartial(Student studentToUpdate);
}
