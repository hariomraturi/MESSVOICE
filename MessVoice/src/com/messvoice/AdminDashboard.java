package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import com.messvoice.data.DataHandler;

public class AdminDashboard extends JFrame {
    private DataHandler dh = new DataHandler();

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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
            votes.forEach((dish, count) -> {
                text.append(dish).append(": ").append(count).append(" votes\n");
            });
        }

        // Average Ratings + Low-rated flags
        text.append("\nAverage Ratings:\n");
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            String dish = entry.getKey();
            double avg = dh.getAverageRating(dish);
            text.append(dish).append(": ");
            if (avg < 3.0) {
                text.append(String.format("%.1f / 5", avg)).append(" (Low-rated - review needed)\n");
            } else {
                text.append(String.format("%.1f / 5\n", avg));
            }
        }

        // Predicted Portions
        text.append("\nPredicted Portions (votes × 1.2 buffer):\n");
        votes.forEach((dish, count) -> {
            int predicted = (int) (count * 1.2);
            text.append(dish).append(": ~").append(predicted).append(" portions\n");
        });

        summary.setText(text.toString());
        add(new JScrollPane(summary), BorderLayout.CENTER);

        setVisible(true);
    }
}