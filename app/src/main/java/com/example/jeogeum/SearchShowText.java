package com.example.jeogeum;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchShowText extends AppCompatActivity  {

    TextView show;
    TextView show2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchtextshow);
        Intent intent = getIntent();

        String text = intent.getExtras().getString("searchText");
        String title = intent.getExtras().getString("instring");

        Button close_btn = (Button) findViewById(R.id.close_btn);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //show.setMovementMethod(new ScrollingMovementMethod());
        show = findViewById(R.id.showtext);
        show2 = findViewById(R.id.title);
        show.setText(text);
        show2.setText(title);
    }


}
