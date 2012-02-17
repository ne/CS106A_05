/*
 * File: Yahtzee.java
 * ------------------
 * This program will play the Yahtzee game.
 * Author: Nicolas Echavarria
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

import java.util.ArrayList;


public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

    public static void main(String[] args) {
        new Yahtzee().start(args);
    }

    public void run() {
        IODialog dialog = getDialog();
        N_Players = dialog.readInt("Enter number of players");
        println("Current number of players:" + N_Players);
        playerNames = new String[N_Players];
        for (int i = 1; i <= N_Players; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }
        display = new YahtzeeDisplay(getGCanvas(), playerNames);

        //TODO 4. Handle the game with more than one player.
        for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
            for (int j = 0; j < N_Players; j++) {
                playGame(j + 1);
            }
        }
    }

    private void playGame(int playerID) {

        for (int k = 0; k <= N_SCORING_CATEGORIES; k++) {
            println("This is turn:" + k);
            display.printMessage(playerNames[playerID - 1] + "\'s turn!, Click the \"Roll Dice\" to roll the dice.");
            display.waitForPlayerToClickRoll(1);
            rollDice();
            display.displayDice(diceValues);

            //Only for debugging
            println("These are the dice values after the first roll");
            println(diceValues[0]);
            println(diceValues[1]);
            println(diceValues[2]);
            println(diceValues[3]);
            println(diceValues[4]);

            for (int i = 0; i <= 1; i++) {
                display.printMessage("Please select which dice would you like to roll again, and press \"Roll again\"");
                display.waitForPlayerToSelectDice();
                diceStatus = new int[N_DICE];
                for (int j = 0; j < N_DICE; j++) {
                    if (display.isDieSelected(j)) {
                        diceStatus[j] = 1;
                        diceValues[j] = 0;
                    } else {
                        diceStatus[j] = 0;
                    }
                }
                //Only for debugging
                println("These are the dice statuses after the first selection");
                println(diceStatus[0]);
                println(diceStatus[1]);
                println(diceStatus[2]);
                println(diceStatus[3]);
                println(diceStatus[4]);

                //Only for debugging
                println("These are the dice values after they are marked as 0 by the first selection");
                println(diceValues[0]);
                println(diceValues[1]);
                println(diceValues[2]);
                println(diceValues[3]);
                println(diceValues[4]);

                rollNewDice();
                display.displayDice(diceValues);

                //Only for debugging
                println("These are the dice values after the second roll");
                println(diceValues[0]);
                println(diceValues[1]);
                println(diceValues[2]);
                println(diceValues[3]);
                println(diceValues[4]);

            }

            //Option to manually set dices to test
            IODialog dialog = getDialog();
            for (int i = 0; i <= 4; i++) {
                diceValues[i] = dialog.readInt("Enter value for dice " + i);
            }
            //Only for debugging
            println("Manual dice values");
            println(diceValues[0]);
            println(diceValues[1]);
            println(diceValues[2]);
            println(diceValues[3]);
            println(diceValues[4]);

            performScoring(playerID);
        }
    }


    /* Rolls the dice and fills up a new array called diceValues */
    private int[] rollDice() {
        for (int i = 0; i < N_DICE; i++) {
            diceValues[i] = rgen.nextInt(1, 6);
        }
        return diceValues;
    }

    /* Rolls the dice two times more and fills up a the array called diceValues */
    private void rollNewDice() {
        for (int i = 0; i < N_DICE; i++) {
            if (diceValues[i] == 0) {
                diceValues[i] = rgen.nextInt(1, 6);
            }
        }
    }

    /* Executes the cycle of category selection, verification, validation and scoring */
    private void performScoring(int playerID) {
        //Cycle of scoring starts
        display.printMessage("Select a category for this roll");
        //The player selects the category to be evaluated and assigned a score
        int category = display.waitForPlayerToSelectCategory();
        println("Category selected:" + category);
        if (verifyCategory(category)) {
            if (validateCategory(category)) {
                calculateScore(diceValues, category);
                println("The calculated score is:" + score);
                display.updateScorecard(category, playerID, score);
                scores[category - 1] = score;
                updateTotalScore(playerID);
            } else {
                display.updateScorecard(category, playerID, 0);
                scores[category - 1] = 0;
                updateTotalScore(playerID);
            }
        } else {
            display.printMessage("That category has already been chosen, please select another one");
            pause(3000);
            performScoring(playerID);
        }

    }

    private void updateTotalScore(int playerID) {

        //Upper Scores and bonus calculation
        if (scores[0] > -1 && scores[1] > -1 && scores[2] > -1 && scores[3] > -1 && scores[4] > -1 && scores[5] > -1){
            int upperScore = scores[0] + scores[1] + scores[2] + scores[3] + scores[4] + scores[5];
            display.updateScorecard(7, playerID, upperScore);
            if (upperScore > 63) {
                display.updateScorecard(8, playerID, 35);
            }
        }

        //Lower scores
        if (scores[8] > -1 && scores[9] > -1 && scores[10] > -1 && scores[11] > -1 && scores[12] > -1 && scores[13] > -1
                && scores[14] > -1){
            int lowerScore = scores[8] + scores[9] + scores[10] + scores[11] + scores[12] + scores[13] + scores[14];
            display.updateScorecard(16, playerID, lowerScore);
        }

        //TODO 1 Error in adding up the bonus
        //Total display
        int totalScore = 0;
        for (int i = 0; i < N_CATEGORIES - 1; i++) {
            if (scores[i] > -1) {
                totalScore += scores[i];
            }
        }
        if ( scores[6] > -1 && scores[15] > -1 ) {
            totalScore = totalScore - scores[6] - scores[15];
        }
        if ( scores[6] > -1 && scores[15] == -1 ) {
            totalScore = totalScore - scores[6];
        }
        if ( scores[6] == -1 && scores[15] > -1 ) {
            totalScore = totalScore - scores[15];
        }

        println("Total score is:" + totalScore);
        display.updateScorecard(17, playerID, totalScore);
    }

    /*Verifies that the category has not been previously selected*/
    private boolean verifyCategory(int category) {
        //Only for debugging
        println("Category selected:" + category);
        println("This is the single-dimensional array");
        println(scores[0]);
        println(scores[1]);
        println(scores[2]);
        println(scores[3]);
        println(scores[4]);
        println(scores[5]);
        println(scores[6]);
        println(scores[7]);
        println(scores[8]);
        println(scores[9]);
        println(scores[10]);
        println(scores[11]);
        println(scores[12]);
        println(scores[13]);
        println(scores[14]);
        println(scores[15]);
        println(scores[16]);
        if (scores[category - 1] == -1) {
            return true;
        } else {
            return false;
        }
    }

    /*Validates that the selected category corresponds to the correct type*/
    private boolean validateCategory(int category) {
        switch (category) {
            case 1: return true;
            case 2: return true;
            case 3: return true;
            case 4: return true;
            case 5: return true;
            case 6: return true;
            case 9:
                int[] three_of_a_kind = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        three_of_a_kind[0]++;
                    }
                    if (diceValues[i] == 2) {
                        three_of_a_kind[1]++;
                    }
                    if (diceValues[i] == 3) {
                        three_of_a_kind[2]++;
                    }
                    if (diceValues[i] == 4) {
                        three_of_a_kind[3]++;
                    }
                    if (diceValues[i] == 5) {
                        three_of_a_kind[4]++;
                    }
                    if (diceValues[i] == 6) {
                        three_of_a_kind[5]++;
                    }
                }
                println("Three of a kind values");
                println(three_of_a_kind[0]);
                println(three_of_a_kind[1]);
                println(three_of_a_kind[2]);
                println(three_of_a_kind[3]);
                println(three_of_a_kind[4]);
                println(three_of_a_kind[5]);

                for (int j = 0; j < N_DICE; j++) {
                    if (three_of_a_kind[j] == 3) {
                        return true;
                    }
                }
                break;
            case 10:
                int[] four_of_a_kind = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        four_of_a_kind[0]++;
                    }
                    if (diceValues[i] == 2) {
                        four_of_a_kind[1]++;
                    }
                    if (diceValues[i] == 3) {
                        four_of_a_kind[2]++;
                    }
                    if (diceValues[i] == 4) {
                        four_of_a_kind[3]++;
                    }
                    if (diceValues[i] == 5) {
                        four_of_a_kind[4]++;
                    }
                    if (diceValues[i] == 6) {
                        four_of_a_kind[5]++;
                    }
                }
                for (int j = 0; j < N_DICE; j++) {
                    if (four_of_a_kind[j] == 4) {
                        return true;
                    }
                }
                break;
            case 11:
                int[] full_house = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        full_house[0]++;
                    }
                    if (diceValues[i] == 2) {
                        full_house[1]++;
                    }
                    if (diceValues[i] == 3) {
                        full_house[2]++;
                    }
                    if (diceValues[i] == 4) {
                        full_house[3]++;
                    }
                    if (diceValues[i] == 5) {
                        full_house[4]++;
                    }
                    if (diceValues[i] == 6) {
                        full_house[5]++;
                    }
                }
                int isFullhouse = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (full_house[j] == 3) {
                        isFullhouse++;
                    }
                    if (full_house[j] == 2) {
                        isFullhouse++;
                    }
                }
                if (isFullhouse == 2) {
                    return true;
                }
                break;
            //TODO 2. Verify the correct assignment of small straight.
            //TODO 3. Replace constant with i to make it shorter.
            case 12:
                int[] small_straight = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        small_straight[0]++;
                    }
                    if (diceValues[i] == 2) {
                        small_straight[1]++;
                    }
                    if (diceValues[i] == 3) {
                        small_straight[2]++;
                    }
                    if (diceValues[i] == 4) {
                        small_straight[3]++;
                    }
                    if (diceValues[i] == 5) {
                        small_straight[4]++;
                    }
                    if (diceValues[i] == 6) {
                        small_straight[5]++;
                    }
                }
                int isSmall_straight = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (small_straight[j] == 1) {
                        isSmall_straight++;
                    }
                }
                if (isSmall_straight == 4) {
                    return true;
                }
                break;
            case 13:
                int[] large_straight = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        large_straight[0]++;
                    }
                    if (diceValues[i] == 2) {
                        large_straight[1]++;
                    }
                    if (diceValues[i] == 3) {
                        large_straight[2]++;
                    }
                    if (diceValues[i] == 4) {
                        large_straight[3]++;
                    }
                    if (diceValues[i] == 5) {
                        large_straight[4]++;
                    }
                    if (diceValues[i] == 6) {
                        large_straight[5]++;
                    }
                }
                int isLarge_straight = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (large_straight[j] == 1) {
                        isLarge_straight++;
                    }
                }
                if (isLarge_straight == 5) {
                    return true;
                }
                break;
            case 14:
                int[] yahtzee = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        yahtzee[0]++;
                    }
                    if (diceValues[i] == 2) {
                        yahtzee[1]++;
                    }
                    if (diceValues[i] == 3) {
                        yahtzee[2]++;
                    }
                    if (diceValues[i] == 4) {
                        yahtzee[3]++;
                    }
                    if (diceValues[i] == 5) {
                        yahtzee[4]++;
                    }
                    if (diceValues[i] == 6) {
                        yahtzee[5]++;
                    }
                }
                int isYahtzee = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (yahtzee[j] == 5) {
                        isYahtzee++;
                    }
                }
                if (isYahtzee == 1) {
                    return true;
                }
                break;
            case 15:
                return true;
        }
        return false;
    }



    /* Calculates the score based on the current state of the dices and the selected category*/
    private int calculateScore(int[] diceValues, int category) {
        switch (category) {
            case 1:
                score = 0;
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        score++;
                    }
                }
                break;
            case 2:
                score = 0;
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 2) {
                        score = score + 2;
                    }
                }
                break;
            case 3:
                score = 0;
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 3) {
                        score = score + 3;
                    }
                }
                break;
            case 4:
                score = 0;
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 4) {
                        score = score + 4;
                    }
                }
                break;
            case 5:
                score = 0;
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 5) {
                        score = score + 5;
                    }
                }
                break;
            case 6:
                score = 0;
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 6) {
                        score = score + 6;
                    }
                }
                break;
            case 9:
                score = 0;
                int[] three_of_a_kind = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        three_of_a_kind[0]++;
                    }
                    if (diceValues[i] == 2) {
                        three_of_a_kind[1]++;
                    }
                    if (diceValues[i] == 3) {
                        three_of_a_kind[2]++;
                    }
                    if (diceValues[i] == 4) {
                        three_of_a_kind[3]++;
                    }
                    if (diceValues[i] == 5) {
                        three_of_a_kind[4]++;
                    }
                    if (diceValues[i] == 6) {
                        three_of_a_kind[5]++;
                    }
                }
                for (int j = 0; j < N_DICE; j++) {
                    if (three_of_a_kind[j] == 3) {
                        for (int k = 0; k < N_DICE; k++) {
                            score += diceValues[k];
                        }
                    }
                }
                break;
            case 10:
                score = 0;
                int[] four_of_a_kind = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        four_of_a_kind[0]++;
                    }
                    if (diceValues[i] == 2) {
                        four_of_a_kind[1]++;
                    }
                    if (diceValues[i] == 3) {
                        four_of_a_kind[2]++;
                    }
                    if (diceValues[i] == 4) {
                        four_of_a_kind[3]++;
                    }
                    if (diceValues[i] == 5) {
                        four_of_a_kind[4]++;
                    }
                    if (diceValues[i] == 6) {
                        four_of_a_kind[5]++;
                    }
                }
                for (int j = 0; j < N_DICE; j++) {
                    if (four_of_a_kind[j] == 4) {
                        for (int k = 0; k < N_DICE; k++) {
                            score += diceValues[k];
                        }
                    }
                }
                break;
            case 11:
                score = 0;
                int[] full_house = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        full_house[0]++;
                    }
                    if (diceValues[i] == 2) {
                        full_house[1]++;
                    }
                    if (diceValues[i] == 3) {
                        full_house[2]++;
                    }
                    if (diceValues[i] == 4) {
                        full_house[3]++;
                    }
                    if (diceValues[i] == 5) {
                        full_house[4]++;
                    }
                    if (diceValues[i] == 6) {
                        full_house[5]++;
                    }
                }
                int isFullhouse = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (full_house[j] == 3) {
                        isFullhouse++;
                    }
                    if (full_house[j] == 2) {
                        isFullhouse++;
                    }
                }
                if (isFullhouse == 2) {
                    score = 25;
                }
                break;
            case 12:
                score = 0;
                int[] small_straight = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        small_straight[0]++;
                    }
                    if (diceValues[i] == 2) {
                        small_straight[1]++;
                    }
                    if (diceValues[i] == 3) {
                        small_straight[2]++;
                    }
                    if (diceValues[i] == 4) {
                        small_straight[3]++;
                    }
                    if (diceValues[i] == 5) {
                        small_straight[4]++;
                    }
                    if (diceValues[i] == 6) {
                        small_straight[5]++;
                    }
                }
                int isSmall_straight = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (small_straight[j] == 1) {
                        isSmall_straight++;
                    }
                }
                if (isSmall_straight == 4) {
                    score = 30;
                }
                break;
            case 13:
                score = 0;
                int[] large_straight = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        large_straight[0]++;
                    }
                    if (diceValues[i] == 2) {
                        large_straight[1]++;
                    }
                    if (diceValues[i] == 3) {
                        large_straight[2]++;
                    }
                    if (diceValues[i] == 4) {
                        large_straight[3]++;
                    }
                    if (diceValues[i] == 5) {
                        large_straight[4]++;
                    }
                    if (diceValues[i] == 6) {
                        large_straight[5]++;
                    }
                }
                int isLarge_straight = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (large_straight[j] == 1) {
                        isLarge_straight++;
                    }
                }
                if (isLarge_straight == 5) {
                    score = 40;
                }
                break;
            case 14:
                score = 0;
                int[] yahtzee = new int[6];
                for (int i = 0; i < N_DICE; i++) {
                    if (diceValues[i] == 1) {
                        yahtzee[0]++;
                    }
                    if (diceValues[i] == 2) {
                        yahtzee[1]++;
                    }
                    if (diceValues[i] == 3) {
                        yahtzee[2]++;
                    }
                    if (diceValues[i] == 4) {
                        yahtzee[3]++;
                    }
                    if (diceValues[i] == 5) {
                        yahtzee[4]++;
                    }
                    if (diceValues[i] == 6) {
                        yahtzee[5]++;
                    }
                }
                int isYahtzee = 0;
                for (int j = 0; j < N_DICE; j++) {
                    if (yahtzee[j] == 5) {
                        isYahtzee++;
                    }
                }
                if (isYahtzee == 1) {
                    score = 50;
                }
                break;
            case 15:
                for (int j = 0; j < N_DICE; j++) {
                    score += diceValues[j];
                }
                break;
        }
        return score;
    }


    /* Private instance variables */
    private int N_Players;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int[] diceValues = new int[N_DICE];
    private int[] diceStatus = new int[N_DICE];
    int[] scores = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private int score;
    private int playerID;

}




