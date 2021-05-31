package com.oguzcan.backgammon.models;

import com.oguzcan.backgammon.controller.ReadFile;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int MAX_LINES = 28;

    private List<Line> lines;

    public Board() {
        lines = new ArrayList<>(MAX_LINES);     // 0 = black, 25 = white, 26 = white, 27 = black

        for (int i = 0; i < MAX_LINES; i++) {
            lines.add(new Line());
        }
    }

    /**
     * Setup New Game
     *
     * @param player0 white
     * @param player1 black
     */
    public void initNewGame(Player player0, Player player1) {
        for(int i=0; i<28; i++) {
            getLines().get(i).removeAllCheckersLine();
        }

        // Player 0 White
        getLines().get(6).addCheckers(player0, 5);
        getLines().get(8).addCheckers(player0, 3);
        getLines().get(13).addCheckers(player0, 5);
        getLines().get(24).addCheckers(player0, 2);


        // Player 1 Black
        getLines().get(1).addCheckers(player1, 2);
        getLines().get(12).addCheckers(player1, 5);
        getLines().get(17).addCheckers(player1, 3);
        getLines().get(19).addCheckers(player1, 5);
    }

    public boolean initLoadGame(Player player0, Player player1) {
        List<String[]> list = ReadFile.readBoardFile();
        // corrupted table file
        if(list.get(0)[0].equals("-999")) {
            return false;
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        try {
            int[] whiteLines = convertToInt(list.get(0));
            int[] blackLines = convertToInt(list.get(1));
            addLiners(player0, whiteLines);
            addLiners(player1, blackLines);

        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public Dice initLoadDice() {
        int[] dices = ReadFile.readDiceFile();
        Dice tempDice =  new Dice();
        int playerId = ReadFile.getIsWhiteTurn() ? 0 : 1;
        tempDice.loadRoll(dices, playerId);

        return tempDice;
    }


    private int[] convertToInt(String[] list) {
        int[] iList = new int[list.length];

        for (int i=0; i<list.length; i++) {

            iList[i] = Integer.parseInt(list[i]);
        }
        return iList;
    }

    // warning
    private void addLiners(Player player, int[] iList) {
        for (int j : iList) {
            getLines().get(j).addCheckersOneByOne(player);
        }
    }


    public List<Line> getLines() {
        return lines;
    }
}
