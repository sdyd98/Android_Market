package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Keyword_allim_Activity extends AppCompatActivity {

    // 유저 정보를 담을 어레이
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    Button Keyword_Add_Btn;
    ImageView Keyword_Icon_Back;

    // 로그인 유저 아이디
    String Login_User_ID;

    // 로그인 유저 포지션
    int Login_User_Position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keywordallim);

        Login_User_ID = getIntent().getStringExtra("User_ID");

        //뷰 매칭
        Keyword_Add_Btn = findViewById(R.id.Keyword_Add_Btn);
        Keyword_Icon_Back = findViewById(R.id.Keyword_Icon_Back);

        // 유저 포지션 파악
        Login_User_Position_Check();

        // 키워드 추가 버튼
        Keyword_Add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Keyword_allim_Activity.this, Keyword_allim_Input_Activity.class);
                startActivity(intent);
            }
        });

        // 뒤로가기
        Keyword_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 유저 정보 가져오기
    public void User_getShared(){
        // 쉐어드 파일이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("User",0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if(user_db_array !=  null) {
            try {
                JSONArray jsonArray_user_db = new JSONArray(user_db_array);
                for (int i = 0; i < jsonArray_user_db.length(); i++) {
                    String data = jsonArray_user_db.optString(i);
                    Log.e("test3", data);
                    Gson gson = new Gson();
                    User_DB user_db = gson.fromJson(data, User_DB.class);
                    User_Db_ArrayList.add(user_db);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 로그인한 유저 position 얻기
    public void Login_User_Position_Check(){
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_ID)){
                Login_User_Position = i;
            }
        }
    }
}