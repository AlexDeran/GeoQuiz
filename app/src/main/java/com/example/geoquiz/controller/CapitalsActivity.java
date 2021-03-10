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

public class CapitalsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final long COUNTDOWN_IN_MILLIS = 11000;

    private TextView mCapitalQuestion;
    private Button mCapitalAnswer1;
    private Button mCapitalAnswer2;
    private Button mCapitalAnswer3;
    private Button mCapitalAnswer4;

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
        setContentView(R.layout.activity_capitals);


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

        mCapitalQuestion = findViewById(R.id.capital_question_txt);
        mCapitalAnswer1 = findViewById(R.id.capital_answer1_btn);
        mCapitalAnswer2 = findViewById(R.id.capital_answer2_btn);
        mCapitalAnswer3 = findViewById(R.id.capital_answer3_btn);
        mCapitalAnswer4 = findViewById(R.id.capital_answer4_btn);

        mScoreDisplay = findViewById(R.id.capitals_score);
        mNbrofQuestion = findViewById(R.id.questions_count);
        mCountDown = findViewById(R.id.capitals_question_timer);
        mProgressBar =findViewById(R.id.capitals_progress_bar);

        // Use the tag property to 'name' the buttons
        mCapitalAnswer1.setTag(0);
        mCapitalAnswer2.setTag(1);
        mCapitalAnswer3.setTag(2);
        mCapitalAnswer4.setTag(3);

        mCapitalAnswer1.setOnClickListener(this);
        mCapitalAnswer2.setOnClickListener(this);
        mCapitalAnswer3.setOnClickListener(this);
        mCapitalAnswer4.setOnClickListener(this);

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

        int taganswer1 = (int) mCapitalAnswer1.getTag();
        int taganswer2 = (int) mCapitalAnswer2.getTag();
        int taganswer3 = (int) mCapitalAnswer3.getTag();
        int taganswer4 = (int) mCapitalAnswer4.getTag();

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
                mCapitalAnswer1.setBackgroundColor(Color.parseColor("#008000"));
            }

            else if(taganswer2 == mCurrentQuestion.getAnswerIndex()){
                mCapitalAnswer2.setBackgroundColor(Color.parseColor("#008000"));;
            }

            else if(taganswer3 == mCurrentQuestion.getAnswerIndex()){
                mCapitalAnswer3.setBackgroundColor(Color.parseColor("#008000"));

            }

            else if(taganswer4 == mCurrentQuestion.getAnswerIndex()){
                mCapitalAnswer4.setBackgroundColor(Color.parseColor("#008000"));
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

                    mCapitalAnswer1.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mCapitalAnswer2.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mCapitalAnswer3.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mCapitalAnswer4.setBackgroundColor(Color.parseColor("#39A1FF"));
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
        mCapitalQuestion.setText(question.getQuestion());
        mCapitalAnswer1.setText(question.getChoiceList().get(0));
        mCapitalAnswer2.setText(question.getChoiceList().get(1));
        mCapitalAnswer3.setText(question.getChoiceList().get(2));
        mCapitalAnswer4.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question("Quelle est la capitale de la France ?",
                Arrays.asList("Berlin", "Paris", "Rome", "Madrid"),
                1);

        Question question2 = new Question("Quelle est la capitale du Canada ?",
                Arrays.asList("Montréal", "Toronto", "Ottawa", "Vancouver"),
                2);

        Question question3 = new Question("Quelle est la capitale de l'Azerbaïdjan ?",
                Arrays.asList("Bakou", "Erevan", "Tbilissi", "Krasnodar"),
                0);

        Question question4 = new Question("Quelle est la capitale du Belize ?",
                Arrays.asList("San José", "Managua", "Tegucigalpa", "Belmopan"),
                3);

        Question question5 = new Question("Quelle est la capitale de la Roumanie ?",
                Arrays.asList("Bucarest", "Sofia", "Budapest", "Chisinau"),
                0);

        Question question6 = new Question("Quelle est la capitale du Malawi ?",
                Arrays.asList("Lusaka", "Lilongwe", "Maputo", "Harare"),
                1);

        Question question7 = new Question("Quelle est la capitale du Brunei ?",
                Arrays.asList("Sri Jayawardenapura Kotte", "Port Moresby", "Bandar Seri Begawan", "Dili"),
                2);

        Question question8 = new Question("Quelle est la bonne orthographe de la capitale de la Slovénie ?",
                Arrays.asList("Lujbljana", "Lbuljana", "Ljubjana", "Ljubljana"),
                3);

        Question question9 = new Question("Quel est le nom actuel de la capitale du Kazakhstan ?",
                Arrays.asList("Akmolinsk", "Tselinograd", "Astana", "Noursoultan"),
                3);

        Question question10 = new Question("Quelle est la capitale de la Turquie ?",
                Arrays.asList("Sofia", "Ankara", "Istanbul", "Damas"),
                1);

        Question question11 = new Question("Quelle est la capitale de l'Australie?",
                Arrays.asList("Sydney", "Melbourne", "Canberra", "Perth"),
                2);

        Question question12 = new Question("Quelle est la capitale du Groenland ?",
                Arrays.asList("Qaanaaq", "Nuuk", "Sisimiut", "Ilulissat"),
                1);

        Question question13 = new Question("Quelle est la capitale du Japon ?",
                Arrays.asList("Kyoto", "Nagoya", "Tokyo", "Osaka"),
                2);

        Question question14 = new Question("Quelle est la capitale des États-Unis ?",
                Arrays.asList("Los Angeles", "Washington", "Miami", "New York"),
                1);

        Question question15 = new Question("Quelle est la capitale du Brésil ?",
                Arrays.asList("Rio de Janeiro", "Sao Paulo", "Brasilia", "Fortaleza"),
                2);

        Question question16 = new Question("Quelle est la capitale de la Cote d'Ivoire ?",
                Arrays.asList("Dakar", "Abuja", "Yamoussoukro", "Abidjan"),
                2);

        Question question17 = new Question("Quelle est la capitale de l'Espagne ?",
                Arrays.asList("Lisbonne", "Valence", "Barcelone", "Madrid"),
                3);

        Question question18 = new Question("Quelle est la capitale de l'Italie ?",
                Arrays.asList("Naples", "Rome", "Turin", "Milan"),
                1);

        Question question19 = new Question("Quelle est la capitale de l'Allemagne ?",
                Arrays.asList("Berlin", "Munich", "Hambourg", "Francfort-sur-le-Main"),
                0);

        Question question20 = new Question("Quelle est la capitale de l'Angleterre ?",
                Arrays.asList("Manchester", "Liverpool", "Birmingham", "Londres"),
                3);

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
                question17,
                question18,
                question19,
                question20));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }
}

