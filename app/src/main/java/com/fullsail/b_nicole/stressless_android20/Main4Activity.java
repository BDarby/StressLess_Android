package com.fullsail.b_nicole.stressless_android20;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class Main4Activity extends AppCompatActivity {

    private static final String TAG = "Main4Activity";
    HomeListAdapter homeListAdapter;
    ListView listView;

    ArrayList<MediaObject> sounds = new ArrayList<>();
    ArrayList<MediaObject> animations = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sounds:
                    Log.e("TAG", "onNavigationItemSelected: 1" );
                    homeListAdapter.setSource(sounds);
                    homeListAdapter.notifyDataSetChanged();

                    return true;
                case R.id.navigation_animations:
                    Log.e("TAG", "onNavigationItemSelected: 2" );
                    homeListAdapter.setSource(animations);
                    homeListAdapter.notifyDataSetChanged();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

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

//        for (String s : animationNames){
//            animations.add(new MediaObject(s));
//        }

        homeListAdapter = new HomeListAdapter(this);
        homeListAdapter.setSource(sounds);

        listView = (ListView) findViewById(R.id.home_list_view);
        listView.setAdapter(homeListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemClick:");
                MediaObject mediaObject = (MediaObject) view.getTag();
                Intent intent = new Intent(Main4Activity.this, Main5Activity.class);
                intent.putExtra("MEDIA_OBJECT", mediaObject);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main4_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

}
