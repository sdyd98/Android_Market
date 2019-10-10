package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class allim_Activity extends AppCompatActivity {

    ImageView Allim_Icon_Back;
    RecyclerView allim_Recycle;
    RecyclerView.Adapter allim_Adapter;
    RecyclerView.LayoutManager allim_LayoutManager;
    Button Fake_Btn, Allim_Del_Btn;

    static ArrayList<allim_Data> allim_Array = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allim);



        Allim_Del_Btn = findViewById(R.id.Allim_Del_Btn);
        Fake_Btn = findViewById(R.id.Fake_Btn);
        Allim_Icon_Back = findViewById(R.id.Allim_Icon_Back);
        allim_Recycle = findViewById(R.id.Allim_Recycle);

        allim_Recycle.setHasFixedSize(true);

        allim_LayoutManager = new LinearLayoutManager(allim_Activity.this, LinearLayoutManager.VERTICAL, false);

        allim_Recycle.setLayoutManager(allim_LayoutManager);

        allim_Adapter = new allim_Adapter(allim_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        allim_Recycle.setAdapter(allim_Adapter);

        Allim_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Allim_Del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allim_Array.clear();
                allim_Adapter.notifyDataSetChanged();
            }
        });

        Fake_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allim_Data a = new allim_Data("댓글이 달렸습니다.");
                allim_Array.add(0, a);
                allim_Adapter.notifyDataSetChanged();
            }
        });



    }
}