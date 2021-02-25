package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geoquiz.controller.MainActivity;
import com.example.geoquiz.model.User;

import static com.example.geoquiz.controller.MainActivity.GAME_ACTIVITY_REQUEST_CODE;
import static com.example.geoquiz.controller.MainActivity.PREF_KEY_SCORE;

public class GetNameActivity extends AppCompatActivity {
    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;
    private SharedPreferences mPreferences;
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);
        mUser = new User();
        mPreferences = getPreferences(MODE_PRIVATE);
        mGreetingText= findViewById(R.id.activity_greeting_text_txt);
        mNameInput = findViewById(R.id.activity_get_name_input);
        mPlayButton = findViewById(R.id.activity_get_name_play_btn);
        mPlayButton.setEnabled(false);
        greetUser();

        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This is where we'll check the user input
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = mNameInput.getText().toString();
                mUser.setFirstName(firstname);
                mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstName()).apply();
                // The user just clicked
                Intent MainActivityIntent = new Intent(GetNameActivity.this, MainActivity.class);
                startActivity(MainActivityIntent);
                greetUser();


            }
        });
    }

    private void greetUser() {
        String firstname = mPreferences.getString(PREF_KEY_FIRSTNAME, null);

        if (null != firstname) {
            String fulltext = "Bon retour, " + firstname + " !";
            mGreetingText.setText(fulltext);
            mNameInput.setText(firstname);
            mNameInput.setSelection(firstname.length());
            mPlayButton.setEnabled(true);
        }
    }

}