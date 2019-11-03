package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Keyword_allim_Input_Activity extends AppCompatActivity {


    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 뷰 선언
    ImageView Keyword_Allim_Close;
    Button Keyword_Finish_Btn;
    AppCompatEditText MinPrice;
    AppCompatEditText MaxPrice;
    AppCompatEditText Keyword;

    // 로그인 유저 아이디
    private String Login_User_Id;

    // 로그인한 유저 포지션
    private int Login_User_Position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keywordinput);

        // 유저 정보 get shared
        User_getShared();

        // 로그인 유저 포지션 확인
        User_Position_Check();

        // 로그인 유저아이디 인텐트 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 뷰 매칭
        Keyword_Allim_Close = findViewById(R.id.Keyword_Allim_Close);
        Keyword = findViewById(R.id.Keyword);
        MinPrice = findViewById(R.id.searchtext2);
        MaxPrice = findViewById(R.id.searchtext3);
        Keyword_Finish_Btn = findViewById(R.id.Keyword_Finish_Btn);

        //TODO 예외처리 해야됨
        //완료 버튼
        Keyword_Finish_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키워드 객체 생성
                Keyword_DB keyword_db = new Keyword_DB(Keyword.getText().toString(),Integer.valueOf(MinPrice.getText().toString()),Integer.valueOf(MaxPrice.getText().toString()));

                // 유저 정보에 키워드 추가
                User_Db_ArrayList.get(Login_User_Position).getUser_Keyword().add(keyword_db);

                // 유저 정보 쉐어드에 저장
                User_Save_Shared();

                // 현재 액티비티 종료
                finish();
            }
        });

        // 닫기 버튼
        Keyword_Allim_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 유저 정보 쉐어드 get
    public void User_getShared() {

        // 쉐어드 이름과 모드 설정
        SharedPreferences sharedPreferences = getSharedPreferences("User", 0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if (user_db_array != null) {
            try {

                // Key="Data" 값을 이용해 JsonArray 생성
                JSONArray jsonArray_user_db = new JSONArray(user_db_array);

                // JsonArray 길이 만큼 반복
                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    // JsonArray 값을 data에 저장
                    String data = jsonArray_user_db.optString(i);

                    // Gson 생성
                    Gson gson = new Gson();

                    // data 값을 gson으로 파싱 하여 객체 생성
                    User_DB user_db = gson.fromJson(data, User_DB.class);

                    // 생성된 객체 추가
                    User_Db_ArrayList.add(user_db);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 유저 포지션 확인
    public void User_Position_Check(){
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_Id)){
                Login_User_Position = i;
                break;
            }
        }
    }

    // 유저 정보 저장하기
    public void User_Save_Shared(){
        // 쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("User", 0);
        // 쉐어드 저장한다 선언
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 예비 어레이리스트 생성하고 현재 user_db_array 스트링값으로 파싱하여 저장
        ArrayList<String> saveUser_Db_Array = new ArrayList<>();

        // User_Db 어레이 사이즈 만큼 반복
        for (int i = 0; i < User_Db_ArrayList.size(); i++) {

            // gson 생성
            Gson gson = new Gson();

            // user_db 에 User_DB 어레이 안에 있는 객체를 gson으로 파싱하여 저장
            String user_db = gson.toJson(User_Db_ArrayList.get(i));

            // 예비 어레이에 파싱한 값 저장
            saveUser_Db_Array.add(user_db);

        }

        // JsonArray 선언
        JSONArray setJsonArray = new JSONArray();

        // 예비 어레이 리스트 사이즈 만큼 반복
        for (int i = 0; i < saveUser_Db_Array.size(); i++) {

            // JsonArray에 데이터 추가
            setJsonArray.put(saveUser_Db_Array.get(i));

        }

        // User_DB 어레이리스트가 null이 아니라면
        if (!User_Db_ArrayList.isEmpty()) {

            // 키값에 데이터 저장
            editor.putString("Data", setJsonArray.toString());

        } else {

            // 빈 값 저장
            editor.putString("Data", null);

        }

        // 저장완료
        editor.commit();

    }
}