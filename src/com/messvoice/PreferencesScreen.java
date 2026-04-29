package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.messvoice.data.DataHandler;

public class PreferencesScreen extends JDialog {
    private DataHandler dh;
    private String rollNumber;
    private JCheckBox vegOnly, noOnion, noGarlic;
    
    public PreferencesScreen(JFrame parent, String rollNumber) {
        super(parent, "Dietary Preferences", true);
        this.rollNumber=rollNumber;
        
        dh = new DataHandler();
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Set Your Dietary Preferences", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);
        
        // Checkboxes
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        vegOnly = new JCheckBox("Vegetarian Only");
        vegOnly.setFont(new Font("Arial", Font.PLAIN, 14));
        
        noOnion = new JCheckBox("No Onion");
        noOnion.setFont(new Font("Arial", Font.PLAIN, 14));
        
        noGarlic = new JCheckBox("No Garlic");
        noGarlic.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(vegOnly);
        panel.add(noOnion);
        panel.add(noGarlic);
        
        add(panel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        
        JButton saveBtn = new JButton("Save Preferences");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> savePreferences());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load existing preferences
        loadPreferences();
        
        setVisible(true);
    }
     
    private void loadPreferences() {
        List<String> prefs = dh.getStudentPreferences(rollNumber);
        vegOnly.setSelected(prefs.contains("vegetarian_only"));
        noOnion.setSelected(prefs.contains("no_onion"));
        noGarlic.setSelected(prefs.contains("no_garlic"));
    }
    
    private void savePreferences() {
        List<String> prefs = new ArrayList<>();
        if (vegOnly.isSelected()) prefs.add("vegetarian_only");
        if (noOnion.isSelected()) prefs.add("no_onion");
        if (noGarlic.isSelected()) prefs.add("no_garlic");
        
        dh.saveStudentPreferences(rollNumber, prefs);
        JOptionPane.showMessageDialog(this, "Preferences saved successfully!");
        dispose();
    }
}