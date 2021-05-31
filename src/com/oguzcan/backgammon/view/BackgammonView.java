package com.oguzcan.backgammon.view;


import com.oguzcan.backgammon.controller.InputController;
import com.oguzcan.backgammon.models.*;


public class BackgammonView {

    private int level = 0;    // show level of screen when i print in methods


    // Displays Full Board Table
    public String displayBoard(boolean isWhiteSide, Board board, Backgammon backgammon) {
        String s = "";
        s += displayBoardHeader(isWhiteSide);
        s += displayBoardUpper(isWhiteSide, board);
        s += displayBoardCenter(isWhiteSide, backgammon);
        s += displayBoardLower(isWhiteSide, board);
        s += displayBoardFooter(isWhiteSide);
        return s;
    }

    private String displayBoardHeader(boolean isWhiteSide) {
        String s = "";
        if (isWhiteSide) {
            s += "+---------------------------------------------------------------+\n";
            s += "|WIN:888| 13 14 15 16 17 18  |(#)| 19 20 21 22 23 24  |    0    |\n";
            s += "+-------+--------------------+---+--------------------+---------+\n";
        } else {
            s += "+---------------------------------------------------------------+\n";
            s += "|WIN:888| 12 11 10  9  8  7  |(#)|  6  5  4  3  2  1  |    0    |\n";
            s += "+-------+--------------------+---+--------------------+---------+\n";
        }
        level += 3;
        return s;
    }

    private String displayBoardUpper(boolean isWhiteSide, Board board) {
        String s = "";

        for (level = 3; level < 8; level++) {
            s += "|       |";
            s += displayLine(isWhiteSide, board);
            s += displayCollected(isWhiteSide, board);
        }
        return s;
    }

    private String displayBoardCenter(boolean isWhiteSide, Backgammon backgammon) {
        Dice tempDice = backgammon.getDice();
        int dicePoint = tempDice.getDicePoint();

        // Show TURN
        String turnTag = isWhiteSide ? ">WHITE<" : ">BLACK<"; // round notify
        if (tempDice.isFirstTurn()) { // first round notify
            turnTag = ">FIRST<";
            tempDice.setFirstTurn(false);
        }

        String s = "";
        s += "|POINT: |                    |   |                    |---------|\n";
        s += "|" + dicePoint + "     ";
        if (dicePoint < 10)
            s += " ";
        s += "|";

        // Dice Texts
        s += "  " + tempDice.getDiceText(0)
                + "       W DICE |   | B DICE     "
                + "  " + tempDice.getDiceText(1) + "  | " + turnTag + " | <----- Turn\n";

        s += "|       |                    |   |                    |---------|\n";

        level += 3;
        return s;
    }

    private String displayBoardLower(boolean isWhiteSide, Board board) {
        String s = "";

        for (level = 11; level < 16; level++) {
            s += "|       |";
            s += displayLine(isWhiteSide, board);
            s += displayCollected(isWhiteSide, board);
        }
        return s;
    }

    private String displayBoardFooter(boolean isWhiteSide) {
        String s = "";
        if (isWhiteSide) {
            s += "+-------+--------------------+---+--------------------+---------+\n";
            s += "|NEW:999| 12 11 10  9  8  7  |(#)|  6  5  4  3  2  1  |    0    |\n";
            s += "+---------------------------------------------------------------+\n";
        } else {
            s += "+-------+--------------------+---+--------------------+---------+\n";
            s += "|NEW:999| 13 14 15 16 17 18  |(#)| 19 20 21 22 23 24  |    0    |\n";
            s += "+---------------------------------------------------------------+\n";
        }
        level = 0;
        return s;
    }

