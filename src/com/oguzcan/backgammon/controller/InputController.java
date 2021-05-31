package com.oguzcan.backgammon.controller;

import java.util.Scanner;

public class InputController {

    public static String nextString() {

        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine();
    }
}
