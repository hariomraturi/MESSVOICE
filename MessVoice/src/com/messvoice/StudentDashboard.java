package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {
    private String studentName;

    public StudentDashboard(String studentName) {
        this.studentName = studentName;
        setTitle("Student Dashboard - " + studentName);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel("Welcome " + studentName, JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcome);

        JButton voteBtn = new JButton("Weekly Voting");
        voteBtn.addActionListener(e -> new VotingScreen(this, studentName));
        panel.add(voteBtn);

        JButton feedbackBtn = new JButton("Daily Feedback");
        feedbackBtn.addActionListener(e -> new FeedbackScreen(this, studentName));
        panel.add(feedbackBtn);

        JButton prefsBtn = new JButton("My Preferences");
        prefsBtn.addActionListener(e -> new PreferencesScreen(this, studentName));
        panel.add(prefsBtn);

        add(panel);

        setVisible(true);
    }
}