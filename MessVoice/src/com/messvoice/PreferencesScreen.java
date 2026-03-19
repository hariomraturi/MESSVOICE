package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import com.messvoice.data.DataHandler;

public class PreferencesScreen extends JDialog {
    private DataHandler dh = new DataHandler();
    private String studentName;
    private JCheckBox vegOnly, noOnion, noGarlic, noDairy, jainFood;

    public PreferencesScreen(JFrame parent, String studentName) {
        super(parent, "My Preferences", true);
        this.studentName = studentName;
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Set Your Preferences", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        vegOnly = new JCheckBox("Vegetarian only");
        noOnion = new JCheckBox("No onion");
        noGarlic = new JCheckBox("No garlic");
        noDairy = new JCheckBox("No dairy");
        jainFood = new JCheckBox("Jain food");

        panel.add(vegOnly);
        panel.add(noOnion);
        panel.add(noGarlic);
        panel.add(noDairy);
        panel.add(jainFood);

        JButton saveBtn = new JButton("Save Preferences");
        saveBtn.addActionListener(e -> savePreferences());

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void savePreferences() {
        List<String> prefs = new ArrayList<>();
        if (vegOnly.isSelected()) prefs.add("vegetarian_only");
        if (noOnion.isSelected()) prefs.add("no_onion");
        if (noGarlic.isSelected()) prefs.add("no_garlic");
        if (noDairy.isSelected()) prefs.add("no_dairy");
        if (jainFood.isSelected()) prefs.add("jain_food");

        dh.saveStudentPreferences(studentName, prefs);
        JOptionPane.showMessageDialog(this, "Preferences saved successfully!");
        dispose();
    }
}