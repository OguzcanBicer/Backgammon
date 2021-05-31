package com.oguzcan.backgammon.models;


import java.util.ArrayList;
import java.util.List;

public class Dice {

    private List<Integer> dice;         // Contains 4 dice
    private boolean isDouble;           // is double dice
    private boolean isFirstTurn;        // Placement roll
    private int dicePoint;              // Total playable dice point
    private String[] diceText;                  // Store dice  // 0 = white // 1 = black


    public Dice() {
        dice = new ArrayList<>();
        diceText = new String[]{"0  0", "0  0"};

    }

    public void firstRoll() {
        int dice1, dice2;
        do {
            dice1 = (int) (6 * Math.random() + 1);
            dice2 = (int) (6 * Math.random() + 1);
        } while (dice1 == dice2);

        dice.add(dice1);
        dice.add(dice2);

        setDiceText(0);
    }

    public void loadRoll(int[] dices, int playerId) {
        dice.clear();

        int dice1 = dices[0];
        int dice2 = dices[1];
        isDouble = dice1 == dice2;
        dicePoint = dice1 + dice2;

        dice.add(dice1);
        dice.add(dice2);

        if (isDouble) {
            dice.add(dice1);
            dice.add(dice2);
        }
        setDiceText(playerId);
        setDicePoint();
    }

    // Standard Roll
    public void roll(int playerId) {
        dice.clear();

        int dice1 = (int) (6 * Math.random() + 1);
        int dice2 = (int) (6 * Math.random() + 1);

        isDouble = dice1 == dice2;
        dicePoint = dice1 + dice2;

        dice.add(dice1);
        dice.add(dice2);

        if (isDouble) {
            dice.add(dice1);
            dice.add(dice2);
        }
        setDiceText(playerId);
        setDicePoint();
    }

    private void setDiceText(int playerId) {
        if (isFirstTurn) {
            diceText[0] = dice.get(0) + "  0";
            diceText[1] = dice.get(1) + "  0";
        } else {
            diceText[playerId] = dice.get(0) + "  " + dice.get(1);
        }
    }

    // Set sum of Player's dice
    public void setDicePoint() {
        dicePoint = 0;
        for (int temp : dice)
            dicePoint += temp;
    }


    public String getDiceText(int playerId) {
        return diceText[playerId];
    }

    public int getDicePoint() {
        return dicePoint;
    }

    public int getDiceCount() {
        return dice.size();
    }

    // Return List of Dice
    public List<Integer> getDice() {
        return dice;
    }

    public int[] getArrayDice() {
        int[] dices = new int[dice.size()];

        for (int i = 0; i < dice.size(); i++) {
            dices[i] = dice.get(i);
        }
        return dices;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public boolean isFirstTurn() {
        return isFirstTurn;
    }

    public void setFirstTurn(boolean isIt) {
        isFirstTurn = isIt;
    }
}
