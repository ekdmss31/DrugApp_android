package com.example.androidlogin;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import listener.OnPostListener;


//게시물 삭제 기능을 따로 빼줌. 인터페이스인 OnPostListener를 구현해주며, 초기화 해주어 연결해줌
//ReviewActivityPost에서 정보를 받아와 기능을 구현해줌.
public class ReviewPostAdapter {
    private Activity activity;
    private OnPostListener onPostListener;

    public ReviewPostAdapter(Activity activity){//생성자
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }

    public void postDelete(ReviewPostInfo reviewPostInfo){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();//FirebaseFirestore 초기화해주는 코드
        String id = reviewPostInfo.getId();//document의 id에 맞게 지워주기 위해 id값을 얻어옴
        firebaseFirestore.collection("posts").document(id).delete()//그 id에 맞는 값들을 지워줌
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//성공시
                        startToast("게시글을 삭제하였습니다.");//삭제시 삭제 토스트를 띄워줌
                        //끝났는줄 알고 post 업데이트를 해줘야 하니까 리스너 필요
                        onPostListener.onDelete(1);
                    }
                }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {//실패시 실패 토스트를 띄워줌
                startToast("게시글 삭제에 실패하였습니다."); }
        });
    }

    public void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }
};
