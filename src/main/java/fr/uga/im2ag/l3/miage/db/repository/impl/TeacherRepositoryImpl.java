package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;
import fr.uga.im2ag.l3.miage.db.model.Teacher;

import javax.persistence.EntityManager;

import java.util.List;

public class TeacherRepositoryImpl extends BaseRepositoryImpl implements TeacherRepository {

    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public TeacherRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public Teacher findHeadingGraduationClassByYearAndName(Integer year, String name) {
        return entityManager.createQuery("select t from Teacher t join t.heading c where c.year=?1 and c.name=?2", Teacher.class)
                .setParameter(1, year)
                .setParameter(2, name)
                .getSingleResult();
    }

    @Override
    public void save(Teacher entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Teacher entity) {
        entityManager.remove(entity);
    }

    @Override
    public Teacher findById(Long id) {
        return entityManager.find(Teacher.class, id);
    }

    @Override
    public List<Teacher> getAll() {
        return entityManager.createNamedQuery("get-all-teachers", Teacher.class).getResultList();
    }
}
