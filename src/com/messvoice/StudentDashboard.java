package com.messvoice;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private String rollNumber;
    private String studentName;
    
    public StudentDashboard(String rollNumber, String studentName) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        
        setTitle("MessVoice - Student Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(41, 128, 185));
        JLabel welcomeLabel = new JLabel("Welcome, " + studentName + " (" + rollNumber + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JButton voteBtn = createButton("Cast Your Vote", new Color(46, 204, 113));
        voteBtn.addActionListener(e -> {
            new VotingScreen(this, rollNumber);
            dispose();
        });
        
        JButton feedbackBtn = createButton("Give Feedback", new Color(52, 152, 219));
        feedbackBtn.addActionListener(e -> {
            new FeedbackScreen(this, rollNumber);
        });
        
        JButton prefBtn = createButton("My Preferences", new Color(155, 89, 182));
        prefBtn.addActionListener(e -> {
            new PreferencesScreen(this, rollNumber);
        });
        
        buttonPanel.add(voteBtn);
        buttonPanel.add(feedbackBtn);
        buttonPanel.add(prefBtn);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        return btn;
    }
}