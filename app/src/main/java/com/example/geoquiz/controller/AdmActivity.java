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

public class AdmActivity extends AppCompatActivity implements View.OnClickListener {

    public static final long COUNTDOWN_IN_MILLIS = 11000;

    private TextView mADMQuestion;
    private Button mADMAnswer1;
    private Button mADMAnswer2;
    private Button mADMAnswer3;
    private Button mADMAnswer4;

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
        setContentView(R.layout.activity_adm);


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

        mADMQuestion = findViewById(R.id.adm_question_txt);
        mADMAnswer1 = findViewById(R.id.adm_answer1_btn);
        mADMAnswer2 = findViewById(R.id.adm_answer2_btn);
        mADMAnswer3 = findViewById(R.id.adm_answer3_btn);
        mADMAnswer4 = findViewById(R.id.adm_answer4_btn);

        mScoreDisplay = findViewById(R.id.adm_score);
        mNbrofQuestion = findViewById(R.id.adm_questions_count);
        mCountDown = findViewById(R.id.adm_question_timer);
        mProgressBar =findViewById(R.id.adm_progress_bar);

        // Use the tag property to 'name' the buttons
        mADMAnswer1.setTag(0);
        mADMAnswer2.setTag(1);
        mADMAnswer3.setTag(2);
        mADMAnswer4.setTag(3);

        mADMAnswer1.setOnClickListener(this);
        mADMAnswer2.setOnClickListener(this);
        mADMAnswer3.setOnClickListener(this);
        mADMAnswer4.setOnClickListener(this);

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

