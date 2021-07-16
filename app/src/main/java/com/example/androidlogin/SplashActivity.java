package com.example.androidlogin;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends MainActivity {//loading화면 -> main화면으로 넘어갈때.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}