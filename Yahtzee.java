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
        playGame();
    }

    private void playGame() {
        for (int k = 0; k <= N_SCORING_CATEGORIES; k++) {
            println("This is turn:" + k);
            //TODO 4. Handle the game with more than one player.

            display.printMessage(playerNames[0] + "\'s turn!, Click the \"Roll Dice\" to roll the dice.");
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

            //TODO 0. Enable option to manually set dices to test
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


            display.printMessage("Select a category for this roll");
            int category = display.waitForPlayerToSelectCategory();
            println("Category selected:" + category);
            scores[category - 1] = -1;

            //Only for debugging
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


            //TODO 2. Verify that the player has not selected the category before.
            //if (verifySelectedCategory(category)){
            //}




            //TODO 3. Implement own checkCategory
            if (YahtzeeMagicStub.checkCategory(diceValues, category)) {
                //Calculate score given the current dice values and approved selected category
                CalculateScore(diceValues, category);
                println("The calculated score is:" + score);
                display.updateScorecard(category, 1, score);
            } else {
                display.updateScorecard(category, 1, 0);
            }

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

    //TODO 1. Verify each one of the score calculation cases
    /* Calculates the score based on the current state of the dices and the selected category*/
    private int CalculateScore(int[] diceValues, int category) {
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
                        score = three_of_a_kind[j] * j;
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
                        score = four_of_a_kind[j] * j;
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
                    if (yahtzee[j] == 6) {
                        isYahtzee++;
                    }
                }
                if (isYahtzee == 1) {
                    score = 50;
                }
                break;
            case 15:
                score = diceValues[0] + diceValues[1] + diceValues[2] + diceValues[3] + diceValues[4] + diceValues[5] + diceValues[6];
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
    private int[] scores = new int[N_CATEGORIES];
    private int score;

}


