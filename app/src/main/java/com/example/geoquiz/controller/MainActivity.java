package com.example.geoquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geoquiz.R;

public class MainActivity extends AppCompatActivity {
    private Button mCapitales;
    private Button mSOL;
    private Button mDFR;
    private Button mUSS;
    private TextView mDernierScoreCap;
    private TextView mDernierScoreADM;
    private TextView mDernierScoreFR;
    private TextView mDernierScoreFlags;

    public static final int GAME_ACTIVITY_REQUEST_CODE= 23;
    public static final int ADM_ACTIVITY_REQUEST_CODE= 12;
    public static final int FR_ACTIVITY_REQUEST_CODE= 2;
    public static final int FLAGS_ACTIVITY_REQUEST_CODE = 5;

    private SharedPreferences mPreferences;

    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_SCOREADM = "PREF_KEY_SCORESOL";
    public static final String PREF_KEY_SCOREFR = "PREF_KEY_SCOREDFR";
    public static final String PREF_KEY_SCOREFLAGS = "PREF_KEY_SCOREUSS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCapitales = findViewById(R.id.capitales_btn);
        mSOL = findViewById(R.id.adm_btn);
        mDFR = findViewById(R.id.fr_btn);
        mUSS = findViewById(R.id.flags_btn);

        mPreferences = getPreferences(MODE_PRIVATE);

        mDernierScoreCap = findViewById(R.id.dernier_score_cap_txt);
        mDernierScoreADM = findViewById(R.id.dernier_score_adm_txt);
        mDernierScoreFR = findViewById(R.id.dernier_score_fr_txt);
        mDernierScoreFlags = findViewById(R.id.dernier_score_flags_txt);

        greetUser();

        mCapitales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CapitalActivityIntent = new Intent(MainActivity.this, CapitalsActivity.class);
                startActivityForResult(CapitalActivityIntent,GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mSOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ADMActivityIntent = new Intent(MainActivity.this, AdmActivity.class);
                startActivityForResult(ADMActivityIntent,ADM_ACTIVITY_REQUEST_CODE);
            }
        });

        mDFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FRActivityIntent = new Intent(MainActivity.this, FrActivity.class);
                startActivityForResult(FRActivityIntent,FR_ACTIVITY_REQUEST_CODE);
            }
        });

        mUSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent FlagsActivityIntent = new Intent(MainActivity.this, FlagsActivity.class);
                startActivityForResult(FlagsActivityIntent, FLAGS_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(CapitalsActivity.BUNDLE_EXTRA_SCORE, 0);
            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();
        }

        if (ADM_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){

            int scoreadm = data.getIntExtra(AdmActivity.BUNDLE_EXTRA_SCORE,0);
            mPreferences.edit().putInt(PREF_KEY_SCOREADM,scoreadm).apply();
        }

        if (FR_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){

            int scorefr = data.getIntExtra(FrActivity.BUNDLE_EXTRA_SCORE,0);
            mPreferences.edit().putInt(PREF_KEY_SCOREFR,scorefr).apply();
        }

        if (FLAGS_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){

            int scoreflags = data.getIntExtra(FlagsActivity.BUNDLE_EXTRA_SCORE,0);
            mPreferences.edit().putInt(PREF_KEY_SCOREFLAGS,scoreflags).apply();
        }

        greetUser();
    }

    private void greetUser() {

            int score = mPreferences.getInt(PREF_KEY_SCORE, 0);
            int scoreadm = mPreferences.getInt(PREF_KEY_SCOREADM, 0);
            int scorefr = mPreferences.getInt(PREF_KEY_SCOREFR,0);
            int scoreflags = mPreferences.getInt(PREF_KEY_SCOREFLAGS,0);

            String fulltext = " Votre dernier score : " + score + " pts";
            mDernierScoreCap.setText(fulltext);

            String fulltextadm = "Votre dernier score : "  + scoreadm + " pts";
            mDernierScoreADM.setText(fulltextadm);

            String fulltextfr = "Votre dernier score : "  + scorefr + " pts";
            mDernierScoreFR.setText(fulltextfr);

            String fulltextflags = "Votre dernier score : "  + scoreflags + " pts";
            mDernierScoreFlags.setText(fulltextflags);


    }
}
