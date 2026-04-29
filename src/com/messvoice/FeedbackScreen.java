package com.messvoice;

import javax.swing.*;
import java.awt.*;
import com.messvoice.data.DataHandler;

public class FeedbackScreen extends JDialog {
    private DataHandler dh;
    private String rollNumber;
    private JComboBox<String> dishCombo;
    private JSlider ratingSlider;
    private JTextArea commentArea;
    
    public FeedbackScreen(JFrame parent, String rollNumber) {
        super(parent, "Give Feedback", true);
        this.rollNumber = rollNumber;
        dh = new DataHandler();
        
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Rate Today's Meal", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Dish selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Select Dish:"), gbc);
        
        dishCombo = new JComboBox<>();
        dh.getMasterMenu().forEach(item -> dishCombo.addItem(item.getDishName()));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(dishCombo, gbc);
        
        // Rating
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Rating:"), gbc);
        
        ratingSlider = new JSlider(1, 5, 3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(ratingSlider, gbc);
        
        // Comment
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Comment:"), gbc);
        
        commentArea = new JTextArea(5, 20);
        commentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(commentArea);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(scrollPane, gbc);
        
        add(formPanel, BorderLayout.CENTER); 
        
        // Submit button
        JButton submitBtn = new JButton("Submit Feedback");
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitFeedback());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void submitFeedback() {
        String dish = (String) dishCombo.getSelectedItem();
        int rating = ratingSlider.getValue();
        String comment = commentArea.getText().trim();
        
        if (comment.isEmpty()) {
            comment = "No comment provided";
        }
        
        dh.saveFeedback(rollNumber, dish, rating, comment);
        JOptionPane.showMessageDialog(this, "Thank you for your feedback!");
        dispose();
    }
}