package com.messvoice;

import com.messvoice.data.DataHandler;
import java.util.Arrays;

public class TestDataHandler {
    public static void main(String[] args) {
        DataHandler dh = new DataHandler();

        // Test vote
        dh.saveVote("Hariom", Arrays.asList("Poha", "Paratha"));

        // Test count
        dh.getVoteCountPerDish().forEach((dish, count) -> {
            System.out.println(dish + ": " + count + " votes");
        });

        // Test if voted
        System.out.println("Has Hariom voted? " + dh.hasVotedThisWeek("Hariom"));
     // Test feedback
        dh.saveFeedback("Hariom", "Dal Makhani", 4, "Good taste");
        dh.saveFeedback("Hariom", "Poha", 5, "Best ever");

        // Test average rating
        System.out.println("Avg rating Dal Makhani: " + dh.getAverageRating("Dal Makhani"));
        System.out.println("Avg rating Poha: " + dh.getAverageRating("Poha"));
        new StudentDashboard("Hariom");
        new PreferencesScreen(null,"Hariom");
        
        new VotingScreen(null,"Hariom");
        
        
        
    }
}