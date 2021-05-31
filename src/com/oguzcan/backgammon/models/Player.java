package com.oguzcan.backgammon.models;

public class Player {

    public static final int PLAYER_0 = 0;           // White
    public static final int PLAYER_1 = 1;           // Black
    private final int id;

    public static final int GAME_MODE_0 = 0;        // normal play
    public static final int GAME_MODE_1 = 1;        // collect checkers play
    private int gameMode;

    public Player(int id) {
        this.id = id;
        gameMode = GAME_MODE_0;
    }


    public int getId() {
        return id;
    }


    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int mode) {
        gameMode = mode;
    }
}
