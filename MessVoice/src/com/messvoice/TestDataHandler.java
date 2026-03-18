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
    }
}