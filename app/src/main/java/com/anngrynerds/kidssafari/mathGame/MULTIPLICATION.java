package com.anngrynerds.kidssafari.mathGame;

import java.util.Random;

public class MULTIPLICATION {


    public int n1, n2, n3;

    public void genrateEasy() {
        n1 = new Random().nextInt(20);
        n2 = new Random().nextInt(10) + 1;
        n3 = new Random().nextInt(20);
    }


    public void genrateMedium() {
        n1 = new Random().nextInt(50) + 1;
        n2 = new Random().nextInt(20) + 1;
        n3 = new Random().nextInt(30) + 2;
    }


    public void genrateHard() {
        n1 = new Random().nextInt(50) + 5;
        n2 = new Random().nextInt(70) + 1;
        n3 = new Random().nextInt(100) + 5;
    }
}