        int taganswer1 = (int) mADMAnswer1.getTag();
        int taganswer2 = (int) mADMAnswer2.getTag();
        int taganswer3 = (int) mADMAnswer3.getTag();
        int taganswer4 = (int) mADMAnswer4.getTag();

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
                mADMAnswer1.setBackgroundColor(Color.parseColor("#008000"));
            }

            else if(taganswer2 == mCurrentQuestion.getAnswerIndex()){
                mADMAnswer2.setBackgroundColor(Color.parseColor("#008000"));;
            }

            else if(taganswer3 == mCurrentQuestion.getAnswerIndex()){
                mADMAnswer3.setBackgroundColor(Color.parseColor("#008000"));

            }

            else if(taganswer4 == mCurrentQuestion.getAnswerIndex()){
                mADMAnswer4.setBackgroundColor(Color.parseColor("#008000"));
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

                    mADMAnswer1.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mADMAnswer2.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mADMAnswer3.setBackgroundColor(Color.parseColor("#39A1FF"));
                    mADMAnswer4.setBackgroundColor(Color.parseColor("#39A1FF"));
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
        mADMQuestion.setText(question.getQuestion());
        mADMAnswer1.setText(question.getChoiceList().get(0));
        mADMAnswer2.setText(question.getChoiceList().get(1));
        mADMAnswer3.setText(question.getChoiceList().get(2));
        mADMAnswer4.setText(question.getChoiceList().get(3));
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

        Question question4 = new Question("Comment s'appelle le passage maritime entre l'Amérique du Sud et l'Antartique ?",
                Arrays.asList("Passage de Cook", "Passage de De Gama","Passage de Magellan", "Passage de Drake"),
                3);

        Question question5 = new Question("Quelles sont les 2 mers qui se rejoignent au canal de Suez ?",
                Arrays.asList("Mers Noire & Caspienne", "Mers Méditerranée & Noire", "Mers Caspienne & Rouge", "Mers Méditerranée & Rouge"),
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

        Question question21 = new Question("Comment s'appelle la capitale de l'État de l'Iowa aux États-Unis ?",
                Arrays.asList("Des Prêtres", "Des Moines", "Des Nonnes", "Des Curés"),
                1);

        Question question22 = new Question("Quel est le plus grand état des États-Unis ?",
                Arrays.asList("Floride", "Californie", "Texas", "Alaska"),
                3);

        Question question23 = new Question("Quel état des États-Unis a pour capitale Raleigh ?",
                Arrays.asList("Caroline du Nord", "Caroline du Sud","Virginie", "Géorgie"),
                0);

        Question question24 = new Question("Quel est la capitale du Texas aux États-Unis ?",
                Arrays.asList("Austin", "Houston","San Antonio", "Dallas"),
                0);

        Question question25 = new Question("Quel est la capitale de la Californie aux États-Unis ?",
                Arrays.asList("Los Angeles", "Sacramento", "San Diego", "San Francisco"),
                1);

        Question question26 = new Question("Quel est l'état le plus peuplé des États Unis ?",
                Arrays.asList("Texas", "Californie", "New York", "Floride"),
                1);

        Question question27 = new Question("Quel est la capitale de l'état de Washington aux États-Unis ?",
                Arrays.asList("Washington", "Baltimore", "Seattle", "Olympia"),
                3);

        Question question28 = new Question("Dans quel état des États-Unis se touve la Vallée de la Mort ?",
                Arrays.asList("Utah", "Arizona", "Californie", "Nevada"),
                2);

        Question question29 = new Question("La ville de Chicago se situe au bord de quel grand lac ?",
                Arrays.asList("Le lac Supérieur", "Le lac Michigan", "Le lac Huron", "Le lac Erié"),
                1);

        Question question30 = new Question("Quelle est la capitale de l'état de Floride aux États-Unis ?",
                Arrays.asList("Jacksonville", "Miami", "Orlando", "Tallahassee"),
                3);

        Question question31 = new Question("Combien y a t-il d'états aux États-Unis ?",
                Arrays.asList("48", "49", "50", "51"),
                2);

        Question question32 = new Question("Quel est la capitale de l'état de New York aux États-Unis ?",
                Arrays.asList("New York", "Syracuse", "Albany", "Buffalo"),
                2);

        Question question33 = new Question("Dans quel état se trouve le Mont Rushmore aux États-Unis ?",
                Arrays.asList("Wyoming", "Dakota du Sud", "Nebraska", "Dakota du Nord"),
                1);

        Question question34 = new Question("Quelle est la capitale des États-Unis ?",
                Arrays.asList("Los Angeles", "Washington", "Miami", "New York"),
                1);

        Question question35 = new Question("Dans quel état se trouve la partie américaine des chutes du Niagara ?",
                Arrays.asList("Michigan", "Wisconsin", "Illinois", "New York"),
                3);

        Question question36 = new Question("Quelle ville américaine est familièrement appelée 'Windy City' ?",
                Arrays.asList("Cincinnati", "Chicago", "Seattle", "Saint-Louis"),
                1);

        Question question37 = new Question("Quelle ville américaine se trouve au bord d'un grand lac salé ?",
                Arrays.asList("Las Vegas", "Salt Lake City", "New York", "Los Angeles"),
                1);

        Question question38 = new Question("Dans quel état des États-Unis se situe le Grand Canyon ?",
                Arrays.asList("Californie", "Utah", "Nouveau-Mexique", "Arizona"),
                3);

        Question question39 = new Question("Dans quel état des États-Unis prend source le Mississippi ?",
                Arrays.asList("Minnesota", "Wisconsin", "Mississippi", "Missouri"),
                0);

        Question question40 = new Question("Dans quelle vile se trouve le quartier de Manhattan ?",
                Arrays.asList("Los Angeles", "Miami ", "New York", "Chicago"),
                2);

        Question question41 = new Question("Quelle est la capitale de la France ?",
                Arrays.asList("Berlin", "Paris", "Rome", "Madrid"),
                1);

        Question question42 = new Question("Quelle est la ville la plus peuplée de France après Paris ?",
                Arrays.asList("Lyon", "Toulouse","Marseille", "Lille"),
                2);

        Question question43 = new Question("Quel est le plus long fleuve Français ?",
                Arrays.asList("La Loire", "La Seine","La Rhône", "La Garonne"),
                0);

        Question question44 = new Question("Quel est le chef-lieu de la Corrèze?",
                Arrays.asList("Poitiers", "Limoges","Guéret", "Tulle"),
                3);

        Question question45 = new Question("Quel fleuve traverse Bordeaux ?",
                Arrays.asList("Gironde", "Dordogne","Tarn", "Garonne"),
                3);

        Question question46 = new Question("Quel département porte le numéro 45 ?",
                Arrays.asList("Loir-et-Cher", "Loiret","Lot", "Loire-atlantique"),
                1);

        Question question47 = new Question("Quel est le chef-lieu de l'Orne ?",
                Arrays.asList("Caen", "Alençon","Évreux", "Laval"),
                1);

        Question question48 = new Question("Quel est le numéro du département de l'Eure ?",
                Arrays.asList("78", "27","29", "54"),
                1);

        Question question49 = new Question("Quel est le numéro du département de la Guadeloupe ?",
                Arrays.asList("978", "972","971", "975"),
                2);

        Question question50 = new Question("Quel est le nom du département 72  ?",
                Arrays.asList("Corrèze", "Seine-Maritime","Sarthe", "Savoie"),
                2);

        Question question51 = new Question("Quel est le numéro du département de la Moselle ?",
                Arrays.asList("57", "88","54", "52"),
                0);

        Question question52 = new Question("Combien y a t-il de départements en France ?",
                Arrays.asList("97", "100","101", "103"),
                2);

        Question question53 = new Question("Quel est le nom du département 60 ?",
                Arrays.asList("Oise", "Moselle","Orne", "Nord"),
                0);

        Question question54 = new Question("Quel est le nom du département 38 ?",
                Arrays.asList("Loire", "Isére","Indre", "Indre-et-Loire"),
                1);

        Question question55 = new Question("Quel est le nom du département 24 ?",
                Arrays.asList("Creuse", "Dordogne","Doubs", "Drôme"),
                1);

        Question question56 = new Question("Quel est le chef-lieu du Tarn ?",
                Arrays.asList("Albi", "Montauban","Toulouse", "Agen"),
                0);

        Question question57 = new Question("Quel est le chef-lieu de la Marne ?",
                Arrays.asList("Reims", "Châlons-en-Champagne","Troyes", "Bar-le-Duc"),
                1);

        Question question58 = new Question("Quel est le nom du département 10 ?",
                Arrays.asList("Ariège", "Aude","Aube", "Aveyron"),
                2);

        Question question59 = new Question("Quel est le chef-lieu de la Charente ?",
                Arrays.asList("La Rochelle", "Cognac", "Royan", "Angoulême"),
                3);

        Question question60 = new Question("Quel fleuve traverse Tours ?",
                Arrays.asList("Touraine", "Loiret","Loire", "Maine"),
                2);


        return new QuestionBank(Arrays.asList(
                question1,
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
                question20,
                question21,
                question22,
                question23,
                question24,
                question25,
                question26,
                question27,
                question28,
                question29,
                question30,
                question31,
                question32,
                question33,
                question34,
                question35,
                question36,
                question37,
                question38,
                question39,
                question40,
                question41,
                question42,
                question43,
                question44,
                question45,
                question46,
                question47,
                question48,
                question49,
                question50,
                question51,
                question52,
                question53,
                question54,
                question55,
                question56,
                question57,
                question58,
                question59,
                question60
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

