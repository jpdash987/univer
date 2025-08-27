package org.ums.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classes",
       uniqueConstraints = @UniqueConstraint(name = "uq_classes_code_term", columnNames = {"code", "term"}))
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String term;

    @Column(name = "teacher_user_id", nullable = false)
    private Long teacherUserId;

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public ClassEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClassEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getTerm() {
        return term;
    }

    public ClassEntity setTerm(String term) {
        this.term = term;
        return this;
    }

    public Long getTeacherUserId() {
        return teacherUserId;
    }

    public ClassEntity setTeacherUserId(Long teacherUserId) {
        this.teacherUserId = teacherUserId;
        return this;
    }
}