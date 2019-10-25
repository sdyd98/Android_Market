package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 뷰 선언
    ImageView Profile_Back_Btn;
    TextView Profile_Bar_User_Name, Profile_User_Name, Profile_Item_Count;
    CircleImageView Profile_User_Icon;

    // 프로필 아이디
    String Profile_Id;

    // 프로필 유저 아이템 어레이 리스트
    ArrayList<Item_DB> Profile_Array = new ArrayList<>();

    // 리사이클러뷰 선언
    RecyclerView Profile_Recycle;
    RecyclerView.LayoutManager Profile_LayoutManager;
    RecyclerView.Adapter Profile_Adapter;

    private int Profile_Position;
    private String Login_User_Id;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // 프로필 아이디 받기
        Profile_Id = getIntent().getStringExtra("Profile_Id");

        // 로그인 유저 아이디 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 유저 정보 저장
        User_getShared();

        // 아이템 정보 저장
        Item_getShared();

        // 프로필 어레이 set
        setProfile_Array();

        // 프로필 포지션 찾기
        Profile_Position_Check();

        // 리사이클러뷰 매칭
        Profile_Recycle = findViewById(R.id.Profile_Recycle);

        // ?
        Profile_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 생성
        Profile_LayoutManager = new GridLayoutManager(getApplicationContext(), 3);

        // 레이아웃 매니저 셋팅
        Profile_Recycle.setLayoutManager(Profile_LayoutManager);

        // 어뎁터 생성
        Profile_Adapter = new Profile_Adapter(Profile_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
                    intent.putExtra("User_ID", Login_User_Id);
                    intent.putExtra("Item_Number", ((Profile_Adapter)Profile_Adapter).getData(position).getItem_number());
                    startActivity(intent);
                }

            }
        });
        // 어뎁터 셋팅
        Profile_Recycle.setAdapter(Profile_Adapter);


        // 뷰 매칭
        Profile_Back_Btn = findViewById(R.id.Profile_Back_Btn);
        Profile_Bar_User_Name = findViewById(R.id.Profile_Bar_User_Name);
        Profile_User_Icon = findViewById(R.id.Profile_User_Icon);
        Profile_User_Name = findViewById(R.id.Profile_User_Name);
        Profile_Item_Count = findViewById(R.id.Profile_Item_Count);

        // 레이아웃 기본 정보 셋팅
        Profile_Bar_User_Name.setText(User_Db_ArrayList.get(Profile_Position).getUser_name()+" 님의 프로필");
        Profile_User_Icon.setImageURI(Uri.parse(User_Db_ArrayList.get(Profile_Position).getUser_icon_img()));
        Profile_User_Name.setText(User_Db_ArrayList.get(Profile_Position).getUser_name());
        Profile_Item_Count.setText("상품 ("+Profile_Array.size()+")");

        // 뒤로가기
        Profile_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        item_db_array.clear();

        User_Db_ArrayList.clear();

        Profile_Array.clear();

        Item_getShared();

        User_getShared();

        setProfile_Array();


        Profile_Adapter.notifyDataSetChanged();

    }

    // 아이템 정보 쉐어드 get
    public void Item_getShared(){

        // 쉐어드 파일 이름 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        // 키값을 통해 데이터 가져옴
        String item_db_array_string = sharedPreferences.getString("data","");

        // 키값을 통해 가져온 데이터가 null 값이 아니라면
        if(item_db_array_string != null){
            try {

                // item_db_array_string (String) 데이터로 JSonArray 생성
                JSONArray jsonArray_item_db = new JSONArray(item_db_array_string);

                // JsonArray 길이 만큼 반복
                for(int i = 0; i < jsonArray_item_db.length(); i++){

                    // JsonArray에 있는 값을 data에 저장
                    String data = jsonArray_item_db.optString(i);

                    // gson 생성
                    Gson gson = new Gson();

                    // item_db에 gson을 이용해 data값 파싱하여 객체 생성
                    Item_DB item_db = gson.fromJson( data, Item_DB.class);

                    // 생성된 객체 어레이에 추가
                    item_db_array.add(item_db);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    // 프로필 주인 상품
    public void setProfile_Array(){
        // 아이템 전체 갯수만큼 반복
        for (int i = 0; i < item_db_array.size(); i++){
            // 프로필 주인 아이디와 같은 아이디가 있으면
            if(item_db_array.get(i).getItem_id().equals(Profile_Id)){
                // 그 아이템 프로필 어레이에 저장
                Profile_Array.add(item_db_array.get(i));
            }
        }
    }

    // 프로필 주인 포지션 찾기
    public void Profile_Position_Check(){
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Profile_Id)){
                Profile_Position = i;
                break;
            }
        }
    }

}