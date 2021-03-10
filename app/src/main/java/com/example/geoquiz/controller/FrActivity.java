package com.example.geoquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoquiz.R;
import com.example.geoquiz.model.Question;
import com.example.geoquiz.model.QuestionBank;

import java.util.Arrays;
import java.util.Locale;

public class FrActivity extends AppCompatActivity implements View.OnClickListener {

    public static final long COUNTDOWN_IN_MILLIS = 11000;

    private TextView mFrQuestion;
    private Button mFrAnswer1;
    private Button mFrAnswer2;
    private Button mFrAnswer3;
    private Button mFrAnswer4;

    private TextView mScoreDisplay;
    private TextView mNbrofQuestion;
    private TextView mCountDown;
    private ProgressBar mProgressBar;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

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



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr);


        mQuestionBank = this.generateQuestions();
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

        mFrQuestion = findViewById(R.id.fr_question_txt);
        mFrAnswer1 = findViewById(R.id.fr_answer1_btn);
        mFrAnswer2 = findViewById(R.id.fr_answer2_btn);
        mFrAnswer3 = findViewById(R.id.fr_answer3_btn);
        mFrAnswer4 = findViewById(R.id.fr_answer4_btn);

        mScoreDisplay = findViewById(R.id.fr_score);
        mNbrofQuestion = findViewById(R.id.fr_questions_count);
        mCountDown = findViewById(R.id.fr_question_timer);
        mProgressBar =findViewById(R.id.fr_progress_bar);

        // Use the tag property to 'name' the buttons
        mFrAnswer1.setTag(0);
        mFrAnswer2.setTag(1);
        mFrAnswer3.setTag(2);
        mFrAnswer4.setTag(3);

        mFrAnswer1.setOnClickListener(this);
        mFrAnswer2.setOnClickListener(this);
        mFrAnswer3.setOnClickListener(this);
        mFrAnswer4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        int taganswer1 = (int) mFrAnswer1.getTag();
        int taganswer2 = (int) mFrAnswer2.getTag();
        int taganswer3 = (int) mFrAnswer3.getTag();
        int taganswer4 = (int) mFrAnswer4.getTag();

        mCountDownTimer.cancel();

        if(responseIndex == mCurrentQuestion.getAnswerIndex()){
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

            if(taganswer1 == mCurrentQuestion.getAnswerIndex()){
                mFrAnswer1.setBackgroundColor(Color.parseColor("#008000"));
            }

            else if(taganswer2 == mCurrentQuestion.getAnswerIndex()){
                mFrAnswer2.setBackgroundColor(Color.parseColor("#008000"));;
            }

            else if(taganswer3 == mCurrentQuestion.getAnswerIndex()){
                mFrAnswer3.setBackgroundColor(Color.parseColor("#008000"));

            }

            else if(taganswer4 == mCurrentQuestion.getAnswerIndex()){
                mFrAnswer4.setBackgroundColor(Color.parseColor("#008000"));
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

                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                    mQuestionCounter++;

                    mScoreDisplay.setText("Score : " + mScore);
                    mNbrofQuestion.setText(mQuestionCounter + "/10");

                    mProgressBar.setProgress(mQuestionCounter);

                    mFrAnswer1.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mFrAnswer2.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mFrAnswer3.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mFrAnswer4.setBackgroundColor(Color.parseColor("#39A1FF"));
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
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
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

        private void displayQuestion(final Question question) {
            mFrQuestion.setText(question.getQuestion());
            mFrAnswer1.setText(question.getChoiceList().get(0));
            mFrAnswer2.setText(question.getChoiceList().get(1));
            mFrAnswer3.setText(question.getChoiceList().get(2));
            mFrAnswer4.setText(question.getChoiceList().get(3));
        }

        private QuestionBank generateQuestions() {

            Question question1 = new Question("Quelle est la capitale de la France ?",
                    Arrays.asList("Berlin", "Paris", "Rome", "Madrid"),
                    1);

            Question question2 = new Question("Quelle est la ville la plus peuplée de France après Paris ?",
                    Arrays.asList("Lyon", "Toulouse","Marseille", "Lille"),
                    2);

            Question question3 = new Question("Quel est le plus long fleuve Français ?",
                    Arrays.asList("La Loire", "La Seine","La Rhône", "La Garonne"),
                    0);

            Question question4 = new Question("Quel est le chef-lieu de la Corrèze?",
                    Arrays.asList("Poitiers", "Limoges","Guéret", "Tulle"),
                    3);

            Question question5 = new Question("Quel fleuve traverse Bordeaux ?",
                    Arrays.asList("Gironde", "Dordogne","Tarn", "Garonne"),
                    3);

            Question question6 = new Question("Quel département porte le numéro 45 ?",
                    Arrays.asList("Loir-et-Cher", "Loiret","Lot", "Loire-atlantique"),
                    1);

            Question question7 = new Question("Quel est le chef-lieu de l'Orne ?",
                    Arrays.asList("Caen", "Alençon","Évreux", "Laval"),
                    1);

            Question question8 = new Question("Quel est le numéro du département de l'Eure ?",
                    Arrays.asList("78", "27","29", "54"),
                    1);

            Question question9 = new Question("Quel est le numéro du département de la Guadeloupe ?",
                    Arrays.asList("978", "972","971", "975"),
                    2);

            Question question10 = new Question("Quel est le nom du département 72  ?",
                    Arrays.asList("Corrèze", "Seine-Maritime","Sarthe", "Savoie"),
                    2);

            Question question11 = new Question("Quel est le numéro du département de la Moselle ?",
                    Arrays.asList("57", "88","54", "52"),
                    0);

            Question question12 = new Question("Combien y a t-il de départements en France ?",
                    Arrays.asList("97", "100","101", "103"),
                    2);

            Question question13 = new Question("Quel est le nom du département 60 ?",
                    Arrays.asList("Oise", "Moselle","Orne", "Nord"),
                    0);

            Question question14 = new Question("Quel est le nom du département 38 ?",
                    Arrays.asList("Loire", "Isére","Indre", "Indre-et-Loire"),
                    1);

            Question question15 = new Question("Quel est le nom du département 24 ?",
                    Arrays.asList("Creuse", "Dordogne","Doubs", "Drôme"),
                    1);

            Question question16 = new Question("Quel est le chef-lieu du Tarn ?",
                    Arrays.asList("Albi", "Montauban","Toulouse", "Agen"),
                    0);

            Question question17 = new Question("Quel est le chef-lieu de la Marne ?",
                    Arrays.asList("Reims", "Châlons-en-Champagne","Troyes", "Bar-le-Duc"),
                    1);

            Question question18 = new Question("Quel est le nom du département 10 ?",
                    Arrays.asList("Ariège", "Aude","Aube", "Aveyron"),
                    2);

            Question question19 = new Question("Quel est le chef-lieu de la Charente ?",
                    Arrays.asList("La Rochelle", "Cognac", "Royan", "Angoulême"),
                    3);

            Question question20 = new Question("Quel fleuve traverse Tours ?",
                    Arrays.asList("Touraine", "Loiret","Loire", "Maine"),
                    2);


            return new QuestionBank(Arrays.asList(question1,
                    question2,
                    question3,
                    question4,
                    question5,
                    question6,
                    question7,
                    question8,
                    question9,
                    question10,
                    question11,
                    question12,
                    question13,
                    question14,
                    question15,
                    question16,
                    question16,
                    question17,
                    question18,
                    question19,
                    question20
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