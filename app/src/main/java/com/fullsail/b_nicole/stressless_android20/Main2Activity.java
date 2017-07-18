package com.fullsail.b_nicole.stressless_android20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText signUpEmail;
    EditText signUpPassword;
    EditText retypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();

        signUpEmail = (EditText) findViewById(R.id.email_sigh_up);
        signUpPassword = (EditText) findViewById(R.id.password_sign_up);
        retypePassword = (EditText) findViewById(R.id.retype_password);

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_2menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String email = String .valueOf(signUpEmail.getText());
        String pwd = String.valueOf(signUpPassword.getText());

        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(intent);
            }
        });

        return super.onOptionsItemSelected(item);
    }
}
