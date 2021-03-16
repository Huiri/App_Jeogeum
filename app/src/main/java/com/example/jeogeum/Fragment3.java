package com.example.jeogeum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.Objects;

public class Fragment3 extends Fragment implements View.OnClickListener {

    private Button Close_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);
    }

    @Override
    public void onClick(View v) {
        Close_btn = (Button) v.findViewById(R.id.finish);
        Close_btn.setOnClickListener(this);
        switch (v.getId()) {
            case R.id.finish:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(Fragment3.this).commit();
                fragmentManager.popBackStack();
        }
        //실행 X
    }
}