package com.guruofficeproject.guruoffice.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.guruofficeproject.guruoffice.R;
import com.guruofficeproject.guruoffice.constants.Constants;
import com.guruofficeproject.guruoffice.fragments.FavoriteFragment;
import com.guruofficeproject.guruoffice.fragments.SearchMovieFragment;
import com.guruofficeproject.guruoffice.realm.RealmManager;


public class MainActivity extends AppCompatActivity {

    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    SharedPreferences sharedPreferences;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId())  {
                case R.id.fragment_1:
                    fragmentTransaction.replace(R.id.frame_container,new SearchMovieFragment(getApplicationContext()))
                            .commit();

                    break;
                case R.id.fragment_2:
                    fragmentTransaction.replace(R.id.frame_container, new FavoriteFragment(getApplicationContext()))
                            .commit();
                    break;
            }
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveSearchLineActivity();
        RealmManager.getInstance().init(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frame_container,new SearchMovieFragment(getApplicationContext()));
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    public void saveSearchLineActivity() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Constants.SHARED_PREFERENCES_SEARCH,"");
        edit.apply();
    }
}
