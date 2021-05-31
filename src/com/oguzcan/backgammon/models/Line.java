package com.oguzcan.backgammon.models;


import java.util.ArrayList;
import java.util.List;

public class Line {


    List<Checker> checkers;

    public Line() {
        checkers = new ArrayList<>();
    }

    public void addCheckers(Player player, int numberOfCheckers) {
        for (int i = 0; i < numberOfCheckers; i++) {
            checkers.add(new Checker(player));
        }
    }

    public void addCheckersOneByOne(Player player) {
        checkers.add(new Checker(player));
    }

    public void removeAllCheckersLine() {
        checkers.clear();
    }

    public int lineCheckers(int playerID) {
        int counter = 0;
        for (Checker temp : checkers) {
            if (temp.getPlayer().getId() == playerID) {
                counter++;
            }
        }
        return counter;
    }

    public int getNumberOfCheckers(int id) {
        int numberCheckers = 0;
        for (Checker checker : checkers) {
            if (checker.getPlayer().getId() == id) {
                numberCheckers++;
            }
        }
        return numberCheckers;
    }

    public int getNumberAllCheckers() {
        int numberCheckers = 0;
        for (Checker checker : checkers) {
            numberCheckers++;
        }
        return numberCheckers;
    }

    public boolean removeChecker(Checker checker) {
        return checkers.remove(checker);
    }

    public void removeCheckers(Player player, int numCheckers) {
        for (int i = 0; i < numCheckers; i++) {
            if (checkers.size() == 0)
                return;
            if (checkers.get(0).getPlayer() == player) {
                if (!removeChecker(checkers.get(0))) {
                    return;
                }
            }
        }
    }

}