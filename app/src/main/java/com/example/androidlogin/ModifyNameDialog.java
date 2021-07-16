package com.example.androidlogin;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class ModifyNameDialog extends DialogFragment {
    private Fragment fragment;

    // 파이어스토어에 저장된 "name"이라는 이름의 값을 pass_key라는 문자에 저장
    private static final String pass_key = "name";

    // 파이어스토어 인증 객체 생성
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 다이얼로그의 확인 버튼과 취소 버튼 객체 생성
    private Button positivebutton;
    private Button negativebutton;

    // 다이얼로그에 입력하는 name 객체 생성
    private EditText name;

    // 새롭게 저장할 name 값 객체 생성
    String newName ="";

    public ModifyNameDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify3_dialog, container, false);

        // 레이아웃에 postivebutton이라는 버튼 값과 negativebutton이라는 버튼 값 저장
        positivebutton = view.findViewById(R.id.positivebutton);
        negativebutton = view.findViewById(R.id.negativebutton);

        // 다이얼로그에 작성하는 name값 저장
        name = view.findViewById(R.id.write_name);


        // 취소 버튼 클릭시
        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 다이얼로그 사라짐
                getDialog().dismiss();
            }
        });

        // 확인 버튼 클릭시
        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 로그인 중인 사용자 가져오기기
               final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // 로그인 상태일 경우
                if(user != null) {
                    // 다이얼로그에 작성한 name값을 string으로 처리하여 샤로운 name값인 newName에 저장
                    newName = name.getText().toString();
                    if (isValidName()) {
                        // 파이어스토어 collection 경로를 "uesrs"로 하고 로그인 중인 유저의 email을 documentID로 하는 정보를 가져옴
                        DocumentReference contact = firebaseFirestore.collection("users").document(user.getEmail());
                        // 새로운 name값으로 업데이트
                        contact.update(pass_key, newName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // 업데이트 성공시 다이얼로그 사라짐
                                        getDialog().dismiss();
                                    }
                                });
                    }
                }
            }

        });

        return view;

    }

    // 이름 유효성 검사
    private boolean isValidName() {
        if (newName.isEmpty()) {
            // 이름 칸이 공백이면 false
            Toast.makeText(getActivity(), R.string.plzinputname, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}



