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

public class FrActivity extends AppCompatActivity implements View.OnClickListener {

        private TextView mDFRQuestion;
        private Button mDFRAnswer1;
        private Button mDFRAnswer2;
        private Button mDFRAnswer3;
        private Button mDFRAnswer4;

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

            mDFRQuestion = findViewById(R.id.dept_fr_question_txt);
            mDFRAnswer1 = findViewById(R.id.dept_fr_answer1_btn);
            mDFRAnswer2 = findViewById(R.id.dept_fr_answer2_btn);
            mDFRAnswer3 = findViewById(R.id.dept_fr_answer3_btn);
            mDFRAnswer4 = findViewById(R.id.dept_fr_answer4_btn);

            // Use the tag property to 'name' the buttons
            mDFRAnswer1.setTag(0);
            mDFRAnswer2.setTag(1);
            mDFRAnswer3.setTag(2);
            mDFRAnswer4.setTag(3);

            mDFRAnswer1.setOnClickListener(this);
            mDFRAnswer2.setOnClickListener(this);
            mDFRAnswer3.setOnClickListener(this);
            mDFRAnswer4.setOnClickListener(this);

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
                    .setMessage("Votre score est de " + mScore +"/10")
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
            mDFRQuestion.setText(question.getQuestion());
            mDFRAnswer1.setText(question.getChoiceList().get(0));
            mDFRAnswer2.setText(question.getChoiceList().get(1));
            mDFRAnswer3.setText(question.getChoiceList().get(2));
            mDFRAnswer4.setText(question.getChoiceList().get(3));
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
}