package com.anngrynerds.kidssafari.spellGame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anngrynerds.kidssafari.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class SpellingGameScreen extends AppCompatActivity {

    private static final String AD_ID_SPELLGAME_REWARDED = "ca-app-pub-9666108206323574/1450025260";
    Context context;
    boolean isEasy, isMed, isHard;
    int[] imgRes;
    String[] imgAns;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;
    Button[] b = new Button[12];
    ImageView imageView;
    ImageView img_l1, img_l2, img_l3, spellHint1, spellHint2, spellHint3;

    int HINTS_int = 3, LIVES_int = 3, scoreInt = 0, HIGHSCORE_INT;
    StringBuilder ipString = new StringBuilder();

    AdRequest adRequest;
    RewardedAd mRewardedAd;
    String answere;
    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    TextView inputTv, scoreTV;
    Animation anim;
    private View.OnClickListener oneKeyOnclickLister;

    private boolean isRewardedAdReady;
    private int MAX_LENGHT_OF_SPELLING;
    private boolean isRewardEarned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spelling_game_screen);
        context = getApplicationContext();

        getSupportActionBar().hide();

        oneKeyOnclickLister = v -> {
            Button b = (Button) v;
            checkAns(b.getText().toString());

        };

//        Init
        {
            imageView = findViewById(R.id.img);

            b[0] = findViewById(R.id.b1);
            b[1] = findViewById(R.id.b2);
            b[2] = findViewById(R.id.b3);
            b[3] = findViewById(R.id.b4);
            b[4] = findViewById(R.id.b5);
            b[5] = findViewById(R.id.b6);
            b[6] = findViewById(R.id.b7);
            b[7] = findViewById(R.id.b8);
            b[8] = findViewById(R.id.b9);
            b[9] = findViewById(R.id.b10);
            b[10] = findViewById(R.id.b11);
            b[11] = findViewById(R.id.b12);

            inputTv = findViewById(R.id.spell_keyout_tv);
            img_l1 = findViewById(R.id.l1);
            img_l2 = findViewById(R.id.l2);
            img_l3 = findViewById(R.id.l3);
            spellHint1 = findViewById(R.id.spell_hint1);
            spellHint2 = findViewById(R.id.spell_hint2);
            spellHint3 = findViewById(R.id.spell_hint3);
            scoreTV = findViewById(R.id.score_textViw);

            b[0].setOnClickListener(oneKeyOnclickLister);
            b[1].setOnClickListener(oneKeyOnclickLister);
            b[2].setOnClickListener(oneKeyOnclickLister);
            b[3].setOnClickListener(oneKeyOnclickLister);
            b[4].setOnClickListener(oneKeyOnclickLister);
            b[5].setOnClickListener(oneKeyOnclickLister);
            b[6].setOnClickListener(oneKeyOnclickLister);
            b[7].setOnClickListener(oneKeyOnclickLister);
            b[8].setOnClickListener(oneKeyOnclickLister);
            b[9].setOnClickListener(oneKeyOnclickLister);
            b[10].setOnClickListener(oneKeyOnclickLister);
            b[11].setOnClickListener(oneKeyOnclickLister);

        }

//        ad code
        {
            RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
            MobileAds.setRequestConfiguration(conf);
            MobileAds.initialize(this, initializationStatus -> {

            });
            AdView adView = findViewById(R.id.spellIngame_banner);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            loadRewardedAd();

        }
