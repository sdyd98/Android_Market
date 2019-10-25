package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class allim_Activity extends AppCompatActivity {

    // 유저 정보를 담을 어레이
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 알림 어레이 리스트
    ArrayList<Allim_Db> Allim_Array = new ArrayList<>();

    // 뷰 선언
    ImageView Allim_Icon_Back;
    Button Allim_Del_Btn;

    // 리사이클러뷰 선언
    RecyclerView allim_Recycle;
    RecyclerView.Adapter allim_Adapter;
    RecyclerView.LayoutManager allim_LayoutManager;

    private String Login_User_Id;

    private int Login_User_Position;

    // 알림 스와이프
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            Allim_Array.remove(position);

            User_Db_ArrayList.get(Login_User_Position).getAllim_db().remove(position);

            allim_Adapter.notifyItemRemoved(position);

            User_Save_Shared();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allim);

        Login_User_Id = getIntent().getStringExtra("User_ID");

        User_getShared();

        Item_getShared();

        Login_User_Position_Check();

        setAllim_List();

        // 뷰 매칭
        Allim_Del_Btn = findViewById(R.id.Allim_Del_Btn);
        Allim_Icon_Back = findViewById(R.id.Allim_Icon_Back);
        allim_Recycle = findViewById(R.id.Allim_Recycle);

        // 리사이클러뷰 사이즈
        allim_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 생성
        allim_LayoutManager = new LinearLayoutManager(allim_Activity.this, LinearLayoutManager.VERTICAL, false);

        // 레이아웃 매니저 설정
        allim_Recycle.setLayoutManager(allim_LayoutManager);

        // 어뎁터 생성
        allim_Adapter = new allim_Adapter(Allim_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
                    intent.putExtra("User_ID", Login_User_Id);
                    intent.putExtra("Item_Number", ((allim_Adapter)allim_Adapter).getData(position).getItem_Number());
                    startActivity(intent);

                    User_Db_ArrayList.get(Login_User_Position).getAllim_db().remove(position);
                    User_Save_Shared();
                }
            }
        });
        allim_Recycle.setAdapter(allim_Adapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(allim_Recycle);

        // 알림 전체 삭제
        Allim_Del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Allim_Array.clear();
                User_Db_ArrayList.get(Login_User_Position).getAllim_db().clear();

                allim_Adapter.notifyDataSetChanged();

                User_Save_Shared();
            }
        });

        // 뒤로가기
        Allim_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        User_Db_ArrayList.clear();

        item_db_array.clear();

        Allim_Array.clear();

        User_getShared();

        Item_getShared();

        setAllim_List();

        allim_Adapter.notifyDataSetChanged();
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
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_Id)){
                Login_User_Position = i;
            }
        }
    }

    // 알림 목록 set
    public void setAllim_List(){
        for(int i =0; i < User_Db_ArrayList.get(Login_User_Position).getAllim_db().size(); i++){
            Allim_Array.add(0, User_Db_ArrayList.get(Login_User_Position).getAllim_db().get(i));
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