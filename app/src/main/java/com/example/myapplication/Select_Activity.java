package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Select_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    //찜 목록 어레이 선언
    ArrayList<Item_DB> Select_Array = new ArrayList<>();

    //로그인 유저 아이디
    String Login_User_Id;

    // 유저 포지션
    int User_position;

    // 뷰 선언
    ImageView Heart_Icon_Back;
    TextView Select_Count;

    // 리사이클러뷰 선언
    RecyclerView Select_Recycle;
    RecyclerView.Adapter Select_Adapter;
    RecyclerView.LayoutManager Select_LayoutManager;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_selet);

        // 유저 정보 불러오기
        User_getShared();

        // 아이템 정보 불러오기
        Item_getShared();

        // 로그인 유저 아이디
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 유저 포지션 체크
        User_Position_Check();

        // 찜 예비 어레이에 데이터 추가
        Select_Array_Add();

        // 뷰 매칭
        Heart_Icon_Back = findViewById(R.id.Heart_Icon_Back);
        Select_Recycle = findViewById(R.id.Select_Recycle);
        Select_Count = findViewById(R.id.Select_Count);

        // 게시글 기본 정보 셋팅
        Select_Count.setText("찜 목록");

        // ?
        Select_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 설정
        Select_LayoutManager = new GridLayoutManager(getApplicationContext(), 3);

        // 레이아웃 set
        Select_Recycle.setLayoutManager(Select_LayoutManager);

        // 어뎁터 생성
        Select_Adapter = new Select_Adapter(Select_Array, getApplicationContext(), Login_User_Id, User_Db_ArrayList, item_db_array);
        Select_Recycle.setAdapter(Select_Adapter);



        // 뒤로가기
        Heart_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    // 액티비티 정지
    @Override
    protected void onPause() {
        super.onPause();

        // 유저 정보 저장
        User_Save_Shared();

        // 아이템 정보 저장
        Item_refreshShared();
    }

    // 액티비티 재시작
    @Override
    protected void onRestart() {
        super.onRestart();

        // 유저 어레이 초기화
        User_Db_ArrayList.clear();

        // 아이템 어레이 초기화
        item_db_array.clear();

        // 예비 찜 어레이 초기화
        Select_Array.clear();

        // 유저 정보 가져오기
        User_getShared();

        // 내아이템 정보 전체 저장
        Item_getShared();

        // 찜 예비 어레이에 데이터 추가
        Select_Array_Add();

        Select_Adapter.notifyDataSetChanged();
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

    // 아이템 어레이 다시 저장
    public void Item_refreshShared(){

        // 쉐어드 이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<String> item_db_string_array = new ArrayList<>();

        //기존 어레이 모든 객체 하나씩 꺼내서 문자화 시킨후 예비 어레이에 추가
        Gson gson = new Gson();
        for(int i = 0; i < item_db_array.size(); i++){
            String data = gson.toJson(item_db_array.get(i));
            item_db_string_array.add(data);
        }

        // Json 선언
        JSONArray setJsonArray = new JSONArray();

        // 예비 어레이 값을 모두 JSonArray에 저장
        for (int i = 0; i < item_db_string_array.size(); i++){
            setJsonArray.put(item_db_string_array.get(i));
        }

        // 키값에 데이터 저장
        if(!item_db_array.isEmpty()){
            editor.putString("data", setJsonArray.toString());
        }
        else{
            editor.putString("data", null);
        }
        editor.commit();
    }

    // 찜 예비 어레이에 데이터 추가
    public  void Select_Array_Add(){
        for(int i = 0; i < User_Db_ArrayList.get(User_position).getUser_Select().size(); i++){
            // 아이템 전체 갯수 만큼 반복
            for(int j = 0; j < item_db_array.size(); j++){
                // 만약 유저 찜목록 아이템과 전체 목록 아이템 아이템 고유번호가 일치한다면
                if(User_Db_ArrayList.get(User_position).getUser_Select().get(i) == item_db_array.get(j).getItem_number()){
                    // 찜 예비 어레이에 추가
                    Select_Array.add(item_db_array.get(j));
                    break;
                }
            }
        }
    }

}