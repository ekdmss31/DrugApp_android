package com.example.androidlogin;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ModifyDialog extends DialogFragment {
    private Fragment fragment;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어스토어에 저장된 "password"이라는 이름의 값을 pass_key라는 문자에 저장
    private final String pass_key = "password";

    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 다이얼로그에 입력하는 password 객체 생성
    private EditText password;

    // 새롭게 저장할 password 값 객체 생성
    private String newPassword ="";

    public ModifyDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_dialog, container, false);

        // 레이아웃에 postivebutton이라는 버튼 값과 negativebutton이라는 버튼 값 저장
        // 다이얼로그의 확인 버튼과 취소 버튼 객체 생성
        Button positivebutton = view.findViewById(R.id.positivebutton);
        Button negativebutton = view.findViewById(R.id.negativebutton);

        // 다이얼로그에 작성하는 password값 저장
        password = view.findViewById(R.id.signup_password);

        // 취소 버튼 클릭시
        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 다이얼로그 사라짐
                getDialog().dismiss();
            }
        });

        //확인 버튼 클릭시
        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 로그인 중인 사용자 가져오기
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // 로그인 상태일 경우
                    // 다이얼로그에 작성한 password값을 string으로 처리하여 새로운 password값인 newPassword에 저장
                    newPassword = password.getText().toString();
                    // 입력한 비밀번호의 유효성 검사
                    if (isValidPasswd()) {
                        // 로그인 중인 사용자의 password를 newPassword로 업데이트
                        assert user != null;
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // 업데이트에 성공하면 파이어스토어에 저장된 password도 업데이트
                                            // 파이어스토어 collection의 경로를 "users"로 하고 로그인 중인 유저의 email을 documentID로 하는 정보를 가져옴
                                            DocumentReference ref = firebaseFirestore.collection("users").document(user.getEmail());
                                            // 새로운 password값으로 업데이트
                                            ref.update(pass_key, newPassword)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // 업데이트 성공시 다이얼로그 사라짐
                                                            getDialog().dismiss();

                                                        }
                                                    });
                                        }
                                    }
                                });
                    }

            }

        });

        return view;

    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (newPassword.isEmpty()) {
            // 비밀번호 칸이 공백이면 false
            Toast.makeText(getActivity(),R.string.plzinpytpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            // 비밀번호 형식이 불일치하면 false
            Toast.makeText(getActivity(), R.string.notvalidpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
