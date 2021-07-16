package com.example.androidlogin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NameMainActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    NameMyAdapter adapter;
    XmlPullParser parser;
    EditText edit = null;
    ImageView imageView;
    TextView resultDrug;
    int eventType;
    private String requestDrugUrl;
    ArrayList<NameDrug> list = null;
    NameDrug nameDrug = null;
    String imag;
    String key = "gyhnkvw8BuHNtPGQzXT5Nluh3Ri3hGlcpEnheMdjI1gjDbZhPSEpy05ofIMaFu2a96c%2FUX%2FzOVblYrTa%2B%2Fu%2Bjg%3D%3D"; //약국 공공데이터 서비스키

    private String getedit;

    //로딩중을 띄워주는 progressDialog
    private ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        // 버튼을 누르면 메인화면으로 이동
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_activity_main);

        edit = (EditText) findViewById(R.id.edit);
        imageView = (ImageView) findViewById(R.id.list_image);
        resultDrug = (TextView) findViewById(R.id.resultDrug);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//리사이클러뷰 초기화
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화

        //리니어레이아웃을 사용하여 리사이클러뷰에 넣어줄것임
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);


        // 홈으로 이동하는 버튼 객체 생성
        ImageButton btn_home = findViewById(R.id.gohome);

        // 홈 버튼 onclicklistener 생성
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

    }


    public void mOnClick(final View view) { //검색 버튼을 클릭 시
        //edit 검색 후 키보드 숨기기
        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(),0);

        getedit = edit.getText().toString();
        if(getedit.getBytes().length <= 0)
        {
            Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            //검색 결과가 다 뜰때까지 로딩중을 띄워줌 -> 공공데이터 파싱을 이용하기 때문에 오래 걸리기 때문이다.
            progressDialog.setMessage("로딩중입니다.");
            progressDialog.show();

            if (view.getId() == R.id.buttonNameSearch) {//버튼을 클릭 시 Thread 발생, 공공데이터를 search하여 불러오는 메서드 실행
                //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함

/*
            @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                public void handleMessage(Message msg) {

                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute();
                }
            };
            new Thread() {
                public void run() {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }.start();
        }

 */

                new Thread(new Runnable() {
                    //데이터가 많을 때 별도로 스레드를 만들어 사용하면 빠르게 실행된다.

                    //안드로이드는 싱글 쓰레드 체제이며, 오직 메인 쓰레드(UI 쓰레드)만이 뷰의 값을 바꿀 수 있는 권한을 갖고 있다.
                    //그래서 뷰의 값에 간섭하는 작업을 하는 쓰레드만을 만들고 뷰에 접근하려 시도하면, 안드로이드 자체적으로 앱을 죽여버린다.
                    // 이 경우를 막기 위해 안드로이드 개발자들은 핸들러라는 것을 만들어서 쓰는 것이다.

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                        runOnUiThread(new Runnable() { //스레드 사용 시 Ui를 이용하기 때문에 runOnUiThread가 필요하다.
                            //지금 작업을 수행하는 쓰레드가 메인 쓰레드라면 즉시 작업을 시작하고
                            //메인 쓰레드가 아니라면 쓰레드 이벤트 큐에 쌓아두는 기능을 하는 게 runOnUiThread다.

                            //Runnable : 특정 동작을 UI 스레드에서 동작하도록 합니다. 만약 현재 스레드가 UI 스레드이면 그 동작은 즉시 수행됩니다.
                            //Thread에서 UI에 접근하여 변경할 때 필요한것이다.
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                MyAsyncTask myAsyncTask = new MyAsyncTask();
                                myAsyncTask.execute();
                            }
                        });
                    }
                }).start();
            }

        }
    }

    //MyAsyncTask의 첫번째 인자는 doInBackground의 파라미터 타입이 될것이다.
    //두번째 인자는 doInBackground 작업 시 진행 단위의 타입
    //세번째 인자는 doInBackground 리턴값.
    //AsyncTask는 비교적 오래 걸리지 않은 작업에 유용하고, Task 캔슬이 용이하며 로직과 UI 조작이 동시에 일어나야 할 때 유용하게 사용됨
    //비동기 타입.  UI 스레드라고도 부릅. 비동기적으로 태스크를 실행하면  먼저 실행된 태스크가 종료되기 전에 다른 태스크를 실행할 수 있음
    //예를 들어 메인 스레드가 실행되는 중에, 다른 스레드를 백그라운드로 실행시켜 두고 계속 메인스레드는 자신의 작업을 하다가, 이 후 백그라운드에서 돌던 스레드가 종료시 결과값을 받을 수 있음

    public class MyAsyncTask extends AsyncTask<String, Void, String> {//공공데이터 파싱하는 코드. 배열에 넣어 불러옴

        @Override
        protected String doInBackground(String... strings) {//전달된 URL을 사용하는 작업을 한다
            //해당 메서드가 background 스레드로 일처리를 해주는 곳. 네트워크를 이용할때 사용됨
            //스레드 이므로 UI스레드가 어떤 일을 하고 있는지 상관없이 별개의 일을 진행한다는 점이다. 따라서 AysncTask는 비동기적으로 작동한다.

            String str = edit.getText().toString();//EditText에 작성된 Text얻어오기
            String drugSearch = null;//약 이름으로 검색하기 위해 null로 초기화해줌



            try {//인코딩을 위한 try catch문
                drugSearch = URLEncoder.encode(str, "UTF-8");//Edit창에 적은 String값을 인코딩 해줌
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            try {
                //일단 false로 선언해준 후 파싱해온 tag이름과 같으면 true로 바꾸어 배열에 넣어줄것임
                boolean drugName = false;
                boolean company = false;
                boolean image = false;
                boolean class_name = false;
                boolean etc_otc_name = false;



                //공공데이터 파싱을 위한 주소
                requestDrugUrl = "http://apis.data.go.kr/1470000/MdcinGrnIdntfcInfoService/getMdcinGrnIdntfcInfoList?ServiceKey="//요청 URL
                        + key  + "&numOfRows=100&item_name=" + drugSearch; //약 이름으로 검색 하기.

                //실질적으로 파싱해서 inputstream해주는 코드
                URL url = new URL(requestDrugUrl); //공공데이터 파싱 주소를 url에 넣음음
                InputStream is = url.openStream(); //Stream파일로 읽어들이기 위해 가져온 url을 연결함.
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//Tag 및 데이터를 가지고 올 때 필요함.
                parser = factory.newPullParser();//string을 xml로 바꾸어 넣을 곳
                parser.setInput(new InputStreamReader(is, "UTF-8"));//string을 xml로.


                //파싱해온 주소의 eventType을 가져옴. 이것을 이용하여 파싱의 시작과 끝을 구분해좀
                eventType = parser.getEventType();
                ///



                //eventType이 END_DOCUMENT이 아닐때까지 while문이 돌아감
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT://eventType이 START_DOCUMENT일 경우
                            list = new ArrayList<>();//배열을 선언해줌
                            break;
                        case XmlPullParser.END_TAG://eventType이 END_TAG일 경우, 태그가 끝나는 부분
                            if (parser.getName().equals("item") && nameDrug != null) {//Tag 이름이 item일경우
                                list.add(nameDrug);//배열에 Drug.java에 들어간 인자들을 넣어주고 끝냄
                            }
                            break;
                        case XmlPullParser.START_TAG://eventType이 START_TAG일 경우, 태그가 시작되는 부분. parser가 시작 태그를 만나면 실행
                            if (parser.getName().equals("item")) {//TAG명이 item일 때 Drug를 초기화 해줌
                                nameDrug = new NameDrug();
                            }
                            //Tag가 시작될 때 다 true로 변경함

                            if (parser.getName().equals("ITEM_NAME")) drugName = true;
                            if (parser.getName().equals("ENTP_NAME")) company = true;
                            if (parser.getName().equals("ITEM_IMAGE")) image = true;
                            if (parser.getName().equals("CLASS_NAME")) class_name = true;
                            if (parser.getName().equals("ETC_OTC_NAME")) etc_otc_name = true;

                            break;
                        case XmlPullParser.TEXT://eventType이 TEXT일 경우. parser가 내용에 접근했을때

                            if (drugName) {//drugName이 true일때 태그의 내용을 저장함.
                                nameDrug.setDrugName(parser.getText());//drug에 약 이름을 set해줌
                                drugName = false;//마지막에 false로 돌려 초기화해줌
                            } else if (company) {//company이 true일때 태그의 내용을 저장함.
                                nameDrug.setCompany(parser.getText());//drug에
                                company = false;
                            } else if (class_name) {//class_name이 true일때 태그의 내용을 저장함.
                                nameDrug.setClassName(parser.getText());//drug에 약 이름을 set해줌
                                class_name = false;//마지막에 false로 돌려 초기화해줌
                            } else if (etc_otc_name) {//etc_otc_name이 true일때 태그의 내용을 저장함.
                                nameDrug.setEtcOtcName(parser.getText());//drug에
                                etc_otc_name = false;
                            } else if (image) {//이미지 parser하는 방법은 조금 다름
                                imag = parser.getText();//가져온 결과는 URL링크형식임
                                try {
                                    //img는 따로 buffer를 이용하여 가져온 후 뿌려줘야함.
                                    URL url1 = new URL(imag);//URL링크 형식으로 받아온 결과를 집어넣음
                                    URLConnection conn = url1.openConnection();// URL을 연결한 객체 생성.
                                    conn.connect();
                                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                                    Bitmap bm = BitmapFactory.decodeStream(bis);
                                    bis.close();
                                    nameDrug.setImage(bm);//Bitmap형식으로된 이미지를 저장해줌
                                    image = false;
                                } catch (Exception ignored) {
                                }
                            }
                            break;
                    }

                    eventType = parser.next();//다음 parser를 찾아옴
                    //detailEventType = detailParser.next();
                }

            } catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute (String s){//adapter를 연결해주는 부분. 이 코드를 이용해 AsyncTask를 실행한다.
            //결과 파라미터를 리턴하면서 그 리턴값을 통해 스레드 작업이 끝났을 때의 동작을 구현합니다.

            super.onPostExecute(s);

            //검색 결과가 다 뜨면 progressDialog를 없앰
            progressDialog.dismiss();


            //어답터 연결.
            adapter = new NameMyAdapter(getApplicationContext(), list);//앞의 인자는 application context를 제공하며, 뒤에 인자는 위에서 값을 넣어준 list.
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();//어댑터에 연결된 항목들을 갱신함.


            if (list.size() != 0) {
                resultDrug.setText(" ");

            } else {
                resultDrug.setText("검색 결과와 일치하는 약이 없습니다.");
            }
        }
    }
}