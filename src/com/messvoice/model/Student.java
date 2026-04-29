package com.messvoice.model;

public class Student {
    private int studentId;
    private String rollNumber;
    private String name;
    
    public Student() {}
    
    public Student(int studentId, String rollNumber, String name) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.name = name;
    }
    
    public int getStudentId() {
        return studentId;
    }
     
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getRollNumber() {
        return rollNumber;
    }
    
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}