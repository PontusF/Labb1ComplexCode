package se.alten.schoolproject.transaction;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONObject;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Default
public class TeacherTransaction implements TeacherTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllTeachers() {
        entityManager.flush();
        Query query = entityManager.createQuery("SELECT s from Teacher s");
        return query.getResultList();

    }

    @Override
    public Teacher getTeacher(String email) {
        try{
            entityManager.flush();
            String queryString = "SELECT s from Teacher s WHERE s.email= :email";
            Query query = entityManager.createQuery(queryString);
            //Query query = entityManager.createNativeQuery(queryString, Student.class);
            query.setParameter("email", email);
            Object answer = query.getSingleResult();
            Teacher result = Teacher.class.cast(answer);
            return result;
        }catch(Exception e){
            return new Teacher();
        }


    }


    @Override
    public int addTeacher(Teacher teacherToAdd) {
        try {
            entityManager.flush();
            entityManager.persist(teacherToAdd);
            entityManager.flush();
            return 0;
        } catch ( PersistenceException pe ) {
            return 1;
        }
    }

    @Override
    public int removeTeacher(String teacher) {
        //JPQL Query
        entityManager.flush();
        Query query = entityManager.createQuery("DELETE FROM Teacher s WHERE s.email = :email");

        query.setParameter("email", teacher);
        return query.executeUpdate();
    }

    @Override
    public int updateTeacher(Teacher teacher) {
        entityManager.flush();
        Teacher teacherFound = (Teacher)entityManager.createQuery("SELECT s FROM Teacher s WHERE s.email = :email")
                .setParameter("email", teacher.getEmail()).getSingleResult();
        Query updateQuery = entityManager.createNativeQuery("UPDATE teacher SET forename = :forename, lastname = :lastname WHERE email = :email", Teacher.class);

        updateQuery.setParameter("forename", teacher.getForename())
                .setParameter("lastname", teacher.getLastname())
                .setParameter("email", teacherFound.getEmail());
        int response = updateQuery.executeUpdate();
        entityManager.flush();
        return response;
    }

    @Override
    public int updateTeacherPartial(Teacher teacher) {
        entityManager.flush();
        Teacher teacherFound = (Teacher)entityManager.createQuery("SELECT s FROM Teacher s WHERE s.email = :email")
                .setParameter("email", teacher.getEmail()).getSingleResult();

        Query query = entityManager.createQuery("UPDATE Teacher SET forename = :teacherForename WHERE email = :email");
        query.setParameter("teacherForename", teacher.getForename())
                .setParameter("email", teacherFound.getEmail())
                .executeUpdate();
        return query.executeUpdate();
    }

    @Override
    public int addStudentToTeacher(Teacher teacher, Student student) {
        entityManager.flush();
        Query query = entityManager.createNativeQuery("INSERT INTO teacher_student(teach_id,stud_id)" +
                " VALUES (:teachId, :studId)");
        query.setParameter("teachId", teacher.getId());
        query.setParameter("studId", student.getId());
        return query.executeUpdate();
    }

    @Override
    public int RemoveStudentFromTeacher(Teacher teacher, Student studentToRemove) {
        entityManager.flush();
        Query query = entityManager.createNativeQuery("DELETE FROM teacher_student s WHERE " +
                "s.stud_id = :studId AND s.teach_id = :teachId");
        query.setParameter("studId", studentToRemove.getId());
        query.setParameter("teachId", teacher.getId());
        return query.executeUpdate();
    }
}
