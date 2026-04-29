package com.messvoice.data;

import com.messvoice.database.DBConnection;
import com.messvoice.model.MenuItem;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DataHandler {
    
    // ==================== STUDENT METHODS ====================
    
    public boolean registerStudent(String rollNumber, String name) {
        String sql = "INSERT INTO students (roll_number, student_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering student: " + e.getMessage());
            return false;
        }
    }
    
    public boolean validateStudent(String rollNumber, String name) {
        String sql = "SELECT * FROM students WHERE roll_number = ? AND student_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.setString(2, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error validating student: " + e.getMessage());
            return false;
        }
    }
    
    public int getStudentId(String rollNumber) {
        String sql = "SELECT student_id FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("student_id");
            }
        } catch (SQLException e) {
            System.out.println("Error getting student ID: " + e.getMessage());
        }
        return -1;
    }
    
    // ==================== MASTER MENU METHODS ====================
    
    public List<MenuItem> getMasterMenu() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM master_menu WHERE is_available = TRUE ORDER BY meal_type, dish_name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setId(rs.getInt("dish_id"));
                item.setDishName(rs.getString("dish_name"));
                item.setMealType(rs.getString("meal_type"));
                item.setCategory(rs.getString("category"));
                item.setAvailable(rs.getBoolean("is_available"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error getting master menu: " + e.getMessage());
        }
        return items;
    }
    
    public List<MenuItem> getDishesByMealType(String mealType) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM master_menu WHERE meal_type = ? AND is_available = TRUE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mealType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setId(rs.getInt("dish_id"));
                item.setDishName(rs.getString("dish_name"));
                item.setMealType(rs.getString("meal_type"));
                item.setCategory(rs.getString("category"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error getting dishes by meal type: " + e.getMessage());
        }
        return items;
    }
    
    public boolean addDishToMenu(String dishName, String mealType, String category) {
        String sql = "INSERT INTO master_menu (dish_name, meal_type, category) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dishName);
            pstmt.setString(2, mealType);
            pstmt.setString(3, category);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding dish: " + e.getMessage());
            return false;
        }
    }
    
    public boolean removeDishFromMenu(int dishId) {
        String sql = "UPDATE master_menu SET is_available = FALSE WHERE dish_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dishId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error removing dish: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== DAILY MENU METHODS (Simplified - returns List of dish names) ====================
    
    public void saveTodayMenu(List<Integer> dishIds, String mealType) {
        LocalDate today = LocalDate.now();
        
        // First delete existing menu for this meal type today
        String deleteSql = "DELETE FROM daily_menu WHERE menu_date = ? AND meal_type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setDate(1, Date.valueOf(today));
            pstmt.setString(2, mealType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting existing menu: " + e.getMessage());
        }
        
        // Insert new menu items
        String insertSql = "INSERT INTO daily_menu (menu_date, dish_id, meal_type) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            for (int dishId : dishIds) {
                pstmt.setDate(1, Date.valueOf(today));
                pstmt.setInt(2, dishId);
                pstmt.setString(3, mealType);
                pstmt.executeUpdate();
            }
            System.out.println("Today's " + mealType + " menu saved successfully");
        } catch (SQLException e) {
            System.out.println("Error saving today's menu: " + e.getMessage());
        }
    }
    
    // Returns list of dish names for today
    public List<String> getTodayMenu() {
        List<String> dishes = new ArrayList<>();
        String sql = "SELECT m.dish_name FROM daily_menu dm " +
                    "JOIN master_menu m ON dm.dish_id = m.dish_id " +
                    "WHERE dm.menu_date = ? ORDER BY dm.meal_type";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                dishes.add(rs.getString("dish_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting today's menu: " + e.getMessage());
        }
        return dishes;
    }
    
    // Returns map of meal type to list of dishes
    public Map<String, List<String>> getTodayMenuByMealType() {
        Map<String, List<String>> menuMap = new HashMap<>();
        menuMap.put("Breakfast", new ArrayList<>());
        menuMap.put("Lunch", new ArrayList<>());
        menuMap.put("Dinner", new ArrayList<>());
        
        String sql = "SELECT m.dish_name, dm.meal_type FROM daily_menu dm " +
                    "JOIN master_menu m ON dm.dish_id = m.dish_id " +
                    "WHERE dm.menu_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String dishName = rs.getString("dish_name");
                String mealType = rs.getString("meal_type");
                menuMap.get(mealType).add(dishName);
            }
        } catch (SQLException e) {
            System.out.println("Error getting menu by meal type: " + e.getMessage());
        }
        return menuMap;
    }
    
    public boolean isTodaysMenuSet(String mealType) {
        String sql = "SELECT COUNT(*) FROM daily_menu WHERE menu_date = ? AND meal_type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setString(2, mealType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking today's menu: " + e.getMessage());
        }
        return false;
    }
    
    // ==================== VOTING METHODS ====================
    
    public boolean saveVote(String rollNumber, List<Integer> dishIds) {
        int studentId = getStudentId(rollNumber);
        if (studentId == -1) return false;
        
        LocalDate today = LocalDate.now();
        int weekNum = today.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
        
        String sql = "INSERT INTO votes (student_id, dish_id, vote_date, week_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int dishId : dishIds) {
                pstmt.setInt(1, studentId);
                pstmt.setInt(2, dishId);
                pstmt.setDate(3, Date.valueOf(today));
                pstmt.setInt(4, weekNum);
                pstmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving vote: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasVotedThisWeek(String rollNumber) {
        int studentId = getStudentId(rollNumber);
        if (studentId == -1) return false;
        
        LocalDate today = LocalDate.now();
        int currentWeek = today.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
        
        String sql = "SELECT COUNT(*) FROM votes WHERE student_id = ? AND week_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, currentWeek);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking vote: " + e.getMessage());
        }
        return false;
    }
    
    public Map<String, Integer> getVoteCountPerDish() {
        Map<String, Integer> counts = new HashMap<>();
        LocalDate today = LocalDate.now();
        int currentWeek = today.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
        
        String sql = "SELECT m.dish_name, COUNT(v.vote_id) as vote_count " +
                    "FROM votes v JOIN master_menu m ON v.dish_id = m.dish_id " +
                    "WHERE v.week_number = ? GROUP BY v.dish_id ORDER BY vote_count DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentWeek);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                counts.put(rs.getString("dish_name"), rs.getInt("vote_count"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting vote counts: " + e.getMessage());
        }
        return counts;
    }
    
    public Map<String, Double> getVotePercentages() {
        Map<String, Integer> counts = getVoteCountPerDish();
        Map<String, Double> percentages = new HashMap<>();
        
        int total = 0;
        for (int count : counts.values()) {
            total += count;
        }
        
        if (total > 0) {
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                double percent = (entry.getValue() * 100.0) / total;
                percentages.put(entry.getKey(), percent);
            }
        }
        return percentages;
    }
    
    public Map<String, Integer> getPredictedPortions() {
        Map<String, Integer> predictions = new HashMap<>();
        Map<String, Integer> votes = getVoteCountPerDish();
        
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            String dish = entry.getKey();
            int voteCount = entry.getValue();
            int predicted = (int) Math.ceil(voteCount * 1.2);
            predictions.put(dish, predicted);
        }
        return predictions;
    }
    
    // ==================== FEEDBACK METHODS ====================
    
    public void saveFeedback(String rollNumber, String dishName, int rating, String comment) {
        int studentId = getStudentId(rollNumber);
        if (studentId == -1) {
            System.out.println("Student not found: " + rollNumber);
            return;
        }
        
        // Get dish id from dish name (case insensitive)
        String getDishSql = "SELECT dish_id FROM master_menu WHERE LOWER(dish_name) = LOWER(?)";
        int dishId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getDishSql)) {
            pstmt.setString(1, dishName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dishId = rs.getInt("dish_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (dishId == -1) {
            System.out.println("Dish not found: " + dishName);
            return;
        }
        
        String sql = "INSERT INTO feedback (student_id, dish_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, dishId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, comment);
            pstmt.executeUpdate();
            System.out.println("Feedback saved for " + dishName + " with rating " + rating);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public double getAverageRating(String dishName) {
        String sql = "SELECT AVG(rating) as avg_rating FROM feedback f " +
                    "JOIN master_menu m ON f.dish_id = m.dish_id WHERE m.dish_name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dishName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // ==================== PREFERENCES METHODS ====================
    
    public void saveStudentPreferences(String rollNumber, List<String> preferences) {
        int studentId = getStudentId(rollNumber);
        if (studentId == -1) return;
        
        // Delete existing preferences
        String deleteSql = "DELETE FROM preferences WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting preferences: " + e.getMessage());
        }
        
        // Insert new preferences
        String insertSql = "INSERT INTO preferences (student_id, preference_type, preference_value) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            for (String pref : preferences) {
                pstmt.setInt(1, studentId);
                pstmt.setString(2, pref);
                pstmt.setString(3, "true");
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error saving preferences: " + e.getMessage());
        }
    }
    
    public List<String> getStudentPreferences(String rollNumber) {
        List<String> prefs = new ArrayList<>();
        int studentId = getStudentId(rollNumber);
        if (studentId == -1) return prefs;
        
        String sql = "SELECT preference_type FROM preferences WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                prefs.add(rs.getString("preference_type"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting preferences: " + e.getMessage());
        }
        return prefs;
    }
    
    // ==================== WEEK MANAGEMENT ====================
    
    public void checkNewWeek() {
        String sql = "SELECT MAX(week_number) as last_week FROM votes";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int lastWeek = 0;
            if (rs.next()) {
                lastWeek = rs.getInt("last_week");
            }
            
            LocalDate today = LocalDate.now();
            int currentWeek = today.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
            
            if (currentWeek != lastWeek && lastWeek != 0) {
                System.out.println("New week started! Previous week: " + lastWeek + ", Current: " + currentWeek);
            }
        } catch (SQLException e) {
            System.out.println("Error checking week: " + e.getMessage());
        }
    }
}