//        ad code end

        String DifficultyLvl = getIntent().getStringExtra("DiffLvl");

        Log.e("onCreate: ", " diff level" + DifficultyLvl);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        HIGHSCORE_INT = prefs.getInt("SpellingHighScore", 0);

        if (DifficultyLvl.equalsIgnoreCase(getResources().getString(R.string.easy))) {
            isEasy = true;

        } else if (DifficultyLvl.equalsIgnoreCase(getResources().getString(R.string.medium))) {
            isMed = true;
        } else if (DifficultyLvl.equalsIgnoreCase(getResources().getString(R.string.hard))) {
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

    private void checkAns(String input) {
        String str = getIpStrig(inputTv.getText().toString()) + input;
        int noOfChars = getNoOfChars(str);
        Log.e("checkAns: ", "Input: " + str);
        if (str.equalsIgnoreCase(answere)) {
            Toast.makeText(context, "Right ans", Toast.LENGTH_SHORT).show();
            inputTv.setText("");
            scoreInt++;
            setScore();
            setImage();
        } else if (noOfChars == MAX_LENGHT_OF_SPELLING) {
            Toast.makeText(context, "Wrong ans", Toast.LENGTH_SHORT).show();
            if (LIVES_int == 0) noLives();
            else {
                LIVES_int--;
                inputTv.setText("");
                if (LIVES_int < 0)
                    noLives();
                else
                    setLivesIcon();
            }
        } else {
            inputTv.setText("");
            StringBuilder inputToSet = new StringBuilder(str);
            int rem = MAX_LENGHT_OF_SPELLING - noOfChars - 1;
            Log.e("checkAns: ", " char remaining : " + rem);
            for (int i = 0; i <= rem; i++) {
                inputToSet.append("_");
            }
            inputTv.setText(inputToSet);
        }

    }

    private void noLives() {
        Toast.makeText(context, "Ran out of Lives", Toast.LENGTH_LONG).show();

//        TODO Ran out od lives

        final Dialog nooLives = new Dialog(SpellingGameScreen.this);
        nooLives.setContentView(R.layout.spelling_no_lives_and_exit);
        TextView headtv = nooLives.findViewById(R.id.no_lives_spell_heading);

        headtv.setText("No Lives!!");
        nooLives.setCancelable(false);
        String scoreString;
        TextView score = nooLives.findViewById(R.id.textView5);

        if (HIGHSCORE_INT < scoreInt) {

            scoreString = "Congrats! You made a High Score!!" +
                    "\n" + scoreInt;
        } else {
            scoreString = "Hi-score: " + HIGHSCORE_INT +
                    "\nYour score: " + scoreInt;
        }
        score.setText(scoreString);

        nooLives.findViewById(R.id.refillLives).setOnClickListener(view -> {
            //Load ad
            if (isRewardedAdReady) {
                displayRewardedAd();
                if (isRewardEarned) {
                    LIVES_int = 3;
                    setLivesIcon();
                    nooLives.dismiss();
                } else {
                    Toast.makeText(context, "Unable to Get Reward", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Unable to load ad at this moment", Toast.LENGTH_LONG).show();
                nooLives.dismiss();
            }
        });
        nooLives.findViewById(R.id.restart_math).setOnClickListener(view -> {
            if (HIGHSCORE_INT < scoreInt) {
                HIGHSCORE_INT = scoreInt;
                SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("SpellingHighScore" + scoreInt, scoreInt);
                editor.apply();
            }
            nooLives.dismiss();
            super.onBackPressed();
        });
        nooLives.show();

    }

    private void noHints() {

        Toast.makeText(context, "Ran out of Hints", Toast.LENGTH_LONG).show();

//        TODO Ran out of hints

        final Dialog nooLives = new Dialog(SpellingGameScreen.this);
        nooLives.setContentView(R.layout.spelling_no_lives_and_exit);
        nooLives.setCancelable(true);
        TextView score = nooLives.findViewById(R.id.textView5);
        TextView headtv = nooLives.findViewById(R.id.no_lives_spell_heading);

        headtv.setText("No Hints!!");
        score.setText("You have No HInts lefts");

        nooLives.findViewById(R.id.refillLives).setOnClickListener(view -> {
            //Load ad
            if (isRewardedAdReady) {
                displayRewardedAd();
                if (isRewardEarned) {
                    HINTS_int = 3;
                    setHintIcon();
                } else {
                    Toast.makeText(context, "Unable to Get Reward", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(context, "Unable to load ad at this moment", Toast.LENGTH_LONG).show();
            nooLives.dismiss();
        });
        Button restart = nooLives.findViewById(R.id.restart_math);
        restart.setText("Loose progress and exit");
        restart.setOnClickListener(view -> {

            nooLives.dismiss();
            super.onBackPressed();
        });
        nooLives.show();

    }


    private String getIpStrig(String str) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '_') {
                ret.append(str.charAt(i));
            }
        }
        return ret.toString();
    }

    private int getNoOfChars(String str) {
        int ret = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '_')
                ret++;
        }
        return ret;
    }

    private void loadRewardedAd() {
        //TODO--Implement
        isRewardedAdReady = false;
        RewardedAd.load(SpellingGameScreen.this, AD_ID_SPELLGAME_REWARDED,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.e("ad load error", loadAdError.getMessage());
                        mRewardedAd = null;

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        isRewardedAdReady = true;
                        Toast.makeText(context, "ad loaded", Toast.LENGTH_LONG).show();
                    }
                });

        if (mRewardedAd == null) {
            Log.e("TAG", "The rewarded ad wasn't ready yet.");
//            loadRewardedAd();
//            Toast.makeText(GameScreen.this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_LONG).show();
        } else {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.e("TAG", "Ad was shown.");
                    mRewardedAd = null;
//                    loadRewardedAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e("TAG", "Ad failed to show.");
                    //  loadRewardedAd();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    Log.e("TAG", "Ad was dismissed.");
                    Toast.makeText(context, "No Reward Earned", Toast.LENGTH_SHORT).show();
                    //     loadRewardedAd();
                }

            });
        }
    }

    private void displayRewardedAd() {
        if (mRewardedAd != null) {
            Activity activityContext = SpellingGameScreen.this;
            mRewardedAd.show(activityContext, rewardItem -> {
                // Handle the reward.
                Log.d("TAG", "The user earned the reward.");
                isRewardEarned = true;
                loadRewardedAd();
            });
        } else {
            Log.d("TAG", "rewarded ad false");
            loadRewardedAd();
        }
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

        for (int i = 0; i < 12; i++) {
            b[i].setBackgroundResource(R.drawable.spell_key_button);
        }


        int index = new Random().nextInt(imgAns.length - 1);
        Log.e("setImage: ", "Index Genrated is " + index);

        try {
            imageView.setImageResource(imgRes[index]);
            answere = imgAns[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(context, "index out of bound-" + imgAns.length + " i->" + index, Toast.LENGTH_SHORT).show();
        }

        ipString.setLength(0);
        MAX_LENGHT_OF_SPELLING = answere.length();
        for (int i = 0; i < MAX_LENGHT_OF_SPELLING; i++) {
            ipString.append("_");
        }

        MAX_LENGHT_OF_SPELLING = answere.length();
        inputTv.setText(ipString);

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

        for (int i = 0; i < 12; i++) {
            b[i].setText(String.format("%s", list.get(i)));
        }

//        b1.setText(String.format("%s", list.get(0)));
//        b2.setText(String.format("%s", list.get(1)));
//        b3.setText(String.format("%s", list.get(2)));
//        b4.setText(String.format("%s", list.get(3)));
//        b5.setText(String.format("%s", list.get(4)));
//        b6.setText(String.format("%s", list.get(5)));
//        b7.setText(String.format("%s", list.get(6)));
//        b8.setText(String.format("%s", list.get(7)));
//        b9.setText(String.format("%s", list.get(8)));
//        b10.setText(String.format("%s", list.get(9)));
//        b11.setText(String.format("%s", list.get(10)));
//        b12.setText(String.format("%s", list.get(11)));
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

        if (HINTS_int <= 0) {
            noHints();
        } else {
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

            for (int i = 0; i < answere.length(); i++) {
                b[i].setBackgroundColor(Color.BLUE);
            }

            for (int i = 0; i < 12; i++) {
                b[i].setText(String.format("%s", list.get(i)));
            }
            HINTS_int--;
            setHintIcon();

        }

    }


}