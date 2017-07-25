package com.fullsail.b_nicole.stressless_android20;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private final int REQ_CODE_SPEECH_OUTPUT = 123;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView emailTextView;
    Button speakBtn;

    MediaListAdapter mediaListAdapter;
    ListView listView;

    ArrayList<MediaObject> sounds = new ArrayList<>();
    ArrayList<MediaObject> animations = new ArrayList<>();
    int listMode = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_sounds:
                    Log.e("TAG", "onNavigationItemSelected: 1" );
                    mediaListAdapter.setSource(sounds);
                    mediaListAdapter.notifyDataSetChanged();

                    listMode = 0;
                    return true;

                case R.id.navigation_animations:
                    Log.e("TAG", "onNavigationItemSelected: 2" );
                    mediaListAdapter.setSource(animations);
                    mediaListAdapter.notifyDataSetChanged();

                    listMode = 1;
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            finish();
        }

        mediaListAdapter = new MediaListAdapter(ProfileActivity.this);
        listView = (ListView) findViewById(R.id.profile_list_view);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.profile_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference location = databaseReference.child("users").child(uid).child("favorites");

        location.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("audio") != null) {
                        Map<String, Object> audios = (Map<String, Object>) map.get("audio");
                        ArrayList<Object> soundsNames = new ArrayList<>(audios.values());

                        for (Object o : soundsNames) {
                            String s = o.toString();
                            Resources res = getResources();
                            int rawId = res.getIdentifier(s.toLowerCase(), "raw", getPackageName());
                            int imageId = res.getIdentifier(s.toLowerCase(), "drawable", getPackageName());
                            MediaObject mediaObject = new MediaObject(s, rawId, imageId);
                            sounds.add(mediaObject);
                        }
                    }

                    if(map.get("video") != null){
                        Map<String, Object> videos = (Map<String, Object>) map.get("video");
                        ArrayList<Object> animationNames = new ArrayList<>(videos.values());

                        for (Object o : animationNames) {
                            String s = o.toString();
                            Resources res = getResources();
                            int rawId = res.getIdentifier(s.toLowerCase(), "raw", getPackageName());
                            int imageId = res.getIdentifier(s.toLowerCase(), "drawable", getPackageName());
                            MediaObject mediaObject = new MediaObject(s, rawId, imageId);

                            animations.add(mediaObject);
                        }
                    }

                    mediaListAdapter.setSource(sounds);
                    listView.setAdapter(mediaListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String profileEmail = mAuth.getCurrentUser().getEmail();
        emailTextView = (TextView) findViewById(R.id.email_profile);
        emailTextView.setText(profileEmail);

        //speakBtn = (Button) findViewById(R.id.speak_button);

//        speakBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openMic();
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MediaObject mediaObject = (MediaObject) view.getTag();

                if (listMode == 0){
                    Intent intent = new Intent(ProfileActivity.this, AudioPlayerActivity.class);
                    intent.putExtra("MEDIA_OBJECT", mediaObject);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ProfileActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("MEDIA_OBJECT", mediaObject);
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);

                alert.setTitle("Removing item?");
                alert.setMessage("Are you sure you want to remove this item from your favorites?");
                alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

//                final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
//                progressDialog.setTitle("Removing...");
//                progressDialog.setMessage("This favorite item is being removed from your favorites...");
//                progressDialog.show();

                final MediaObject mediaObject = (MediaObject) view.getTag();
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                alert.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int ii) {

                        if (listMode == 0){
//                    progressDialog.setMessage("This audio item is being removed from your favorites...");
                            DatabaseReference location = databaseReference.child("users").child(uid).child("favorites").child("audio");
                            location.child(mediaObject.getSourceName()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    sounds.remove(i);
                                    mediaListAdapter.setSource(sounds);
                                    mediaListAdapter.notifyDataSetChanged();
                                    //progressDialog.hide();
                                }
                            });

                        }else{
                            //progressDialog.setMessage("This video item is being removed from your favorites...");
                            DatabaseReference location = databaseReference.child("users").child(uid).child("favorites").child("video");
                            location.child(mediaObject.getSourceName()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    animations.remove(i);
                                    mediaListAdapter.setSource(animations);
                                    mediaListAdapter.notifyDataSetChanged();
                                    //progressDialog.hide();
                                }
                            });
                        }

                    }
                });

                alert.show();

                return true;
            }
        });
    }


//    private void openMic(){
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is your request?");
//        try {
//            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
//        } catch (ActivityNotFoundException tim){
//
//        }
//
//
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode){
//            case REQ_CODE_SPEECH_OUTPUT:{
//                if(resultCode == RESULT_OK && null != data){
//                    ArrayList<String> voiceIntent = data.getStringArrayListExtra(RecognizerIntent.EXTRA_LANGUAGE);
//                   //this is where I think I make the voice command do something
//
//                }
//                break;
//            }
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.signout_button){
            performLogout();
        }

        if (id == R.id.delete_button){
            this.performDelete();
        }

        return super.onOptionsItemSelected(item);
    }

    public void performLogout(){
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setTitle("Logout Warning!");
        alert.setMessage("Are you sure you want to logout from your account?");

        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int ii) {
                mAuth.signOut();
                ProfileActivity.this.finish();
            }
        });

        alert.show();
    }

    public void performDelete(){

        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setTitle("Warning Deleting Account!");
        alert.setMessage("Are you sure you want to delete you account?\nThis action can't be undone...");
        final EditText pwdEditText = new EditText(this);
        pwdEditText.setHint("Enter your password");
        pwdEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        alert.setView(pwdEditText);

        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int ii) {
                Log.e(TAG, "onClick: ");

                String email = mAuth.getCurrentUser().getEmail();
                String pwd = String.valueOf(pwdEditText.getText());

                if (pwd.isEmpty()){
                    Toast.makeText(ProfileActivity.this, "You must enter your password! Try again...", Toast.LENGTH_SHORT).show();
                }else{
                    AuthCredential authCredential = EmailAuthProvider.getCredential(email, pwd);
                    performDelete2(authCredential);
                }
            }
        });

        alert.show();
    }

    public void performDelete2(final AuthCredential credential){
        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    performDelete3();
                }else{
                    String errorMessage = task.getException().getLocalizedMessage();
                    Log.e(TAG, "onComplete: " + errorMessage );
                    Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void performDelete3(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(uid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.e(TAG, "onComplete: ");

                if(databaseError == null){
                    Log.e(TAG, "databaseError == null: " );
                    performDelete4();
                }else{
                    Log.e(TAG, "databaseError == null else: " + databaseError.getMessage() );
                }
            }
        });
    }
    public void performDelete4(){
        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "task.isSuccessful:");
                    ProfileActivity.this.finish();
                }
            }
        });
    }

}
