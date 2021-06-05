package com.anngrynerds.kidssafari.mathGame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anngrynerds.kidssafari.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class MathGameScreen extends AppCompatActivity {

    public static final String AD_ID_MATHGAME_REWARDED = "ca-app-pub-9666108206323574/1450025260";
    Context context;
    RewardedAd mRewardedAd;
    ImageView mathHint1, mathHint2, mathHint3;
    ImageView img_l1, img_l2, img_l3;
    TransitionDrawable td;
    TextView problemTV,
            stat,
            answerTV,
            scoreTV;
    Button ans1, ans2, ans3, ans4, ans5, ans6;
    String sign, difLvl;
    int n1, n2, n3, ansInt, scoreInt = 0, HIGHSCORE_INT;
    boolean isEasy, isHard, isMedium;
    boolean isAdd, isMul, isDiv, isSub;
    int HINTS_int = 3, LIVES_int = 3;
    AdRequest adRequest;
    String scoreText;
    private View.OnClickListener ansOnclickListener;
    private boolean isRewardedAdReady;
    private boolean isRewardEarned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_math_game_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ansOnclickListener = v -> {
            Button b = (Button) v;
            if (Integer.parseInt(b.getText().toString()) == ansInt) {
                correctAnswer(b);
            } else {
                wrongAnswer(b);
            }
        };

//        view Initializations
        {
            context = getApplicationContext();
            mathHint1 = findViewById(R.id.math_hint1);
            mathHint2 = findViewById(R.id.math_hint2);
            mathHint3 = findViewById(R.id.math_hint3);
            img_l1 = findViewById(R.id.l1);
            img_l2 = findViewById(R.id.l2);
            img_l3 = findViewById(R.id.l3);
            problemTV = findViewById(R.id.problem);
            stat = findViewById(R.id.stat);
            answerTV = findViewById(R.id.ansrr);
            scoreTV = findViewById(R.id.score);
            ans1 = findViewById(R.id.ans1);
            ans2 = findViewById(R.id.ans2);
            ans3 = findViewById(R.id.ans3);
            ans4 = findViewById(R.id.ans4);
            ans5 = findViewById(R.id.ans5);
            ans6 = findViewById(R.id.ans6);
            ans1.setOnClickListener(ansOnclickListener);
            ans2.setOnClickListener(ansOnclickListener);
            ans3.setOnClickListener(ansOnclickListener);
            ans4.setOnClickListener(ansOnclickListener);
            ans5.setOnClickListener(ansOnclickListener);
            ans6.setOnClickListener(ansOnclickListener);
        }

//        Ad code
        {
            RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
            MobileAds.setRequestConfiguration(conf);
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });
            adRequest = new AdRequest.Builder().build();
            AdView adView = findViewById(R.id.math_ingame_banner);
            adView.loadAd(adRequest);
            loadRewardedAd();

        }
