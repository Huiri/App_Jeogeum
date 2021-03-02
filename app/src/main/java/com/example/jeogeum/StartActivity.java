package com.example.jeogeum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ViewPager viewPager = findViewById(R.id.pager);

        MypageAdapter adapter = new MypageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
    }

}