package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.model.Student;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class StudentRepositoryImpl extends BaseRepositoryImpl implements StudentRepository {


    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public StudentRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void save(Student entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Student entity) {
        entityManager.remove(entity);
    }

    @Override
    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public List<Student> getAll() {
        return entityManager.createNamedQuery("get-all-students", Student.class).getResultList();
    }

    @Override
    public List<Student> findStudentHavingGradeAverageAbove(double minAverage) {
        return entityManager.createQuery(
                "select s " +
                        "   from Student s " +
                        "   join s.grades g " +
                        "group by s having sum(g.value*g.weight)/sum(g.weight) > ?1",
                Student.class).setParameter(1, minAverage).getResultList();
    }
}
