package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Keyword_allim_Activity extends AppCompatActivity {

    Button Keyword_Add_Btn;
    ImageView Keyword_Icon_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keywordallim);

        Keyword_Add_Btn = findViewById(R.id.Keyword_Add_Btn);
        Keyword_Icon_Back = findViewById(R.id.Keyword_Icon_Back);

        Keyword_Add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Keyword_allim_Activity.this, Keyword_allim_Input_Activity.class);
                startActivity(intent);
            }
        });

        Keyword_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}