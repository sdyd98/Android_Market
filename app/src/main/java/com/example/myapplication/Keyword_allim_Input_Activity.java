package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Keyword_allim_Input_Activity extends AppCompatActivity {


    ImageView Keyword_Allim_Close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keywordinput);

        Keyword_Allim_Close = findViewById(R.id.Keyword_Allim_Close);

        Keyword_Allim_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}