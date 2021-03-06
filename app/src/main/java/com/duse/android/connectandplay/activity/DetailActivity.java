package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.fragment.DetailFragment;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class DetailActivity extends AppCompatActivity {
    /**
     * adds the detail fragment to the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {

            //add fragment to activity
            DetailFragment fragment = new DetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, fragment)
                    .commit();

        }
    }
}
