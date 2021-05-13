package com.anngrynerds.kidssafari.mathGame;

import java.util.Random;

public class SUBTRACTION {


    public int n1, n2, n3;

    public void genrateEasy() {
        n1 = new Random().nextInt(10);
        n2 = new Random().nextInt(15) + n1;
        n3 = new Random().nextInt(20);
    }


    public void genrateMedium() {
        n1 = new Random().nextInt(11) + 1;
        n2 = new Random().nextInt(28) + 1 + n1;
        n3 = new Random().nextInt(30) + 2 + n1;
    }


    public void genrateHard() {
        n1 = new Random().nextInt(50) + 5;
        n2 = new Random().nextInt(100) + 1 + n1;
        n3 = new Random().nextInt(250) + 5 + n1;
    }
}
