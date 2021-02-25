package com.example.geoquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.R;
import com.example.geoquiz.model.Question;
import com.example.geoquiz.model.QuestionBank;

import java.util.Arrays;

public class USSActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mUSSQuestion;
    private Button mUSSAnswer1;
    private Button mUSSAnswer2;
    private Button mUSSAnswer3;
    private Button mUSSAnswer4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uss);

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

        mUSSQuestion = findViewById(R.id.uss_question_txt);
        mUSSAnswer1 = findViewById(R.id.uss_answer1_btn);
        mUSSAnswer2 = findViewById(R.id.uss_answer2_btn);
        mUSSAnswer3 = findViewById(R.id.uss_answer3_btn);
        mUSSAnswer4 = findViewById(R.id.uss_answer4_btn);

        // Use the tag property to 'name' the buttons
        mUSSAnswer1.setTag(0);
        mUSSAnswer2.setTag(1);
        mUSSAnswer3.setTag(2);
        mUSSAnswer4.setTag(3);

        mUSSAnswer1.setOnClickListener(this);
        mUSSAnswer2.setOnClickListener(this);
        mUSSAnswer3.setOnClickListener(this);
        mUSSAnswer4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);

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

        if(responseIndex == mCurrentQuestion.getAnswerIndex()){
            // Bon
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            // Mauvais
            Toast.makeText(this, "Mauvaise réponse !", Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                if (--mNumberOfQuestions == 0) {
                    // End the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000);
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

    private void displayQuestion(final Question question) {
        mUSSQuestion.setText(question.getQuestion());
        mUSSAnswer1.setText(question.getChoiceList().get(0));
        mUSSAnswer2.setText(question.getChoiceList().get(1));
        mUSSAnswer3.setText(question.getChoiceList().get(2));
        mUSSAnswer4.setText(question.getChoiceList().get(3));
    }
    private QuestionBank generateQuestions() {

        Question question1 = new Question("Comment s'appelle la capitale de l'État de l'Iowa ?",
                Arrays.asList("Des Prêtres", "Des Moines", "Des Nonnes", "Des Curés"),
                1);

        Question question2 = new Question("Quel est le plus grand état des États-Unis ?",
                Arrays.asList("Floride", "Californie", "Texas", "Alaska"),
                3);

        Question question3 = new Question("Quel état a pour capitale Raleigh ?",
                Arrays.asList("Caroline du Nord", "Caroline du Sud","Virginie", "Géorgie"),
                0);

        Question question4 = new Question("Quel est la capitale du Texas ?",
                Arrays.asList("Austin", "Houston","San Antonio", "Dallas"),
                0);

        Question question5 = new Question("Quel est la capitale de Californie ?",
                Arrays.asList("Los Angeles", "Sacramento", "San Diego", "San Francisco"),
                1);

        Question question6 = new Question("Quel est l'état le plus peuplé des États Unis ?",
                Arrays.asList("Texas", "Californie", "New York", "Floride"),
                1);

        Question question7 = new Question("Quel est la capitale de l'état de Washington ?",
                Arrays.asList("Washington", "Baltimore", "Seattle", "Olympia"),
                3);

        Question question8 = new Question("Dans quel état se touve la Vallée de la Mort ?",
                Arrays.asList("Utah", "Arizona", "Californie", "Nevada"),
                2);

        Question question9 = new Question("La ville de Chicago se situe au bord de quel grand lac ?",
                Arrays.asList("Le lac Supérieur", "Le lac Michigan", "Le lac Huron", "Le lac Erié"),
                1);

        Question question10 = new Question("Quelle est la capitale de l'état de Floride ?",
                Arrays.asList("Jacksonville", "Miami", "Orlando", "Tallahassee"),
                3);

        Question question11 = new Question("Combien y a t-il d'états aux États-Unis ?",
                Arrays.asList("48", "49", "50", "51"),
                2);

        Question question12 = new Question("Quel est la capitale de l'état de New York ?",
                Arrays.asList("New York", "Syracuse", "Albany", "Buffalo"),
                2);

        Question question13 = new Question("Dans quel état se trouve le Mont Rushmore ?",
                Arrays.asList("Wyoming", "Dakota du Sud", "Nebraska", "Dakota du Nord"),
                1);

        Question question14 = new Question("Quelle est la capitale des États-Unis ?",
                Arrays.asList("Los Angeles", "Washington", "Miami", "New York"),
                1);

        Question question15 = new Question("Dans quel état se trouve la partie américaine des chutes du Niagara ?",
                Arrays.asList("Michigan", "Wisconsin", "Illinois", "New York"),
                3);

        Question question16 = new Question("Quelle ville est familièrement appelée 'Windy City' ?",
                Arrays.asList("Cincinnati", "Chicago", "Seattle", "Saint-Louis"),
                1);

        Question question17 = new Question("Quelle ville se trouve au bord d'un grand lac salé ?",
                Arrays.asList("Las Vegas", "Salt Lake City", "New York", "Los Angeles"),
                1);

        Question question18 = new Question("Dans quel état se situe le Grand Canyon ?",
                Arrays.asList("Californie", "Utah", "Nouveau-Mexique", "Arizona"),
                3);

        Question question19 = new Question("Dans quel état prend source le Mississippi ?",
                Arrays.asList("Minnesota", "Wisconsin", "Mississippi", "Missouri"),
                0);

        Question question20 = new Question("Où se situe la quartier de Manhattan ?",
                Arrays.asList("Los Angeles", "Miami ", "New York", "Chicago"),
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
                question17,
                question18,
                question19,
                question20));

    }
}