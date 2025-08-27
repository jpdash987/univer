package org.ums.model;

import jakarta.persistence.*;

@Entity @Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_name", unique = true, nullable = false)
    private String userName;
    private String fullName;
    private String email;
    private String status;

    // getters/setters
    public Long getId(){return id;}
    public String getUserName(){return userName;}
    public void setUserName(String u){this.userName=u;}
    public String getFullName(){return fullName;}
    public void setFullName(String f){this.fullName=f;}
    public String getEmail(){return email;}
    public void setEmail(String e){this.email=e;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
}