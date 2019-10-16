package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 검색된 단어 어레이 생성
    ArrayList<String> search_item = new ArrayList<>();

    // 리사이클러뷰
    RecyclerView Search_Recycle;
    RecyclerView.Adapter Search_Adapter;
    RecyclerView.LayoutManager Search_LayoutManager;

    // 뷰선언
    ImageView Search_Icon_Back_Btn, Search_Btn;
    EditText Search_text;

    // 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // 아이템 정보 가져옴
        Item_getShared();

        // 뷰 매칭
        Search_Icon_Back_Btn = findViewById(R.id.Search_Result_Back);
        Search_text = findViewById(R.id.Search_text);
        Search_Recycle = findViewById(R.id.Search_Recycle);
        Search_Btn = findViewById(R.id.Search_Btn);

        // ??
        Search_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 리니어
        Search_LayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        // 레이아웃 매니저 적용
        Search_Recycle.setLayoutManager(Search_LayoutManager);

        // 포커스 주기
        Search_text.requestFocus();

        // 키보드 자동 올리기
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Search_text, 0);

        // 키보드 검색 버튼 누르면 수행
        Search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == EditorInfo.IME_ACTION_SEARCH){
                    Intent intent = new Intent(getApplicationContext(), Search_Result_Activity.class);
                    // 검색어 인텐트
                    intent.putExtra("Item_Name", Search_text.getText().toString());
                    // 아이디 인텐트
                    intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        // 검색 버튼
        Search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search_Result_Activity.class);
                // 검색어 인텐트
                intent.putExtra("Item_Name", Search_text.getText().toString());
                // 아이디 인텐트
                intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));
                startActivity(intent);
                finish();
            }
        });

        // 서치화면 Back 버튼
        Search_Icon_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 검색창 단어 실시간 체크
        Search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // 검색어 치는 동안 로직
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 게시글 전체 갯수 만큼 반복
                for(int i = 0; i < item_db_array.size(); i++){
                    // 검색어가 포함된 게시글 있을시 어레이에 추가
                    if(item_db_array.get(i).getItem_name().contains(Search_text.getText().toString())){
                        // 단 이미 검색된 어레이에 있으면 추가 안함
                            if(!search_item.contains(item_db_array.get(i).getItem_name())){
                                search_item.add(item_db_array.get(i).getItem_name());
                        }
                    }
                }
            }

            // 검색어 다친후 로직
            @Override
            public void afterTextChanged(Editable s) {
                // 공백이라면 검색된 아이템 초기화
                if(Search_text.getText().toString().equals("")){
                    search_item.clear();
                }
                // 검색어랑 이미 들어가 있는 아이템이 일치 하지 않다면 검색된 아이템 삭제
                for(int i = 0; i < search_item.size(); i++){
                    if(!search_item.get(i).contains(Search_text.getText().toString())){
                        search_item.remove(i);
                    }
                }

                for(int i = 0; i < search_item.size(); i++){

                }

                // 검색된 아이템 이름 어뎁터
                Search_Adapter = new Search_Adapter(search_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 포지션 값 가져오기
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(getApplicationContext(), Search_Result_Activity.class);
                            // 아이템 이름 인텐트
                            intent.putExtra("Item_Name", ((Search_Adapter) Search_Adapter).getData(position));
                            // 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));
                            startActivity(intent);
                            finish();
                        }
                    }
                }, Search_text.getText().toString());
                Search_Recycle.setAdapter(Search_Adapter);
            }
        });
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
}