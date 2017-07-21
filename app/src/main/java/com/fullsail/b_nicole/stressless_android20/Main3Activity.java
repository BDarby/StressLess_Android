package com.fullsail.b_nicole.stressless_android20;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = "Main3Activity";
    private final int REQ_CODE_SPEECH_OUTPUT = 123;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    TextView emailTextView;
    Button speakBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            finish();
        }

        String profileEmail = mAuth.getCurrentUser().getEmail();
        emailTextView = (TextView) findViewById(R.id.email_profile);
        emailTextView.setText(profileEmail);

        speakBtn = (Button) findViewById(R.id.speak_button);

        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMic();
            }
        });

    }


    private void openMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is your request?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException tim){

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_OUTPUT:{
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> voiceIntent = data.getStringArrayListExtra(RecognizerIntent.EXTRA_LANGUAGE);
                   //this is where I think I make the voice command do something

                }
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main3_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mAuth.signOut();
        finish();

        return super.onOptionsItemSelected(item);
    }
}
