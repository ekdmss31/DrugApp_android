package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SideEffectMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_effect_main);

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
    }

    public void child(View v){
        Intent intent = new Intent(getApplicationContext(), SideEffectChild.class);
        startActivity(intent);
    }
    public void pregnant(View v){
        Intent intent = new Intent(getApplicationContext(), SideEffectPregnant.class);
        startActivity(intent);
    }
    public void senior(View v){
        Intent intent = new Intent(getApplicationContext(), SideEffectSenior.class);
        startActivity(intent);
    }
}
