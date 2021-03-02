package com.example.jeogeum.ui.login;


import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeogeum.Main_WriteContent;
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
    private boolean saveLoginData;
    EditText etId, etPassword;
    CheckBox check;
    SharedPreferences.Editor editor;

    FirebaseFirestore db;
    FirebaseDatabase database;

    SharedPreferences login_info;
    private String login = "login", id, pass;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // 입력 된 아이디, 비밀번호
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassWord);
        check = (CheckBox)findViewById(R.id.check);

        login_info = getSharedPreferences(login, 0);
        load();
        editor= login_info.edit();




        // 로딩
        //progress = (ProgressBar)findViewById(R.id.progress);
        // 로그인 버튼
        Button btnLogin = findViewById(R.id.btnLogin);
        if (saveLoginData) {
            etId.setText(id);
            etPassword.setText(pass);
            check.setChecked(saveLoginData);
        }
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
                                                    save();
                                                    // 다음 화면으로 전환
                                                    Intent in = new Intent(LoginActivity.this, Main_WriteContent.class);
                                                    in.putExtra("email", stEmail);
                                                    startActivity(in);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG,"Error : "+task.getException());
                                                }
                                            });

                                } else {
                                    // 실패
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    startToast("로그인 실패");
                                }
                            }
                        });

            }
        });
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
            }
        });
//
//        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(isChecked){
//                    String ID = etId.getText().toString();
//                    String PW = etPassword.getText().toString();
//
//                    editor.putString("id", ID);
//                    editor.putString("pass", PW);
//                    editor.putBoolean("Auto_Login_enabled", true);
//                    editor.commit();
//                }else{
////			editor.remove("ID");
////			editor.remove("PW");
////			editor.remove("Auto_Login_enabled");
//                    editor.clear();
//                    editor.commit();
//                }
//            }
//        });
//        if(login_info.getBoolean("Auto_Login_enabled", false)){
//
//            etId.setText(login_info.getString("ID", ""));
//
//            etId.setText(login_info.getString("PW", ""));
//
//            check.setChecked(true);
//
//        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void startToast(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    public Boolean checkcheck(){
        check = findViewById(R.id.checkBox);

        return check.isChecked();
    }
    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = login_info.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", check.isChecked());
        editor.putString("id", etId.getText().toString().trim());
        editor.putString("pass", etPassword.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = login_info.getBoolean("SAVE_LOGIN_DATA", false);
        id = login_info.getString("id", "");
        pass = login_info.getString("pass", "");
    }


}
