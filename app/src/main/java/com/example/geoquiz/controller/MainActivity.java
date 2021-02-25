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
    private TextView mDernierScoreSOL;
    private TextView mDernierScoreDFR;
    private TextView mDernierScoreUSS;

    public static final int GAME_ACTIVITY_REQUEST_CODE= 23;
    public static final int SOL_ACTIVITY_REQUEST_CODE= 12;
    public static final int DEPT_FR_ACTIVITY_REQUEST_CODE= 2;
    public static final int USS_ACTIVITY_REQUEST_CODE = 5;

    private SharedPreferences mPreferences;

    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_SCORESOL = "PREF_KEY_SCORESOL";
    public static final String PREF_KEY_SCOREDFR = "PREF_KEY_SCOREDFR";
    public static final String PREF_KEY_SCOREUSS = "PREF_KEY_SCOREUSS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCapitales = findViewById(R.id.capitales_btn);
        mSOL = findViewById(R.id.sol_btn);
        mDFR = findViewById(R.id.dfr_btn);
        mUSS = findViewById(R.id.usa_states_btn);

        mPreferences = getPreferences(MODE_PRIVATE);

        mDernierScoreCap = findViewById(R.id.dernier_score_cap_txt);
        mDernierScoreSOL = findViewById(R.id.dernier_score_sol_txt);
        mDernierScoreDFR = findViewById(R.id.dernier_score_dfr_txt);
        mDernierScoreUSS = findViewById(R.id.dernier_score_usa_txt);

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
                Intent SOLActivityIntent = new Intent(MainActivity.this, DivActivity.class);
                startActivityForResult(SOLActivityIntent,SOL_ACTIVITY_REQUEST_CODE);
            }
        });

        mDFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DFRActivityIntent = new Intent(MainActivity.this, FrActivity.class);
                startActivityForResult(DFRActivityIntent,DEPT_FR_ACTIVITY_REQUEST_CODE);
            }
        });

        mUSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent USSActivityIntent = new Intent(MainActivity.this, USSActivity.class);
                startActivityForResult(USSActivityIntent,USS_ACTIVITY_REQUEST_CODE);
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

        if (SOL_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){

            int scoresol = data.getIntExtra(DivActivity.BUNDLE_EXTRA_SCORE,0);
            mPreferences.edit().putInt(PREF_KEY_SCORESOL,scoresol).apply();
        }

        if (DEPT_FR_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){

            int scoredfr = data.getIntExtra(FrActivity.BUNDLE_EXTRA_SCORE,0);
            mPreferences.edit().putInt(PREF_KEY_SCOREDFR,scoredfr).apply();
        }

        if (USS_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){

            int scoreuss = data.getIntExtra(USSActivity.BUNDLE_EXTRA_SCORE,0);
            mPreferences.edit().putInt(PREF_KEY_SCOREUSS,scoreuss).apply();
        }

        greetUser();
    }

    private void greetUser() {

            int score = mPreferences.getInt(PREF_KEY_SCORE, 0);
            int scoresol = mPreferences.getInt(PREF_KEY_SCORESOL, 0);
            int scoredfr = mPreferences.getInt(PREF_KEY_SCOREDFR,0);
            int scoreuss = mPreferences.getInt(PREF_KEY_SCOREUSS,0);

            String fulltext = " Votre dernier score : " + score + " pts";
            mDernierScoreCap.setText(fulltext);

            String fulltextsol = "Votre dernier score : "  + scoresol + " pts";
            mDernierScoreSOL.setText(fulltextsol);

            String fulltextdfr = "Votre dernier score : "  + scoredfr + " pts";
            mDernierScoreDFR.setText(fulltextdfr);

            String fulltextuss = "Votre dernier score : "  + scoreuss + " pts";
            mDernierScoreUSS.setText(fulltextuss);


    }
}
