package org.ums.model;

import jakarta.persistence.*;

@Entity @Table(name="admissions")
public class Admission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private String program;
    private String status; // PENDING, APPROVED, REJECTED
    // getters/setters
    public Long getId(){return id;}
    public Long getStudentId(){return studentId;}
    public void setStudentId(Long id){this.studentId=id;}
    public String getProgram(){return program;}
    public void setProgram(String p){this.program=p;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
}