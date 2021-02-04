package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeogeum.ui.login.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    EditText etId, etPassword, etPasswordCheck, etNickname;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        // 입력 된 아이디, 비밀번호
        etId = (EditText)findViewById(R.id.etId);
        etPassword = (EditText)findViewById(R.id.etPassWord);
        etPasswordCheck = (EditText)findViewById(R.id.etPassWordCheck);
        etNickname = (EditText)findViewById(R.id.etNickname);

        // 회원 입력 정보 저장 버튼 ( Id, Password )
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stEmail = etId.getText().toString();
                String stPassword = etPassword.getText().toString();
                String stPasswordCheck = etPasswordCheck.getText().toString();
                String stNickname = etNickname.getText().toString();

                // Email, Password 입력 여부 파악
                if(stNickname.isEmpty()) {
                    startToast("이름을 입력해주세요.");
                    return;
                }
                if(stEmail.isEmpty()) {
                    startToast("이메일을 입력해주세요.");
                    return;
                }
                if(stPassword.isEmpty()) {
                    startToast("비밀번호를 입력해주세요.");
                    return;
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
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
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