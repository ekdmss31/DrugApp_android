package com.example.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FindpwActivity extends AppCompatActivity {

    // 파이어베이스 인증 객체 생성
    private FirebaseFirestore firebaseFirestore;

    // 작성한 이메일 값과 이름을 저장할 객체 생성
    private EditText editTextEmail;
    private EditText editTextName;

    private String sendemail = "";
    private String sendname = "";

    // 회원의 이메일을 보여줄 textview 객체 생성
    private TextView textshowpw;
    private TextView textshowtext;

    // 로그인 화면으로 돌아가기 위한 버튼 객체 생성
    private Button btngotologin;

    // 사용자에게 실시간 진행상태를 알려주는 ProgressDialog 객체 생성
    private ProgressDialog progressDialog;

    // 뒤로가기 버튼 클릭시 로그인 화면 갱신
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(getApplication(),MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        // id가 write_email인 editText에 대한 메서드 저장
        editTextEmail = findViewById(R.id.write_email);
        // id가 write_phone인 editText에 대한 메서드 저장
        editTextName = findViewById(R.id.write_name);
        // id가 tv_showid인 textview에 대한 메서드 저장
        textshowpw = findViewById(R.id.tv_showpw);
        // id가 tv_showtext인 textview에 대한 메서드 저장
        textshowtext = findViewById(R.id.tv_showtext);

        // 로그인 화면으로 돌아가는 버튼에 대한 메서드 저장
        btngotologin = findViewById(R.id.btn_gotologin);

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

        btngotologin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //로그인으로 돌아가기 버튼을 누르면 MainActivity로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    public void findPw(View view)
    {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        sendemail = editTextEmail.getText().toString();
        sendname = editTextName.getText().toString();

       // findpw 메서드 실행
        findpw(sendemail, sendname);
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (sendemail.isEmpty()) {
            // 이메일 칸이 공백이면 false
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(sendemail).matches()) {
            // 이메일 형식이 불일치하면 false
            return false;
        } else {
            return true;
        }
    }

    // 아이디 찾기 메서드
    private void findpw(final String sendname, final String sendphone) {
        // 프로그레스 디이얼로그 생성하여 보여줌
        progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
        progressDialog.show();
        // 파이어스토어 가져옴
        firebaseFirestore = FirebaseFirestore.getInstance();
        // 파이어스토어의 collection 경로를 "users"로 생성
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 파이어스토어의 "users"에서 정보를 가져오는 것이 성공하면
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                // doument의 id와 입력한 이름이 같고 저장된 휴대폰번호와 입력한 휴대폰 번호가 같으면 입력한 이름의 저장된 이메일을 보여줌
                                if (sendname.equals(document.getData().get("email")) && sendphone.equals(document.getData().get("name"))) {
                                    textshowtext.setText("회원님의 비밀번호은 다음과 같습니다.");
                                    textshowpw.setText(document.getData().get("password").toString());
                                    // 로그인 화면으로 가는 버튼을 VISIBLE 처리하여 보여줌
                                    btngotologin.setVisibility(View.VISIBLE);
                                    break;
                                }
                                // 입력한 정보와 파이어베이스에 저장된 정보가 다르면 일치하는 회원정보가 없다는 텍스트를 보여줌
                                else {
                                    textshowtext.setText("일치하는 회원정보가 없습니다.");
                                    textshowpw.setText("");
                                }
                            }
                        }
                        // 프로그래스 다이얼로그 사라짐
                        progressDialog.dismiss();
                    }
                });

    }
}


