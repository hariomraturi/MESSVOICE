package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {
    private JTextField nameField;

    public LoginScreen() {
        setTitle("MessVoice - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Welcome to MessVoice", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Name/Roll:"), gbc);

        nameField = new JTextField(20);
        gbc.gridy = 1;
        gbc.gridx = 1;
        add(nameField, gbc);

        JButton studentBtn = new JButton("Login as Student");
        studentBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter name/roll");
                return;
            }
            new StudentDashboard(name); // Opens student dashboard
            dispose(); // Close login window
        });
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(studentBtn, gbc);

        JButton adminBtn = new JButton("Login as Admin");
        adminBtn.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog("Admin Password:");
            if ("mess123".equals(pass)) {
                new AdminDashboard();  // Opens admin dashboard
                dispose();  // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Wrong password");
            }
        });
        gbc.gridy = 3;
        add(adminBtn, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}