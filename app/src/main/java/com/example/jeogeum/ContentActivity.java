package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {

    public static final String NICK_KEY = "nick";
    public static final String Text_KEY = "text";
    public static final String Lock_KEY = "lock";
    public static final String Id_KEY = "id";
    public static final String Date_KEY = "date";
    private static final String TAG = "ContentActivity";

    EditText write_text;
    EditText write_nick;
    CheckBox checkBox;

    Button main_complete_btn;
    SimpleDateFormat date;

    String id;
    private final DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/wer");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        id = getIntent().getStringExtra("email");
        //finish();
        main_complete_btn = findViewById(R.id.main_complete_btn);
        main_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText(v);
            }


            public Boolean checkcheckbox(){
                checkBox = (CheckBox)findViewById(R.id.checkBox);

                if(checkBox.isChecked()){
                    return true;
                }
                else{
                    return false;
                }
            }



//            public void fetchText() {
//                mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            String text = documentSnapshot.getString(Text_KEY);
//                            String nick = documentSnapshot.getString(NICK_KEY);
//
//                            contentsview.setText("\"" + text + "\" -- " + nick);
//
//                            //한번에 꺼내기
//                            //Map<String, Object> myData = documentSnapshot.getData();
//                        }
//                    }
//                });
//            }

            public void saveText(View view) {
                write_text = (EditText) findViewById(R.id.write_text);
                write_nick = (EditText) findViewById(R.id.write_nick);
                String text = write_text.getText().toString();
                String nick = write_nick.getText().toString();
                //date = new SimpleDateFormat("yyyy-MM-dd");
                Date currentTime = Calendar.getInstance().getTime();

                if(text.isEmpty() || nick.isEmpty()){
                    Toast.makeText(ContentActivity.this, "공백 X", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String, Object> dataToSave = new HashMap<String, Object>();
                    dataToSave.put(Id_KEY, id);
                    dataToSave.put(NICK_KEY, nick);
                    dataToSave.put(Text_KEY, text);
                    dataToSave.put(Lock_KEY, checkcheckbox());
                    dataToSave.put(Date_KEY, currentTime);
                    mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ContentActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Document has been saved!");
                            Intent intent = new Intent(ContentActivity.this, ShowMyText.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Fail!", e);
                        }
                    });
                }
            }
        });
    }
}
