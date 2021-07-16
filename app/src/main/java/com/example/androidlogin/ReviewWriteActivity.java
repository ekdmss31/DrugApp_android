package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ReviewWriteActivity extends ReviewMainActivity {
    private static final String TAG = "WritePostActivity";
    private RelativeLayout loadrLayout;
    private FirebaseFirestore firebaseFirestore;
    private EditText contentEditText;
    private EditText titleEditText;
    private ReviewPostInfo reviewPostInfo2;//database에 올린 결과들을 가져오는 변수

    //user가져오는 변수
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //이 java파일에서는 activity_write_post창을 보여줄것임.
        setContentView(R.layout.review_activity_write_post);
        //초기화
        loadrLayout = findViewById(R.id.loaderLayout);
        contentEditText = findViewById(R.id.contentEditText);
        titleEditText = findViewById(R.id.titleEditText);
        //확인(check) 버튼을 누를 때 onClickListener함수를 불러옴
        findViewById(R.id.check).setOnClickListener(onClickListener);
        //초기화해줌. MainActivity.java의 intent putExtra에서 넣어준 값을 받아옴
        reviewPostInfo2 = (ReviewPostInfo)getIntent().getSerializableExtra("postInfo");
        postInit();//수정버튼을 눌렀을 때 그 전의 게시물을 불러오는 메서드를 onCreate에서 선언해줌
    }


    /*
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(), ReviewMainActivity.class));
        finish();
    }

     */


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.check) { //확인버튼을 누르면 firebase에 전송
                loadrLayout.setVisibility(View.VISIBLE);
                contentsUpdate();//데이터 베이스에 contents가 업데이트됨
                myStartActivity(ReviewMainActivity.class);//MainActivity화면으로 넘어감

            }
        }
    };
    //text 업데이트를 위한 코드
    private void contentsUpdate() {
        //titleEditText과 contentEditText의 값을 받아서 string값으로 받아옴
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentEditText)).getText().toString();
        final Date date = reviewPostInfo2 ==null? new Date() : reviewPostInfo2.getCreatedAt();//날짜가 존재하지 않으면 현재 날짜를 불러오고, 수정 시 날짜가 존재하니까 그때는 그 날짜 그대로 유지시켜줌
        //제목과 글이 둘 다 입력 되었을 때 실행됨
        if(title.length() > 0 && contents.length()>0){
            loadrLayout.setVisibility(View.VISIBLE);
            // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//나중에 DB에서 게시글을 올린 사람 찾기를 위한 메서드.
            user = firebaseAuth.getCurrentUser();
            assert user != null;
            ReviewPostInfo reviewPostInfo = new ReviewPostInfo(title, contents, date, user.getEmail());
            uploader(reviewPostInfo);//값들이 postinfo로 들어와 uploader 메서드로 들어감
        }else {//그렇지 않으면 게시글을 입력해달라는 toast가 띄워짐
            startToast("게시글을 입력해주세요");
        }
    }

    //데이터베이스에 직접 업로드 되는 코드
    private void uploader(ReviewPostInfo reviewPostInfo){
        //post collection에 contentsUpdate()에서 받아온 정보를 입력함.
        firebaseFirestore = FirebaseFirestore.getInstance();
        //수정할 때 데이터들도 가져와 수정해야 함.

        //값이 null이면 앞에것을 반환.->게시물 등록 시 사용됨. null이 아니면 뒤에것을 반환->수정버튼 이용 시 사용됨
        final DocumentReference documentReference = reviewPostInfo2 ==null? firebaseFirestore.collection("posts").document()
                :firebaseFirestore.collection("posts").document(reviewPostInfo2.getId());

        //게시물에서 입력한 텍스트들을 받아와서 database의 document부분에 넣어줌.
        documentReference.set(reviewPostInfo.getPostInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//성공시
                        Log.d(TAG,"id");
                        loadrLayout.setVisibility(View.GONE);//로딩화면 띄워주는 코드
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override// 실패 시
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error",e);
                        loadrLayout.setVisibility(View.GONE);//로딩화면 띄워주는 코드
                    }
                });
    }

    private void postInit (){//수정버튼을 눌렀을 때 그 전의 값들을 넣어주는 역할을 함
        if(reviewPostInfo2 !=null){
            titleEditText.setText(reviewPostInfo2.getTitle());
            contentEditText.setText(reviewPostInfo2.getContents());
        }
    }

    private void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class c) {//화면 전환을 위한 메서드를 함수로 정의함
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, 1);
        finish();
    }


}