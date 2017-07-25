package com.fullsail.b_nicole.stressless_android20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText editEmail;
    EditText editPassword;
    Button notAMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        notAMember = (Button) findViewById(R.id.notAMember);

        notAMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            Log.e(TAG, "onCreate: mAuth.getCurrentUser() != null " );
//            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
//            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            Log.e(TAG, "onCreate: mAuth.getCurrentUser() != null " );
            this.finish();
        }
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Login Account Error");
        alertDialog.setMessage("Please correct your credentials and try again...");
        alertDialog.setNeutralButton("Ok", null);

        String email = String.valueOf(editEmail.getText());
        String pwd = String.valueOf(editPassword.getText());

        if (!email.isEmpty() && !pwd.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        SignInActivity.this.finish();
//                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
//                        startActivity(intent);
                    }else {
                        alertDialog.setMessage("The email and/or password is incorrect...");
                        alertDialog.show();
                    }
                }
            });
        }else{
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
