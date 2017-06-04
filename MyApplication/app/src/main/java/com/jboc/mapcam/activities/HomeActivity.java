package com.jboc.mapcam.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.R;

/**
 * Created by Ztkmk on 2017-06-04.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.cast_mini_controller_gradient_light);

        MainActivity.SetButton(HomeActivity.this, R.id.home_button, R.id.album_button, R.id.map_button);
    }
}
