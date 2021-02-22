package com.example.jeogeum;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchShowText extends AppCompatActivity {

    TextView show;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_text);
        Intent intent = getIntent();

        String text = intent.getExtras().getString("searchText");

        Button close_btn = findViewById(R.id.close_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        show.setMovementMethod(new ScrollingMovementMethod());
        show = findViewById(R.id.show);
        show.setText(text);
    }

}
