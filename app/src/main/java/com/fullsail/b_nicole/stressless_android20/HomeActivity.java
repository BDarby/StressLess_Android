package com.fullsail.b_nicole.stressless_android20;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    MediaListAdapter mediaListAdapter;
    ListView listView;

    ArrayList<MediaObject> sounds = new ArrayList<>();
    ArrayList<MediaObject> animations = new ArrayList<>();

    ImageView featuredImage;
    ImageButton playFeaturedButton;
    MediaObject featuredAudioMediaObject;
    MediaObject featuredVideoMediaObject;
    int randomFeaturedAudioIndex = 0;
    int randomFeaturedVideoIndex = 0;

    int listMode = 0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            randomSetUp();

            switch (item.getItemId()) {
                case R.id.navigation_sounds:
                    Log.e("TAG", "onNavigationItemSelected: 1" );
                    mediaListAdapter.setSource(sounds);
                    mediaListAdapter.notifyDataSetChanged();
                    featuredImage.setImageResource(featuredAudioMediaObject.getImageResource());
                    listMode = 0;
                    return true;

                case R.id.navigation_animations:
                    Log.e("TAG", "onNavigationItemSelected: 2" );
                    mediaListAdapter.setSource(animations);
                    mediaListAdapter.notifyDataSetChanged();
                    featuredImage.setImageResource(featuredVideoMediaObject.getImageResource());
                    listMode = 1;
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        featuredImage = (ImageView) findViewById(R.id.featured_image_view);
        playFeaturedButton = (ImageButton) findViewById(R.id.play_featured_button);
        playFeaturedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listMode == 0){
                    Intent intent = new Intent(HomeActivity.this, AudioPlayerActivity.class);
                    intent.putExtra("MEDIA_OBJECT", featuredAudioMediaObject);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("MEDIA_OBJECT", featuredVideoMediaObject);
                    startActivity(intent);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String[] soundsNames = getResources().getStringArray(R.array.sounds);
        String[] animationNames = getResources().getStringArray(R.array.animations);

        for (String s : soundsNames){
            Resources res = getResources();
            int rawId = res.getIdentifier(s.toLowerCase(), "raw", getPackageName());
            int imageId = res.getIdentifier(s.toLowerCase(), "drawable", getPackageName());
            MediaObject mediaObject = new MediaObject(s, rawId, imageId);
            sounds.add(mediaObject);
        }

        for (String s : animationNames){
            Resources res = getResources();
            int rawId = res.getIdentifier(s.toLowerCase(), "raw", getPackageName());
            int imageId = res.getIdentifier(s.toLowerCase(), "drawable", getPackageName());
            MediaObject mediaObject = new MediaObject(s, rawId, imageId);
            animations.add(mediaObject);
        }

        mediaListAdapter = new MediaListAdapter(this);
        mediaListAdapter.setSource(sounds);

        listView = (ListView) findViewById(R.id.home_list_view);
        listView.setAdapter(mediaListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MediaObject mediaObject = (MediaObject) view.getTag();

                if (listMode == 0){
                    Intent intent = new Intent(HomeActivity.this, AudioPlayerActivity.class);
                    intent.putExtra("MEDIA_OBJECT", mediaObject);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("MEDIA_OBJECT", mediaObject);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        randomSetUp();
        if (listMode == 0) {
            featuredImage.setImageResource(featuredAudioMediaObject.getImageResource());
        }else{
            featuredImage.setImageResource(featuredVideoMediaObject.getImageResource());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void randomSetUp(){
        randomFeaturedAudioIndex = new Random().nextInt(sounds.size());
        featuredAudioMediaObject = sounds.get(randomFeaturedAudioIndex);

        randomFeaturedVideoIndex = new Random().nextInt(animations.size());
        featuredVideoMediaObject = animations.get(randomFeaturedVideoIndex);
    }

}
