package com.example.geoquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.R;
import com.example.geoquiz.model.ImgBank;
import com.example.geoquiz.model.ImgQuestion;

import java.util.Arrays;
import java.util.Locale;

public class FlagsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final long COUNTDOWN_IN_MILLIS = 11000;

    private TextView mFlagsQuestion;
    private Button mFlagsAnswer1;
    private Button mFlagsAnswer2;
    private Button mFlagsAnswer3;
    private Button mFlagsAnswer4;
    private ImageView mImage;

    private TextView mScoreDisplay;
    private TextView mNbrofQuestion;
    private TextView mCountDown;
    private ProgressBar mProgressBar;

    private ImgBank mImgBank;
    private ImgQuestion mImgQuestion;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private boolean mEnableTouchEvents;
    private int mQuestionTotal;
    private int mQuestionCounter;

    private ColorStateList CountDownColor;
    private CountDownTimer mCountDownTimer;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags);

        mImgBank = this.generateQuestions();
        mScore = 0;
        mNumberOfQuestions = 10;

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = 10;
        }

        mEnableTouchEvents = true;

        mFlagsQuestion = findViewById(R.id.flags_question_txt);
        mImage = findViewById(R.id.flags_image);
        mFlagsAnswer1 = findViewById(R.id.flags_answer1_btn);
        mFlagsAnswer2 = findViewById(R.id.flags_answer2_btn);
        mFlagsAnswer3 = findViewById(R.id.flags_answer3_btn);
        mFlagsAnswer4 = findViewById(R.id.flags_answer4_btn);

        mScoreDisplay = findViewById(R.id.flags_score);
        mNbrofQuestion = findViewById(R.id.flags_questions_count);
        mCountDown = findViewById(R.id.flags_question_timer);
        mProgressBar =findViewById(R.id.flags_progress_bar);

        // Use the tag property to 'name' the buttons
        mFlagsAnswer1.setTag(0);
        mFlagsAnswer2.setTag(1);
        mFlagsAnswer3.setTag(2);
        mFlagsAnswer4.setTag(3);

        mFlagsAnswer1.setOnClickListener(this);
        mFlagsAnswer2.setOnClickListener(this);
        mFlagsAnswer3.setOnClickListener(this);
        mFlagsAnswer4.setOnClickListener(this);

        mImgQuestion = mImgBank.getImgQuestion();
        this.displayImgQuestion(mImgQuestion);

        mQuestionTotal = 10;
        mQuestionCounter = 1;

        CountDownColor = mCountDown.getTextColors();
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        int taganswer1 = (int) mFlagsAnswer1.getTag();
        int taganswer2 = (int) mFlagsAnswer2.getTag();
        int taganswer3 = (int) mFlagsAnswer3.getTag();
        int taganswer4 = (int) mFlagsAnswer4.getTag();

        mCountDownTimer.cancel();

        if(responseIndex == mImgQuestion.getAnswerIndex()){
            // Bon
            Toast toast =  Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,350);
            toast.show();

            v.setBackgroundColor(Color.parseColor("#008000"));

            mScore++;
        } else {
            // Mauvais
            Toast toast = Toast.makeText(this, "Mauvaise réponse !",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,350);
            toast.show();

            v.setBackgroundColor(Color.parseColor("#830000"));

            if(taganswer1 == mImgQuestion.getAnswerIndex()){
                mFlagsAnswer1.setBackgroundColor(Color.parseColor("#008000"));
            }

            else if(taganswer2 == mImgQuestion.getAnswerIndex()){
                mFlagsAnswer2.setBackgroundColor(Color.parseColor("#008000"));;
            }

            else if(taganswer3 == mImgQuestion.getAnswerIndex()){
                mFlagsAnswer3.setBackgroundColor(Color.parseColor("#008000"));

            }

            else if(taganswer4 == mImgQuestion.getAnswerIndex()){
                mFlagsAnswer4.setBackgroundColor(Color.parseColor("#008000"));
            }
        }
        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                mCountDownTimer.cancel();
                timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                startCountDown();
                if (--mNumberOfQuestions == 0 && mQuestionCounter <= 10) {
                    // End the game
                    endGame();
                } else {
                    mImgQuestion = mImgBank.getImgQuestion();
                    displayImgQuestion(mImgQuestion);
                    mQuestionCounter++;

                    mScoreDisplay.setText("Score : " + mScore);
                    mNbrofQuestion.setText(mQuestionCounter + "/10");

                    mProgressBar.setProgress(mQuestionCounter);

                    mFlagsAnswer1.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mFlagsAnswer2.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mFlagsAnswer3.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mFlagsAnswer4.setBackgroundColor(Color.parseColor("#39A1FF"));
                }
            }
        }, 2000);
    }

    private void startCountDown() {
        mCountDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDown();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDown();
                mCountDownTimer.cancel();
                if (--mNumberOfQuestions == 0) {
                    // End the game
                    endGame();
                } else {
                    mImgQuestion = mImgBank.getImgQuestion();
                    displayImgQuestion(mImgQuestion);
                    mQuestionCounter++;
                    mScoreDisplay.setText("Score : " + mScore);
                    mNbrofQuestion.setText(mQuestionCounter + "/10");
                    timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                    startCountDown();
                }
            }
        }.start();
    }

    private void  updateCountDown(){

        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mCountDown.setText(timeFormatted);

        if(timeLeftInMillis < 6000){
            mCountDown.setTextColor(Color.RED);
        }
        else{
            mCountDown.setTextColor(CountDownColor);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bien joué !")
                .setMessage("Votre score est de " + mScore + "/10")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // End the activity
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void displayImgQuestion(final ImgQuestion imgQuestion) {
        mFlagsQuestion.setText(imgQuestion.getImgQuestion());
        mImage.setImageResource(imgQuestion.getImage());
        mFlagsAnswer1.setText(imgQuestion.getChoiceList().get(0));
        mFlagsAnswer2.setText(imgQuestion.getChoiceList().get(1));
        mFlagsAnswer3.setText(imgQuestion.getChoiceList().get(2));
        mFlagsAnswer4.setText(imgQuestion.getChoiceList().get(3));
    }
    private ImgBank generateQuestions() {

        ImgQuestion imgQuestion1 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_argentina, Arrays.asList("Argentine", "Bolivie","Uruguay", "Colombie"),
                0);

        ImgQuestion imgQuestion2 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_australia, Arrays.asList("Australie", "Nouvelle-Zélande","Grande-Bretagne", "États-Unis"),
                0);

        ImgQuestion imgQuestion3 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_belgium, Arrays.asList("Pays-Bas", "Allemagne","Espagne", "Belgique"),
                3);

        ImgQuestion imgQuestion4 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_brazil, Arrays.asList("Belize", "Bulgarie","Brésil", "Bangladesh"),
                2);

        ImgQuestion imgQuestion5 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_denmark, Arrays.asList("Suède", "Danemark","Norvège", "Islande"),
                1);

        ImgQuestion imgQuestion6 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_fiji, Arrays.asList("Finlande", "Îles Cook","Fidji", "Îles Féroé"),
                2);

        ImgQuestion imgQuestion7 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_india, Arrays.asList("Irak", "Inde","Ghana", "Éthiopie"),
                1);

        ImgQuestion imgQuestion8 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_germany, Arrays.asList("Arménie", "Pays-Bas","Belgique", "Allemagne"),
                3);

        ImgQuestion imgQuestion9 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_kuwait, Arrays.asList("Koweït", "Irak","Afghanistan", "Égypte"),
                0);

        ImgQuestion imgQuestion10 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_new_zealand, Arrays.asList("Grande-Bretagne", "Nouvelle-Zélande","Australie", "États-Unis"),
                1);

        ImgQuestion imgQuestion11 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_south_africa, Arrays.asList("Botswana", "Cameroun","Afrique du Sud", "Sénégal"),
                2);

        ImgQuestion imgQuestion12 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_kiribati, Arrays.asList("Nauru", "Kiribati","Seychelles", "Maldives"),
                1);

        ImgQuestion imgQuestion13 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_switzerland, Arrays.asList("Vatican", "Tonga","Suisse", "Liechtenstein"),
                2);

        ImgQuestion imgQuestion14 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_seychelles, Arrays.asList("Maldives", "Seychelles","Comores", "Tuvalu"),
                1);

        ImgQuestion imgQuestion15 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_albania, Arrays.asList("Albanie", "Monténégro","Macédoine", "Moldavie"),
                0);

        ImgQuestion imgQuestion16 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_afghanistan, Arrays.asList("Iran", "Syrie","Irak", "Afghanistan"),
                3);

        ImgQuestion imgQuestion17 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_azerbaijan, Arrays.asList("Turquie", "Syrie","Azerbaïdjan", "Arménie"),
                2);

        ImgQuestion imgQuestion18 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_bangladesh, Arrays.asList("Bangladesh", "Népal","Bhoutan", "Sri Lanka"),
                0);

        ImgQuestion imgQuestion19 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_barbados, Arrays.asList("Cuba", "Porto Rico","Barbade", "Panama"),
                2);

        ImgQuestion imgQuestion20 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_belarus, Arrays.asList("Estonie", "Lituanie","Lettonie", "Biélorussie"),
                3);

        ImgQuestion imgQuestion21 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_belize, Arrays.asList("Guatemala", "Belize","Honduras", "Costa Rica"),
                1);

        ImgQuestion imgQuestion22 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_bosnia_and_herzegovina, Arrays.asList("Croatie", "Slovénie","Bosnie-Herzégovine", "République Tchéque"),
                2);

        ImgQuestion imgQuestion23 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_brunei, Arrays.asList("Philippines", "Cambodge","Brunei", "Laos"),
                2);

        ImgQuestion imgQuestion24 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_cambodia, Arrays.asList("Laos", "Vietnam","Birmanie", "Cambodge"),
                3);

        ImgQuestion imgQuestion25 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_canada, Arrays.asList("Canada", "États-Unis","Mexique", "Pérou"),
                0);

        ImgQuestion imgQuestion26 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_chile, Arrays.asList("Chili", "Uruguay","Paraguay", "Argentine"),
                0);

        ImgQuestion imgQuestion27 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_china, Arrays.asList("Vietnam", "Mongolie","Chine", "Japon"),
                2);

        ImgQuestion imgQuestion28 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_croatia, Arrays.asList("Slovaquie", "Slovénie","Croatie", "Albanie"),
                2);

        ImgQuestion imgQuestion29 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_cyprus, Arrays.asList("Malte", "Chypre","Bulgarie", "Égypte"),
                1);

        ImgQuestion imgQuestion30 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_czech_republic, Arrays.asList("Slovaquie", "Hongrie","Slovénie", "République Tchéque"),
                3);

        ImgQuestion imgQuestion31 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_ecuador, Arrays.asList("Équateur", "Colombie","Vénézuela", "Bolivie"),
                0);

        ImgQuestion imgQuestion32 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_egypt, Arrays.asList("Libye", "Mauritanie","Soudan", "Égypte"),
                3);

        ImgQuestion imgQuestion33 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_estonia, Arrays.asList("Estonie", "Lituanie","Lettonie", "Finlande"),
                0);

        ImgQuestion imgQuestion34 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_finland, Arrays.asList("Suède", "Finlande","Norvège", "Danemark"),
                1);

        ImgQuestion imgQuestion35 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_france, Arrays.asList("Espagne", "Allemagne","France", "Italie"),
                2);

        ImgQuestion imgQuestion36 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_greece, Arrays.asList("Croatie", "Albanie","Malte", "Grèce"),
                3);

        ImgQuestion imgQuestion37 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_iceland, Arrays.asList("Islande", "Finlande","Norvège", "Suède"),
                0);

        ImgQuestion imgQuestion38 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_iran, Arrays.asList("Irak", "Iran","Koweït", "Syrie"),
                1);

        ImgQuestion imgQuestion39 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_ireland, Arrays.asList("Cote d'Ivoire", "Pays de Galles","Irlande", "Hongrie"),
                2);

        ImgQuestion imgQuestion40 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_italy, Arrays.asList("Allemagne", "France","Suisse", "Italie"),
                3);

        ImgQuestion imgQuestion41 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_jamaica, Arrays.asList("Jamaïque", "Barbades","Porto-Rico", "Cuba"),
                0);

        ImgQuestion imgQuestion42 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_japan, Arrays.asList("Phillipines", "Japon","Chine", "Mongolie"),
                1);

        ImgQuestion imgQuestion43 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_kazakhstan, Arrays.asList("Mongolie", "Azerbaïdjan","Kazakhstan", "Arménie"),
                2);

        ImgQuestion imgQuestion44 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_kenya, Arrays.asList("Éthiopie", "Somalie","Togo", "Kenya"),
                3);

        ImgQuestion imgQuestion45 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_liechtenstein, Arrays.asList("Liechtenstein", "Slovénie","Slovaquie", "Serbie"),
                0);

        ImgQuestion imgQuestion46 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_mexico, Arrays.asList("Honduras", "Mexique","Costa Rica", "Panama"),
                1);

        ImgQuestion imgQuestion47 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_mongolia, Arrays.asList("Kazakhstan", "Turkménistan","Mongolie", "Bhoutan"),
                2);

        ImgQuestion imgQuestion48 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_montenegro, Arrays.asList("Albanie", "Serbie","Monténégro", "Macédoine"),
                2);

        ImgQuestion imgQuestion49 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_nepal, Arrays.asList("Bangladesh", "Bhoutan","Birmanie", "Népal"),
                3);

        ImgQuestion imgQuestion50 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_netherlands, Arrays.asList("Pays-Bas", "Luxembourg","Russie", "France"),
                0);

        ImgQuestion imgQuestion51 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_norway, Arrays.asList("Finlande", "Norvège","Islande", "Danemark"),
                1);

        ImgQuestion imgQuestion52 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_portugal, Arrays.asList("Espagne", "Brésil","Italie", "Portugal"),
                3);

        ImgQuestion imgQuestion53 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_russia, Arrays.asList("Russie", "Pays-Bas","Luxembourg", "Arménie"),
                0);

        ImgQuestion imgQuestion54 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_saudi_arabia, Arrays.asList("Yemen", "Arabie Saoudite","Oman", "Irak"),
                1);

        ImgQuestion imgQuestion55 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_serbia, Arrays.asList("Croatie", "Albanie","Serbie", "Moldavie"),
                2);

        ImgQuestion imgQuestion56 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_slovakia, Arrays.asList("Slovénie", "Serbie","Croatie", "Slovaquie"),
                3);

        ImgQuestion imgQuestion57 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_south_korea, Arrays.asList("Corée du Sud", "Corée du Nord","Vietnam", "Japon"),
                0);

        ImgQuestion imgQuestion58 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_spain, Arrays.asList("Portugal", "Espagne","Argentine", "Uruguay"),
                1);

        ImgQuestion imgQuestion59 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_sri_lanka, Arrays.asList("Inde", "Bhoutan","Sri Lanka", "Bangladesh"),
                2);

        ImgQuestion imgQuestion60 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_sweden, Arrays.asList("Islande", "Norvège","Danemark", "Suède"),
                3);

        ImgQuestion imgQuestion61 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_ukraine, Arrays.asList("Ukraine", "Moldavie","Roumanie", "Bulgarie"),
                0);

        ImgQuestion imgQuestion62 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_united_kingdom, Arrays.asList("Australie", "Royaume-Uni","Nouvelle-Zélande", "États-Unis"),
                1);

        ImgQuestion imgQuestion63 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_uruguay, Arrays.asList("Argentine", "Paraguay","Uruguay", "Chili"),
                2);

        ImgQuestion imgQuestion64 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_usa, Arrays.asList("Royaume-Uni", "Australie","Nouvelle-Zélande", "États-Unis"),
                3);

        ImgQuestion imgQuestion65 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_vatican, Arrays.asList("Vatican", "Monaco","Saint-Marin", "Andorre"),
                0);

        ImgQuestion imgQuestion66 = new ImgQuestion("À quel pays appartient ce drapeau ?",
                R.drawable.ic_flag_of_venezuela, Arrays.asList("Colombie", "Vénézuela","Équateur", "Suriname"),
                1);


        return new ImgBank(Arrays.asList(
                imgQuestion1,
                imgQuestion2,
                imgQuestion3,
                imgQuestion4,
                imgQuestion5,
                imgQuestion6,
                imgQuestion7,
                imgQuestion8,
                imgQuestion9,
                imgQuestion10,
                imgQuestion11,
                imgQuestion12,
                imgQuestion13,
                imgQuestion14,
                imgQuestion15,
                imgQuestion16,
                imgQuestion17,
                imgQuestion18,
                imgQuestion19,
                imgQuestion20,
                imgQuestion21,
                imgQuestion22,
                imgQuestion23,
                imgQuestion24,
                imgQuestion25,
                imgQuestion26,
                imgQuestion27,
                imgQuestion28,
                imgQuestion29,
                imgQuestion30,
                imgQuestion31,
                imgQuestion32,
                imgQuestion33,
                imgQuestion34,
                imgQuestion35,
                imgQuestion36,
                imgQuestion37,
                imgQuestion38,
                imgQuestion39,
                imgQuestion40,
                imgQuestion41,
                imgQuestion42,
                imgQuestion43,
                imgQuestion44,
                imgQuestion45,
                imgQuestion46,
                imgQuestion47,
                imgQuestion48,
                imgQuestion49,
                imgQuestion50,
                imgQuestion51,
                imgQuestion52,
                imgQuestion53,
                imgQuestion54,
                imgQuestion55,
                imgQuestion56,
                imgQuestion57,
                imgQuestion58,
                imgQuestion59,
                imgQuestion60,
                imgQuestion61,
                imgQuestion62,
                imgQuestion63,
                imgQuestion64,
                imgQuestion65,
                imgQuestion66
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }
}