package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Showtext extends AppCompatActivity {

    FirebaseFirestore db;
    TextView show;

    private static final String TAG = "ShowText_complete";

    public static final String NICK_KEY = "nick";
    public static final String Text_KEY = "text";
    public static final String ID_KEY = "id";
    private final DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("post").document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtext);
        Intent intent = getIntent();
        String text = intent.getExtras().getString("write_text");

        db = FirebaseFirestore.getInstance();

        Button close_btn = findViewById(R.id.close_btn);
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