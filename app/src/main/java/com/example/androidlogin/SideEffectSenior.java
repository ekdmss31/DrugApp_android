package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class SideEffectSenior extends AppCompatActivity {
    EditText senSearch;
    Button senBtn;

    private String drugName = null; //검색 결과
    private String image; //사진 주소 값 저장
    private RecyclerView sen_recy;
    ArrayList<SideDrug> list = null;
    SideEffectAdapter mAdapter;

    private LinearLayoutManager linearLayoutManager;

    private TextView senOut;//검색한 노인약이 있는지 확인하는

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_effect_senior);

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

        sen_recy = (RecyclerView)findViewById(R.id.sen_recy);//리사이클러뷰 초기화
        sen_recy.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화

        //리니어레이아웃을 사용하여 리사이클러뷰에 넣어줄것임
        linearLayoutManager = new LinearLayoutManager(this);
        sen_recy.setLayoutManager(linearLayoutManager);



        senSearch = (EditText)findViewById(R.id.senSearch);
        senOut = (TextView) findViewById(R.id.senOut);


        findViewById(R.id.senBtn).setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            list = new ArrayList<>();

            drugName = senSearch.getText().toString();
            Log.e("drugName : ", drugName);

            if (drugName!=null) {
                Log.e("dg", "임부금기");
                senoirJson();

                mAdapter = new SideEffectAdapter(getApplicationContext(), list);
                sen_recy.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            if (list.size()!= 0) {
                senOut.setText(" ");
            } else {
                senOut.setText("검색 결과와 일치하는 약이 없습니다.");
            }

        }
    };


    //json에서 조건에 맞는 것 검색(식별자) 3가지.
    public void senoirJson(){
        try{
            InputStream is = getAssets().open("senior.json"); //assests파일에 저장된 druglist_final.json 파일 열기
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            //
            InputStream is2 = getAssets().open("druglist.json"); //assests파일에 저장된 druglist_final.json 파일 열기
            byte[] buffer2 = new byte[is2.available()];
            is2.read(buffer2);
            is2.close();
            String json2 = new String(buffer2, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("senior"); //json파일에서 의약품리스트의 배열명, jsonArray로 저장

            //
            JSONObject jsonObject2 = new JSONObject(json2);
            JSONArray jsonArray2 = jsonObject2.getJSONArray("druglist"); //json파일에서 의약품리스트의 배열명, jsonArray로 저장

            for(int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getString("품목명").contains(drugName)) {
                    SideDrug sideDrug = new SideDrug();
                    Log.e("drugName : ", drugName);
                    Log.e("1번째 : ", jsonObject.getString("품목명"));
                    sideDrug.setSide_drugName(jsonObject.getString("품목명"));
                    sideDrug.setSide_company(jsonObject.getString("업소명"));
                    sideDrug.setSide_className(jsonObject.getString("구분"));
                    sideDrug.setSide_ingredient(jsonObject.getString("성분명"));
                    sideDrug.setSide_detail(jsonObject.getString("상세정보"));

                    for(int j=0; j<jsonArray2.length(); j++) {
                        jsonObject2 = jsonArray2.getJSONObject(j);
                        if (jsonObject.getString("품목명").contains(jsonObject2.getString("품목명"))) {
                            image = jsonObject2.getString("큰제품이미지");
                            sideDrug.setSide_image(image);
                            Log.e("image ", "success!!");
                            break;
                        }
                    }

                    sideDrug.setAge("노인");
                    sideDrug.setAgeDetail(" ");

                    list.add(sideDrug);
                }
            }
            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
        }catch (Exception e){e.printStackTrace();}
    }
}