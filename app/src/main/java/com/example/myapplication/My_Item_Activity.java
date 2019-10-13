package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class My_Item_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 내 상품 어레이 생성
    ArrayList<Item_DB> my_item_db_array = new ArrayList<>();

    // 뷰 서언
    ImageView My_Menu_Back;
    RecyclerView My_Item_Recycle;
    RecyclerView.Adapter My_Item_Adapter;
    RecyclerView.LayoutManager My_Item_LayoutManager;
    TextView My_Item_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myitem);

        // 뷰 매칭
        My_Menu_Back = findViewById(R.id.My_Item_Back);
        My_Item_Count = findViewById(R.id.My_Item_Count);
        My_Item_Recycle = findViewById(R.id.My_Item_Recycle);

        My_Item_Recycle.setHasFixedSize(true);
        My_Item_LayoutManager = new LinearLayoutManager(My_Item_Activity.this, LinearLayoutManager.VERTICAL, false);
        My_Item_Recycle.setLayoutManager(My_Item_LayoutManager);



        //뒤로가기
        My_Menu_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 내상품 어뎁터
        My_Item_Adapter = new My_Item_Adapter(my_item_db_array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    ((My_Item_Adapter)My_Item_Adapter).getData(position);
                    Intent intent = new Intent(My_Item_Activity.this, Buy_Activity.class);

                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_number());
                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                    startActivity(intent);
//                    intent.putExtra("Item_Name", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Name());
//                    intent.putExtra("Item_Price", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Price());
//                    intent.putExtra("Item_Img", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Img());
//                    intent.putExtra("Item_Detail", ((My_Item_Adapter)My_Item_Adapter).getData(position).getItem_Detail());
//                    intent.putExtra("Item_Categori", ((My_Item_Adapter)My_Item_Adapter).getData(position).getCategori_Name());
                }
            }
        });
        My_Item_Recycle.setAdapter(My_Item_Adapter);
    }

    // 아이템 정보 쉐어드 get
    public void Item_getShared(){
        // 쉐어드 파일 이름 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        // 키값을 통해 데이터 가져옴
        String item_db_array_string = sharedPreferences.getString("data","");

        Log.e("check_123", item_db_array_string );
        // 키값을 통해 가져온 데이터가 null 값이 아니라면
        if(item_db_array_string != null){
            try {
                // String 데이터를 JSonArray 로 파싱 하여 다시 아이템 객체 어레이에 넣음
                JSONArray jsonArray_item_db = new JSONArray(item_db_array_string);
                for(int i = 0; i < jsonArray_item_db.length(); i++){
                    String data = jsonArray_item_db.optString(i);
                    Gson gson = new Gson();
                    Item_DB item_db = gson.fromJson( data, Item_DB.class);
                    item_db_array.add(item_db);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        item_db_array.clear();
        my_item_db_array.clear();

        // 아이템 정보 가져옴
        Item_getShared();

        // 내상품 판별
        for(int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_writer().equals(getIntent().getStringExtra("User_ID"))){
                my_item_db_array.add(item_db_array.get(i));
            }
        }


        // 새로고침
        My_Item_Adapter.notifyDataSetChanged();

        // 내상품 카운트
        if(my_item_db_array.size() == 0) {
            My_Item_Count.setText("나의 상품 0개");
        }
        else{
            My_Item_Count.setText("나의 상품 "+my_item_db_array.size() + "개");
        }
    }
}