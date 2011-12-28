/*
 * File: Yahtzee.java
 * ------------------
 * This program will play the Yahtzee game.
 * Author: Nicolas Echavarria
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;


public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

    public static void main(String[] args) {
        new Yahtzee().start(args);
    }

    public void run() {
        IODialog dialog = getDialog();
        nPlayers = dialog.readInt("Enter number of players");
        playerNames = new String[nPlayers];
        for (int i = 1; i <= nPlayers; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }
        display = new YahtzeeDisplay(getGCanvas(), playerNames);
        playGame();
    }

    private void playGame() {

//        TODO Handle the game with more than one player.

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

        for (int j = 0; j <= 1; j++) {
            display.waitForPlayerToSelectDice();
            diceStatus = new int[N_DICE];
            for (int i = 0; i < N_DICE; i++) {
                if (display.isDieSelected(i)) {
                    diceStatus[i] = 1;
                    diceValues[i] = 0;
                } else {
                    diceStatus[i] = 0;
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
            println("These are the dice values after they are affected to 0 by the first selection");
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
        display.printMessage("Select a category for this roll");
        int category = display.waitForPlayerToSelectCategory();
        //Only for debugging
        println(category);
//        TODO General validations for category. Verify that the player has not selected the category before.
//        TODO Implement own checkCategory
//        TODO Calculate correctly the scores
        if (YahtzeeMagicStub.checkCategory(diceValues, category)){
            display.updateScorecard(category, 1, 25);
        } else {
            display.updateScorecard(category, 1, 0);
        }

    }



    /* Rolls the dice and fills up a new array called diceValues */
    private int[] rollDice() {
        for (int i = 0; i < N_DICE; i++) {
            diceValues[i] = rgen.nextInt(1, 6);
        }
        return diceValues;
    }

    private void rollNewDice() {
        for (int i = 0; i < N_DICE; i++) {
            if (diceValues[i] == 0){
                diceValues[i] = rgen.nextInt(1, 6);
            }
        }
    }

    /* Private instance variables */
    private int nPlayers;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int[] diceValues = new int[N_DICE];
    private int[] diceStatus = new int[N_DICE];

}
