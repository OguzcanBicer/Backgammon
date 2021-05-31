package com.oguzcan.backgammon.models;


import com.oguzcan.backgammon.controller.ReadFile;
import com.oguzcan.backgammon.controller.WriteFile;


public class Backgammon {

    private Board board;

    private Dice dice;

    private Player player0, player1, currentPlayer, winner;

    public final static int NEW_GAME = 999;
    public final static int LOAD_GAME = 0;
    public static int initializeMode = 0;

    public boolean init(int initializeMode) {
        board = new Board();
        player0 = new Player(Player.PLAYER_0);
        player1 = new Player(Player.PLAYER_1);
        dice = new Dice();

        if (initializeMode == 999) {
            getBoard().initNewGame(player0, player1);
            dice.setFirstTurn(true);
            dice.firstRoll();
            currentPlayer = dice.getDice().get(0) > dice.getDice().get(1) ? player0 : player1;
            String text = isWhite() ? "White" : "Black";
            System.out.println("Placement dice rolled. " + text + " goes first!");
        } else {
            boolean isSucceed;
            isSucceed = getBoard().initLoadGame(player0, player1);
            if (!isSucceed) {
                return false;
            }
            dice = getBoard().initLoadDice();
            if(dice.getDice().get(0) == -999) {
                return false;
            }
            currentPlayer = ReadFile.getIsWhiteTurn() ? player0 : player1;
        }

        return true;
    }


    public String moveChecker(int[] moves) { // Black increment // White Decrease
        String result = null;
        int movable = Math.abs(moves[0] - moves[1]);

        if (!isBoard(moves)) {
            return "Out of the board. Please enter (0-24):";
        }

        // Same Location 1 -> 1
        if (moves[0] == moves[1]) return "You cannot move the same location.";

        // dice <---> start harmony
        if (!haveCorrectDice(movable)) return "You cannot move here for this dice.";

        // multi dice play validation. if  5. and 3. line unavailable you can't play 8 (5,3)
        if (dice.getDiceCount() > 1) {
            result = extraValidation(moves, movable);
            if (result != null) {
                return result;
            }
        }

        // basic Position validates
        result = validatePosition(moves);
        if (result != null) {
            return result;
        }

        return null;
    }

    public String moveCheckerMode2(int[] moves) {
        String result = null;
        int movable = Math.abs(moves[0] - moves[1]);


        if (!isBoard(moves)) {
            return "Out of the board. Please enter (0-24):";
        }

        // Same Location 1 -> 1
        if (moves[0] == moves[1]) return "You cannot move the same location.";

        // dice <---> start harmony
        if (!haveCorrectDice(movable, moves)) return "You cannot move here for this dice.";

        // basic Position validates
        result = validatePosition(moves);
        if (result != null) {
            return result;
        }

        return null;
    }

