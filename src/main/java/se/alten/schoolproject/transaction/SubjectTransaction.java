package se.alten.schoolproject.transaction;


import org.hibernate.Session;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Stateless
@Default
public class SubjectTransaction implements SubjectTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllSubjects() {
        Query query = entityManager.createQuery("SELECT s FROM Subject s");
        return query.getResultList();
    }

    @Override
    public int addSubject(Subject subject) {
        try {
            entityManager.flush();
            entityManager.persist(subject);
            entityManager.flush();
            return 0;
        } catch ( PersistenceException pe ) {
            return 1;
        }
    }

    @Override
    public List<Subject> getSubjectsByName(List<String> subject) {
        entityManager.flush();
        String queryStr = "SELECT sub FROM Subject sub WHERE sub.title IN :subject";
        TypedQuery<Subject> query = entityManager.createQuery(queryStr, Subject.class);
        query.setParameter("subject", subject);

        return query.getResultList();
    }

    @Override
    public int addStudentToSubject(Subject subject, Student student) {
        try{
            entityManager.flush();
            Query query = entityManager.createNativeQuery("INSERT INTO student_subject(stud_id,subj_id)" +
                    " VALUES (:studId, :subjId)");
            query.setParameter("studId", student.getId());
            query.setParameter("subjId", subject.getId());
            query.executeUpdate();
            return 1;
        }catch(Exception e){
            return 0;
        }


    }

    @Override
    public int removeSubject(String title) {
        //JPQL Query
        entityManager.flush();
        Query query = entityManager.createQuery("DELETE FROM Subject s WHERE s.title = :title");
        query.setParameter("title", title);
        return query.executeUpdate();
    }

    @Override
    public int RemoveStudentFromSubject(Subject subject, Student studentToRemove) {
        entityManager.flush();
        Query query = entityManager.createNativeQuery("DELETE FROM student_subject s WHERE " +
                "s.stud_id = :studId AND s.subj_id = :subjId");
        query.setParameter("studId", studentToRemove.getId());
        query.setParameter("subjId", subject.getId());
        return query.executeUpdate();

    }

    @Override
    public int addTeacherToSubject(Subject subject, Teacher teacher) {
        try{
            entityManager.flush();
            Query query = entityManager.createNativeQuery("INSERT INTO teacher_subject(teach_id,subj_id)" +
                    " VALUES (:teachId, :subjId)");
            query.setParameter("teachId", teacher.getId());
            query.setParameter("subjId", subject.getId());
            query.executeUpdate();
            return 1;
        }catch(Exception e){
            return 0;
        }


    }

    @Override
    public int RemoveTeacherFromSubject(Subject subject, Teacher teacherToRemove) {
        entityManager.flush();
        Query query = entityManager.createNativeQuery("DELETE FROM teacher_subject s WHERE " +
                "s.teach_id = :teachId AND s.subj_id = :subjId");
        query.setParameter("teachId", teacherToRemove.getId());
        query.setParameter("subjId", subject.getId());
        return query.executeUpdate();

    }
}