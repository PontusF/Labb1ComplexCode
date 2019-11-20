package se.alten.schoolproject.transaction;

import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONObject;
import se.alten.schoolproject.entity.Student;

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
public class StudentTransaction implements StudentTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllStudents() {
        entityManager.flush();
        Query query = entityManager.createQuery("SELECT s from Student s");
        return query.getResultList();

    }

    @Override
    public Student getStudent(String email) {
        try{
            entityManager.flush();
            String queryString = "SELECT s from Student s WHERE s.email= :email";
            Query query = entityManager.createQuery(queryString);
            //Query query = entityManager.createNativeQuery(queryString, Student.class);
            query.setParameter("email", email);
            Object answer = query.getSingleResult();
            Student result = Student.class.cast(answer);
            return result;
        }catch(Exception e){
            Student student = new Student();
            return student.toEntity("{}");
        }


    }


    @Override
    public int addStudent(Student studentToAdd) {
        try {
            entityManager.flush();
            entityManager.persist(studentToAdd);
            entityManager.flush();
            return 0;
        } catch ( PersistenceException pe ) {
            return 1;
        }
    }

    @Override
    public int removeStudent(String student) {
        //JPQL Query
        entityManager.flush();
        Query query = entityManager.createQuery("DELETE FROM Student s WHERE s.email = :email");

        //Native Query
        //Query query = entityManager.createNativeQuery("DELETE FROM student WHERE email = :email", Student.class);
        //named för att det ska vara säkrare
        query.setParameter("email", student);
             return query.executeUpdate();
    }

    @Override
    public int updateStudent(Student student) {
        entityManager.flush();
        Student studentFound = (Student)entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                .setParameter("email", student.getEmail()).getSingleResult();
        Query updateQuery = entityManager.createNativeQuery("UPDATE student SET forename = :forename, lastname = :lastname WHERE email = :email", Student.class);

        updateQuery.setParameter("forename", student.getForename())
                   .setParameter("lastname", student.getLastname())
                   .setParameter("email", studentFound.getEmail());
        int response = updateQuery.executeUpdate();
        entityManager.flush();
        return response;
    }

    @Override
    public int updateStudentPartial(Student student) {
        entityManager.flush();
        Student studentFound = (Student)entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                .setParameter("email", student.getEmail()).getSingleResult();

        Query query = entityManager.createQuery("UPDATE Student SET forename = :studentForename WHERE email = :email");
        query.setParameter("studentForename", student.getForename())
                .setParameter("email", studentFound.getEmail())
                .executeUpdate();
        return query.executeUpdate();
    }
}
