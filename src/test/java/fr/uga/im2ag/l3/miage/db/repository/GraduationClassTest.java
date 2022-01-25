package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GraduationClassTest extends Base {

    GraduationClassRepository classRepository;

    @BeforeEach
    void before() {
        classRepository = daoFactory.newGraduationClassRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveClass() {
        final var aClass = Fixtures.createClass();
        entityManager.getTransaction().begin();
        classRepository.save(aClass);
        entityManager.getTransaction().commit();
        entityManager.detach(aClass);
        GraduationClass persistentClass = classRepository.findById(GraduationClass.class, aClass.getId());
        assertThat(persistentClass)
                .isNotSameAs(aClass);
        assertThat(persistentClass.getName()).isEqualTo(aClass.getName());
        assertThat(persistentClass.getYear()).isEqualTo(aClass.getYear());
    }


    @Test
    void shouldFindByYearAndName() {
        final var class1 = Fixtures.createClass();
        final var class2 = Fixtures.createClass();
        entityManager.getTransaction().begin();
        classRepository.save(class1);
        classRepository.save(class2);
        entityManager.getTransaction().commit();

        final var byYearAndName = classRepository.findByYearAndName(class1.getYear(), class1.getName());
        assertThat(byYearAndName.getId()).isEqualTo(class1.getId());

    }

}
