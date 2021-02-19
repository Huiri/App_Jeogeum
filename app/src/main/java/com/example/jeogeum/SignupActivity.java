package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeogeum.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseDatabase database;
    EditText etId, etPassword, etPasswordCheck, etNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // DB변수 선언
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

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
                db.collection("user").whereEqualTo("nickname", stNickname)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            int temp=0;
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                    String value = ds.get("email").toString();
                                    String[] result = value.split("\n");
                                    temp = result[0].length();
                                    Log.d(TAG, ""+result[0]);
                                }
                                user_input(stEmail, stPassword, stPasswordCheck, stNickname);
                            }
                            private void user_input(String stEmail,String stPassword, String stPasswordCheck, String stNickname) {
                                    if (stEmail.isEmpty()) {
                                        startToast("이메일을 입력해주세요.");
                                        return;
                                    }
                                    if (stPassword.isEmpty()) {
                                        startToast("비밀번호를 입력해주세요.");
                                        return;
                                    }
                                    if (stPassword.length() < 6) {
                                        startToast("비밀번호를 6자 이상 입력해주세요.");
                                        return;
                                    }
                                    if (stPasswordCheck.isEmpty()) {
                                        startToast("비밀번호 확인을 입력해주세요.");
                                        return;
                                    }
                                    if (!(stPassword.equals(stPasswordCheck))) {
                                        startToast("비밀번호가 일치하지 않습니다.");
                                        return;
                                    }
                                    if (stNickname.isEmpty()) {
                                        startToast("이름을 입력해주세요.");
                                        return;
                                    }
                                    if (temp != 0) {
                                        startToast("사용중인 저자명입니다.");
                                        return;
                                    }
                                    else {
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

                                                            // User DB속 Document 생성
                                                            Map<String, Object> user = new HashMap<>();
                                                            user.put("email", stEmail);
                                                            user.put("nickname", stNickname);

                                                            db.collection("user").document(stEmail)
                                                                    .set(user)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d(TAG, "문서 생성 성공");
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w(TAG, "문서 생성 에러", e);
                                                                        }
                                                                    });
                                                            // 다음 화면으로 전환
                                                            Intent in = new Intent(SignupActivity.this, LoginActivity.class);
                                                            in.putExtra("email", stEmail);
                                                            startActivity(in);
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
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
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