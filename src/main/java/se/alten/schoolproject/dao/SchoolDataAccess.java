package se.alten.schoolproject.dao;

import org.apache.lucene.facet.taxonomy.SearcherTaxonomyManager;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.transaction.TeacherTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
//här vi ska göra våra beräkningar, transaktioner etc
@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();
    private Subject subject = new Subject();
    private SubjectModel subjectModel = new SubjectModel();
    private Teacher teacher = new Teacher();
    private TeacherModel teacherModel = new TeacherModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Inject
    TeacherTransactionAccess teacherTransactionAccess;

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

                List<Subject> subjects = subjectTransactionAccess.getSubjectsByName(studentToAdd.getSubjects());

                subjects.forEach(sub -> {
                    studentToAdd.getSubject().add(sub);
                });
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

    @Override
    public List listAllSubjects() {
        List  withIndex = subjectTransactionAccess.listAllSubjects();
        List<SubjectModel> removedIndexList = new ArrayList();
        for (Object o : withIndex){
            Subject iteratedSubject = Subject.class.cast(o);
            SubjectModel model = subjectModel.toModel(iteratedSubject);
            removedIndexList.add(model);
        }
        return removedIndexList;
    }
    //TODO:Write this method
    @Override
    public Response addSubject(String subjectModel) {

        Subject subjectToAdd = subject.toEntity(subjectModel);
        Set<Student> students = new HashSet<>();
        subjectToAdd.setStudents(students);
        if (subjectToAdd.getTitle().length() == 0 || subjectToAdd.getTitle()== null)   {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Fill in title please\"}").build();
        }

        int hits = subjectTransactionAccess.addSubject(subjectToAdd);
        if(noHits(hits)){
            List<String> subjectResponse = new ArrayList<>();
            subjectResponse.add(subjectToAdd.getTitle());
            return getSubjects(subjectResponse);
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Already exists.\"}").build();
    }
    @Override
    public Response addStudentToSubject(String studentEmail, String subjectName){

        Student studentToAdd =studentTransactionAccess.getStudent(studentEmail);
        Subject subjectToAdd = singleSubjectFromDBByName(subjectName);
        int AmountOfHits= subjectTransactionAccess.addStudentToSubject(subjectToAdd, studentToAdd);
        return Response.ok().build();
    }

    @Override
    public Response addTeacherToSubject(String teacherEmail, String subjectName){

        Teacher teacherToAdd =teacherTransactionAccess.getTeacher(teacherEmail);
        Subject subjectToAdd = singleSubjectFromDBByName(subjectName);
        int AmountOfHits= subjectTransactionAccess.addTeacherToSubject(subjectToAdd, teacherToAdd);
        return Response.ok().build();
    }
    @Override
    public Response removeStudentFromSubject(String studentEmail, String subjectName){
        Student studentToRemove =studentTransactionAccess.getStudent(studentEmail);
        Subject subject = singleSubjectFromDBByName(subjectName);
        int amountOfHits= subjectTransactionAccess.RemoveStudentFromSubject(subject, studentToRemove);
        if (amountOfHits >0){
            return Response.ok("true").build();
        }
        return Response.ok("false").build();
    }

    @Override
    public List listAllTeachers() {
        List  withIndex = teacherTransactionAccess.listAllTeachers();
        List<TeacherModel> removedIndexList = new ArrayList();
        for (Object o : withIndex){
            Teacher iteratedTeacher = Teacher.class.cast(o);
            TeacherModel model = teacherModel.toModel(iteratedTeacher);
            removedIndexList.add(model);
        }
        return removedIndexList;
    }

    @Override
    public Response getTeacher(String email) {
        Teacher teacherToGet = teacherTransactionAccess.getTeacher(email);
        TeacherModel model = teacherModel.toModel(teacherToGet);
        Response response = Response.ok(model).build();
        return response;
    }

    @Override
    public Response addTeacher(String teacherModel) {
        Teacher teacherToAdd = teacher.toEntity(teacherModel);

        if (checkForEmptyVariables(teacherToAdd)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Fill in all details please\"}").build();
        }

        int hits = teacherTransactionAccess.addTeacher(teacherToAdd);
        if(noHits(hits)){

            List<Subject> subjects = subjectTransactionAccess.getSubjectsByName(teacherToAdd.getSubjects());

            subjects.forEach(sub -> {
                teacherToAdd.getSubject().add(sub);
            });
            return getTeacher(teacherToAdd.getEmail());
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Already exists.\"}").build();

    }

    @Override
    public boolean removeTeacher(String email) {
        int amountOfDeletions = teacherTransactionAccess.removeTeacher(email);
        if (amountOfDeletions > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public Response updateTeacher(String teacherModel) {
        Teacher teacherToUpdate = teacher.toEntity(teacherModel);

        if (checkForEmptyVariables(teacherToUpdate)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Fill in all details please\"}").build();
        }

        int hits = teacherTransactionAccess.updateTeacher(teacherToUpdate);
        if(noHits(hits)){
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"No match in DB.\"}").build();
        }
        return getTeacher(teacherToUpdate.getEmail());
    }

    @Override
    public Response updateTeacherPartial(String teacherModel) {
        Teacher teacherToUpdate = teacher.toEntity(teacherModel);

        int hits = teacherTransactionAccess.updateTeacherPartial(teacherToUpdate);
        if(noHits(hits)){
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"No match in DB.\"}").build();
        }
        return getTeacher(teacherToUpdate.getEmail());
    }

    @Override
    public Response removeTeacherFromSubject(String teacherEmail, String subjectName) {
        Teacher teacherToRemove =teacherTransactionAccess.getTeacher(teacherEmail);
        Subject subject = singleSubjectFromDBByName(subjectName);

        int amountOfHits= subjectTransactionAccess.RemoveTeacherFromSubject(subject, teacherToRemove);
        if (amountOfHits >0){
            return Response.ok("true").build();
        }
        return Response.ok("false").build();
    }

    @Override
    public Response addStudentToTeacher(String studentEmail, String teacherEmail) {
        Student studentToAdd =studentTransactionAccess.getStudent(studentEmail);
        Teacher teacherToAdd = teacherTransactionAccess.getTeacher(teacherEmail);
        int AmountOfHits= teacherTransactionAccess.addStudentToTeacher(teacherToAdd, studentToAdd);
        return Response.ok().build();
    }

    @Override
    public Response removeStudentFromTeacher(String studentEmail, String teacherEmail) {
        Student studentToRemove =studentTransactionAccess.getStudent(studentEmail);
        Teacher teacher = teacherTransactionAccess.getTeacher(teacherEmail);
        int amountOfHits= teacherTransactionAccess.RemoveStudentFromTeacher(teacher, studentToRemove);
        if (amountOfHits >0){
            return Response.ok("true").build();
        }
        return Response.ok("false").build();
    }

    @Override
    public Response getSubjects(List<String> name) {
        List withIndex = subjectTransactionAccess.getSubjectsByName(name);

        List<SubjectModel> removedIndexList = new ArrayList();
        for (Object o : withIndex){
            Subject iteratedSubject = Subject.class.cast(o);
            SubjectModel model = subjectModel.toModel(iteratedSubject);
            removedIndexList.add(model);
        }
        Response response = Response.ok(removedIndexList).build();
        return response;
    }

    @Override
    public boolean removeSubject(String subject) {
        int amountOfDeletions = subjectTransactionAccess.removeSubject(subject);
        if (amountOfDeletions > 0) {
            return true;
        } else
            return false;
    }


    public boolean checkForEmptyVariables(Student student) {
        return Stream.of(student.getForename(), student.getLastname(), student.getEmail()).anyMatch(String::isBlank);
    }

    public boolean checkForEmptyVariables(Teacher teacher) {
        return Stream.of(teacher.getForename(), teacher.getLastname(), teacher.getEmail()).anyMatch(String::isBlank);
    }

    public boolean noHits(int sqlResponse) {
        if (sqlResponse > 0) {
            return false;
        }
        return true;
    }
    public Subject singleSubjectFromDBByName(String name){
        List<String> subjectArray= new ArrayList<>();
        subjectArray.add(name);
        List<Subject> recievedSubject = subjectTransactionAccess.getSubjectsByName(subjectArray);
        return recievedSubject.get(0);
    }
}
