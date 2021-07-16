package com.example.androidlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Locale;

import listener.OnPostListener;

//게시글화면 만들기. 해당하는 게시글을 클릭하면 자세히 볼 수 있는 화면이 나옴
public class ReviewActivityPost extends ReviewMainActivity {
    private ReviewPostInfo reviewPostInfo;
    private ReviewPostAdapter reviewPostAdapter;
    FirebaseFirestore firebaseFirestore;
    Button modify2;
    Button delete2;
    private String email;
    private TextView textName;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //이 java파일에서는 activity_write_post창을 보여줄것임.
        setContentView(R.layout.review_activity_post);

        reviewPostInfo = (ReviewPostInfo) getIntent().getSerializableExtra("postInfo");
        //title 넣어주기
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(reviewPostInfo.getTitle());

        //게시물을 추가한 날짜 넣어주기
        TextView createTextView = findViewById(R.id.createdTextView);
        createTextView.setText(new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(reviewPostInfo.getCreatedAt()));//local시간을 생성하여 넣어줄것임.
        //contents 넣어주기
        final TextView contentsTextView = findViewById(R.id.contentsTextView);
        contentsTextView.setText(reviewPostInfo.getContents());

        textName = findViewById(R.id.textName);
        textName.setText(reviewPostInfo.getEmail());

        modify2 = findViewById(R.id.modify2);
        delete2 = findViewById(R.id.delete2);




        //user 값 비교
        // 로그인 중인 사용자를 불러옴

        // 로그인 중인 사용자의 이메일 값
        //String checkmail = user.getEmail();
        // 다이얼로그에 입력한 이메일 값

        userCheck();

        reviewPostAdapter = new ReviewPostAdapter(this);
        reviewPostAdapter.setOnPostListener(onPostListener);//ReviewPostAdapter에 연결해줌.

        // 홈으로 이동하는 버튼 객체 생성
        ImageButton btn_home = findViewById(R.id.gohome);

        // 홈 버튼 onclicklistener 생성
        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 버튼을 누르면 메인화면으로 이동
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        });


    }
    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(int position) {
            Log.e("로그", "삭제 성공");
        }

        @Override
        public void onModify(int position) {
            Log.e("로그", "수정 성공");
        }
    };


    //수정버튼, 삭제버튼 클릭 시 실행해주는 메서드를 구현함
    private void buttonClick(){

        //dialog를 이용해서 삭제를 재 확인 해주는 메서드를 구현함.
        final AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        //삭제버튼 클릭 시
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oDialog.setMessage("삭제하시겠습니까?")//Dialog로 듸워줌
                        .setTitle("알림")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener()//아니오 클릭 시
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Log.i("Dialog", "취소");
                                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNeutralButton("예", new DialogInterface.OnClickListener()//예 버튼 클릭 시
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //실제 삭제 기능을 수행하는 코드.
                                reviewPostAdapter.postDelete(reviewPostInfo); //reviewPostInfo의 postDelete메서드를 이용해 삭제시킴
                                myStartActivity(ReviewMainActivity.class, reviewPostInfo);//intent로 ReviewMainActivity로 넘겨줌
                                finish();
                            }
                        })
                        .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                        .show();
            }
        });
        //수정버튼 클릭 시
        modify2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(ReviewWriteActivity.class, reviewPostInfo);//바로 ReviewWriteActivity창으로 넘겨줌. 다시 게시물을 작성할 수 있는 창으로 넘겨주는것임.

            }
        });
    }

    private void userCheck(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 로그인중인 유저
        email = user.getEmail();

        firebaseFirestore =  FirebaseFirestore.getInstance();

        if (email.equals(reviewPostInfo.getEmail())) {
            //Log.e("log : ",document.getData().get("user").toString());
            Log.e("log : ",reviewPostInfo.getEmail());
            modify2.setVisibility(View.VISIBLE);
            delete2.setVisibility(View.VISIBLE);
            buttonClick();
        }
        // 입력한 정보와 파이어베이스에 저장된 정보가 다르면 일치하는 회원정보가 없다는 텍스트를 보여줌
        else {
            //Log.e("log : ",document.getData().get("user").toString());
            Log.e("log : ",reviewPostInfo.getEmail());
            modify2.setVisibility(View.GONE);
            delete2.setVisibility(View.GONE);
        }
    }
    private void myStartActivity (Class c, ReviewPostInfo reviewPostInfo){//intent를 이용하여 id 값을 전달해줄것임.
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", reviewPostInfo);//앞에는 key값, 뒤에는 실제 값
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //id값을 보내주면 WritePostActivity에서 받아서 쓸것임
        startActivity(intent);
        finish();
    }
    private void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}