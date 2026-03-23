package com.messvoice;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Map;
import com.messvoice.data.DataHandler;

public class AdminDashboard extends JFrame {
    private DataHandler dh = new DataHandler();

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(700, 600); // Larger size to see everything
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setFont(new Font("Arial", Font.PLAIN, 14));
        summary.setLineWrap(true);
        summary.setWrapStyleWord(true);
        summary.setMargin(new Insets(10, 10, 10, 10));

        StringBuilder text = new StringBuilder("Admin Dashboard - Latest Data\n\n");

        // Vote Counts
        Map<String, Integer> votes = dh.getVoteCountPerDish();
        if (votes.isEmpty()) {
            text.append("No votes recorded yet.\n");
        } else {
            text.append("Vote Counts:\n");
            votes.forEach((dish, count) -> text.append(dish).append(": ").append(count).append(" votes\n"));
        }

        // Average Ratings
        text.append("\nAverage Ratings:\n");
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            String dish = entry.getKey();
            double avg = dh.getAverageRating(dish);
            text.append(dish).append(": ").append(String.format("%.1f / 5", avg));
            if (avg < 3.0) {
                text.append(" (Low-rated - review needed)");
            }
            text.append("\n");
        }

        // Predicted Portions
        text.append("\nPredicted Portions (votes × 1.2 buffer):\n");
        votes.forEach((dish, count) -> {
            int predicted = (int) (count * 1.2);
            text.append(dish).append(": ~").append(predicted).append(" portions\n");
        });

        // Low-rated Dishes
        text.append("\nLow-rated Dishes (<3.0):\n");
        List<String> lowRated = dh.getLowRatedDishes(3.0);
        if (lowRated.isEmpty()) {
            text.append("None\n");
        } else {
            lowRated.forEach(dish -> text.append(dish).append("\n"));
        }

        summary.setText(text.toString());
        mainPanel.add(new JScrollPane(summary), BorderLayout.CENTER);

        // Progress Bars (East side)
        JPanel barPanel = new JPanel(new GridLayout(votes.size() + 1, 1, 5, 5));
        barPanel.add(new JLabel("Vote % Bars:"));
        votes.forEach((dish, count) -> {
            JProgressBar bar = new JProgressBar(0, 100);
            int percent = (int) ((count / 10.0) * 100); // adjust 10 if more students
            bar.setValue(percent);
            bar.setString(dish + ": " + percent + "%");
            bar.setStringPainted(true);
            barPanel.add(bar); 
        });
        mainPanel.add(barPanel, BorderLayout.EAST);

        // Export button (South)
        JButton exportBtn = new JButton("Export Weekly Report");
        exportBtn.addActionListener(e -> {
            StringBuilder report = new StringBuilder("MessVoice Weekly Report - " + LocalDate.now() + "\n\n");
            report.append(summary.getText());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("weekly_report.txt"))) {
                writer.write(report.toString());
                JOptionPane.showMessageDialog(this, "Report exported to weekly_report.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        mainPanel.add(exportBtn, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
