package com.messvoice.data;
import com.messvoice.model.MenuItem;
import java.io.*;
import java.util.*;

public class DataHandler {
    private static final String VOTES_FILE = "data/votes.txt";
    private static final String FEEDBACK_FILE="data/feedback.txt";

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
 // Save feedback
    public void saveFeedback(String studentName, String dish, int rating, String comment) {
        System.out.println("Trying to save feedback: " + studentName + " - " + dish + " - " + rating);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FEEDBACK_FILE, true))) {
            writer.write(studentName + "," + dish + "," + rating + "," + comment.replace(",", " "));
            writer.newLine();
            System.out.println("Feedback saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }

    // Get average rating for a dish
    public double getAverageRating(String dishName) {
        int total = 0;
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].trim().equals(dishName)) {
                    total += Integer.parseInt(parts[2].trim());
                    count++;
                }
            }
        } catch (IOException | NumberFormatException e) {
            // ignore
        }
        return count == 0 ? 0 : (double) total / count;
    }
 // Save student preferences
    public void saveStudentPreferences(String studentName, List<String> prefs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/preferences.txt", true))) {
            writer.write(studentName + "," + String.join(",", prefs));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get student preferences
    public List<String> getStudentPreferences(String studentName) {
        List<String> prefs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/preferences.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(studentName + ",")) {
                    String[] parts = line.split(",");
                    for (int i = 1; i < parts.length; i++) {
                        prefs.add(parts[i].trim());
                    }
                    break;
                }
            }
        } catch (IOException e) {
            // no preferences yet
        }
        return prefs;
    }
 // Get all menu items (hardcoded for simplicity)
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Poha", "Monday Breakfast", "veg"));
        items.add(new MenuItem("Aloo Paratha", "Monday Breakfast", "veg"));
        items.add(new MenuItem("Dal Makhani", "Monday Lunch", "veg"));
        items.add(new MenuItem("Rajma", "Monday Lunch", "veg"));
        return items;
    }
    public List<String> getLowRatedDishes(double threshold) {
        List<String> lowRated = new ArrayList<>();
        Map<String, Double> ratings = getAverageRatingPerDish();
        for (Map.Entry<String, Double> entry : ratings.entrySet()) {
            if (entry.getValue() < threshold) {
                lowRated.add(entry.getKey());
            }
        }
        return lowRated;
    }
    public Map<String, Double> getAverageRatingPerDish() {
        Map<String, Double> averages = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String dish = parts[1].trim();
                    int rating = Integer.parseInt(parts[2].trim());
                    averages.put(dish, averages.getOrDefault(dish, 0.0) + rating);
                    // Wait, this is count + sum, need to adjust
                    // Better: use two maps or count separately
                    // Simple version for now (average per dish)
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        // To make it correct, let's calculate properly
        Map<String, Integer> sumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String dish = parts[1].trim();
                    int rating = Integer.parseInt(parts[2].trim());
                    sumMap.put(dish, sumMap.getOrDefault(dish, 0) + rating);
                    countMap.put(dish, countMap.getOrDefault(dish, 0) + 1);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        for (String dish : sumMap.keySet()) {
            int sum = sumMap.get(dish);
            int count = countMap.get(dish);
            averages.put(dish, count == 0 ? 0.0 : (double) sum / count);
        }
        return averages;
    }
    
}