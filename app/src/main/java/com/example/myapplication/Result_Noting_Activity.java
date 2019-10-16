package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result_Noting_Activity extends AppCompatActivity {

    // 뷰 선언
    ImageView Result_Back_Btn;
    TextView Result_Noting_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_noting);

        // 뷰매칭
        Result_Back_Btn = findViewById(R.id.Result_Back_Btn);
        Result_Noting_text = findViewById(R.id.Result_Noting_text);

        // 검색 결과 없음 액티비티 뒤로가기
        Result_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 검색결과 적용
        Result_Noting_text.setText("\""+getIntent().getStringExtra("Item_Name")+"\"");
    }
}