    // Displays Collected Checkers CLEAN
    private String displayCollected(boolean isWhiteSide, Board board) {
        String s = "  |";
        int brokenCheckers;
        int index = isWhiteSide ? 26 : 27;
        int index2 = isWhiteSide ? 27 : 26;
        if (level < 8) {
            brokenCheckers = board.getLines().get(index2).getNumberAllCheckers();
            for (int i = 0; i < 3; i++) {
                if (isWhiteSide && brokenCheckers > (level - 3) * 3) {              // Up black
                    s += " B ";
                    brokenCheckers--;
                } else if (!isWhiteSide && brokenCheckers > (level - 3) * 3) {      // Up white
                    s += " W ";
                    brokenCheckers--;
                } else
                    s += " X ";
            }
        } else {
            brokenCheckers = board.getLines().get(index).getNumberAllCheckers();
            for (int i = 0; i < 3; i++) {
                if (isWhiteSide && brokenCheckers > (15 - level) * 3) {             // down white
                    s += " W ";
                    brokenCheckers--;
                } else if (!isWhiteSide && brokenCheckers > (15 - level) * 3) {     // down black
                    s += " B ";
                    brokenCheckers--;
                } else
                    s += " X ";
            }
        }
        s += "|\n";
        return s;
    }

    // Displays Eaten Checkers
    private String displayHit(boolean isWhiteSide, Board board) {
        String s = "";

        if (level < 8) {        // upper black      // upper white
            if (isWhiteSide && board.getLines().get(0).getNumberAllCheckers() > (level - 3)) {
                s += "| B |";
            } else if (!isWhiteSide && (board.getLines().get(25).getNumberAllCheckers()) > (level - 3)) {
                s += "| W |";
            } else
                s += "|   |";
        } else {                // lower white      // lower black
            if (isWhiteSide && board.getLines().get(25).getNumberAllCheckers() > (15 - level)) {
                s += "| W |";
            } else if (!isWhiteSide && board.getLines().get(0).getNumberAllCheckers() > (15 - level)) {
                s += "| B |";
            } else
                s += "|   |";
        }
        return s;
    }

    // Displays Lines
    private String displayLine(boolean isWhiteSide, Board board) {
        String s = "";


        if ((isWhiteSide && level < 8) || (!isWhiteSide && level > 10)) {
            for (int i = 13; i <= 18; i++) {
                s += displayChecker(board, i);
            }
            s += "  ";

            // EATEN
            s += displayHit(isWhiteSide, board);

            for (int i = 19; i <= 24; i++) {
                s += displayChecker(board, i);
            }
        } else {
            for (int i = 12; i >= 7; i--) {
                s += displayChecker(board, i);
            }
            s += "  ";

            // EATEN
            s += displayHit(isWhiteSide, board);

            for (int i = 6; i >= 1; i--) {
                s += displayChecker(board, i);
            }
        }
        return s;
    }

    // displayLine sub method
    private String displayChecker(Board board, int i) {
        String s = "  ";
        int upperBalancer = level - 3;
        int lowerBalancer = 15 - level;
        if (level < 8) {
            if (board.getLines().get(i).lineCheckers(Player.PLAYER_0) > upperBalancer) {
                s += "W";
            } else if (board.getLines().get(i).lineCheckers(Player.PLAYER_1) > upperBalancer) {
                s += "B";
            } else {
                s += ".";
            }
        } else {
            if (board.getLines().get(i).lineCheckers(Player.PLAYER_0) > lowerBalancer) {
                s += "W";
            } else if (board.getLines().get(i).lineCheckers(Player.PLAYER_1) > lowerBalancer) {
                s += "B";
            } else {
                s += ".";
            }
        }
        return s;
    }


    public void displayExit() {
        System.out.println("Program exit.");
    }

    public void displayCongrats(Player player) {
        String name = player.getId() == 0 ? "White" : "Black";
        System.out.println("Congrats " + name + " win!");
    }

    // Input Move
    public String[] getLocation() {
        String[] move = new String[2];
        System.out.print("Start:");
        move[0] = InputController.nextString();
        System.out.print("Moves to:");
        move[1] = InputController.nextString();

        return move;
    }

    public void displayMessage(String text) {
        System.out.println(text);
    }
}
