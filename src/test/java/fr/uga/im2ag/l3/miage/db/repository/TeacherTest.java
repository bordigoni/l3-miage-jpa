package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.dao.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.dao.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.dao.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherTest extends Base {

    TeacherRepository teacherRepository;
    SubjectRepository subjectRepository;
    GraduationClassRepository graduationClassRepository;
    StudentRepository studentRepository;

    @BeforeEach
    void before() {
        teacherRepository = daoFactory.newTeacherRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
        graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
        studentRepository = daoFactory.newStudentRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveTeacher() {
        final var subject = Fixtures.createSubject();
        final var aClass = Fixtures.createClass();
        final var stu1 = Fixtures.createStudent(aClass);
        final var stu2 = Fixtures.createStudent(aClass);

        final var teacher = Fixtures.createTeacher(subject, aClass, stu1, stu2);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        graduationClassRepository.save(aClass);
        studentRepository.save(stu1);
        studentRepository.save(stu2);
        teacherRepository.save(teacher);
        entityManager.getTransaction().commit();

        entityManager.detach(teacher);
        entityManager.detach(aClass);
        entityManager.detach(stu1);
        entityManager.detach(stu2);
        entityManager.detach(teacher);

        final var teacherFromDB = teacherRepository.findById(Teacher.class, teacher.getId());
        assertThat(teacherFromDB).isNotSameAs(teacher);
        assertThat(teacherFromDB.getTeaching()).isNotNull();
        assertThat(teacherFromDB.getTeaching().getName()).isEqualTo(teacher.getTeaching().getName());
        assertThat(teacherFromDB.getFavorites()).isNotSameAs(teacher.getFavorites());
        assertThat(teacherFromDB.getFavorites())
                .extracting(Student::getId)
                .containsExactly(stu1.getId(), stu2.getId());
        assertThat(teacherFromDB.getHeading()).isNotNull().isNotSameAs(teacher.getHeading());
        assertThat(teacherFromDB.getHeading().getName()).isEqualTo(teacher.getHeading().getName());

    }

    @Test
    void shouldDeleteTeacher() {
        final var subject = Fixtures.createSubject();
        final var aClass = Fixtures.createClass();
        final var stu1 = Fixtures.createStudent(aClass);
        final var stu2 = Fixtures.createStudent(aClass);

        final var teacher = Fixtures.createTeacher(subject, aClass, stu1, stu2);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        graduationClassRepository.save(aClass);
        studentRepository.save(stu1);
        studentRepository.save(stu2);
        teacherRepository.save(teacher);
        entityManager.getTransaction().commit();

        teacherRepository.delete(teacher);

        final var teacherFromDB = teacherRepository.findById(Teacher.class, teacher.getId());
        assertThat(teacherFromDB).isNull();

    }

    @Test
    void shouldFindHeadingGraduationClassByYearAndName() {

        final var subject = Fixtures.createSubject();
        final var aClass = Fixtures.createClass();
        aClass.setYear(2020);
        aClass.setName("L3MIAGE");
        final var teacher = Fixtures.createTeacher(subject, aClass);
        final var aClass2 = Fixtures.createClass();
        final var teacher2 = Fixtures.createTeacher(subject, aClass2);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        graduationClassRepository.save(aClass);
        graduationClassRepository.save(aClass2);
        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);
        entityManager.getTransaction().commit();

        final var l3MIAGE2020Teacher = teacherRepository.findHeadingGraduationClassByYearAndName(2020, "L3MIAGE");
        assertThat(l3MIAGE2020Teacher).isNotNull();
        assertThat(l3MIAGE2020Teacher.getId()).isEqualTo(teacher.getId());
    }

}
