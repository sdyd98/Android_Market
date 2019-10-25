package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Mymenu_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 내가 본상품 고유번호
    static ArrayList<Integer> My_Menu_Number_Array = new ArrayList();

    // 내가 본상품
    ArrayList<Item_DB> My_Menu_Array = new ArrayList();
    
    // 뷰 서언
    ImageView My_Menu_Icon_Close;
    ImageView My_Menu_Icon_User;
    TextView My_Menu_Icon_Chat, My_Menu_Icon_Allim, My_Menu_Icon_Keyword_Allim, My_Menu_User_Name, Item_Size, Heart_Size, Follower_Size, Following_Size;
    LinearLayout Item_Layout, Heart_Layout, Following_Layout, Followers_Layout;
    Button My_Menu_Logout;

    // 리사이클러뷰 선언
    RecyclerView My_Menu_Recycle;
    RecyclerView.Adapter My_Menu_Adapter;
    RecyclerView.LayoutManager My_Menu_LayoutManager;

    // 로그인 유저 아이디
    String Login_User_Id;
    // 로그인 유저 포지션
    private int User_position;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymenu);

        // 로그인 유저 아이디 인텐트 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 유저 정보 가져오기
        User_getShared();

        // 내아이템 정보 전체 저장
        Item_getShared();

        // 유저 포지션 파악
        User_Position_Check();

        // 뷰매칭
        My_Menu_User_Name = findViewById(R.id.My_Menu_User_Name);
        My_Menu_Icon_Allim = findViewById(R.id.My_Menu_Icon_Allim);
        My_Menu_Icon_Chat = findViewById(R.id.My_Menu_Icon_Chat);
        Item_Layout = findViewById(R.id.Item_Layout);
        Heart_Layout = findViewById(R.id.Heart_Layout);
        Following_Layout = findViewById(R.id.Following_Layout);
        Followers_Layout = findViewById(R.id.Follower_Layout);
        My_Menu_Icon_Close = findViewById(R.id.My_Menu_Icon_Close);
        My_Menu_Icon_User = findViewById(R.id.My_Menu_Icon_User);
        My_Menu_Icon_Keyword_Allim = findViewById(R.id.My_Menu_Icon_Keyword_Allim);
        My_Menu_Logout = findViewById(R.id.My_Menu_Logout);
        My_Menu_Recycle = findViewById(R.id.My_Menu_Recycle);
        Item_Size = findViewById(R.id.Item_Size);
        Heart_Size = findViewById(R.id.Heart_Size);
        Follower_Size = findViewById(R.id.Follower_Size);
        Following_Size = findViewById(R.id.Following_Size);

        // ?
        My_Menu_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 선언
        My_Menu_LayoutManager = new LinearLayoutManager(Mymenu_Activity.this, LinearLayoutManager.HORIZONTAL, false);

        // 레이아웃 매니저 셋팅
        My_Menu_Recycle.setLayoutManager(My_Menu_LayoutManager);

        // 액티비티 기본 정보 셋팅
        Item_Size.setText(String.valueOf(User_Item_Count()));
        My_Menu_User_Name.setText(User_Db_ArrayList.get(User_position).getUser_name());
        My_Menu_Icon_User.setImageURI(Uri.parse(User_Db_ArrayList.get(User_position).getUser_icon_img()));
        Heart_Size.setText(String.valueOf(User_Db_ArrayList.get(User_position).getUser_Select().size()));
        Follower_Size.setText(String.valueOf(User_Db_ArrayList.get(User_position).getUser_Follower().size()));
        Following_Size.setText(String.valueOf(User_Db_ArrayList.get(User_position).getUser_Following().size()));


        // 내가 본 상품          내가본 상품 판단 - > 게시물을 들어 갔을때 알려줘야함
        My_Menu_Adapter = new My_Menu_Adapter(My_Menu_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null) {
                    int position = (int)obj;
                    ((My_Menu_Adapter)My_Menu_Adapter).getData(position);
                    Intent intent = new Intent(Mymenu_Activity.this, Buy_Activity.class);

                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getItem_number());
                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", Login_User_Id);

                    startActivity(intent);
                    //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                    // 하나씩 넘길수도 있고
                    // 한번에 넘길수도 있다 (Serializable)
                }
            }
        });
        My_Menu_Recycle.setAdapter(My_Menu_Adapter);

