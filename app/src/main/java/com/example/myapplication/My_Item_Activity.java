package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class My_Item_Activity extends AppCompatActivity {

    ImageView My_Menu_Back;
    RecyclerView My_Item_Recycle;
    RecyclerView.Adapter My_Item_Adapter;
    RecyclerView.LayoutManager My_Item_LayoutManager;
    TextView My_Item_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myitem);

        My_Menu_Back = findViewById(R.id.My_Item_Back);
        My_Item_Count = findViewById(R.id.My_Item_Count);
        My_Menu_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        My_Item_Recycle = findViewById(R.id.My_Item_Recycle);
        My_Item_Recycle.setHasFixedSize(true);
        My_Item_LayoutManager = new LinearLayoutManager(My_Item_Activity.this, LinearLayoutManager.VERTICAL, false);

        My_Item_Recycle.setLayoutManager(My_Item_LayoutManager);

        My_Item_Adapter = new My_Item_Adapter(Main_Activity.test1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    ((My_Item_Adapter)My_Item_Adapter).getData(position);
                    Intent intent = new Intent(My_Item_Activity.this, Buy_Activity.class);
                    intent.putExtra("Item_Name", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Name());
                    intent.putExtra("Item_Price", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Price());
                    intent.putExtra("Item_Img", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Img());
                    intent.putExtra("Item_Detail", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Detail());
                    intent.putExtra("Item_Categori", ((My_Item_Adapter)My_Item_Adapter).getData(position).getCategori_Name());
                    startActivity(intent);
                }
            }
        });
        My_Item_Recycle.setAdapter(My_Item_Adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        My_Item_Adapter.notifyDataSetChanged();
        if(Main_Activity.test1.size() == 0) {
            My_Item_Count.setText("나의 상품 0개");
        }
        else{
            My_Item_Count.setText("나의 상품 "+Main_Activity.test1.size() + "개");
        }
    }
}