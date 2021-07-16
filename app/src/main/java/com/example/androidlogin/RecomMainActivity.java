package com.example.androidlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RecomMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    Button[] recomBtn = new Button[8];
    Button result_recombtn;
    private String recom_id;

    private String chooserecom = null;
    //TextView textrecom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recom_activity_main);

        // 홈으로 이동
        ImageButton btn_home = findViewById(R.id.gohome);

        // 홈 버튼 클릭이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 누르면 메인화면으로 이동
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        });

        //증상 버튼 이벤트 실행
        settingRecombtn();
    }

    //증상 버튼 이벤트
    public void settingRecombtn(){
        for(int i=0; i <recomBtn.length; i++){
            recom_id = "recom_btn" + (i+1); //버튼 아이디값 저장
            recomBtn[i] = findViewById(getResources().getIdentifier(recom_id, "id",getPackageName())); //버튼 초기화
        }

        for(Button buttonId : recomBtn){
            buttonId.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    result_recombtn = findViewById(v.getId());
                    //result_recombtn.setBackgroundResource(R.drawable.choose_btton); //해당아이디 버튼의 배경색을 바꿈
                    //result_recombtn.setTextColor(Color.WHITE);
                    chooserecom = result_recombtn.getText().toString(); //선택 증상을 저장

                    //////여기서 for문으로 thiscolor랑 result.getText.toString()비교해서 배경색 다시 바꿔주기
                    Log.e("다음 클릭 후 : ", chooserecom);

                    Intent intent = new Intent(getApplicationContext(), RecomSearchActivity.class);
                    intent.putExtra("chooserecom",chooserecom);

                    Log.e("길이 : ", String.valueOf(chooserecom.length()));
                    startActivity(intent);
                }
            });
        }
    }

}