package com.example.jeogeum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class YourDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_detail);

        Button close_btn = (Button) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String title = getIntent().getStringExtra("word");
        String content = getIntent().getStringExtra("text");

        TextView word = (TextView)findViewById(R.id.title);
        word.setText(title);
        TextView text= (TextView)findViewById(R.id.content);
        text.setText(content);

    }
}