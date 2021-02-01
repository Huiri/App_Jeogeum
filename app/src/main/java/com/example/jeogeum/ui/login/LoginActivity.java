package com.example.jeogeum.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeogeum.MainActivity;
import com.example.jeogeum.R;
import com.example.jeogeum.ui.login.LoginViewModel;
import com.example.jeogeum.ui.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText etId, etPassword, etPasswordCheck;
    //ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // 입력 된 아이디, 비밀번호
        etId = (EditText)findViewById(R.id.etId);
        etPassword = (EditText)findViewById(R.id.etPassWord);
        etPasswordCheck = (EditText)findViewById(R.id.etPassWordCheck);
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
        // 회원 입력 정보 저장 버튼 ( Id, Password )
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stEmail = etId.getText().toString();
                String stPassword = etPassword.getText().toString();
                String stPasswordCheck = etPasswordCheck.getText().toString();

                // Email, Password 입력 여부 파악
                if(stEmail.isEmpty()) {
                    startToast("이메일을 입력해주세요.");
                }
                if(stPassword.isEmpty()) {
                    startToast("비밀번호를 입력해주세요.");
                }
                if(stPasswordCheck.isEmpty()) {
                    startToast("비밀번호 확인을 입력해주세요.");
                    return;
                }
                if(!(stPassword.equals(stPasswordCheck))) {
                    startToast("비밀번호가 일치하지 않습니다.");
                    return;
                }
                else {
                    //progress.setVisibility(View.VISIBLE);
                    // 회원가입 기능 구현
                    mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // progress.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        // 성공
                                        Log.d(TAG, "createUserWithEmail:success");
                                        startToast("회원가입 성공");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                    } else {
                                        // 실패
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        startToast("회원가입 실패");
                                    }
                                    // ...
                                }
                            });
                }
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
}
