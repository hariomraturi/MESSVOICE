package com.messvoice.model;
import java.time.LocalDate;

public class Vote {
    private int voteId;
    private int studentId;
    private String studentName;
    private String dishName;
    private LocalDate voteDate;
    private int weekNumber;
    
    public Vote() {}
     
    public Vote(int studentId, String studentName, String dishName, LocalDate voteDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.dishName = dishName;
        this.voteDate = voteDate;
        this.weekNumber = voteDate.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
    }
    
    public int getVoteId() {
        return voteId;
    }
    
    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getDishName() {
        return dishName;
    }
    
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
    
    public LocalDate getVoteDate() {
        return voteDate;
    }
    
    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
        this.weekNumber = voteDate.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
    }
    
    public int getWeekNumber() {
        return weekNumber;
    }
}