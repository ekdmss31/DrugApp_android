package com.example.androidlogin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

public class AuthemailDialog extends Dialog {

    // 확인 버튼 생성
    private Button positivebutton;
    // 확인 버튼의 onClickListner 생성
    private View.OnClickListener positiveListener;

    // 이메일 인증 다이얼로그
    public AuthemailDialog(@NonNull Context context, View.OnClickListener positiveListener) {
        super(context);
        this.positiveListener = positiveListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 밖의 화면은 흐리게 함
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.authemail_dialog);

        // 확인 버튼
        positivebutton = findViewById(R.id.positivebutton);
        // 클릭 리스너
        positivebutton.setOnClickListener(positiveListener);
    }
}