//        // 이름 설정
//        if(My_Menu_Detail_Activity.User_Name != null){
//            My_Menu_User_Name.setText(My_Menu_Detail_Activity.User_Name);
//        }
//
//        //  이미지 설정
//        if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
//            My_Menu_Icon_User.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
//        }

        // 알림 목록 클릭
        My_Menu_Icon_Allim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, allim_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });


        // 키워드 알림 클릭
        My_Menu_Icon_Keyword_Allim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Keyword_allim_Activity.class);
                startActivity(intent);
            }
        });

        // 채팅 클릭
        My_Menu_Icon_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });

        // 팔로워 목록
        Followers_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Followers_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });

        // 팔로잉 목록
        Following_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Following_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });

        // 찜 클릭
        Heart_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Select_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });

        // 내 상품 클릭
        Item_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, My_Item_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });

        // 아이콘 선택
        My_Menu_Icon_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, My_Menu_Detail_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });


        // 닫기 버튼
        My_Menu_Icon_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Main_Activity.class);
                setResult( RESULT_OK, intent);
                finish();
            }
        });

        // 로그아웃 버튼 - > 메인화면으로 전환
        My_Menu_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 화면에서 로그인 화면으로 전환
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                User_Db_ArrayList.get(User_position).setAuto_login(false);
                My_Menu_Array.clear();
                My_Menu_Number_Array.clear();
                Main_Activity.Main_activity.finish();
                User_Save_Shared();
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다." , Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        if (requestCode == TEST_DATA) {
//            if(My_Menu_Detail_Activity.User_Name != null){
//                My_Menu_User_Name.setText(My_Menu_Detail_Activity.User_Name);
//            }
//            if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
//                My_Menu_Icon_User.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
//            }
//        }
//    }

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

    // 유저 포지션 확인
    public void User_Position_Check(){
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_Id)){
                User_position = i;
                break;
            }
        }
    }

    // 로그인한 유저에 상품 목록
    public int User_Item_Count(){
        int Item_count = 0;
        for(int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_id().equals(Login_User_Id)){
                Item_count++;
            }
        }
        return  Item_count;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // 유저 어레이 초기화
        User_Db_ArrayList.clear();

        // 아이템 어레이 초기화
        item_db_array.clear();

        // 유저 정보 가져오기
        User_getShared();

        // 내아이템 정보 전체 저장
        Item_getShared();

        // 유저 포지션 파악
        User_Position_Check();

        // 액티비티 기본 정보 셋팅
        Item_Size.setText(String.valueOf(User_Item_Count()));
        My_Menu_User_Name.setText(User_Db_ArrayList.get(User_position).getUser_name());
        My_Menu_Icon_User.setImageURI(Uri.parse(User_Db_ArrayList.get(User_position).getUser_icon_img()));
        Heart_Size.setText(String.valueOf(User_Db_ArrayList.get(User_position).getUser_Select().size()));
        Follower_Size.setText(String.valueOf(User_Db_ArrayList.get(User_position).getUser_Follower().size()));
        Following_Size.setText(String.valueOf(User_Db_ArrayList.get(User_position).getUser_Following().size()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 내가 본 상품 어레이 초기화
        My_Menu_Array.clear();

        // 내가본 상품 판단
        for(int i = 0; i < My_Menu_Number_Array.size(); i++){
            for(int j = 0; j < item_db_array.size(); j++){
                if(item_db_array.get(j).getItem_number() == My_Menu_Number_Array.get(i)){
                    My_Menu_Array.add(0,item_db_array.get(j));
                }
            }
        }
        My_Menu_Adapter.notifyDataSetChanged();
    }
}
