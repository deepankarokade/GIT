package com.git.Admin.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_sets")
public class ExamSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String setName; // e.g. "a", "b", "c"

    @Column(unique = true)
    private String setId; // e.g. "SET-A", "SET-B"

    public ExamSet() {
    }

    public ExamSet(Long id, String setName, String setId) {
        this.id = id;
        this.setName = setName;
        this.setId = setId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ExamSet examSet = (ExamSet) o;
        return id != null && id.equals(examSet.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
