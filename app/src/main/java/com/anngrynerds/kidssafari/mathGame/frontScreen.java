package com.anngrynerds.kidssafari.mathGame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.anngrynerds.kidssafari.MainScreen;
import com.anngrynerds.kidssafari.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.MessageFormat;
import java.util.Objects;

import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class frontScreen extends AppCompatActivity {
    Button add, sub, mul, div;
    CardView easy, med, hard;

    String sign, diff;
    View.OnClickListener signOnclickListener;
    ConstraintLayout CL_selectSign, CL_selectDiff;
    int hisE, hisM, hisH;
    private TextView hiScE, hiScH, hiScM, sig;
    private View.OnClickListener cardOnclickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_math_front_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        cardOnclickListener = v -> {
            CardView c = (CardView) v;
            diff = c.getTag().toString();
            startActivity(new Intent(frontScreen.this, MathGameScreen.class)
                    .putExtra("sign", sign)
                    .putExtra("difficulty", diff)
            );
        };

        signOnclickListener = v -> {
            Button b = (Button) v;
            sign = b.getText().toString();
            SetDifficulty();
        };


//        Ad code
        RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
        MobileAds.setRequestConfiguration(conf);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView adView = findViewById(R.id.mathfp_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
//        End Ad code

//        get High Score


//        end high score

        CL_selectDiff = findViewById(R.id.mathgame_layer2);
        CL_selectSign = findViewById(R.id.mathgame_layer1);
        hiScE = findViewById(R.id.hightscoreE);
        hiScM = findViewById(R.id.hightscoreM);
        hiScH = findViewById(R.id.hightscoreH);
        sig = findViewById(R.id.diff_for);

        easy = findViewById(R.id.math_easy);
        med = findViewById(R.id.math_medium);
        hard = findViewById(R.id.math_hard);


        easy.setOnClickListener(cardOnclickListener);
        med.setOnClickListener(cardOnclickListener);
        hard.setOnClickListener(cardOnclickListener);

        CL_selectSign.setVisibility(View.VISIBLE);
        CL_selectDiff.setVisibility(View.GONE);


        findViewById(R.id.back).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainScreen.class)));
        add = findViewById(R.id.plus);
        sub = findViewById(R.id.minus);
        mul = findViewById(R.id.mult);
        div = findViewById(R.id.div);
        add.setOnClickListener(signOnclickListener);
        sub.setOnClickListener(signOnclickListener);
        mul.setOnClickListener(signOnclickListener);
        div.setOnClickListener(signOnclickListener);


    }

    private void SetDifficulty() {
        CL_selectSign.setVisibility(View.GONE);
        CL_selectDiff.setVisibility(View.VISIBLE);


        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        hisE = prefs.getInt("MathHighScore" + "math_e" + sign, 0);
        hisM = prefs.getInt("MathHighScore" + "math_m" + sign, 0);
        hisH = prefs.getInt("MathHighScore" + "math_h" + sign, 0);


        hiScE.setText(MessageFormat.format("Hi-Score: {0}", hisE));
        hiScM.setText(MessageFormat.format("Hi-Score: {0}", hisM));
        hiScH.setText(MessageFormat.format("Hi-Score: {0}", hisH));
        switch (sign) {
            case "Addition":
                sig.setText(R.string.for_addition);
                break;
            case "Subtraction":
                sig.setText(R.string.for_sub);
                break;
            case "Division":
                sig.setText(R.string.for_div);
                break;
            case "Multiplication":
                sig.setText(R.string.for_mul);
                break;
        }


    }


}