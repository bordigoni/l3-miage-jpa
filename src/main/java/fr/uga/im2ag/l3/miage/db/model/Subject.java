package fr.uga.im2ag.l3.miage.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@NamedQueries(
        @NamedQuery(name = "get-all-subjects", query = "select s from Subject s")
)
public class Subject {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Float hours;

    @Column(nullable = false)
    private Date start;

    @Column(nullable = false, name = "s_end")
    private Date end;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Subject setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public Subject setPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Float getHours() {
        return hours;
    }

    public Subject setHours(Float hours) {
        this.hours = hours;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public Subject setStart(Date start) {
        this.start = start;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public Subject setEnd(Date end) {
        this.end = end;
        return this;
    }
}
