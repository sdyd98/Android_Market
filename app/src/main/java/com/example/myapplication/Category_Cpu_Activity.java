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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Category_Cpu_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    ImageView Category_Cpu_Back_Btn, Category_Cpu_Mymenu_Btn, Category_Cpu_Search_Icon;
    TextView Category_Name;
    //static int Category_position_number;

    //리사이클러뷰 선언
    private RecyclerView Category_Item_Recycle;
    // 어뎁터 선언
    private RecyclerView.Adapter cimAdapter;
    // 레이아웃 매니저 선언
    private RecyclerView.LayoutManager LayoutManager_Category_Item_Recycle;

    // 셋팅값 선언
    ArrayList<Item_DB> Category_Item_Cpu = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_Gpu = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_RAM = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_MB = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_SSD = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_HDD = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_POWER = new ArrayList<>();
    ArrayList<Item_DB> Category_Item_COOLER = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categori_cpu);

        Item_getShared();

        //리사이클러뷰 매칭
        Category_Item_Recycle = findViewById(R.id.Category_Detail_Recycle);

        // 얘는 뭘까
        Category_Item_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저  getApplicationContext?? 무엇
        LayoutManager_Category_Item_Recycle = new GridLayoutManager(getApplicationContext(), 3);

        // 레이아웃 매니저 set
        Category_Item_Recycle.setLayoutManager(LayoutManager_Category_Item_Recycle);

        Category_Name = findViewById(R.id.Category_Name);
        Category_Cpu_Search_Icon = findViewById(R.id.Category_Cpu_Search_Icon);
        Category_Cpu_Back_Btn = findViewById(R.id.Category_Cpu_Back_Btn);
        Category_Cpu_Mymenu_Btn = findViewById(R.id.Category_Cpu_Mymenu_Btn);
        Category_Name.setText(getIntent().getStringExtra("Category_Name"));



        if(Category_Name.getText().toString().equals("CPU")) {
            Category_Item_Cpu.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("CPU")) {
                    Category_Item_Cpu.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_Cpu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //Category_position_number = position;
//                            intent.putExtra("Item_Name", ((Category_Adapter) cimAdapter).getData(position).getItem_Name());
//                            intent.putExtra("Item_Price", ((Category_Adapter) cimAdapter).getData(position).getItem_Price());
//                            intent.putExtra("Item_Img", ((Category_Adapter) cimAdapter).getData(position).getItem_Img());
//                            intent.putExtra("Item_Detail", ((Category_Adapter) cimAdapter).getData(position).getItem_Detail());
//                            intent.putExtra("Item_Categori", ((Category_Adapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
            }






        if(Category_Name.getText().toString().equals("GPU")) {
            Category_Item_Gpu.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("GPU")) {
                    Category_Item_Gpu.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_Gpu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //Category_position_number = position;
//                            intent.putExtra("Item_Name", ((Category_Adapter) cimAdapter).getData(position).getItem_Name());
//                            intent.putExtra("Item_Price", ((Category_Adapter) cimAdapter).getData(position).getItem_Price());
//                            intent.putExtra("Item_Img", ((Category_Adapter) cimAdapter).getData(position).getItem_Img());
//                            intent.putExtra("Item_Detail", ((Category_Adapter) cimAdapter).getData(position).getItem_Detail());
//                            intent.putExtra("Item_Categori", ((Category_Adapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
        }







        if(Category_Name.getText().toString().equals("RAM")) {
            Category_Item_RAM.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("RAM")) {
                    Category_Item_RAM.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_RAM, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
//                            intent.putExtra("Item_Name", ((Category_Adapter) cimAdapter).getData(position).getItem_Name());
//                            intent.putExtra("Item_Price", ((Category_Adapter) cimAdapter).getData(position).getItem_Price());
//                            intent.putExtra("Item_Img", ((Category_Adapter) cimAdapter).getData(position).getItem_Img());
//                            intent.putExtra("Item_Detail", ((Category_Adapter) cimAdapter).getData(position).getItem_Detail());
//                            intent.putExtra("Item_Categori", ((Category_Adapter) cimAdapter).getData(position).getCategori_Name());
                            //Category_position_number = position;
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
        }






        if(Category_Name.getText().toString().equals("MB")) {
            Category_Item_MB.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("MB")) {
                    Category_Item_MB.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_MB, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
        }






        if(Category_Name.getText().toString().equals("SSD")) {
            Category_Item_SSD.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("SSD")) {
                    Category_Item_SSD.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_SSD, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);

        }






        if(Category_Name.getText().toString().equals("HDD")) {
            Category_Item_HDD.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("HDD")) {
                    Category_Item_HDD.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_HDD, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
            }








        if(Category_Name.getText().toString().equals("POWER")) {
            Category_Item_POWER.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("Power")) {
                    Category_Item_POWER.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_POWER, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);

        }






        if(Category_Name.getText().toString().equals("COOLER")) {
            Category_Item_COOLER.clear();
            for (int i = 0; i < item_db_array.size(); i++) {
                if (item_db_array.get(i).getCategory_name().equals("Cooler")) {
                    Category_Item_COOLER.add(item_db_array.get(i));
                }
            }
                cimAdapter = new Category_Adapter(Category_Item_COOLER, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);

                            intent.putExtra("Item_Number", ((Category_Adapter)cimAdapter).getData(position).getItem_number());
                            // 현재 접속 유저 아이디 인텐트
                            intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                            startActivity(intent);
                            //intent.putStringArrayListExtra("array",((Category_Adapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
            }



        Category_Cpu_Mymenu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_Cpu_Activity.this, Mymenu_Activity.class);
                startActivity(intent);
            }
        });

        Category_Cpu_Search_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_Cpu_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        Category_Cpu_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent intent = new Intent(getApplicationContext(), Category_Cpu_Activity.class);
        intent.putExtra("Category_Name", Category_Name.getText().toString());
        intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}