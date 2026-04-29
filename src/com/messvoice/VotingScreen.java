package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.messvoice.data.DataHandler;
import com.messvoice.model.MenuItem;

public class VotingScreen extends JDialog {
    private DataHandler dh;
    private String rollNumber;
    private JCheckBox[] checkboxes;
    private List<MenuItem> menuItems; 
    
    public VotingScreen(JFrame parent, String rollNumber) {
        super(parent, "Weekly Voting", true);
        this.rollNumber = rollNumber;
        dh = new DataHandler();
        
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Check if already voted
        if (dh.hasVotedThisWeek(rollNumber)) {
            JOptionPane.showMessageDialog(this, 
                "You have already voted this week!\nVoting opens again on Monday.");
            dispose();
            return;
        }
        
        // Title
        JLabel title = new JLabel("Vote for Next Week's Menu", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);
        
        // Menu items with preferences
        menuItems = dh.getMasterMenu();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        List<String> prefs = dh.getStudentPreferences(rollNumber);
        checkboxes = new JCheckBox[menuItems.size()];
        
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            JCheckBox cb = new JCheckBox(item.getDishName() + " (" + item.getMealType() + " - " + item.getCategory() + ")");
            
            // Apply preferences
            boolean disabled = false;
            if (prefs.contains("vegetarian_only") && item.getCategory().equals("non-veg")) {
                disabled = true;
            }
            if (prefs.contains("no_onion") && item.getDishName().toLowerCase().contains("onion")) {
                disabled = true;
            }
            if (prefs.contains("no_garlic") && item.getDishName().toLowerCase().contains("garlic")) {
                disabled = true;
            }
            
            if (disabled) {
                cb.setEnabled(false);
                cb.setForeground(Color.GRAY);
                cb.setText(cb.getText() + " (Restricted by your preferences)");
            }
            
            panel.add(cb);
            checkboxes[i] = cb;
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
        
        // Submit button
        JButton submitBtn = new JButton("Submit Vote");
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitVote());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void submitVote() {
        List<Integer> selectedDishIds = new ArrayList<>();
        
        for (int i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].isSelected()) {
                selectedDishIds.add(menuItems.get(i).getId());
            }
        }
        
        if (selectedDishIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one dish");
            return;
        }
        
        boolean success = dh.saveVote(rollNumber, selectedDishIds);
        if (success) {
            JOptionPane.showMessageDialog(this, "Vote submitted successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error submitting vote. You may have already voted.");
        }
    }
}