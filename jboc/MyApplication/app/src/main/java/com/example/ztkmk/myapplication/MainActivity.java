package com.example.ztkmk.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ztkmk.myapplication.activities.AlbumActivity;

public class MainActivity extends AppCompatActivity{

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Click Screen to Load RxTestActivity
        constraintLayout = (ConstraintLayout) findViewById(R.id.mainlayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity(AlbumActivity.class);
            }
        });
    }

    public void StartActivity(Class activityClass) {

        try {

            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        } catch (Exception e){

            e.printStackTrace();
        }
    }
}
