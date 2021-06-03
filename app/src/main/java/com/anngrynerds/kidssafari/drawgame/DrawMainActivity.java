package com.anngrynerds.kidssafari.drawgame;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.anngrynerds.kidssafari.R;

import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawMainActivity extends AppCompatActivity {


    SeekBar widthSb;
    LinearLayout control;
    Dialog d, dask;
    View drawLayout, newC, ask;
    Button show;
    private DrawPadView hbView;
    private int paintWidth;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Objects.requireNonNull(this.getSupportActionBar()).hide();

        setContentView(R.layout.activity_draw_main);

        d = new Dialog(DrawMainActivity.this);
        dask = new Dialog(DrawMainActivity.this);
        drawLayout = getLayoutInflater().inflate(R.layout.draw_dialog, null);

        dask.setContentView(ask);

        hbView = findViewById(R.id.drawPadView1);

        widthSb = findViewById(R.id.seekBar1);

        control = findViewById(R.id.controlls);

        widthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
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

    }

    public void setC(View view) {
        String color = (String) view.getTag();

        if (color.equals("#ffffff")) {
            widthSb.setProgress(100);
            control.setBackgroundColor(Color.WHITE);
            findViewById(R.id.mainl).setBackgroundColor(Color.WHITE
            );

        }
        if (color.equals("pencil")) {
            hbView.setColor(Color.BLACK);
            widthSb.setProgress(3);
            control.setBackgroundColor(Color.BLACK);
            findViewById(R.id.mainl).setBackgroundColor(Color.BLACK);

        } else {
            hbView.setColor(Color.parseColor(color));
            control.setBackgroundColor(Color.parseColor(color));
            findViewById(R.id.mainl).setBackgroundColor(Color.parseColor(color));


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