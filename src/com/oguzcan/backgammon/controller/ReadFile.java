package com.oguzcan.backgammon.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {

    private static boolean isWhiteTurn;

    /**
     * Read diceLog.txt file and set isWhiteTurn
     *
     * @return the dice numbers
     */
    public static int[] readDiceFile() {
        File file = new File("diceLog.txt");
        List<String> list = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        }
        String lastLine = list.get(list.size() - 1);
        if (lastLine.contains("White's")) {
            ReadFile.isWhiteTurn = true;
        } else if (lastLine.contains("Black's")){
            ReadFile.isWhiteTurn = false;
        } else {
            System.out.println("diceList txt file corrupted.");
            return new int[] {-999, -998};
        }
        lastLine = lastLine.substring(14);
        String[] diceText = lastLine.split(" {2}");

        int[] dices = new int[2];

        dices[0] = Integer.parseInt(diceText[0]);
        dices[1] = Integer.parseInt(diceText[1]);

        return dices;
    }

    public static List<String[]> readBoardFile() {
        File file = new File("Table.dat");
        List<String[]> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...


            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String linesCode;
                if (line.contains("Save Code White: %%(")) {
                    linesCode = line.substring(20);
                    String[] liste = linesCode.split(" ");
                    list.add(liste);
                }
                if (line.contains("Save Code Black: %%(")) {
                    linesCode = line.substring(20);
                    String[] liste = linesCode.split(" ");
                    list.add(liste);
                }
            }
        } catch (FileNotFoundException e) {
            //handle this
        }
        if(list.size() != 2) {
            System.out.println("readBoarder dat file corrupted.");
            list.add(new String[]{""});
            list.add(new String[]{""});
            list.get(0)[0] = "-999";
        }
        return list;
    }

    public static boolean getIsWhiteTurn() {
        return isWhiteTurn;
    }
}

// 11