package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
//här vi ska göra våra beräkningar, transaktioner etc
@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List listAllStudents() {
        List  withIndex = studentTransactionAccess.listAllStudents();
        List<StudentModel> removedIndexList = new ArrayList();
        for (Object o : withIndex){
            Student iteratedStudent = Student.class.cast(o);
            StudentModel model = studentModel.toModel(iteratedStudent);
            removedIndexList.add(model);
        }
        return removedIndexList;
    }

    @Override
    public Response getStudent(String email) {
            Student studentToGet = studentTransactionAccess.getStudent(email);
           StudentModel model = studentModel.toModel(studentToGet);
            Response response = Response.ok(model).build();
            return response;
    }
    //write code here
    @Override
    public Response addStudent(String newStudent) {
        Student studentToAdd = student.toEntity(newStudent);

        if (checkForEmptyVariables(studentToAdd)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Fill in all details please\"}").build();
        }

            int hits = studentTransactionAccess.addStudent(studentToAdd);
            if(noHits(hits)){
                return getStudent(studentToAdd.getEmail());
            }
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Already exists.\"}").build();

    }

    @Override
    public boolean removeStudent(String studentEmail) {
        int amountOfDeletions = studentTransactionAccess.removeStudent(studentEmail);
        if (amountOfDeletions > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public Response updateStudent(String studentModel) {
        Student studentToUpdate = student.toEntity(studentModel);

        if (checkForEmptyVariables(studentToUpdate)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Fill in all details please\"}").build();
        }

        int hits = studentTransactionAccess.updateStudent(studentToUpdate);
        if(noHits(hits)){
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"No match in DB.\"}").build();
        }
        return getStudent(studentToUpdate.getEmail());
    }

    @Override
    public Response updateStudentPartial(String studentModel) {
        Student studentToUpdate = student.toEntity(studentModel);

        int hits = studentTransactionAccess.updateStudentPartial(studentToUpdate);
        if(noHits(hits)){
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"No match in DB.\"}").build();
        }
        return getStudent(studentToUpdate.getEmail());

    }

    public boolean checkForEmptyVariables(Student student) {
        return Stream.of(student.getForename(), student.getLastname(), student.getEmail()).anyMatch(String::isBlank);
    }

    public boolean noHits(int sqlResponse) {
        if (sqlResponse > 0) {
            return false;
        }
        return true;
    }
}
