package fr.uga.im2ag.l3.miage.db.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("S")
@NamedQuery(name = "get-all-students", query = "select s from Student s")
public class Student extends Person {

    @ManyToOne(fetch = FetchType.EAGER)
    private GraduationClass belongTo;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Grade> grades;

    public GraduationClass getBelongTo() {
        return belongTo;
    }

    public Student setBelongTo(GraduationClass belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public Student setGrades(List<Grade> grades) {
        this.grades = grades;
        return this;
    }
}
