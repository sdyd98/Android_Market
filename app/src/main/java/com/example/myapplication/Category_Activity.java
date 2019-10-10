package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Category_Activity extends AppCompatActivity {

    ImageView Category_Back_Btn, Category_Cpu_Btn, Category_Gpu_Btn, Category_Ram_Btn, Category_MB_Btn, Category_Ssd_Btn, Category_Hdd_Btn, Category_Power_Btn, Category_Cooler_Btn;
    ImageView Category_Sell_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        Category_Back_Btn = findViewById(R.id.Category_Back_Btn);
        Category_Cpu_Btn = findViewById(R.id.Category_Cpu_Btn);
        Category_Gpu_Btn = findViewById(R.id.Category_Gpu_Btn);
        Category_Ram_Btn = findViewById(R.id.Category_Ram_Btn);
        Category_MB_Btn = findViewById(R.id.Category_MB_Btn);
        Category_Ssd_Btn = findViewById(R.id.Category_Ssd_Btn);
        Category_Hdd_Btn = findViewById(R.id.Category_Hdd_Btn);
        Category_Power_Btn = findViewById(R.id.Category_Power_Btn);
        Category_Cooler_Btn = findViewById(R.id.Category_Cooler_Btn);

        Category_Cpu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_Activity.this, Category_Cpu_Activity.class);
                startActivity(intent);
            }
        });



        Category_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}