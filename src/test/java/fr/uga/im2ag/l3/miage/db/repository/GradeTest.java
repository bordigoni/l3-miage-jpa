package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.dao.api.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GradeTest extends Base {

    GradeRepository gradeRepository;
    SubjectRepository subjectRepository;

    @BeforeEach
    void before() {
        gradeRepository = daoFactory.newGradeRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveGrade() {
        final var subject = Fixtures.createSubject();
        final var grade = Fixtures.createGrade(subject);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        entityManager.getTransaction().commit();
        entityManager.detach(grade);
        entityManager.detach(subject);

        var persistentGrade = gradeRepository.findById(Grade.class, grade.getId());
        assertThat(persistentGrade).isNotNull().isNotSameAs(grade);
        assertThat(persistentGrade.getSubject()).isNotNull().isNotSameAs(grade.getSubject());

        assertThat(persistentGrade.getValue()).isEqualTo(grade.getValue());
        assertThat(persistentGrade.getSubject().getName()).isEqualTo(grade.getSubject().getName());
    }

    @Test
    void shouldFailUpdateGrade() {
        final var subject = Fixtures.createSubject();
        final var grade = Fixtures.createGrade(subject);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        entityManager.getTransaction().commit();

        final var value = grade.getValue();

        entityManager.getTransaction().begin();
        grade.setValue(10.0f);
        gradeRepository.save(grade);
        entityManager.getTransaction().commit();

        entityManager.detach(grade);

        Grade gradeFromDB = gradeRepository.findById(Grade.class, grade.getId());

        assertThat(gradeFromDB.getValue()).isEqualTo(value);

    }

    @Test
    void shouldFindHighestGrades() {
        final var subject1 = Fixtures.createSubject();
        final var grade1_s1 = Fixtures.createGrade(subject1);
        final var grade2_s1 = Fixtures.createGrade(subject1);
        final var subject2 = Fixtures.createSubject();
        final var grade1_s2 = Fixtures.createGrade(subject2);
        final var grade2_s2 = Fixtures.createGrade(subject2);
        final var grade3_s2 = Fixtures.createGrade(subject2);

        grade1_s1.setValue(21f);
        grade1_s2.setValue(21f);
        grade2_s2.setValue(21f);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        gradeRepository.save(grade1_s1);
        gradeRepository.save(grade2_s1);
        gradeRepository.save(grade1_s2);
        gradeRepository.save(grade2_s2);
        gradeRepository.save(grade3_s2);
        entityManager.getTransaction().commit();

        final var highestGrades = gradeRepository.findHighestGrades(3);
        assertThat(highestGrades)
                .extracting(Grade::getId)
                .containsExactlyInAnyOrder(grade1_s1.getId(), grade1_s2.getId(), grade2_s2.getId());

    }

    @Test
    void shouldFindHighestGradesBySubject() {
        final var subject1 = Fixtures.createSubject();
        final var grade1_s1 = Fixtures.createGrade(subject1);
        final var grade2_s1 = Fixtures.createGrade(subject1);
        final var subject2 = Fixtures.createSubject();
        final var grade1_s2 = Fixtures.createGrade(subject2);
        final var grade2_s2 = Fixtures.createGrade(subject2);
        final var grade3_s2 = Fixtures.createGrade(subject2);

        grade1_s2.setValue(21f);
        grade2_s2.setValue(21f);


        entityManager.getTransaction().begin();
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        gradeRepository.save(grade1_s1);
        gradeRepository.save(grade2_s1);
        gradeRepository.save(grade1_s2);
        gradeRepository.save(grade2_s2);
        gradeRepository.save(grade3_s2);
        entityManager.getTransaction().commit();

        final var highestGradesBySubject = gradeRepository.findHighestGradesBySubject(2, subject2);
        assertThat(highestGradesBySubject)
                .extracting(Grade::getId)
                .containsExactlyInAnyOrder(grade1_s2.getId(), grade2_s2.getId());

    }

}
