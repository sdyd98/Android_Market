package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Following_Activity extends AppCompatActivity {

    // 유저 정보를 담을 어레이
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 팔로잉 어레이 리스트
    ArrayList<User_DB> Following_Array = new ArrayList<>();

    //뷰 선언
    ImageView Following_Icon_Back;

    //리사이클러뷰 선언
    RecyclerView Following_Recycle;
    RecyclerView.LayoutManager Following_LayoutManager;
    RecyclerView.Adapter Following_Adapter;

    //로그인 유저 아이디
    String Login_User_ID;

    //로그인 유저 포지션
    int Login_User_Position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_following);

        //로그인 유저 아이디 인텐트
        Login_User_ID = getIntent().getStringExtra("User_ID");

        // 유저 정보 가져오기
        User_getShared();

        // 아이템 정보 가져오기
        Item_getShared();

        //로그인 유저 아이디 포지션 체크
        Login_User_Position_Check();

        // 팔로잉 리스트 생성
        Following_Check();

        // 리사이클러뷰 매칭
        Following_Recycle = findViewById(R.id.Following_Recycle);

        // ?
        Following_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 설정
        Following_LayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        // 레이아웃 매니저 set
        Following_Recycle.setLayoutManager(Following_LayoutManager);

        // 어뎁터 생성
        Following_Adapter = new Following_Adapter(Following_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if (obj != null) {
                    int position = (int) obj;
                    Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
                    intent.putExtra("User_ID", Login_User_ID);
                    intent.putExtra("Profile_Id", ((Following_Adapter) Following_Adapter).getData(position).getUser_id());
                    startActivity(intent);
                }
            }
        }, item_db_array);
        Following_Recycle.setAdapter(Following_Adapter);


        // 뷰매칭
        Following_Icon_Back = findViewById(R.id.Following_Icon_Back);



        //뒤로가기
        Following_Icon_Back.setOnClickListener(new View.OnClickListener() {
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

    // 로그인한 유저 position 얻기
    public void Login_User_Position_Check(){
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_ID)){
                Login_User_Position = i;
            }
        }
    }

    // 팔로잉 어뎁터 어레이에 유저 어레이 추가
    public void Following_Check(){
        // 로그인 유저 팔로잉 어레이 만큼 반복
        for(int i = 0; i < User_Db_ArrayList.get(Login_User_Position).getUser_Following().size(); i++){
            // 전체 유저 수 만큼 어레이 반복
            for(int j = 0; j < User_Db_ArrayList.size(); j++){
                // 팔로잉 어레이 유저 아이디 값과 전체 유저 어레이 유저 아이디 값이 같다면
                if(User_Db_ArrayList.get(Login_User_Position).getUser_Following().get(i).equals(User_Db_ArrayList.get(j).getUser_id())){
                    // 해당 유저 팔로잉 어뎁터 어레이에 추가
                    Following_Array.add(User_Db_ArrayList.get(j));
                    break;
                }
            }
        }
    }
}