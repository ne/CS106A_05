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
        display.printMessage(playerNames[0] + "\'s turn!, Click the \"Roll Dice\" to roll the dice.");
        display.waitForPlayerToClickRoll(1);
        for (int j = 0; j <= 2; j++) {
            rollDice();
            display.displayDice(diceValues);
            display.printMessage("Select the dice you wish to re-roll and click \"Roll again\"");
            display.waitForPlayerToSelectDice();
            diceStatus = new int[N_DICE];
            for (int i = 0; i < N_DICE; i++) {
                if (display.isDieSelected(i)) {
                    diceStatus[i] = 1;
                } else {
                    diceStatus[i] = 0;
                }
            }
            println(diceStatus[0]);
            println(diceStatus[1]);
            println(diceStatus[2]);
            println(diceStatus[3]);
            println(diceStatus[4]);
            println("Hello my friend console");
            println(diceValues.toString());

        }

    }

    /* Rolls the dice and fills up a new array called diceValues */
    private int[] rollDice() {
        for (int i = 0; i < N_DICE; i++) {
            diceValues[i] = rgen.nextInt(1, 6);
        }
        return diceValues;
    }


    /* Private instance variables */
    private int nPlayers;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int[] diceValues = new int[N_DICE];
    private int[] diceStatus;

}
