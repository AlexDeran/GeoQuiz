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

public class TcpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final long COUNTDOWN_IN_MILLIS = 11000;

    private TextView mTCPQuestion;
    private Button mTCPAnswer1;
    private Button mTCPAnswer2;
    private Button mTCPAnswer3;
    private Button mTCPAnswer4;
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
        setContentView(R.layout.activity_tcp);

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

        mTCPQuestion = findViewById(R.id.tcp_question_txt);
        mImage = findViewById(R.id.tcp_image);
        mTCPAnswer1 = findViewById(R.id.tcp_answer1_btn);
        mTCPAnswer2 = findViewById(R.id.tcp_answer2_btn);
        mTCPAnswer3 = findViewById(R.id.tcp_answer3_btn);
        mTCPAnswer4 = findViewById(R.id.tcp_answer4_btn);

        mScoreDisplay = findViewById(R.id.tcp_score);
        mNbrofQuestion = findViewById(R.id.tcp_questions_count);
        mCountDown = findViewById(R.id.tcp_question_timer);
        mProgressBar =findViewById(R.id.tcp_progress_bar);

        // Use the tag property to 'name' the buttons
        mTCPAnswer1.setTag(0);
        mTCPAnswer2.setTag(1);
        mTCPAnswer3.setTag(2);
        mTCPAnswer4.setTag(3);

        mTCPAnswer1.setOnClickListener(this);
        mTCPAnswer2.setOnClickListener(this);
        mTCPAnswer3.setOnClickListener(this);
        mTCPAnswer4.setOnClickListener(this);

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

        int taganswer1 = (int) mTCPAnswer1.getTag();
        int taganswer2 = (int) mTCPAnswer2.getTag();
        int taganswer3 = (int) mTCPAnswer3.getTag();
        int taganswer4 = (int) mTCPAnswer4.getTag();

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
                mTCPAnswer1.setBackgroundColor(Color.parseColor("#008000"));
            }

            else if(taganswer2 == mImgQuestion.getAnswerIndex()){
                mTCPAnswer2.setBackgroundColor(Color.parseColor("#008000"));;
            }

            else if(taganswer3 == mImgQuestion.getAnswerIndex()){
                mTCPAnswer3.setBackgroundColor(Color.parseColor("#008000"));

            }

            else if(taganswer4 == mImgQuestion.getAnswerIndex()){
                mTCPAnswer4.setBackgroundColor(Color.parseColor("#008000"));
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

                    mTCPAnswer1.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mTCPAnswer2.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mTCPAnswer3.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mTCPAnswer4.setBackgroundColor(Color.parseColor("#39A1FF"));
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
        mCountDownTimer.cancel();
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
        mTCPQuestion.setText(imgQuestion.getImgQuestion());
        mImage.setImageResource(imgQuestion.getImage());
        mTCPAnswer1.setText(imgQuestion.getChoiceList().get(0));
        mTCPAnswer2.setText(imgQuestion.getChoiceList().get(1));
        mTCPAnswer3.setText(imgQuestion.getChoiceList().get(2));
        mTCPAnswer4.setText(imgQuestion.getChoiceList().get(3));
    }
    private ImgBank generateQuestions() {

        ImgQuestion imgQuestion1 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.ic_shape_of_france, Arrays.asList("France", "Allemagne","Espagne", "Belgique"),
                0);

        ImgQuestion imgQuestion2 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.eiffel_tower, Arrays.asList("Allemagne", "France","Belgique", "Italie"),
                1);

        ImgQuestion imgQuestion3 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.ic_shape_of_spain, Arrays.asList("Portugal", "Italie","Espagne", "Suisse"),
                2);

        ImgQuestion imgQuestion4 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.sagrada_familia, Arrays.asList("France", "Portugal","Italie", "Espagne"),
                3);

        ImgQuestion imgQuestion5 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.ic_shape_of_canada, Arrays.asList("Canada", "États-Unis","Russie", "Australie"),
                0);

        ImgQuestion imgQuestion6 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.cn_tower, Arrays.asList("États-Unis", "Canada","Allemagne", "Australie"),
                1);

        ImgQuestion imgQuestion7 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_russia, Arrays.asList("Chine", "Ukraine","Russie", "Kazakhstan"),
                2);

        ImgQuestion imgQuestion8 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.moscow_basile, Arrays.asList("Ukraine", "Bulgarie","Roumanie", "Russie"),
                3);

        ImgQuestion imgQuestion9 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_australia, Arrays.asList("Australie", "Canada","États-Unis", "Russie"),
                0);

        ImgQuestion imgQuestion10 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.sydney_opera, Arrays.asList("États-Unis", "Australie","Canada", "Russie"),
                1);

        ImgQuestion imgQuestion11 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.ic_shape_of_germany, Arrays.asList("Australie", "Pologne","Allemagne", "Russie"),
                2);

        ImgQuestion imgQuestion12 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.brandeburg_gate, Arrays.asList("États-Unis", "Pologne","Danemark", "Allemagne"),
                3);

        ImgQuestion imgQuestion13 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_brazil, Arrays.asList("Brésil", "Colombie","Vénézuela", "Argentine"),
                0);

        ImgQuestion imgQuestion14 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.rio_corcovado, Arrays.asList("Argentine", "Brésil","Colombie", "Chili"),
                1);

        ImgQuestion imgQuestion15 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_usa, Arrays.asList("Canada", "Mexique","États-Unis", "Russie"),
                2);

        ImgQuestion imgQuestion16 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.liberty_statue, Arrays.asList("Canada", "Mexique","Australie", "États-Unis"),
                3);

        ImgQuestion imgQuestion17 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_japan, Arrays.asList("Japon", "Chine","Corée du Sud", "Malaisie"),
                0);

        ImgQuestion imgQuestion18 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.mount_fuji, Arrays.asList("Chine", "Japon","Corée du Sud", "Bangladesh"),
                1);

        ImgQuestion imgQuestion19 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_italy, Arrays.asList("France", "Espagne","Italie", "Grèce"),
                2);

        ImgQuestion imgQuestion20 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.colosseum, Arrays.asList("France", "Espagne","Grèce", "Italie"),
                3);

        ImgQuestion imgQuestion21 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_china, Arrays.asList("Chine", "Russie","Japon", "Mongolie"),
                0);

        ImgQuestion imgQuestion22 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.the_great_wall, Arrays.asList("Japon", "Chine","Russie", "Mongolie"),
                1);

        ImgQuestion imgQuestion23 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_egypt, Arrays.asList("Algérie", "Libye","Égypte", "Kenya"),
                2);

        ImgQuestion imgQuestion24 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.pyramids, Arrays.asList("Libye", "Kenya","Tchad", "Égypte"),
                3);

        ImgQuestion imgQuestion25 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_gb, Arrays.asList("Grande-Bretagne", "Phillipines","Japon", "Indonésie"),
                0);

        ImgQuestion imgQuestion26 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.big_ben, Arrays.asList("Canada", "Grande-Bretagne","Australie", "États-Unis"),
                1);

        ImgQuestion imgQuestion27 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_greece, Arrays.asList("Japon", "Italie","Grèce", "Croatie"),
                2);

        ImgQuestion imgQuestion28 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.acropolis, Arrays.asList("Italie", "Turquie","Croatie", "Grèce"),
                3);

        ImgQuestion imgQuestion29 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_iceland, Arrays.asList("Islande", "Irlande","Danemark", "Finlande"),
                0);

        ImgQuestion imgQuestion30 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.godafoss, Arrays.asList("Norvège", "Islande","Suède", "Finlande"),
                1);

        ImgQuestion imgQuestion31 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_india, Arrays.asList("Chine", "Iran","Inde", "Mongolie"),
                2);

        ImgQuestion imgQuestion32 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.taj_mahal, Arrays.asList("Sri Lanka", "Népal","Mongolie", "Inde"),
                3);

        ImgQuestion imgQuestion33 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_peru, Arrays.asList("Pérou", "Équateur","Argentine", "Bolivie"),
                0);

        ImgQuestion imgQuestion34 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.machu_pichu, Arrays.asList("Bolivie", "Pérou","Équateur", "Costa-Rica"),
                1);

        ImgQuestion imgQuestion35 = new ImgQuestion("Quel est ce pays ?",
                R.drawable.shape_of_denmark, Arrays.asList("Estonie", "Lituanie","Danemark", "Pologne"),
                2);

        ImgQuestion imgQuestion36 = new ImgQuestion("Dans quel pays se situe cet endroit ?",
                R.drawable.nyhavn, Arrays.asList("Suède", "Islande","Danemark", "Norvège"),
                3);


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
                imgQuestion36
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