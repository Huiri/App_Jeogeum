package com.example.jeogeum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class SearchText extends AppCompatActivity {

    private static final String TAG = "SearchText";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter Adapter;

    ArrayList<String> searchText = new ArrayList<>();
    ArrayList<String> nickname = new ArrayList<>();



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_text);

        Intent intent = getIntent();
        String instring = intent.getStringExtra("searchdata");

        TextView searchcontent = findViewById(R.id.searchcontent);

        recyclerView = findViewById(R.id.search_list);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Button close_btn = (Button) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db.collection("post").whereEqualTo("word", instring).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchText.add(document.get("text").toString());
                                nickname.add(document.get("nick").toString());
                            }

                            if(searchText.size()==0){
                                searchcontent.setText("해당 글감이 존재하지 않습니다.");
                            }
                            else {
                                searchcontent.setText("검색 결과 : " + instring);
                            }
                            Adapter = new searchtextadaper(searchText, instring, nickname);
                            Adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(Adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

}