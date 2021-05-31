package com.oguzcan.backgammon.controller;

import com.oguzcan.backgammon.models.*;
import com.oguzcan.backgammon.view.*;

public class BackgammonController {
    private final Backgammon model = new Backgammon();
    private final BackgammonView view = new BackgammonView();


    public void init() {
        start();
    }

    public void start() {
        // corrupted file error
        Backgammon.initializeMode = model.init(Backgammon.initializeMode) ? Backgammon.LOAD_GAME : Backgammon.NEW_GAME;

        // Save placement dice to diceLog file
        if (model.getDice().isFirstTurn()) {
            WriteFile.writeDice(model.getPlacementDice(), true);
            view.displayMessage(saveBoard());

            // After placement first roll  &  Write diceLog
            model.getDice().roll(model.getCurrentPlayer().getId());
            WriteFile.writeDice(model.getDiceText(), false);
        }
        play();
    }

    private void play() {

        while (!model.isGameFinish()) {
            // Save Files to file and display board
            try {
                System.out.print(saveBoard());
            } catch (NullPointerException e) {
                Backgammon.initializeMode = Backgammon.NEW_GAME;
                break;
            }
            int tempGameMode = model.isAllCheckersInHomeBoard() ? Player.GAME_MODE_1 : Player.GAME_MODE_0;
            model.getCurrentPlayer().setGameMode(tempGameMode);

            if (model.getCurrentPlayer().getGameMode() != Player.GAME_MODE_1 && !model.haveRealMove()) {
                System.out.println("You don't have correct moves");
                model.switchPlayer();
                model.getDice().roll(model.getCurrentPlayer().getId());
                WriteFile.writeDice(model.getDiceText(), false);
                continue;
            }

            String[] move = view.getLocation();
            if (move[0].equals("999") || move[1].equals("999") || Backgammon.initializeMode == 999) {
                Backgammon.initializeMode = Backgammon.NEW_GAME;
                break;
            }

            if(move[0].equals("888") || move[1].equals("888")) {
                int who = model.isWhite() ? 26 : 27;
                int needs = 15 - model.getBoard().getLines().get(who).getNumberAllCheckers();
                model.getBoard().getLines().get(who).addCheckers(model.getCurrentPlayer(), needs);
                continue;
            }

            if(model.diceCheck_25_26_27(move)) {
                System.out.println("Out of the board. Please enter (0-24)<:");
                continue;
            }

            int iMove[] = new int[2];

            // Normal Game with no collect
            if(model.getCurrentPlayer().getGameMode() == Player.GAME_MODE_0) {

                if (move[0].equals("#"))
                    move[0] = model.getCurrentPlayer().getId() == Player.PLAYER_0 ? "25" : "0";

                try {
                    iMove[0] = Integer.parseInt(move[0]);           // start
                    iMove[1] = Integer.parseInt(move[1]);           // destination
                } catch (NumberFormatException e) {
                    System.out.println("invalid line selected");
                    continue;
                }
                // moveChecker
                String moveResult = model.moveChecker(iMove);
                if (moveResult != null) {
                    view.displayMessage(moveResult);
                    continue;
                }
                // remove dice and make move
                moveResult = model.makeMove(iMove);
                if(moveResult != null) {
                    view.displayMessage(moveResult);
                }
            // play game mode 1 = collect checker on the board
            } else {
                if (move[1].equals("0"))
                    move[1] = model.getCurrentPlayer().getId() == Player.PLAYER_0 ? "26" : "27";
                try {
                    iMove[0] = Integer.parseInt(move[0]);           // start
                    iMove[1] = Integer.parseInt(move[1]);           // destination
                } catch (NumberFormatException e) {
                    System.out.println("invalid line selected");
                    continue;
                }

                // moveChecker
                String moveResult = model.moveCheckerMode2(iMove);
                if (moveResult != null) {
                    view.displayMessage(moveResult);
                    continue;
                }

                moveResult = model.makeMove(iMove);
                if(moveResult != null) {
                    view.displayMessage(moveResult);
                }
            }
        }

        // new game or finish game
        if (model.isGameFinish()) {
            System.out.println("Game Finished!");
            clappers();
            exit();
        } else {
            System.out.println("New game Launched!");
            start();
        }
    }

    private String saveBoard() {
        String boardText = view.displayBoard(model.isWhite(), model.getBoard(), model);
        String code = generateCode();
        WriteFile.writeBoard(boardText + code);


        return boardText;
    }

    private String generateCode() {
        String s = "\n\n\n\n\n\n\n";
        s += model.generateSaveCode(true);
        s += model.generateSaveCode(false);
        return s;
    }


    private void clappers() {
        view.displayCongrats(model.getWinner());
    }

    private void exit() {
        view.displayExit();
        System.exit(0);
    }
}