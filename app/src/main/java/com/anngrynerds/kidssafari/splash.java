package com.anngrynerds.kidssafari;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CountDownTimer(2000, 1) {

            public void onTick(long millisUntilFinished) {
                //pg.setProgress((int) ((int) max - millisUntilFinished));
            }

            public void onFinish() {
                Intent i = new Intent(splash.this, MainScreen.class);
                startActivity(i);
                finish();
            }
        }.start();

    }
}