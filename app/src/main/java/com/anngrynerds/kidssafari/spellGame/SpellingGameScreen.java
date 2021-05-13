package com.anngrynerds.kidssafari.spellGame;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anngrynerds.kidssafari.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.rewarded.RewardedAd;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class SpellingGameScreen extends AppCompatActivity {

    Context context;
    Boolean isEasy, isMed, isHard;
    int[] imgRes;
    String[] imgAns;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;
    ImageView imageView;
    ImageView img_l1, img_l2, img_l3, spellHint1, spellHint2, spellHint3;

    int HINTS_int = 3, LIVES_int = 3, scoreInt = 0, HIGHSCORE_INT;

    AdRequest adRequest;
    RewardedAd mRewardedAd;
    String answere;
    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    TextView inputTv, scoreTV;
    Animation anim;
    private View.OnClickListener oneKeyOnclickLister;
    private boolean isRewardedAdReady;
    private int MAX_LENGHT_OF_SPELLING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spelling_game_screen);
        context = getApplicationContext();

        oneKeyOnclickLister = v -> {
            Button b = (Button) v;
            String s = inputTv.getText().toString() + b.getText().toString();
            if (s.equalsIgnoreCase(answere)) {
//                Correct answer
            } else if (s.length() == MAX_LENGHT_OF_SPELLING) {
//                wrong answer
            }
            ;
        };

        imageView = findViewById(R.id.img);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        b10 = findViewById(R.id.b10);
        b11 = findViewById(R.id.b11);
        b12 = findViewById(R.id.b12);
        inputTv = findViewById(R.id.spell_keyout_tv);
        img_l1 = findViewById(R.id.l1);
        img_l2 = findViewById(R.id.l2);
        img_l3 = findViewById(R.id.l3);
        spellHint1 = findViewById(R.id.spell_hint1);
        spellHint2 = findViewById(R.id.spell_hint2);
        spellHint3 = findViewById(R.id.spell_hint3);
        scoreTV = findViewById(R.id.score_textViw);

        b1.setOnClickListener(oneKeyOnclickLister);
        b2.setOnClickListener(oneKeyOnclickLister);
        b3.setOnClickListener(oneKeyOnclickLister);
        b4.setOnClickListener(oneKeyOnclickLister);
        b5.setOnClickListener(oneKeyOnclickLister);
        b6.setOnClickListener(oneKeyOnclickLister);
        b7.setOnClickListener(oneKeyOnclickLister);
        b8.setOnClickListener(oneKeyOnclickLister);
        b9.setOnClickListener(oneKeyOnclickLister);
        b10.setOnClickListener(oneKeyOnclickLister);
        b11.setOnClickListener(oneKeyOnclickLister);
        b12.setOnClickListener(oneKeyOnclickLister);


//        ad code
        {
            RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
            MobileAds.setRequestConfiguration(conf);
            MobileAds.initialize(this, initializationStatus -> {

            });
            AdView adView = findViewById(R.id.spellIngame_banner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        loadRewardedAd();
//        ad code end
        String DifficultyLvl = getIntent().getStringExtra("DiffLvl");

        if (DifficultyLvl.equalsIgnoreCase(getResources().getString(R.string.easy))) {
            isEasy = true;
            MAX_LENGHT_OF_SPELLING = 5;
        } else if (DifficultyLvl.equalsIgnoreCase(getResources().getString(R.string.medium))) {
            MAX_LENGHT_OF_SPELLING = 8;
            isMed = true;
        } else if (DifficultyLvl.equalsIgnoreCase(getResources().getString(R.string.hard))) {
            MAX_LENGHT_OF_SPELLING = 12;
            isHard = true;
        } else {
            Toast.makeText(context, "Something went wrong please restart the game", Toast.LENGTH_SHORT).show();
        }


        setScore();
        setLivesIcon();
        setHintIcon();
        loadImagesResource(); //loading into array
        setImage(); //choosing image and answr and  buttons

    }

    private void loadRewardedAd() {
        //TODO--Implement
    }

    private void setHintIcon() {
        spellHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        spellHint2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        spellHint3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        if (HINTS_int == 2) {
            spellHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));

        } else if (HINTS_int == 1) {
            spellHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
            spellHint2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
        } else if (HINTS_int == 0) {
            spellHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
            spellHint2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
            spellHint3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
        }
    }

    private void setLivesIcon() {
        img_l1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F46363")));
        img_l2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F46363")));
        img_l3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F46363")));
        if (LIVES_int == 2) {
            img_l1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#63FFFFFF")));

        } else if (LIVES_int == 1) {
            img_l1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#63FFFFFF")));
            img_l2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#63FFFFFF")));
        } else if (LIVES_int == 0) {
            img_l1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#63FFFFFF")));
            img_l2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#63FFFFFF")));
            img_l3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#63FFFFFF")));
        }
    }

    private void setScore() {
        scoreTV.setText(MessageFormat.format("Score: {0}", scoreInt));
    }

    private void setImage() {

        int index = new Random().nextInt(imgAns.length - 1);
        Log.e("setImage: ", "Index Genrated is " + index);

        try {
            imageView.setImageResource(imgRes[index]);
            answere = imgAns[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(context, "index out of bound-" + imgAns.length + " i->" + index, Toast.LENGTH_SHORT).show();
        }

        inputTv.setText("____");
        setButtons();
    }

    private void setButtons() {
        String s = "";

        try {
            s = answere + getRandomChars(12 - answere.length());
        } catch (Exception e) {
            e.printStackTrace();
        }

        char[] chars = s.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char aChar : chars) {
            list.add(aChar);
        }
        Collections.shuffle(list);
        b1.setText(String.format("%s", list.get(0)));
        b2.setText(String.format("%s", list.get(1)));
        b3.setText(String.format("%s", list.get(2)));
        b4.setText(String.format("%s", list.get(3)));
        b5.setText(String.format("%s", list.get(4)));
        b6.setText(String.format("%s", list.get(5)));
        b7.setText(String.format("%s", list.get(6)));
        b8.setText(String.format("%s", list.get(7)));
        b9.setText(String.format("%s", list.get(8)));
        b10.setText(String.format("%s", list.get(9)));
        b11.setText(String.format("%s", list.get(10)));
        b12.setText(String.format("%s", list.get(11)));
    }

    private String getRandomChars(int lim) {
        StringBuilder ret = new StringBuilder();
        for (int a = 0; a < lim; a++) {
            ret.append(letters.charAt(new Random().nextInt(26)));
        }
        return ret.toString();
    }

    private void loadImagesResource() {
        if (isEasy) {
            imgRes = ResKeyValue.easyRes;
            imgAns = ResKeyValue.easyAns;
        } else if (isMed) {
            imgRes = ResKeyValue.medRes;
            imgAns = ResKeyValue.medAns;
        } else if (isHard) {
            imgRes = ResKeyValue.hardRes;
            imgAns = ResKeyValue.hardAns;
        }

    }

    public void showHint(View view) {
    }
}