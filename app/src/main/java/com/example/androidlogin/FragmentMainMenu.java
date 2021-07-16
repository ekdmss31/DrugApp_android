package com.example.androidlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentMainMenu extends Fragment {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // ImageButton 객체 생성
    private ImageButton btn_rotate;
    private ImageButton btn_image;
    private ImageButton btn_map;
    private ImageButton btn_review;
    private ImageButton btn_shape;
    private ImageButton btn_recom;
    private ImageButton btn_side;

    public FragmentMainMenu(){
    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_menu 화면을 inflate해서 보여줌
        View view = inflater.inflate(R.layout.fragment_menu,container,false);

        btn_rotate = view.findViewById(R.id.btn_rotate);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_anim);
        btn_rotate.setAnimation(animation);

        // 이미지 버튼의 각 아이디 값에 맞는 버튼 저장
        btn_image = view.findViewById(R.id.ImageButton);
        btn_map = view.findViewById(R.id.mapButton);
        btn_review = view.findViewById(R.id.reviewButton);
        btn_shape = view.findViewById(R.id.shapeButton);
        btn_recom = view.findViewById(R.id.btn_recom);
        btn_side = view.findViewById(R.id.sideButton);

        // 이름으로 검색하기 위한 이미지 버튼 클릭시
        btn_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // NameMainActivity로 화면 전환
                Intent intent = new Intent(getActivity(),NameMainActivity.class);
                startActivity(intent);
            }
        });

        // 사용자 근처 약국을 보여주기 위한 이미지 버튼 클릭시
        btn_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // MapMainActivity로 화면 전환
                Intent intent = new Intent(getActivity(),MapMainActivity.class);
                startActivity(intent);
            }
        });

        // 리뷰 게시판을 보여주기 위한 이미지 버튼 클릭시
        btn_review.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // ReviewMainAcivity로 화면 전환
                Intent intent  = new Intent(getActivity(),ReviewMainActivity.class);
                startActivity(intent);

            }
        });

        // 모양으로 검색하기 위한 이미지 버튼 클릭시
        btn_shape.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // FormMainActivity로 화면 전환
                Intent intent = new Intent(getActivity(), FormMainActivity.class);
                startActivity(intent);
            }
            });

        btn_recom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // FormMainActivity로 화면 전환
                Intent intent = new Intent(getActivity(), RecomMainActivity.class);
                startActivity(intent);
            }
        });

        // 부작용 버튼 클릭시 --> 추가된 부분
        btn_side.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // FormMainActivity로 화면 전환
                Intent intent = new Intent(getActivity(), SideEffectMainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