    public int getMostLeft() {
        if (isWhite()) {
            for (int i = 24; i > 0; i--) {
                if (getOneLine(i).getNumberOfCheckers(currentPlayer.getId()) > 0) {
                    return i;
                }
            }
        } else {
            for (int i = 1; i <= 24; i++) {
                if (getOneLine(i).getNumberOfCheckers(currentPlayer.getId()) > 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean isAllCheckersInHomeBoard() {
        int checkersOffHome = 0;
        if (isWhite()) {
            for (int i = 24; i > 6; i--) {
                checkersOffHome += getOneLine(i).getNumberOfCheckers(currentPlayer.getId());
            }
        } else {
            for (int i = 18; i > 0; i--) {
                checkersOffHome += getOneLine(i).getNumberOfCheckers(currentPlayer.getId());
            }
        }
        if (checkersOffHome > 0) return false;
        return true;
    }

    public boolean diceCheck_25_26_27(String[] move) {
        for (String temp : move)
            if (temp.equals("25") || temp.equals("26") || temp.equals("27"))
                return true;
        return false;
    }

    public boolean isBoard(int[] moves) {
        for (int temp : moves)
            if (temp > 27 && temp < 0) {
                return false;
            }
        return true;
    }

    public String makeMove(int[] moves) {
        String result = null;
        int movable;
        boolean isLast = false;
        if (moves[1] == 26 || moves[1] == 27) {
            if (getMostLeft() != moves[0]) {
                movable = isWhite() ? moves[0] : 25 - moves[0];
            } else {
                movable = isWhite() ? getMostLeft() : 25 - getMostLeft();
                isLast = true;
            }

        } else {
            movable = Math.abs(moves[0] - moves[1]);
        }

        // validate move <--> dice relation and remove played dice
        result = correctDice(movable, isLast);
        if (result != null) {
            return result;
        }
        dice.setDicePoint();

        // remove one checker on start line
        getOneLine(moves[0]).removeCheckers(currentPlayer, 1);
        // eating opponent checker.
        if (getOneLine(moves[1]).getNumberOfCheckers(getNonCurrentPlayer().getId()) == 1) {
            getOneLine(moves[1]).removeCheckers(getNonCurrentPlayer(), 1);
            int broken = currentPlayer.getId() == player0.getId() ? 0 : 25;         // add broken checker
            getOneLine(broken).addCheckers(getNonCurrentPlayer(), 1);

        }
        // add one checker on destination line
        getOneLine(moves[1]).addCheckers(currentPlayer, 1);

        // if all dice played, switch player, REROLL and write new dice to diceLog
        if (dice.getDiceCount() == 0 && !isGameFinish()) {
            switchPlayer();
            dice.roll(currentPlayer.getId());
            WriteFile.writeDice(getDiceText(), false);
        }
        return null;
    }

    public boolean haveRealMove() {
        int[] dices = getDice().getArrayDice();
        for (int iStart = 25; iStart >= 0; iStart--) {
            if (isWhite() && iStart == 0) {
                continue;
            } else if (!isWhite() && iStart == 25) {
                continue;
            }
            if (getOneLine(iStart).getNumberOfCheckers(currentPlayer.getId()) >= 1) {

                if (isWhite()) {
                    for (int temp : dices) {
                        if (temp + iStart >= 0 && temp + iStart < 26)
                            if(iStart-temp >=0 && iStart-temp<=27)
                                if (isEatable(iStart - temp))
                                    return true;
                    }
                } else {
                    for (int temp : dices)
                        if (temp + iStart >= 0 && temp + iStart < 26)
                            if (isEatable(iStart + temp))
                                return true;

                }
            }
        }
        return false;
    }

    // for game mode 0
    private boolean haveCorrectDice(int movable) {
        int sum = 0;


        // check dice[0] & dice[0] + dice[1]
        for (int i = 0; i < dice.getDiceCount(); i++) {
            sum += dice.getDice().get(i);
            if (sum == movable)
                return true;
        }
        // check dice[1]
        if (dice.getDiceCount() > 1) {
            if (dice.getDice().get(1) == movable)
                return true;
        }
        return false;
    }

    // for game mode 1
    private boolean haveCorrectDice(int movable, int[] move) {
        int sum = 0;
        int mostLeft = getMostLeft();

        if (move[1] == 26) { // white
            if (mostLeft > move[0])
                return haveCorrectDice(move[0]);
            if (mostLeft == move[0])
                return true;

        } else if (move[1] == 27) {
            if (mostLeft < move[0])
                return haveCorrectDice(25 - move[0]);
            if (mostLeft == move[0])
                return true;

        } else {
            // check dice[0] & dice[0] + dice[1]
            for (int i = 0; i < dice.getDiceCount(); i++) {
                sum += dice.getDice().get(i);
                if (sum == movable)
                    return true;
            }
            // check dice[1]
            if (dice.getDiceCount() > 1) {
                if (dice.getDice().get(1) == movable)
                    return true;
            }
        }


        return false;
    }

    private String extraValidation(int[] moves, int movable) {
        int[] dices = dice.getArrayDice();

        // double dice multiple play
        if (dices.length > 2) {
            if (movable >= dices[0] * 2) {
                for (int i = 1; i < (movable / dices[0]); i++) {
                    if (!isEatable(Math.abs(moves[0] - dices[0] * i))) {
                        return "Double dice, invalid play.";
                    }
                }
            }
        }
        // non double dice, once play multiple dice
        else if(dices.length == 2) {
            try {
                int func1 = isWhite() ? moves[0] - dices[0] : moves[0] + dices[0];
                int func2 = isWhite() ? moves[0] - dices[1] : moves[0] + dices[1];
                if (dices[0] + dices[1] == movable && !isEatable(func1) && !isEatable(func2)) {
                    return "Non double dice, invalid play.";
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return "Try to play multiple dice function, but we have single dice XD";
            }
        }
        return null;
    }

    private String validatePosition(int[] moves) {

        // black backwards
        if (moves[0] < moves[1] && isWhite() && moves[1] != 26) {
            return "You cannot move backwards.(white)";
        }
        // white backwards
        else if (moves[0] > moves[1] && !isWhite() && moves[0] != 27) {
            return "You cannot move backwards.(Black)";
        }

        // Validation Start location
        if (getOneLine(moves[0]).getNumberOfCheckers(currentPlayer.getId()) == 0) {
            return "You don't have any checkers on this line.";
        }

        // Validation Destination location
        if (!isEatable(moves[1]) && (moves[1] != 26 && moves[1] != 27)) {
            return "You cannot eat that much.";
        }

        // Broken check for White. Start must 25.
        if ((moves[0] != 25)) {
            if (isWhite() && getOneLine(25).getNumberAllCheckers() != 0) {
                return "White you have broken checkers.";
            }
        }
        // Broken check for Black. Start must 0.
        if (moves[0] != 0) {
            if (!isWhite() && getOneLine(0).getNumberAllCheckers() != 0) {
                return "Black you have broken checkers.";
            }
        }
        return null;
    }

    private boolean isEatable(int index) {
        if (getOneLine(index).getNumberOfCheckers(getNonCurrentPlayer().getId()) > 1)
            return false;
        return true;
    }

    private String correctDice(int move, boolean isLast) {
        int[] dices = dice.getArrayDice();
        if (isLast) {
            for (int i = 0; i < dices.length; i++) {
                if (dices[i] >= move) {
                    dice.getDice().remove(i);
                    return null;
                }
            }
        } else {
            if (dices.length > 1) {
                if (dice.isDouble()) {
                    if (move == dices[0] * 4 && dice.getDiceCount() == 4) {
                        dice.getDice().clear();
                        return null;
                    } else if (move == dices[0] * 3 && dice.getDiceCount() >= 3) {
                        dice.getDice().remove(2);
                        dice.getDice().remove(1);
                        dice.getDice().remove(0);
                        return null;
                    } else if (move == dices[0] * 2 && dice.getDiceCount() >= 2) {
                        dice.getDice().remove(0);
                        dice.getDice().remove(0);
                        return null;
                    } else if (move == dices[0]) {
                        dice.getDice().remove(0);
                        return null;
                    }
                } else {
                    if (move == dices[0]) {
                        dice.getDice().remove(0);
                        return null;
                    } else if (move == dices[1]) {
                        dice.getDice().remove(1);
                        return null;
                    } else if (move == dices[0] + dices[1]) {
                        dice.getDice().clear();
                        return null;
                    }
                }
            } else {
                if (move == dices[0]) {
                    dice.getDice().remove(0);
                    return null;
                }
            }
        }
        return "Invalid move";
    }


    public int getCollectedCheckers(Player player) {
        int collected;
        int playerNo = player.getId() == 0 ? 26 : 27;     // 25 White / 0 Black

        collected = board.getLines().get(playerNo).lineCheckers(player.getId());

        return collected;
    }


    public Board getBoard() {
        return board;
    }

    public Dice getDice() {
        return dice;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNonCurrentPlayer() {
        return currentPlayer == player0 ? player1 : player0;
    }

    public boolean isWhite() {
        return currentPlayer.getId() == Player.PLAYER_0;
    }

    private Line getOneLine(int index) {
        return board.getLines().get(index);
    }

    public Player getWinner() {
        return winner;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == player0 ? player1 : player0;
    }

    public boolean isGameFinish() {
        if (getCollectedCheckers(player0) == 15) {
            winner = player0;
            return true;
        } else if (getCollectedCheckers(player1) == 15) {
            winner = player1;
            return true;
        }
        return false;
    }


    public String getDiceText() {
        String keyword = isWhite() ? "White's dice: " : "Black's dice: ";
        String temp = dice.getDiceText(currentPlayer.getId());

        return keyword + temp + "\n";
    }

    public String getPlacementDice() {
        String keyword = "Placement dice: (White) ";
        String temp = dice.getDiceText(0) + " (Black) --> ";


        temp += isWhite() ? " White goes first.\n" : " Black goes first.\n";
        return keyword + temp;
    }

    public String generateSaveCode(boolean isWhite) {
        String s = "";
        int foundHim;
        if (isWhite) {
            s += "Save Code White: %%(";


            for (int i = 1; i < 25; i++) {
                if (getOneLine(i).getNumberOfCheckers(0) > 0) {
                    foundHim = getOneLine(i).getNumberOfCheckers(0);
                    s += Translate(foundHim, i);
                }
            }

            foundHim = getOneLine(25).getNumberAllCheckers();           // white broken checkers
            s += Translate(foundHim, 25);

            foundHim = getOneLine(26).getNumberAllCheckers();           // white collected checkers
            s += Translate(foundHim, 26);

        } else {
            s += "Save Code Black: %%(";

            foundHim = getOneLine(0).getNumberAllCheckers();            // black broken checkers
            s += Translate(foundHim, 0);

            for (int i = 1; i < 25; i++) {
                if (getOneLine(i).getNumberOfCheckers(1) > 0) {
                    foundHim = getOneLine(i).getNumberOfCheckers(1);
                    s += Translate(foundHim, i);
                }
            }

            foundHim = getOneLine(27).getNumberAllCheckers();           // black collected checkers
            s += Translate(foundHim, 27);
        }
        s += "\n";
        return s;
    }

    private String Translate(int foundHim, int i) {
        String s = "";
        for (int j = 0; j < foundHim; j++) {
            s += i + " ";
        }
        return s;
    }

}