//        Ad code end---------------

        sign = getIntent().getStringExtra("sign");
        difLvl = getIntent().getStringExtra("difficulty");

        if (sign.isEmpty() && difLvl.isEmpty()) {
            Toast.makeText(context, "Something went wrong, please restart the game",
                    Toast.LENGTH_LONG).show();
        } else {
            isEasy = difLvl.equals(getResources().getString(R.string.math_easy));
            isMedium = difLvl.equals(getResources().getString(R.string.math_med));
            isHard = difLvl.equals(getResources().getString(R.string.math_hard));
            isAdd = sign.equals(getResources().getString(R.string.addition));
            isSub = sign.equals(getResources().getString(R.string.subtraction));
            isMul = sign.equals(getResources().getString(R.string.multiplication));
            isDiv = sign.equals(getResources().getString(R.string.division));
        }
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        HIGHSCORE_INT = prefs.getInt("MathHighScore" + difLvl + sign, 0);


        setIconsHints();
        setIconsLives();
        updateScore();
        getNumbers();


    }

    private void wrongAnswer(Button clickedB) {
        stat.setBackgroundColor(Color.parseColor("#39ff0000"));
        stat.setText(R.string.wrong);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        animation.setStartTime(1500);
        stat.startAnimation(animation);
        clickedB.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wrong_math));
        clickedB.setBackground(ContextCompat.getDrawable(context, R.drawable.trans_w));
        td = (TransitionDrawable) clickedB.getBackground();
        td.startTransition(1500);
        td.reverseTransition(1500);
        new CountDownTimer(1000, 10) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                stat.setVisibility(View.GONE);
            }
        }.start();
        LIVES_int--;
        if (LIVES_int < 0)
            noLives();
        else
            setIconsLives();
    }

    private void noLives() {
        Toast.makeText(context, "Ran out of Lives", Toast.LENGTH_LONG).show();

//        TODO Ran out od lives

        final Dialog nooLives = new Dialog(MathGameScreen.this);
        nooLives.setContentView(R.layout.maths_no_lives_and_exit);
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
                    setIconsLives();
                } else {
                    Toast.makeText(context, "Unable to Get Reward", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(context, "Unable to load ad at this moment", Toast.LENGTH_LONG).show();
            nooLives.dismiss();
        });
        nooLives.findViewById(R.id.restart_math).setOnClickListener(view -> {
            if (HIGHSCORE_INT < scoreInt) {
                SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("MathHighScore" + difLvl + sign, scoreInt);
                editor.apply();
            }
            nooLives.dismiss();
            super.onBackPressed();
        });
        nooLives.show();

    }

    private void displayRewardedAd() {

        if (mRewardedAd != null) {
            Activity activityContext = MathGameScreen.this;
            mRewardedAd.show(activityContext, rewardItem -> {
                // Handle the reward.
                Log.d("TAG", "The user earned the reward.");
                isRewardEarned = true;
                loadRewardedAd();
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            loadRewardedAd();
        }
    }

    private void correctAnswer(Button b) {
        String ansString = " = " + b.getText().toString();
        answerTV.setText(ansString);
        answerTV.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
        stat.setBackgroundColor(Color.parseColor("#2f5eff00"));
        stat.setText(R.string.correct);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        animation.setStartTime(1500);
        animation.setDuration(190);
        stat.startAnimation(animation);
        scoreInt = scoreInt + 2;
        updateScore();

        new CountDownTimer(1000, 2000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                stat.setVisibility(View.GONE);
                answerTV.setText("=?");
                getNumbers();
            }
        }.start();

    }


    private void getNumbers() {
        if (isAdd) {
            ADDITION addition = new ADDITION();

            if (isEasy) {
                addition.genrateEasy();
                n1 = addition.n1;
                n2 = addition.n2;
                n3 = 0;
            } else if (isMedium) {
                addition.genrateMedium();
                n1 = addition.n1;
                n2 = addition.n2;
                n3 = addition.n3;
            } else if (isHard) {//hard
                addition.genrateHard();
                n1 = addition.n1;
                n2 = addition.n2;
                n3 = addition.n3;
            }
            ansInt = n1 + n2 + n3;
            String s = n1 + "+" + n2 + "+" + n3;
            problemTV.setText(s);
            setAnsTiles();

        } else if (isSub) {
            SUBTRACTION subtraction = new SUBTRACTION();
            if (isEasy) {
                subtraction.genrateEasy();
                n1 = subtraction.n1;
                n2 = subtraction.n2;
                n3 = 0;
            } else if (isMedium) {
                subtraction.genrateMedium();
                n1 = subtraction.n1;
                n2 = subtraction.n2;
                n3 = subtraction.n3;
            } else if (isHard) {//hard
                subtraction.genrateHard();
                n1 = subtraction.n1;
                n2 = subtraction.n2;
                n3 = subtraction.n3;
            }
            ansInt = n1 - n2 - n3;
            String s = n1 + "-" + n2 + "-" + n3;
            problemTV.setText(s);
            setAnsTiles();
        } else if (isMul) {
            MULTIPLICATION multiplication = new MULTIPLICATION();
            if (isEasy) {
                multiplication.genrateEasy();
                n1 = multiplication.n1;
                n2 = multiplication.n2;
                n3 = 0;
                ansInt = n1 * n2;
            } else if (isMedium) {
                multiplication.genrateMedium();
                n1 = multiplication.n1;
                n2 = multiplication.n2;
                n3 = multiplication.n3;
                ansInt = n1 * n2;
            } else if (isHard) {//hard
                multiplication.genrateHard();
                n1 = multiplication.n1;
                n2 = multiplication.n2;
                n3 = multiplication.n3;
                ansInt = n1 * n2 * n3;
            }
            String s = n1 + "x" + n2 + "x" + n3;
            problemTV.setText(s);
            setAnsTiles();
        } else if (isDiv) {
            DIVISION division = new DIVISION();
            if (isEasy) {
                division.genrateEasy();
                n1 = division.n1;
                n2 = division.n2;
                n3 = 0;
            } else if (isMedium) {
                division.genrateMedium();
                n1 = division.n1;
                n2 = division.n2;
            } else if (isHard) {//hard
                division.genrateHard();
                n1 = division.n1;
                n2 = division.n2;
            }
            ansInt = n2 / n1;
            String s = n2 + "/" + n1;
            problemTV.setText(s);
            setAnsTiles();
        }
    }

    private void setAnsTiles() {

        int x;
        int y;
        int a;
        int b;
        int c;

        do {
            x = new Random().nextInt(3) + ansInt - new Random().nextInt(6);
            y = new Random().nextInt(4) - ansInt + new Random().nextInt(5);
            a = new Random().nextInt(5) + ansInt + new Random().nextInt(4);
            b = new Random().nextInt(6) - ansInt + new Random().nextInt(3);
            c = new Random().nextInt(7) + ansInt + new Random().nextInt(2);
        } while (
                x == ansInt || y == ansInt || a == ansInt || b == ansInt || c == ansInt ||
                        x == a || x == b || x == c || x == y ||
                        y == a || y == b || y == c ||
                        a == b || a == c ||
                        b == c

        );

        int[] ar = new int[]{a, b, c, x, y, ansInt};

        Log.e("", " unShuffled arr " + Arrays.toString(ar));
        for (int i = ar.length - 1; i > 0; i--) {
            int index = new Random().nextInt(i + 1);
            // Simple swap
            int aa = ar[index];
            ar[index] = ar[i];
            ar[i] = aa;
        }
        Log.e("", " Shuffled arr " + Arrays.toString(ar));

        ans1.setText(String.valueOf(ar[0]));
        ans2.setText(String.valueOf(ar[1]));
        ans3.setText(String.valueOf(ar[2]));
        ans4.setText(String.valueOf(ar[3]));
        ans5.setText(String.valueOf(ar[4]));
        ans6.setText(String.valueOf(ar[5]));


    }

    private void updateScore() {
        scoreText = "Score" + scoreInt;
        scoreTV.setText(scoreText);
    }

    private void setIconsHints() {
        mathHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        mathHint2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        mathHint3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        if (HINTS_int == 2) {
            mathHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));

        } else if (HINTS_int == 1) {
            mathHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
            mathHint2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
        } else if (HINTS_int == 0) {
            mathHint1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
            mathHint2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
            mathHint3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#55FFFFFF")));
        }
    }

    private void setIconsLives() {
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

    private void loadRewardedAd() {
        isRewardedAdReady = false;
        RewardedAd.load(this, AD_ID_MATHGAME_REWARDED,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("ad load error", loadAdError.getMessage());
                        mRewardedAd = null;

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
//                        Log.d("TAG", "onAdFailedToLoad");\
                        isRewardedAdReady = true;
                       // Toast.makeText(context, "ad loaded", Toast.LENGTH_SHORT).show();
                    }
                });

        if (mRewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
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

    public void showHint(View view) {
        if (HINTS_int <= 0) {
            noHints();
        } else {
            displayAnswer();
        }
    }

    private void noHints() {

        //  Toast.makeText(context, "No- Hints", Toast.LENGTH_SHORT).show();

//        TODO Ran out of hints

        final Dialog noHints = new Dialog(MathGameScreen.this);
        noHints.setContentView(R.layout.maths_no_lives_and_exit);
        noHints.setCancelable(true);
        String scoreString;
        TextView score = noHints.findViewById(R.id.textView5);
        score.setText(R.string.watch_to_refill);
        Button showAd = noHints.findViewById(R.id.refillLives);
        showAd.setText(R.string.refill_hints);


        showAd.setOnClickListener(view -> {
            //Load ad
            if (isRewardedAdReady) {
                displayRewardedAd();
                if (isRewardEarned) {
                    HINTS_int = 3;
                    setIconsHints();
                } else {
                    Toast.makeText(context, "Unable to Get Reward", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(context, "Unable to load ad at this moment", Toast.LENGTH_LONG).show();

            loadRewardedAd();
            noHints.dismiss();
        });

        noHints.show();


    }

    private void displayAnswer() {
        Toast.makeText(context, "answer is " + ansInt, Toast.LENGTH_LONG).show();
        HINTS_int--;
        setIconsHints();
    }

    @Override
    public void onBackPressed() {

        final Dialog d = new Dialog(MathGameScreen.this);
        d.setContentView(R.layout.maths_no_lives_and_exit);
        TextView t = d.findViewById(R.id.no_lives_math_heading);
        t.setText(R.string.do_you_want_to_exit);

        ImageView i = d.findViewById(R.id.imageView);
        i.setVisibility(View.GONE);

        TextView tt = d.findViewById(R.id.textView5);
        tt.setText(R.string.you_ll_lost_progress);

        Button noExit = d.findViewById(R.id.refillLives);
        noExit.setText(R.string.continue_game);
        noExit.setOnClickListener(view -> d.dismiss());

        Button exit = d.findViewById(R.id.restart_math);
        exit.setText(R.string.exit_game);
        exit.setOnClickListener(view -> {
            d.dismiss();
            MathGameScreen.super.onBackPressed();
        });

        d.show();

    }
}