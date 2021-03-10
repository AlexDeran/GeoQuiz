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

        return new ImgBank(Arrays.asList(imgQuestion1,
                imgQuestion2,
                imgQuestion3,
                imgQuestion4,
                imgQuestion5,
                imgQuestion6,
                imgQuestion7,
                imgQuestion8,
                imgQuestion9,
                imgQuestion10));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }
}