package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.model.Teacher;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class SubjectRepositoryImpl extends BaseRepositoryImpl implements SubjectRepository {

    public SubjectRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void save(Subject entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Subject entity) {
        entityManager.remove(entity);
    }

    @Override
    public Subject findById(Long id) {
        return entityManager.find(Subject.class, id);
    }

    @Override
    public List<Subject> getAll() {
        return entityManager.createNamedQuery("get-all-subjects", Subject.class)
                .getResultList();
    }

    @Override
    public Collection<Teacher> findTeachers(Long id) {
        return entityManager.createQuery(
                        "select t from Teacher t join t.teaching s where s.id=?1",
                        Teacher.class)
                .setParameter(1, id)
                .getResultList();
    }
}
