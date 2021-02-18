package com.example.jeogeum;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyWritingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "ShowMyList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_writing);

        Button close_btn = (Button) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 사용자 이메일 확인
        String id = getIntent().getStringExtra("email");
        // 특정 id 속 post 출력
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // 배열은 사이즈가 고정되어 있으므로 ArrayList로 받고 배열로 변환
                            ArrayList<String> array1 = new ArrayList();
                            ArrayList<String> array2 = new ArrayList();
                            // 데이터 값 ArrayList에서 받기
                            // 들어갈 데이터가 글감, 내용 이므로 이렇게 만들어봄.   알고리즘 열심히 공부합시다. 난 몰라
                            String[][] myDataset;
                            String[] check = {"word", "text"};
                            int num = task.getResult().size();
                            for(int i=0;i<2;i++) {
                                int j = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (i == 0) {
                                        array1.add(document.getData().get(check[i]).toString());
                                        Log.d(TAG,"Passss  : "+array1.get(j++));
                                    }
                                    else {
                                        array2.add(document.getData().get(check[i]).toString());
                                    }
                                }
                            }
                            // 배열 List크기 만큼 선언 후 값 넣기
                            myDataset = new String[3][array1.size()];
                            int size = 0;
                            for (String temp : array1) {
                                myDataset[0][size++] = temp;
                            }
                            size = 0;
                            for (String temp : array2) {
                                myDataset[1][size++] = temp;
                            }
                            myWritingAdapter mAdapter = new myWritingAdapter(myDataset);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}