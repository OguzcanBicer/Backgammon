package com.oguzcan.backgammon.controller;


import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {

    public static void writeBoard(String inputText) {
        try {
            FileWriter myWriter = new FileWriter("Table.dat");
            myWriter.write(inputText);
            myWriter.close();
            //System.out.println("Successfully wrote to the Table.dat file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    // isFirstTurn decides recreate file or append exiting file
    public static void writeDice(String inputText, boolean isFirstTurn) {
        try {
            FileWriter myWriter = new FileWriter("diceLog.txt", !isFirstTurn);
            myWriter.write(inputText);
            myWriter.close();
            //System.out.println("Successfully wrote to the diceLog.text file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
