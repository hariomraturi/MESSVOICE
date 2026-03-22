package com.messvoice.model;
import java.time.LocalDate;
public class Vote {
	 private String studentName;
	    private String dishName;
	    private LocalDate voteDate;

	    public Vote() {}

	    public Vote(String studentName, String dishName, LocalDate voteDate) {
	        this.studentName = studentName;
	        this.dishName = dishName;
	        this.voteDate = voteDate;
	    }

	    public String getStudentName() { return studentName; }
	    public void setStudentName(String studentName) { this.studentName = studentName; }
	    public String getDishName() { return dishName; }
	    public void setDishName(String dishName) { this.dishName = dishName; }
	    public LocalDate getVoteDate() { return voteDate; }
	    public void setVoteDate(LocalDate voteDate) { this.voteDate = voteDate; }
}
