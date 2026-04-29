package com.messvoice;

import javax.swing.*;

import com.messvoice.data.DataHandler;
import com.messvoice.database.DBConnection;

import java.awt.*;


public class LoginScreen extends JFrame {
    private JTextField rollField;
    private JTextField nameField;
    private DataHandler dh;

    public LoginScreen() {
        dh = new DataHandler();
        setTitle("MessVoice - Student Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel title = new JLabel("Welcome to MessVoice", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);
        
        // Roll number
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Roll Number:"), gbc);
        
        rollField = new JTextField(15);
        gbc.gridx = 1;
        add(rollField, gbc);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Name:"), gbc);
        
        nameField = new JTextField(15);
        gbc.gridx = 1;
        add(nameField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(46, 204, 113));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.addActionListener(e -> doLogin());
        
        JButton registerBtn = new JButton("New User? Register");
        registerBtn.setBackground(new Color(52, 152, 219));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.addActionListener(e -> doRegister());
        
        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setBackground(new Color(231, 76, 60));
        adminBtn.setForeground(Color.WHITE);
        adminBtn.addActionListener(e -> adminLogin());
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        buttonPanel.add(adminBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
        
        setVisible(true);
    }
    
    private void doLogin() {
        String roll = rollField.getText().trim();
        String name = nameField.getText().trim();
        
        if (roll.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both roll number and name");
            return;
        }
        
        if (dh.validateStudent(roll, name)) {
            new StudentDashboard(roll, name);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Student not found! Please register first.");
        }
    }
    
    private void doRegister() {
        String roll = rollField.getText().trim();
        String name = nameField.getText().trim();
        
        if (roll.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter roll number and name");
            return;
        }
        
        if (dh.registerStudent(roll, name)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
        } else {
            JOptionPane.showMessageDialog(this, "Student already exists!");
        }
    }
    
    private void adminLogin() {
        String pass = JOptionPane.showInputDialog("Enter Admin Password:");
        if ("mess123".equals(pass)) {
            new AdminDashboard(); 
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Wrong password!");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test database connection first
            if (DBConnection.testConnection()) {
                DataHandler dh = new DataHandler();
                dh.checkNewWeek();  // Check for new week
                new LoginScreen().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Database connection failed!\nCheck if MySQL is running.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}