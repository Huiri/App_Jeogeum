package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jeogeum.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PreSettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final String TAG = "PreSettingsActivity";
    private PreferenceScreen screen;
    private EditTextPreference change_nick;
    String email, nick;
    SwitchPreference auto;
    private Preference byebye, change_password;

    String shared = "file", login = "login";
    SharedPreferences sharedPreferences, login_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pre_settings);
        addPreferencesFromResource(R.xml.pref_settings);

        sharedPreferences = getSharedPreferences(shared, 0);
        login_info = getSharedPreferences(login, 0);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        nick = intent.getStringExtra("nick");

        screen = getPreferenceScreen();
        change_nick = (EditTextPreference) findPreference("change_nick");

        change_password = (Preference) findPreference("change_password");
        auto = (SwitchPreference) findPreference("auto");
        byebye = (Preference) findPreference("byebye");

        change_nick.setOnPreferenceChangeListener(this);
        //change_password.setOnPreferenceChangeListener(this);

        byebye.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                goodbye();
                //show_dialog(1);
                //Toast.makeText(PreSettingsActivity.this, "계정탈퇴 완료", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        change_password.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //show_dialog(2);
                change_pass();
                return false;
            }
        });
        //show_change();
        //find_nick();
        updateDefault();
        //practice();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateDefault();
    }
    public void practice(){
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Toast.makeText(PreSettingsActivity.this, document.getId().toString() + document.getData().toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.i(TAG, "onPreferenceChange: " + preference + ", newValue : " + newValue);

        String value = (String) newValue;
        if(preference == change_nick){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nick", value);
            editor.commit();
            change_nick();
            change_nick.setSummary(value);
        }
//        else if(preference == change_password){
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("password", value);
//            editor.commit();
//            change_password.setSummary(value);
//        }
        show_change();
        return true;
    }
    public void show_change(){
        String value = sharedPreferences.getString("change_nick", "");
        change_nick.setDefaultValue(value);
//        String pass = sharedPreferences.getString("change_password", "");
//        change_password.setDefaultValue(pass);
    }

    public void change_pass(){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(PreSettingsActivity.this, "비밀번호를 재설정할 수 있도록 계정 정보로 이메일을 전송하였습니다!", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PreSettingsActivity.this, "이메일 발송에 실패했습니다ㅠㅠ", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error deleting document", e);
                }
            });
        }

    public void change_nick(){
        String value = sharedPreferences.getString("change_nick", "");
        DocumentReference UserRef = db.collection("user").document(email);

        UserRef.update("nickname", value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

    public void show_nick(){

    }
    public void delete_id(){
        db.collection("user").document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }
    public void delete_text(){
        db.collection("post").document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
    public void goodbye(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
        //delete_id();
        delete_login_info();
    }
    public void delete_login_info(){
        SharedPreferences.Editor editor = login_info.edit();
        editor.putString("id", null);
        editor.putString("pass", null);
        editor.commit();
    }
    private void updateDefault(){
        String value = sharedPreferences.getString("nick", "");
        change_nick.setSummary(value);
    }

    public void show_dialog(int i){
        if(i == 1){
            AlertDialog.Builder msgBuilder = new AlertDialog.Builder(PreSettingsActivity.this) .setTitle("계정 탈퇴") .setMessage("계정을 삭제합니다")
                    .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PreSettingsActivity.this, "취소", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goodbye();
                            Toast.makeText(PreSettingsActivity.this, "계정을 삭제합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show();
        }
        else if(i == 2){
            AlertDialog.Builder msgBuilder = new AlertDialog.Builder(PreSettingsActivity.this) .setTitle("비밀번호 변경") .setMessage("비밀번호를 재설정할 수 있는 메일을 전송할까요?")
                    .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PreSettingsActivity.this, "취소", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            change_pass();
                            Toast.makeText(PreSettingsActivity.this, "이메일을 전송합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show();
        }

    }
}