package com.messvoice.data;

import java.io.*;
import java.util.*;

public class DataHandler {
    private static final String VOTES_FILE = "data/votes.txt";

    // Save a vote (student name + list of dishes)
    public void saveVote(String studentName, List<String> dishes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VOTES_FILE, true))) {
            writer.write(studentName + "," + String.join(",", dishes));
            writer.newLine();
            System.out.println("Vote saved for " + studentName);
        } catch (IOException e) {
            System.err.println("Error saving vote: " + e.getMessage());
        }
    }

    // Check if student voted this week (basic check)
    public boolean hasVotedThisWeek(String studentName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(VOTES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(studentName + ",")) {
                    return true;
                }
            }
        } catch (IOException e) {
            // file not found = no vote yet
        }
        return false;
    }

    // Get vote count per dish
    public Map<String, Integer> getVoteCountPerDish() {
        Map<String, Integer> counts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(VOTES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                for (int i = 1; i < parts.length; i++) { // skip name
                    String dish = parts[i].trim();
                    counts.put(dish, counts.getOrDefault(dish, 0) + 1);
                }
            }
        } catch (IOException e) {
            // file not found = empty
        }
        return counts;
    }
    
}