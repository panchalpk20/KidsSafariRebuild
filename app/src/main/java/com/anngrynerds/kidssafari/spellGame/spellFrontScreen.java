package com.anngrynerds.kidssafari.spellGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.anngrynerds.kidssafari.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Objects;

import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class spellFrontScreen extends AppCompatActivity {

    Button easy, medium, hard;
    private View.OnClickListener onClickOnSpellDiffButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_front_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        onClickOnSpellDiffButton = v -> {
            Button b = (Button) v;
            startActivity(new Intent(spellFrontScreen.this, SpellingGameScreen.class)
                    .putExtra("DiffLvl", b.getText().toString()));
        };

//        Ad Code
        {
            RequestConfiguration conf = new RequestConfiguration.Builder().setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
            MobileAds.setRequestConfiguration(conf);
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            AdView adView = findViewById(R.id.spellfp_banner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
//         Ad code ENd

        easy = findViewById(R.id.spell_easy);
        medium = findViewById(R.id.spell_medium);
        hard = findViewById(R.id.spell_hard);
        easy.setOnClickListener(onClickOnSpellDiffButton);
        medium.setOnClickListener(onClickOnSpellDiffButton);
        hard.setOnClickListener(onClickOnSpellDiffButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backPressed(View view) {
        onBackPressed();
    }
}