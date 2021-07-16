package com.example.androidlogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import listener.OnPostListener;

public class ReviewMainActivity extends AppCompatActivity {
    private static final String TAG = "WritePostActivity";
    private  ArrayList<ReviewPostInfo> postList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ReviewMainAdapter mainAdapter;
    private FirebaseFirestore firebaseFirestore;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;
    FragmentMainMenu fragmentMainMenu;

    @Override public void onStart() {
        super.onStart();
        // 파이어베이스에 로그인 중인지 판단
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //이 파일에서는 activity_main.xml창을 보여줄것임.
        setContentView(R.layout.review_activity_main);

        fragmentMainMenu = new FragmentMainMenu();

        // 로그인 중인 사용자가 있는 지 판단
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                // 로그인한 사용자가 있는 경우
                if (user != null) {
                    Log.e("로그","리뷰게시판입니다.");
                }
                // 로그인한 사용자가 없는 경우
                else {
                    login();
                }
            }
        };

        //recyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView);//recyclerViewid연결
        recyclerView.setHasFixedSize(true);//recylerView 기존 성능 강화
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReviewMainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        //firebase collection의 모든 문서 가져오기
        //firebase 문서에서 가져온 메서드. createdAt 기준으로 내림차순으로 정렬됨
        firebaseFirestore = FirebaseFirestore.getInstance();
        postUpdate();

        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);//게시글 추가 버튼을 클릭 시
        findViewById(R.id.gohome).setOnClickListener(onClickListener);
    }

    private void login() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if((!this.isFinishing())){
            dialog.setMessage("로그인 후 이용해주세요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

    }

    //게시글 추가 버튼을 클릭할 때 처리하는 기능
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {//게시글 추가 버튼을 눌렀을 때 WritePostActivity.java로 넘겨줌
            if (view.getId() == R.id.floatingActionButton) {
                myStartActivity(ReviewWriteActivity.class);
            }
            else if (view.getId() == R.id.gohome){
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    };

    //실제 게시물을 보여주고 업데이트 해주는 코드
    public void postUpdate(){
        firebaseFirestore.collection("posts").orderBy("createdAt", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            postList = new ArrayList<>();//arraylist에 받아온값들을 다 넣어주어 보여줌
                            postList.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId()+" => "+document.getData());
                                postList.add(new ReviewPostInfo(//데이터를 다 가져와 postList배열에 넣어줌.
                                        document.getData().get("title").toString(),
                                        document.getData().get("contents").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId(),
                                        document.getData().get("user").toString()
                                        //firebaseFirestore.collection("users").document().getId()//user의 email값을 가져옴.
                                ));//각 post들을 구분할 수 있게 하기 위해 post의id값을 얻어옴
                                //들어간 데이터 로그로 확인하기
                                Log.e("로그","title : "+ document.getData().get("title").toString());
                                Log.e("로그","data : "+ document.getData().get("contents").toString());
                                //Log.e("로그","email : "+ firebaseFirestore.collection("users").document().getId());
                            }
                            //게시물 업데이트(새로고침)을 위한 메서드. 데이터가 업데이트 되면 adapter를 다시 바꿔줘야함.
                            //MainAdaper에서 넘겨줌.
                            mainAdapter = new ReviewMainAdapter(ReviewMainActivity.this, postList);
                            mainAdapter.setOnPostListener(onPostListener);//onPostListener를 넘겨주면 MainAdapter에서도 쓸수있음.
                            recyclerView.setAdapter(mainAdapter);
                            mainAdapter.notifyDataSetChanged();
                        }else {
                            Log.d(TAG, "Error : ",task.getException());
                        }

                    }
                });



    }
    OnPostListener onPostListener = new OnPostListener() {//인터페이스인 OnPostListener를 가져와서 구현해줌
        @Override
        public void onDelete(int position) {//MainAdapter에 넘겨주기 위한 메서드 작성

            String id = postList.get(position).getId();//document의 id에 맞게 지워주기 위해 id값을 얻어옴
            firebaseFirestore.collection("posts").document(id).delete()//그 id에 맞는 값들을 지워줌
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {//성공시
                            startToast("게시글을 삭제하였습니다.");
                            postUpdate();//새로고침을 위해 이 이벤트를 mainActivity에서 알아야함.->listener를 만들어줘야함
                        }
                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {//실패시
                    startToast("게시글 삭제에 실패하였습니다.");
                }
            });
        }

        @Override
        public void onModify(int position) {//여기서 수정하면 writepostActivity를 켜서 수정해주는코드
            myStartActivity(ReviewWriteActivity.class,postList.get(position));
        }
    };


    //Main화면에서 다른 화면으로 념겨주는 intent기능을 따로 함수로 만들었음.
    private void myStartActivity(Class c) {//게시물을 추가하는 경우 WritePostActivity 화면으로 넘겨주는 코드
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

    private void myStartActivity(Class c, ReviewPostInfo reviewPostInfo) {//intent를 이용하여 id 값을 전달해줄것임.
        Intent intent = new Intent(this,c);
        intent.putExtra("postInfo", reviewPostInfo);//앞에는 key값, 뒤에는 실제 값
        //id값을 보내주면 WritePostActivity에서 받아서 쓸것임
        startActivity(intent);
    }

    private void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}