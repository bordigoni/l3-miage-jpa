package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTest extends Base {

    StudentRepository studentRepository;
    GradeRepository gradeRepository;
    GraduationClassRepository graduationClassRepository;
    SubjectRepository subjectRepository;

    @BeforeEach
    void before() {
        studentRepository = daoFactory.newStudentRepository(entityManager);
        gradeRepository = daoFactory.newGradeRepository(entityManager);
        graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveStudent() {
        final var aClass = Fixtures.createClass();
        final var student = Fixtures.createStudent(aClass);
        final var subject = Fixtures.createSubject();
        final var grade1 = Fixtures.createGrade(subject);
        final var grade2 = Fixtures.createGrade(subject);
        student.setGrades(Arrays.asList(grade1, grade2));

        entityManager.getTransaction().begin();
        graduationClassRepository.save(aClass);
        subjectRepository.save(subject);
        studentRepository.save(student);
        aClass.addStudent(student);

        entityManager.getTransaction().commit();

        entityManager.detach(aClass);
        entityManager.detach(student);
        entityManager.detach(subject);
        entityManager.detach(grade1);
        entityManager.detach(grade2);

        final var studentDB = studentRepository.findById(Student.class, student.getId());
        assertThat(studentDB).isNotSameAs(student);
        assertThat(studentDB.getLastName()).isEqualTo(student.getLastName());
        assertThat(studentDB.getGrades())
                .extracting(Grade::getValue)
                .containsExactlyInAnyOrder(grade1.getValue(), grade2.getValue());
        assertThat(studentDB.getBelongTo().getName())
                .isEqualTo(student.getBelongTo().getName());

        final var graduationClassDB = graduationClassRepository.findById(GraduationClass.class, aClass.getId());
        assertThat(graduationClassDB).isNotSameAs(aClass);
        assertThat(graduationClassDB.getStudents()).extracting(Student::getLastName).containsExactly(student.getLastName());

    }

    @Test
    void shouldUpdateStudent() {
        final var aClass = Fixtures.createClass();
        final var student = Fixtures.createStudent(aClass);
        final var subject = Fixtures.createSubject();
        final var grade1 = Fixtures.createGrade(subject);
        final var grade2 = Fixtures.createGrade(subject);
        student.setGrades(Arrays.asList(grade1, grade2));

        entityManager.getTransaction().begin();
        graduationClassRepository.save(aClass);
        subjectRepository.save(subject);
        studentRepository.save(student);
        aClass.addStudent(student);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        final var grades = new ArrayList<>(student.getGrades());
        grades.add(Fixtures.createGrade(subject));
        student.setGrades(grades);
        studentRepository.save(student);
        entityManager.getTransaction().commit();
        entityManager.detach(student);

        final var studentDB = studentRepository.findById(Student.class, student.getId());
        assertThat(studentDB).isNotSameAs(student);
        assertThat(studentDB.getGrades()).hasSize(3);
    }

    @Test
    void shouldDeleteStudent() {
        // TODO
    }

    @Test
    void shouldFindStudentHavingGradeAverageAbove() {
        final var aClass = Fixtures.createClass();
        final var student1 = Fixtures.createStudent(aClass);
        final var student2 = Fixtures.createStudent(aClass);
        final var subject1 = Fixtures.createSubject();
        final var grade1 = Fixtures.createGrade(subject1);
        final var grade2 = Fixtures.createGrade(subject1);
        grade1.setValue(15f);
        grade2.setValue(15f);
        student1.setGrades(Arrays.asList(grade1, grade2));
        final var subject2 = Fixtures.createSubject();
        final var grade3 = Fixtures.createGrade(subject2);
        final var grade4 = Fixtures.createGrade(subject2);
        grade3.setValue(4f);
        grade4.setValue(4f);
        student2.setGrades(Arrays.asList(grade3, grade4));

        entityManager.getTransaction().begin();
        graduationClassRepository.save(aClass);
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        studentRepository.save(student1);
        studentRepository.save(student2);
        aClass.addStudent(student1);
        aClass.addStudent(student2);
        entityManager.getTransaction().commit();

        final var studentHavingGradeAverageAbove10 = studentRepository.findStudentHavingGradeAverageAbove(10.f);
        assertThat(studentHavingGradeAverageAbove10).extracting(Student::getLastName).containsExactly(student1.getLastName());

    }

}
