package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private EditTextPreference change_nick, change_password;
    String email, password, nick;

    private Preference auto, byebye;

    String shared = "file";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pre_settings);
        addPreferencesFromResource(R.xml.pref_settings);

        sharedPreferences = getSharedPreferences(shared, 0);
        //updateDefault();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        nick = intent.getStringExtra("nick");

        screen = getPreferenceScreen();
        change_nick = (EditTextPreference) findPreference("change_nick");
        change_password = (EditTextPreference) findPreference("change_password");

        auto = (Preference) findPreference("auto");
        byebye = (Preference) findPreference("byebye");

        change_nick.setOnPreferenceChangeListener(this);
        change_password.setOnPreferenceChangeListener(this);
        //updateDefault();


        byebye.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //goodbye();
                Toast.makeText(PreSettingsActivity.this, "계정탈퇴 완료", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //show_change();
        //find_nick();
        updateDefault();

    }

    @Override
    public void onResume(){
        super.onResume();
        updateDefault();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.i(TAG, "onPreferenceChange: " + preference + ", newValue : " + newValue);

        String value = (String) newValue;
        if(preference == change_nick){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nick", value);
            editor.commit();
            change_nick.setSummary(value);
        }else if(preference == change_password){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", value);
            editor.commit();
            change_password.setSummary(value);
        }
        show_change();
        return true;
    }
    public void show_change(){
        String value = sharedPreferences.getString("change_nick", "");
        change_nick.setDefaultValue(value);
        String pass = sharedPreferences.getString("change_password", "");
        change_password.setDefaultValue(pass);
    }

    public void change_pass(){

    }
    public void change_nick(){

    }
    public void show_nick(){

    }
    private void updateDefault(){
        String value = sharedPreferences.getString("nick", "");
        change_nick.setSummary(value);
    }
}