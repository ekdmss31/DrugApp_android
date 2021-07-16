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

public class FormSearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<FormDrug> list = null;
    FormMyAdapter mAdapter;

    //선택한 색상, 모양 제형 저장할 변수
    // 표시앞,뒤
    private String choosecolor;
    private String chooseshape;
    private String choosetype;
    private String searchmarkfront;
    private String searchmarkback;

    private LinearLayoutManager linearLayoutManager;

    private TextView drug_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_shape_search_list);


        drug_result = (TextView) findViewById(R.id.drug_result);

        choosecolor = getIntent().getStringExtra("choosecolor");
        chooseshape = getIntent().getStringExtra("chooseshape");
        choosetype = getIntent().getStringExtra("choosetype");
        searchmarkfront = getIntent().getStringExtra("searchmarkfront");
        searchmarkback = getIntent().getStringExtra("searchmarkback");


        Log.e("result : ", choosecolor + "/ " + chooseshape + "/ " + choosetype + "/" + searchmarkfront + "/" + searchmarkback);

        if (choosecolor == null && chooseshape == null && choosetype ==null) {
            marksearchJson();
            Log.e("dg","식별자");
        }
        else {
            searchJson();
            Log.e("dg","컬러");
        }
        recyclerView = (RecyclerView)findViewById(R.id.rv_recyclerview);//리사이클러뷰 초기화
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화

        //리니어레이아웃을 사용하여 리사이클러뷰에 넣어줄것임
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new FormMyAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (list.size() != 0) {
            drug_result.setText(" ");

        } else {
            drug_result.setText("검색 결과와 일치하는 약이 없습니다.");
        }

    }


    //json에서 조건에 맞는 것 검색(색상, 모양, 제형) 7가지.
    public void searchJson(){
        try{
            InputStream is = getAssets().open("druglist.json"); //assests파일에 저장된 druglist_final.json 파일 열기
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("druglist"); //json파일에서 의약품리스트의 배열명, jsonArray로 저장

            list = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);

                //'색상, 모양, 제형' 선택하고 검색하기(3개의 카테고리 중 하나만 선택 하고 검색 가능)
                //1. 색상만 선택된 경우
                if(choosecolor != null && chooseshape == null && choosetype == null){
                    if ((jsonObject.getString("색상앞").contains(choosecolor))) {
                        FormDrug formDrug = new FormDrug();
                        Log.e("1번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형"));

                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));

                        list.add(formDrug);
                    }
                }
                //2. 색상 & 모양
                else if(choosecolor != null && chooseshape != null && choosetype == null){
                    if ((jsonObject.getString("색상앞").contains(choosecolor)) && (jsonObject.getString("의약품제형").equals(chooseshape))) {
                        FormDrug formDrug = new FormDrug();
                        Log.e("2번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));

                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
                //3. 색상 & 제형
                else if(choosecolor != null && chooseshape == null){
                    if ((jsonObject.getString("색상앞").contains(choosecolor)) && (choosetype.contains(jsonObject.getString("제형코드명")))) {
                        FormDrug formDrug = new FormDrug();
                        Log.e("3번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
                //4. 모양만
                else if(chooseshape != null && choosecolor == null && choosetype == null) {
                    if (jsonObject.getString("의약품제형").equals(chooseshape)) {
                        FormDrug formDrug = new FormDrug();
                        Log.e("4번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
                //5.모양 & 제형
                else if(chooseshape != null && choosecolor == null) {
                    if (jsonObject.getString("의약품제형").equals(chooseshape)) {
                        FormDrug formDrug = new FormDrug();
                        Log.e("5번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
                // 6. 제형만
                else if(choosetype != null && chooseshape == null) {
                    if (choosetype.contains(jsonObject.getString("제형코드명"))) {
                        FormDrug formDrug = new FormDrug();
                        Log.e("6번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
                // 7. 모두 선택
                else {
                    if((jsonObject.getString("색상앞").contains(choosecolor)) && jsonObject.getString("의약품제형").equals(chooseshape) && (choosetype.contains(jsonObject.getString("제형코드명")))){
                        FormDrug formDrug = new FormDrug();
                        Log.e("7번 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
            }

            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
        }catch (Exception e){e.printStackTrace();}

    }


    //json에서 조건에 맞는 것 검색(식별자) 3가지.
    public void marksearchJson(){
        try{
            InputStream is = getAssets().open("druglist.json"); //assests파일에 저장된 druglist_final.json 파일 열기
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("druglist"); //json파일에서 의약품리스트의 배열명, jsonArray로 저장

            list = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);

                //8. 표시앞만
                if(searchmarkfront != null && searchmarkback == null) { //식별자 앞이 입력됐을 경우
                    if (searchmarkfront.equals(jsonObject.getString("표시앞")))
                    {
                        FormDrug formDrug = new FormDrug();
                        Log.e("8번째 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }

                } //9. 표시 앞 뒤 둘 다 입력
                else if(searchmarkfront != null){ //두개 다 입력
                    if (searchmarkfront.equals(jsonObject.getString("표시앞")) && searchmarkback.equals(jsonObject.getString("표시뒤")))
                    {
                        FormDrug formDrug = new FormDrug();
                        Log.e("9번째 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }//10. 표시뒤만
                else if(searchmarkback != null) { //식별자 뒤가 입력됐을 경우
                    if (searchmarkback.equals(jsonObject.getString("표시뒤")))
                    {
                        FormDrug formDrug = new FormDrug();
                        Log.e("10번째 : ", jsonObject.getString("품목명") + jsonObject.getString("색상앞") + jsonObject.getString("의약품제형") + jsonObject.getString("제형코드명") + jsonObject.getString("표시앞") + jsonObject.getString("표시뒤"));
                        formDrug.setImage(jsonObject.getString("큰제품이미지"));
                        formDrug.setDrugName(jsonObject.getString("품목명"));
                        formDrug.setCompany(jsonObject.getString("업소명"));
                        formDrug.setClassName(jsonObject.getString("분류명"));
                        formDrug.setEtcOtcName(jsonObject.getString("전문일반구분"));
                        list.add(formDrug);
                    }
                }
            }

            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
        }catch (Exception e){e.printStackTrace();}
    }


}