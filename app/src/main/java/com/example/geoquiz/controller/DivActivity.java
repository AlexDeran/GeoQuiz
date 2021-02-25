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

public class DivActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mSOLQuestion;
    private Button mSOLAnswer1;
    private Button mSOLAnswer2;
    private Button mSOLAnswer3;
    private Button mSOLAnswer4;

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
        setContentView(R.layout.activity_div);

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

        mSOLQuestion = findViewById(R.id.sol_question_txt);
        mSOLAnswer1 = findViewById(R.id.sol_answer1_btn);
        mSOLAnswer2 = findViewById(R.id.sol_answer2_btn);
        mSOLAnswer3 = findViewById(R.id.sol_answer3_btn);
        mSOLAnswer4 = findViewById(R.id.sol_answer4_btn);

        // Use the tag property to 'name' the buttons
        mSOLAnswer1.setTag(0);
        mSOLAnswer2.setTag(1);
        mSOLAnswer3.setTag(2);
        mSOLAnswer4.setTag(3);

        mSOLAnswer1.setOnClickListener(this);
        mSOLAnswer2.setOnClickListener(this);
        mSOLAnswer3.setOnClickListener(this);
        mSOLAnswer4.setOnClickListener(this);

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
        mSOLQuestion.setText(question.getQuestion());
        mSOLAnswer1.setText(question.getChoiceList().get(0));
        mSOLAnswer2.setText(question.getChoiceList().get(1));
        mSOLAnswer3.setText(question.getChoiceList().get(2));
        mSOLAnswer4.setText(question.getChoiceList().get(3));
    }
    private QuestionBank generateQuestions() {

        Question question1 = new Question("Comment s'appelle le détroit séparant la Russie de l'Alaska ?",
                Arrays.asList("Le détroit de Malacca", "Le détroit de Magellan","Le détroit de Béring", "Le détroit des Dardanelles"),
                2);

        Question question2 = new Question("Quelle étendue d'eau est considéré comme le plus grand lac du monde ?",
                Arrays.asList("Le lac Supérieur", "Le lac Huron","La mer Caspienne", "Le lac Victoria"),
                2);

        Question question3 = new Question("Quel pays est bordé par la mer des Laptev ?",
                Arrays.asList("Russie", "Estonie","Norvége", "Finlande"),
                0);

        Question question4 = new Question("Le détroit de Kara permet à 2 mers de se rencontrer, quelles sont-elles ?",
                Arrays.asList("Mer des Laptev & Mer de Kara", "Mer de Chine Méridionale & Mer de Kara","Mer de Barents & Mer de Kara", "Mer de Kara & Mer des Philippines"),
                2);

        Question question5 = new Question("Quelles sont les 2 mers qui se rejoignent au canal de Suez ?",
                Arrays.asList("Mer Noire & Mer Caspienne", "Mer Méditerranée & Mer Noire", "Mer Caspienne & Mer Rouge", "Mer  Méditerranée & Mer Rouge"),
                3);

        Question question6 = new Question("Sur quelle île se trouve la ville de Palerme ?",
                Arrays.asList("Corse", "Sardaigne", "Ibiza", "Sicile"),
                3);

        Question question7 = new Question("L'île de Sakhaline appartient à quel pays ?",
                Arrays.asList("Japon", "Chine", "Russie", "Indonésie"),
                2);

        Question question8 = new Question("Quel est le plus petit pays du monde ?",
                Arrays.asList("Monaco", "Liechtenstein", "Vatican", "Nauru"),
                2);

        Question question9 = new Question("À quel pays l'oblast de Kaliningrad appartient-il ?",
                Arrays.asList("Pologne", "Russie", "Ukraine", "Allemagne"),
                1);

        Question question10 = new Question("Quel est le plus grand désert du monde ?",
                Arrays.asList("Sahara", "Atacama", "Gobi", "Antartique"),
                3);

        Question question11 = new Question("Une ville turque porte le nom d'un super-héros, quel est-il ?",
                Arrays.asList("Superman", "Aquaman", "Batman", "Spiderman"),
                2);

        Question question12 = new Question("Combien y a-t-il de continents sur Terre ?",
                Arrays.asList("4", "5", "6", "7"),
                3);

        Question question13 = new Question("Quelle est la longueur du Danube ?",
                Arrays.asList("1993 km", "2387 km","2576 km", "2860 km"),
                3);

        Question question14 = new Question("Quel pays n'a pas de frontières avec l'Espagne ? ",
                Arrays.asList("Maroc", "Algérie","Grande-Bretagne", "Portugal"),
                1);

        Question question15 = new Question("Quel pays n'est pas traversé par le Rhin ?",
                Arrays.asList("Autriche", "Allemagne","Pays-Bas", "France"),
                0);

        Question question16 = new Question("Quel détroit sépare les 2 îles de la Nouvelle-Zélande",
                Arrays.asList("Détroit de Malacca", "Détroit de Magellan", "Détroit de Cook", "Détroit du Bosphore"),
                2);

        Question question17 = new Question("A quel pays appartient le Groenland ?",
                Arrays.asList("Norvége", "Danemark", "Suéde", "Islande"),
                1);

        Question question18 = new Question("A quel pays appartient le Svalbard ?",
                Arrays.asList("Russie", "Danemark", "Norvége", "Islande"),
                2);

        Question question19 = new Question("Comment s'appelle le lieu habité le plus au nord de la planète ?",
                Arrays.asList("Longyearbyen", "Alert", "Thulé", "Nord"),
                1);

        Question question20 = new Question("Quelle ville se trouve à la fois au Nigeria et au Portugal ?",
                Arrays.asList("Porto", "Lagos", "Calabar", "Evora"),
                1);

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

