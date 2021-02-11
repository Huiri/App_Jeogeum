package com.example.jeogeum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowMyText extends AppCompatActivity {

    private static final String TAG = "ShowMytext";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    TextView show;
    private final DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/wer");
    public static final String NICK_KEY = "nick";
    public static final String Text_KEY = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_text);
        show = (TextView)findViewById(R.id.show);
        Button close_btn = (Button)findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button fetch = (Button)findViewById(R.id.fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchText(v);
            }
        });

//        db.collection("sampleData")
//        .get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        //Toast.makeText(ShowMytext.this, document.getId() + document.getData(), Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        TextView show = (TextView)findViewById(R.id.show);
//                        show.setText(document.getId() + document.getData());
//
//                    }
//                } else {
//                    Log.w(TAG, "Error getting documents.", task.getException());
//                }
//            }
//        });
    }
    public void fetchText(View view) {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String text = documentSnapshot.getString(Text_KEY);
                    String nick = documentSnapshot.getString(NICK_KEY);

                    show.setText("\"" + text + "\" -- " + nick);

                    //Content content = documentSnapshot.toObject(Content.class);
                    //한번에 꺼내기
                    //Map<String, Object> myData = documentSnapshot.getData();
                }
            }
        });
    }
}