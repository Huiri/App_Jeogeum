package com.example.jeogeum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class YourWritingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "ShowMyList";
    private boolean Lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_writing);

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

        // post 중 Lock 안 걸린 전체 출력
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                //.whereEqualTo("lock", Lock)  아직 디비 완성 안됨.
                .whereEqualTo("nickname", "dogdd")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // 배열은 사이즈가 고정되어 있으므로 ArrayList로 받고 배열로 변환
                            ArrayList<String> array1 = new ArrayList();
                            ArrayList<String> array2 = new ArrayList();
                            ArrayList<String> array3 = new ArrayList();
                            // 데이터 값 ArrayList에서 받기
                            // 들어갈 데이터가 글감
                            String[] check = {"word", "nickname", "text"};
                            int num = task.getResult().size();
                            for(int i=0;i<3;i++) {
                                int j = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (i == 0) {
                                        array1.add(document.getData().get(check[i]).toString());
                                    }
                                    else if(i == 1) {
                                        array2.add(document.getData().get(check[i]).toString());
                                    }
                                    else {
                                        array3.add(document.getData().get(check[i]).toString());
                                    }
                                }
                            }
                            // 배열 List크기 만큼 선언 후 값 넣기
                            String[][] myDataset = new String[4][array1.size()];
                            int size = 0;
                            for (String temp : array1) {
                                myDataset[0][size++] = temp;
                            }
                            size = 0;
                            for (String temp : array2) {
                                myDataset[1][size++] = temp;
                            }
                            size = 0;
                            for (String temp : array3) {
                                myDataset[2][size++] = temp;
                            }

                            myDataset[3][0] = "false";
                            myWritingAdapter mAdapter = new myWritingAdapter(myDataset);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        String email = getIntent().getStringExtra("email");
        // 이전 액티비티로 가려고 한다면 intent가 아닌 finish로 되돌아가기 -> intent많이 안 쌓임
        String previous = getIntent().getStringExtra("previous");
        String now = "your";
        if (id == R.id.write) {
            if("main".equals(previous)) {
                finish();
            }
            else {
                Intent intent = new Intent(YourWritingActivity.this, Main_WriteContent.class);
                intent.putExtra("email", email);
                intent.putExtra("previous", now);
                startActivity(intent);
            }
        } else if (id == R.id.my) {
            if("my".equals(previous)) {
                finish();
            }
            else {
                Intent intent = new Intent(YourWritingActivity.this, MyWritingActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("previous", now);
                startActivity(intent);
            }
        } else if (id == R.id.your) {
            Toast.makeText(this, "현재 페이지입니다.", Toast.LENGTH_LONG).show();
        } else if (id == R.id.words) {
            Toast.makeText(this, "네번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(Main_WriteContent.this, ShowWordList.class);
            //startActivity(intent);
        } else if (id == R.id.logout) {
            Toast.makeText(this, "알림 off 선택됨.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.setting) {
            Toast.makeText(this, "설정으로 이동.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(Main_WriteContent.this, ShowWordList.class);
            //startActivity(intent);
        }
        return true;
    }
}