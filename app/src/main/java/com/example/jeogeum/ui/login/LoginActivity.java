package com.example.jeogeum.ui.login;


import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeogeum.MainActivity;
import com.example.jeogeum.R;
import com.example.jeogeum.SignupActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText etId, etPassword;

    FirebaseFirestore db;
    FirebaseDatabase database;


    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        String date = "20210131";

        String addDay = null;
        try {
            addDay = AddDate(date, 0, 0, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(addDay);




        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // 입력 된 아이디, 비밀번호
        etId = (EditText)findViewById(R.id.etId);
        etPassword = (EditText)findViewById(R.id.etPassWord);
        // 로딩
        //progress = (ProgressBar)findViewById(R.id.progress);
        // 로그인 버튼
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stEmail = etId.getText().toString();
                String stPassword = etPassword.getText().toString();
                // Email, Password 입력 여부 파악
                if(stEmail.isEmpty()) {
                    startToast("이메일을 입력해주세요.");
                    return;
                }
                if(stPassword.isEmpty()) {
                    startToast("비밀번호를 입력해주세요.");
                    return;
                }
                // progress.setVisibility(View.VISIBLE);
                // 로그인 기능 구현
                mAuth.signInWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //progress.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    // 성공
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Email, name 확인
                                    String stUserEmail = user.getEmail();
                                    String stUserName = user.getDisplayName();
                                    Log.d(TAG, "stUSerEmail: " + stUserEmail + ", stUserName: " + stUserName);


                                    db.collection("user").whereEqualTo("nickname", "dogdd")
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    String temp = null;
                                                    int i=0;
                                                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                                        String value = ds.get("email").toString();
                                                        String[] result = value.split("\n");
                                                        temp = result[i];
                                                    }
                                                    Log.d(TAG, " aasd "+temp);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });



                                    // 다음 화면으로 전환
                                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                    in.putExtra("email", stEmail);
                                    startActivity(in);
                                } else {
                                    // 실패
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    startToast("로그인 실패");
                                }
                            }
                        });

            }
        });
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private String AddDate(String strDate, int year, int month, int day) throws ParseException {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date dt = dtFormat.parse(strDate);
        cal.setTime(dt); cal.add(Calendar.YEAR, year)
        ; cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);
        return dtFormat.format(cal.getTime());
    }
}
