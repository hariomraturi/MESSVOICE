package com.messvoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Map;
import com.messvoice.data.DataHandler;
import com.messvoice.model.MenuItem;

public class AdminDashboard extends JFrame {
    private DataHandler dh; 
    
    public AdminDashboard() {
        dh = new DataHandler();
        setTitle("Admin Dashboard - MessVoice");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        JLabel title = new JLabel("MessVoice Admin Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(41, 128, 185));
        mainPanel.add(title, BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Vote Results", createVoteResultsPanel());
        tabbedPane.addTab("Detailed Table", createTablePanel());  // NEW JTABLE TAB
        tabbedPane.addTab("Feedback", createFeedbackPanel());
        tabbedPane.addTab("Actions", createActionsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }
    
    // ==================== NEW: JTABLE PANEL ====================
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Vote Details Table", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);
        
        // Create table
        String[] columns = {"Dish Name", "Meal Type", "Votes", "Percentage", "Avg Rating"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        Map<String, Integer> votes = dh.getVoteCountPerDish();
        Map<String, Double> percentages = dh.getVotePercentages();
        
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            String dish = entry.getKey();
            int voteCount = entry.getValue();
            double percent = percentages.getOrDefault(dish, 0.0);
            double rating = dh.getAverageRating(dish);
            
            // Get meal type
            String mealType = "N/A";
            for (MenuItem item : dh.getMasterMenu()) {
                if (item.getDishName().equals(dish)) {
                    mealType = item.getMealType();
                    break;
                }
            }
            
            Object[] row = {dish, mealType, voteCount, String.format("%.1f%%", percent), String.format("%.1f", rating)};
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            Map<String, Integer> newVotes = dh.getVoteCountPerDish();
            Map<String, Double> newPercentages = dh.getVotePercentages();
            
            for (Map.Entry<String, Integer> entry : newVotes.entrySet()) {
                String dish = entry.getKey();
                int voteCount = entry.getValue();
                double percent = newPercentages.getOrDefault(dish, 0.0);
                double rating = dh.getAverageRating(dish);
                
                String mealType = "N/A";
                for (MenuItem item : dh.getMasterMenu()) {
                    if (item.getDishName().equals(dish)) {
                        mealType = item.getMealType();
                        break;
                    }
                }
                model.addRow(new Object[]{dish, mealType, voteCount, String.format("%.1f%%", percent), String.format("%.1f", rating)});
            }
        });
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== VOTE RESULTS PANEL (Progress Bars) ====================
    
    private JPanel createVoteResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Map<String, Integer> votes = dh.getVoteCountPerDish();
        Map<String, Double> percentages = dh.getVotePercentages();
        
        if (votes.isEmpty()) {
            JLabel noData = new JLabel("No votes recorded this week", JLabel.CENTER);
            noData.setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(noData, BorderLayout.CENTER);
            return panel;
        }
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        int totalVotes = 0;
        for (int count : votes.values()) {
            totalVotes += count;
        }
        
        JLabel totalLabel = new JLabel("Total Votes This Week: " + totalVotes);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(totalLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Vote Results"));
        
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            String dish = entry.getKey();
            int count = entry.getValue();
            double percent = percentages.getOrDefault(dish, 0.0);
            
            JPanel itemPanel = new JPanel(new BorderLayout(5, 2));
            itemPanel.setMaximumSize(new Dimension(800, 40));
            
            JLabel dishLabel = new JLabel(dish + " (" + count + " votes)");
            dishLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue((int) percent);
            bar.setStringPainted(true);
            bar.setString(String.format("%.1f%%", percent));
            bar.setForeground(new Color(46, 204, 113));
            
            itemPanel.add(dishLabel, BorderLayout.WEST);
            itemPanel.add(bar, BorderLayout.CENTER);
            progressPanel.add(itemPanel);
            progressPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        mainPanel.add(progressPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Predicted portions
        JPanel predictionPanel = new JPanel();
        predictionPanel.setLayout(new BoxLayout(predictionPanel, BoxLayout.Y_AXIS));
        predictionPanel.setBorder(BorderFactory.createTitledBorder("Predicted Portions (20% extra)"));
        
        Map<String, Integer> predictions = dh.getPredictedPortions();
        for (Map.Entry<String, Integer> entry : predictions.entrySet()) {
            JPanel predItemPanel = new JPanel(new BorderLayout(5, 2));
            predItemPanel.setMaximumSize(new Dimension(800, 30));
            
            JLabel dishLabel = new JLabel(entry.getKey());
            JLabel portionLabel = new JLabel(entry.getValue() + " portions");
            portionLabel.setForeground(new Color(41, 128, 185));
            portionLabel.setFont(new Font("Arial", Font.BOLD, 12));
            
            predItemPanel.add(dishLabel, BorderLayout.WEST);
            predItemPanel.add(portionLabel, BorderLayout.EAST);
            predictionPanel.add(predItemPanel);
            predictionPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        }
        mainPanel.add(predictionPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== FEEDBACK PANEL ====================
    
    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        StringBuilder text = new StringBuilder("===== AVERAGE RATINGS =====\n\n");
        Map<String, Integer> votes = dh.getVoteCountPerDish();
        
        if (votes.isEmpty()) {
            text.append("No feedback available yet.\n");
        } else {
            for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                String dish = entry.getKey();
                double avg = dh.getAverageRating(dish);
                text.append(dish).append(": ");
                
                int fullStars = (int) avg;
                for (int i = 0; i < fullStars; i++) text.append("★");
                for (int i = fullStars; i < 5; i++) text.append("☆");
                text.append(String.format(" (%.1f/5)", avg));
                if (avg < 3.0) text.append(" - NEEDS IMPROVEMENT");
                text.append("\n\n");
            }
        }
        
        feedbackArea.setText(text.toString());
        panel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== ACTIONS PANEL ====================
    
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JButton setMenuBtn = new JButton("Set Today's Menu");
        setMenuBtn.setBackground(new Color(52, 152, 219));
        setMenuBtn.setForeground(Color.WHITE);
        setMenuBtn.setFont(new Font("Arial", Font.BOLD, 16));
        setMenuBtn.addActionListener(e -> new TodayMenuScreen(this));
        
        JButton exportBtn = new JButton("Export Weekly Report");
        exportBtn.setBackground(new Color(46, 204, 113));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFont(new Font("Arial", Font.BOLD, 16));
        exportBtn.addActionListener(e -> exportReport());
        
        JButton refreshBtn = new JButton("Refresh Dashboard");
        refreshBtn.setBackground(new Color(155, 89, 182));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 16));
        refreshBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });
        
        panel.add(setMenuBtn);
        panel.add(exportBtn);
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private void exportReport() {
        try {
            File dir = new File("reports");
            if (!dir.exists()) dir.mkdir();
            
            String filename = "reports/weekly_report_" + LocalDate.now() + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            
            writer.write("MESSVOICE WEEKLY REPORT - " + LocalDate.now());
            writer.newLine();
            writer.newLine();
            writer.write("VOTE RESULTS:");
            writer.newLine();
            
            Map<String, Integer> votes = dh.getVoteCountPerDish();
            for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + " votes");
                writer.newLine();
            }
            
            writer.newLine();
            writer.write("PREDICTED PORTIONS:");
            writer.newLine();
            Map<String, Integer> predictions = dh.getPredictedPortions();
            for (Map.Entry<String, Integer> entry : predictions.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + " portions");
                writer.newLine();
            }
            
            writer.close();
            JOptionPane.showMessageDialog(this, "Report exported to " + filename);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}