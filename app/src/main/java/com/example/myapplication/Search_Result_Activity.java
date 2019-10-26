package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class Search_Result_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 검색 결과 어레이 생성
    ArrayList<Item_DB> Search_Result_array = new ArrayList<>();

    // 검색어 저장 어레이 생성
    ArrayList<Search_Text_DB> Search_Text_Save_Array = new ArrayList<>();

    // 리사이클러뷰
    RecyclerView Search_Result_Recycle;
    RecyclerView.Adapter Search_Result_Adapter;
    RecyclerView.LayoutManager Search_Result_LayoutManager;

    ImageView Search_Result_Back;

    // 검색창
    TextView Search_Box;
    TextView Search_Text_Result;

    // 검색어 판단
    boolean Search_Check = false;

    // 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        // 전체 아이템 정보 가져오기
        Item_getShared();

        // 검색어 전체 정보 가져오기
        getSearch_Text_Save();

        // 검색어 인텐트 받은걸로 아이템 전체 목록에 존제하는지 판단
        for (int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_name().contains(getIntent().getStringExtra("Item_Name"))){
                Search_Result_array.add(item_db_array.get(i));
            }
        }

        // 만약 검색 결과가 없으면 검색결과 없음 액티비티로 전환
        if(Search_Result_array.size() == 0){
            finish();
            Intent intent = new Intent(getApplicationContext(), Result_Noting_Activity.class);
            intent.putExtra("Item_Name", getIntent().getStringExtra("Item_Name"));
            startActivity(intent);
        }
        // 검색된 결과의 아이템이 있다면 검색어 저장
        else{

            // 검색어 포지션 정보
            int Search_Text_Position = 0;

            // 검색어 저장 어레이 사이즈 만큼 반복
            for (int i = 0;  i < Search_Text_Save_Array.size(); i++){
                // 만약 저장된 어레이에 검색어가 이미 저장되어 있다면
                if(Search_Text_Save_Array.get(i).getSearch_Text().equals(getIntent().getStringExtra("Item_Name"))){
                    // 검색 체크 true
                    Search_Check = true;

                    // 검색어 포지션 저장
                    Search_Text_Position = i;

                    break;
                }
            }

            // 만약 검색한 검색어가 이미 검색되었다면
            if(Search_Check == true){

                // 검색어 카운트 + 1
                Search_Text_Save_Array.get(Search_Text_Position).setSearch_Count(Search_Text_Save_Array.get(Search_Text_Position).getSearch_Count()+1);

                Toast.makeText(getApplicationContext(), "검색된 횟수"+Search_Text_Save_Array.get(Search_Text_Position).getSearch_Count(), Toast.LENGTH_SHORT).show();
            }

            // 처음 검색한 검색어라면
            else{
                // 새로운 검색어 객체 생성
                Search_Text_DB search_text_db = new Search_Text_DB(getIntent().getStringExtra("Item_Name"), 1);

                // 검색어 저장 어레이에 검색어 정보 추가
                Search_Text_Save_Array.add(search_text_db);
            }

            // 쉐어드에 저장
            setSearch_Text_Save();
        }

        // 리사이클러뷰 매칭
        Search_Result_Recycle = findViewById(R.id.Search_Result_Recycle);
        Search_Result_Recycle.setHasFixedSize(true);
        Search_Result_LayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        Search_Result_Recycle.setLayoutManager(Search_Result_LayoutManager);

        // 뷰 매칭
        Search_Result_Back = findViewById(R.id.Search_Result_Back);
        Search_Box = findViewById(R.id.Search_Box);
        Search_Text_Result = findViewById(R.id.Search_Text_Result);

        // 검색결과 게시글 추가 어뎁터
        Search_Result_Adapter = new Search_Result_Adapter(Search_Result_array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 포지션 값 가져오기
                Object obj = v.getTag();
                if (obj != null) {
                    int position = (int) obj;

                    Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((Search_Result_Adapter) Search_Result_Adapter).getData(position).getItem_number());
                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                    startActivity(intent);

                }
            }
        });
        Search_Result_Recycle.setAdapter(Search_Result_Adapter);

        // 검색어 기반으로 검색 텍스트 변경
        Search_Text_Result.setText("'"+getIntent().getStringExtra("Item_Name")+"' 에 대한 검색결과 "+Search_Result_array.size()+"개");
        // 뒤로가기 버튼
        Search_Result_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 검색창 클릭
        Search_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search_Activity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    // 액티비티 시작되기 직전
    @Override
    protected void onResume() {
        super.onResume();

        item_db_array.clear();
        Search_Result_array.clear();

        // 전체 아이템 정보 가져오기
        Item_getShared();

        // 검색어 인텐트 받은걸로 아이템 전체 목록에 존제하는지 판단
        for (int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_name().contains(getIntent().getStringExtra("Item_Name"))){
                Search_Result_array.add(item_db_array.get(i));
            }
        }

        Search_Result_Adapter.notifyDataSetChanged();
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

    // 검색어 저장 쉐어드 get
    public void getSearch_Text_Save(){

        // 쉐어드 파일이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item",0);

        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String Search_DB_Array = sharedPreferences.getString("Search_Text", "");

        // JsonArray를 파싱하여 Search_DB형 어레이리스트에 담는과정
        if(Search_DB_Array !=  null) {
            try {

                JSONArray jsonArray_user_db = new JSONArray(Search_DB_Array);

                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    String data = jsonArray_user_db.optString(i);

                    Gson gson = new Gson();

                    Search_Text_DB search_text_db = gson.fromJson(data, Search_Text_DB.class);

                    Search_Text_Save_Array.add(search_text_db);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // 검색어 저장 쉐어드 set
    public void setSearch_Text_Save(){

        // 쉐어드 이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 예비 어레이리스트 생성
        ArrayList<String> Save_Search_Text_Array  = new ArrayList<>();

        // Search_Text 어레이 사이즈 만큼 반복
        for (int i = 0; i < Search_Text_Save_Array.size(); i++) {

            // gson 생성
            Gson gson = new Gson();

            // Search_DB  에 Search_Text 어레이 안에 있는 객체를 gson으로 파싱하여 저장
            String Search_DB = gson.toJson(Search_Text_Save_Array.get(i));

            // 예비 어레이에 파싱한 값 저장
            Save_Search_Text_Array.add(Search_DB );

        }

        // JsonArray 선언
        JSONArray setJsonArray = new JSONArray();

        // 예비 어레이 리스트 사이즈 만큼 반복
        for (int i = 0; i < Save_Search_Text_Array.size(); i++) {

            // JsonArray에 데이터 추가
            setJsonArray.put(Save_Search_Text_Array.get(i));

        }

        // User_DB 어레이리스트가 null이 아니라면
        if (!Search_Text_Save_Array.isEmpty()) {

            // 키값에 데이터 저장
            editor.putString("Search_Text", setJsonArray.toString());

        } else {

            // 빈 값 저장
            editor.putString("Search_Text", null);

        }

        // 저장완료
        editor.commit();

    }
}