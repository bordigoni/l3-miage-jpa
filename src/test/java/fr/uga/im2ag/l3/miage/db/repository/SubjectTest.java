package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.dao.api.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubjectTest extends Base {

    SubjectRepository subjectRepository;
    TeacherRepository teacherRepository;

    @BeforeEach
    public void before() {
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
        teacherRepository = daoFactory.newTeacherRepository(entityManager);
    }

    @AfterEach
    public void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveSubject() {

        final var subject = Fixtures.createSubject();

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        entityManager.getTransaction().commit();
        entityManager.detach(subject);

        var pSubject = subjectRepository.findById(subject.getId());
        assertThat(pSubject).isNotNull().isNotSameAs(subject);
        assertThat(pSubject.getName()).isEqualTo(subject.getName());

    }

    @Test
    void shouldUpdateSubject() {
        final var subject = Fixtures.createSubject();

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        subject.setName("My Subject");
        subjectRepository.save(subject);
        entityManager.getTransaction().commit();
        entityManager.detach(subject);

        Subject subjectFromDB = subjectRepository.findById(Subject.class, subject.getId());
        assertThat(subject).isNotSameAs(subjectFromDB);
        assertThat(subject.getName()).isEqualTo("My Subject");

    }

    @Test
    void shouldDeleteSubject() {
        final var subject = Fixtures.createSubject();

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        entityManager.getTransaction().commit();

        final var all = subjectRepository.getAll(Subject.class);
        assertThat(all).contains(subject);

        subjectRepository.delete(subject);
        assertThat(subjectRepository.findById(Subject.class, subject.getId())).isNull();
    }

    @Test
    void shouldFindTeachersForSubject() {

        entityManager.getTransaction().begin();

        final var subject = Fixtures.createSubject();
        final var subject2 = Fixtures.createSubject();
        subjectRepository.save(subject);
        subjectRepository.save(subject2);
        final var teacher1 = Fixtures.createTeacher(subject, null);
        final var teacher2 = Fixtures.createTeacher(subject, null);
        final var teacher3 = Fixtures.createTeacher(subject2, null);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        entityManager.getTransaction().commit();

        var teachers = subjectRepository.findTeachers(subject.getId());
        assertThat(teachers)
                .containsExactlyInAnyOrder(teacher1, teacher2);

        var teachers2 = subjectRepository.findTeachers(subject2.getId());
        assertThat(teachers2)
                .containsExactly(teacher3);

    }

}
