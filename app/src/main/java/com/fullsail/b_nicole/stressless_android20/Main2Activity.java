package com.fullsail.b_nicole.stressless_android20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Test comment

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
        getMenuInflater().inflate(R.menu.main2_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Create Account Error");
        alertDialog.setMessage("Please correct your credentials and try again...");
        alertDialog.setNeutralButton("Ok", null);
        String email = String .valueOf(signUpEmail.getText());
        String pwd = String.valueOf(signUpPassword.getText());

        if (!email.isEmpty() && !pwd.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
                        startActivity(intent);
                    }else{
                        alertDialog.setMessage(task.getException().getLocalizedMessage() + " Please correct your credentials and try again...");
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
