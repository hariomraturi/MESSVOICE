package com.messvoice.model;
import java.time.LocalDate;
public class Feedback {
	private String studentName;
    private String dishName;
    private int rating;
    private String comment;
    private LocalDate feedbackDate;

    public Feedback() {}

    public Feedback(String studentName, String dishName, int rating, String comment, LocalDate feedbackDate) {
        this.studentName = studentName;
        this.dishName = dishName;
        this.rating = rating;
        this.comment = comment;
        this.feedbackDate = feedbackDate;
    }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDate getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDate feedbackDate) { this.feedbackDate = feedbackDate; }
}
