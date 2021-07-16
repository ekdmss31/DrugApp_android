package com.example.androidlogin;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class RecomSearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<Recom> list; // 리스트 고치기
    RecomMyAdapter mAdapter;

    private String chooserecom;
    private LinearLayoutManager linearLayoutManager;

    private String image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recom_list);

        //선택한 증상 텍스트값 넘겨준 것 저장
        chooserecom = getIntent().getStringExtra("chooserecom");

        Log.e("레콤서치 : ", chooserecom);

        recomJson();

        recyclerView = (RecyclerView)findViewById(R.id.recom_recyclerview);//리사이클러뷰 초기화
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화

        //리니어레이아웃을 사용하여 리사이클러뷰에 넣어줄것임
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new RecomMyAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    //증상에 맞는 약 json파일에서 검색
    public void recomJson() {
        try {
            InputStream is = getAssets().open("recom.json"); //assests파일에 저장된 druglist_final.json 파일 열기
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");



            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("recomlist"); //json파일에서 의약품리스트의 배열명, jsonArray로 저장

            list = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if(chooserecom.length()<=3) {
                    if (jsonObject.getString("효능효과").contains(chooserecom)) {
                        Recom recom = new Recom();
                        Log.e("1번 : ", jsonObject.getString("품목명"));

                        recom.setImage(jsonObject.getString("이미지"));
                        recom.setName(jsonObject.getString("품목명"));
                        recom.setCompany(jsonObject.getString("업체명"));
                        recom.setSymptom(jsonObject.getString("효능효과"));
                        Log.e("레콤서치 : ", jsonObject.getString("이미지"));
                        list.add(recom);
                    }
                }

                else if(chooserecom.length() >=19){
                    if(chooserecom.equals("감기(콧물, 코막힘, 기침, 발열)")){
                        if (jsonObject.getString("효능효과").contains("감기") || jsonObject.getString("효능효과").contains("콧물") || jsonObject.getString("효능효과").contains("코막힘")
                                ||jsonObject.getString("효능효과").contains("기침") || jsonObject.getString("효능효과").contains("발열")) {
                            Recom recom = new Recom();
                            Log.e("1번 : ", jsonObject.getString("품목명"));

                            recom.setImage(jsonObject.getString("이미지"));
                            recom.setName(jsonObject.getString("품목명"));
                            recom.setCompany(jsonObject.getString("업체명"));
                            recom.setSymptom(jsonObject.getString("효능효과"));
                            list.add(recom);
                        }
                    }
                    if(chooserecom.equals("통증(두통, 치통, 인후통, 생리통)")){
                        if (jsonObject.getString("효능효과").contains("통증") || jsonObject.getString("효능효과").contains("두통") || jsonObject.getString("효능효과").contains("치통")
                                ||jsonObject.getString("효능효과").contains("인후통") || jsonObject.getString("효능효과").contains("생리통")) {
                            Recom recom = new Recom();
                            Log.e("1번 : ", jsonObject.getString("품목명"));

                            recom.setImage(jsonObject.getString("이미지"));
                            recom.setName(jsonObject.getString("품목명"));
                            recom.setCompany(jsonObject.getString("업체명"));
                            recom.setSymptom(jsonObject.getString("효능효과"));
                            list.add(recom);
                        }
                    }
                }
                else{
                    if(chooserecom.equals("체력저하 . 육체피로")){
                        if (jsonObject.getString("효능효과").contains("체력저하") || jsonObject.getString("효능효과").contains("육체피로")) {
                            Recom recom = new Recom();
                            Log.e("1번 : ", jsonObject.getString("품목명"));

                            recom.setImage(jsonObject.getString("이미지"));
                            recom.setName(jsonObject.getString("품목명"));
                            recom.setCompany(jsonObject.getString("업체명"));
                            recom.setSymptom(jsonObject.getString("효능효과"));
                            list.add(recom);
                        }
                    }
                    if(chooserecom.equals("치주질환(치료)")){
                        if (jsonObject.getString("효능효과").contains("치주")) {
                            Recom recom = new Recom();
                            Log.e("1번 : ", jsonObject.getString("품목명"));

                            recom.setImage(jsonObject.getString("이미지"));
                            recom.setName(jsonObject.getString("품목명"));
                            recom.setCompany(jsonObject.getString("업체명"));
                            recom.setSymptom(jsonObject.getString("효능효과"));
                            list.add(recom);
                        }
                    }if(chooserecom.equals("식욕감퇴 . 소화불량")){
                        if (jsonObject.getString("효능효과").contains("식욕감퇴") || jsonObject.getString("효능효과").contains("소화불량")) {
                            Recom recom = new Recom();
                            Log.e("1번 : ", jsonObject.getString("품목명"));

                            recom.setImage(jsonObject.getString("이미지"));
                            recom.setName(jsonObject.getString("품목명"));
                            recom.setCompany(jsonObject.getString("업체명"));
                            recom.setSymptom(jsonObject.getString("효능효과"));
                            list.add(recom);
                        }
                    }
                    if(chooserecom.equals("화상 . 외상")){
                        if (jsonObject.getString("효능효과").contains("화상") || jsonObject.getString("효능효과").contains("외상")) {
                            Recom recom = new Recom();
                            Log.e("1번 : ", jsonObject.getString("품목명"));

                            recom.setImage(jsonObject.getString("이미지"));
                            recom.setName(jsonObject.getString("품목명"));
                            recom.setCompany(jsonObject.getString("업체명"));
                            recom.setSymptom(jsonObject.getString("효능효과"));
                            list.add(recom);
                        }
                    }

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}