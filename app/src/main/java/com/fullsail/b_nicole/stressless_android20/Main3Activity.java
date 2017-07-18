package com.fullsail.b_nicole.stressless_android20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = "Main3Activity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView emailTextView;

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
