package com.anngrynerds.kidssafari.mathGame;


import java.util.Random;

public class DIVISION {


    public int n1, n2, tempn3;


    public void genrateEasy() {
        n1 = new Random().nextInt(10) + 1;
        tempn3 = new Random().nextInt(5);
        n2 = tempn3 * n1;
    }


    public void genrateMedium() {
        n1 = new Random().nextInt(20) + 2;
        tempn3 = new Random().nextInt(50);
        n2 = tempn3 * n1;

    }


    public void genrateHard() {
        n1 = new Random().nextInt(50) + 5;
        tempn3 = new Random().nextInt(100);
        n2 = tempn3 * n1;
    }

}
