package com.anngrynerds.kidssafari.drawgame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anngrynerds.kidssafari.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Objects;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class DrawMainActivity extends AppCompatActivity {


    SeekBar widthSb;
    LinearLayout control;
    Dialog dialog;
    View drawLayout;
    Button pensize;
    TextView dTitle, dHeading, seekbarTv;
    Button dYes, dNo;
    private DrawPadView hbView;
    private int paintWidth;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        setContentView(R.layout.activity_draw_main);


//        ad code
        RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
        MobileAds.setRequestConfiguration(conf);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adRequest = new AdRequest.Builder().build();

        loadFullscreenAd();


//        ------------------

        dialog = new Dialog(DrawMainActivity.this);
        drawLayout = getLayoutInflater().inflate(R.layout.draw_dialog, null);
        dialog.setContentView(drawLayout);

        dTitle = drawLayout.findViewById(R.id.draw_dialog_title);
        dHeading = drawLayout.findViewById(R.id.draw_dialog_heading);
        dYes = drawLayout.findViewById(R.id.draw_dialog_yes);
        dNo = drawLayout.findViewById(R.id.draw_dialog_no);

        seekbarTv = findViewById(R.id.seekbartv);
        seekbarTv.setVisibility(View.GONE);

        pensize = findViewById(R.id.pensize);
        hbView = findViewById(R.id.drawPadView1);
        widthSb = findViewById(R.id.seekBar1);
        widthSb.setVisibility(View.GONE);
        control = findViewById(R.id.controlls);
        widthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                seekbarTv.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                seekbarTv.setVisibility(View.VISIBLE);
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                seekbarTv.setText("" + progress);
                seekbarTv.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                paintWidth = progress + 1;
                hbView.setPaintWidth(paintWidth);
            }
        });

        findViewById(R.id.draw_save).setOnClickListener(v -> {
            showsaveDialog();
        });

        findViewById(R.id.draw_newfile).setOnClickListener(v -> {
            showNewDialog();
        });

        pensize.setOnClickListener(v -> {
            if (widthSb.getVisibility() == View.GONE) {
                widthSb.setVisibility(View.VISIBLE);
            } else {
                widthSb.setVisibility(View.GONE);
            }

        });

        ImageButton save = findViewById(R.id.draw_save);

        ConstraintLayout parent = findViewById(R.id.mainl);
        TextView tv = new TextView(this);
        tv.setX(save.getX() + 10);
        parent.addView(tv);

    }

    private void loadFullscreenAd() {

        InterstitialAd.load(this, "ca-app-pub-9666108206323574/1143171374", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Toast.makeText(DrawMainActivity.this, "Ad loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Toast.makeText(DrawMainActivity.this, "Ad not loaded " + loadAdError.getMessage(), Toast.LENGTH_LONG).show();
                mInterstitialAd = null;
            }
        });

        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.
                    Log.d("TAG", "The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
                    Log.d("TAG", "The ad was shown.");
                }
            });

        }
    }

    private void showNewDialog() {

        dTitle.setText("New Drawing");
        dHeading.setText("Unsaved drawing will be Lost !");

        dYes.setOnClickListener(v -> {
            hbView.clearScreen();
            displayFullscreenAd();
            dialog.dismiss();
        });

        dNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void displayFullscreenAd() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(DrawMainActivity.this);
        } else {
            Log.e("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void showsaveDialog() {
        //dialog for saving

        if (ContextCompat.checkSelfPermission(DrawMainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            dialogAskforPermission();
        } else {

            dTitle.setText("Save File");
            dHeading.setText("Save Your Drawing");

            hbView.setDrawingCacheEnabled(true);


            dYes.setOnClickListener(v -> {

                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), hbView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "drawing");

                if (imgSaved != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                    displayFullscreenAd();
                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                hbView.destroyDrawingCache();
                dialog.dismiss();
            });

            dNo.setOnClickListener(v -> {
                dialog.dismiss();

            });

            dialog.show();
        }

    }

    private void dialogAskforPermission() {
        dTitle.setText("permission");
        dHeading.setText("To save your drawing we need permission to access your storage");

        dialog.show();

        dYes.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(DrawMainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            dialog.dismiss();

        });
        dNo.setOnClickListener(v -> dialog.dismiss());

        if (ContextCompat.checkSelfPermission(DrawMainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            dialog.show();
        }

    }

    public void setC(View view) {
        String color = (String) view.getTag();

        if (color.equals("#ffffff")) {
            widthSb.setProgress(100);
//            pensize.setBackgroundColor(Color.WHITE);
//            pensize.setTextColor(Color.BLACK);
            findViewById(R.id.mainl).setBackgroundColor(Color.WHITE
            );

        }
        if (color.equals("pencil")) {
            hbView.setColor(Color.BLACK);
            widthSb.setProgress(3);
//            pensize.setBackgroundColor(Color.BLACK);
//            pensize.setTextColor(Color.WHITE);
            findViewById(R.id.mainl).setBackgroundColor(Color.BLACK);

        } else {
            hbView.setColor(Color.parseColor(color));
            pensize.setBackgroundColor(Color.parseColor(color));
            pensize.setTextColor(Color.BLACK);
            //     findViewById(R.id.mainl).setBackgroundColor(Color.parseColor(color));

        }

    }


    public void colorPeek(View view) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.RED, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                hbView.setColor(color);
                control.setBackgroundColor(color);
                findViewById(R.id.mainl).setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        dialog.show();
    }
}