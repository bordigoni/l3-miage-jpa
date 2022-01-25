package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Subject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class GradeRepositoryImpl extends BaseRepositoryImpl implements GradeRepository {

    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public GradeRepositoryImpl(EntityManager entityManager) {
        super(entityManager);

    }

    @Override
    public List<Grade> findHighestGrades(int limit) {
        final TypedQuery<Grade> gradeQuery = entityManager.createQuery("select g from Grade g order by g.value desc", Grade.class)
                .setMaxResults(limit);
        return gradeQuery.getResultList();
    }

    @Override
    public List<Grade> findHighestGradesBySubject(int limit, Subject subject) {
         final  TypedQuery<Grade> gradeQuery = entityManager.createQuery(
                 "select g from Grade g join g.subject s where s=?1 order by g.value desc",
                         Grade.class)
                 .setParameter(1, subject)
                 .setMaxResults(limit);

        return gradeQuery.getResultList();

    }

    @Override
    public void save(Grade entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Grade entity) {
        entityManager.remove(entity);
    }

    @Override
    public Grade findById(Long id) {
        return entityManager.find(Grade.class, id);
    }

    @Override
    public List<Grade> getAll() {
        return entityManager.createNamedQuery("get-all-grades", Grade.class)
                .getResultList();
    }
}
