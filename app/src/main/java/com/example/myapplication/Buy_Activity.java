package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Buy_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 내상품 어레이 객체 생성
    ArrayList<Item_DB> my_item_db_array = new ArrayList<>();

//    // 상품 고유 넘버
//    int Item_Number = getIntent().getIntExtra("Item_Number", 0);

    // 상품 position
    int position;

    // 로그인한 유저
    String User_ID;
    // requestcoed
    //final static int Sell_fix_Check = 750;

    // 뷰 선언
    ImageView Buy_Image, back, user, Buy_Search_Icon, user_icon;
    TextView price1, itemname, Buy_Call_Icon, item_text, Buy_Location_Icon, user_name, categori_name, timetext, looktext, heart_count;
    Button del, fix, Comments_Btn;
    EditText Comments_Detail;

    // 리사이클러뷰 ~
    private RecyclerView Comments_RecyclerView, My_Sell_Item_Recycle;
    private RecyclerView.Adapter CmmAdapter, My_Sell_Item_Adapter;
    private RecyclerView.LayoutManager LayoutManager_Comments, My_Sell_Item_LayoutManager;

    static ArrayList<Comments> Comments_Array = new ArrayList<>();
    //static ArrayList<Buy_My_Item_List> Buy_my_item = new ArrayList<>();


    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_interface);

        // 아이템 전체 정보 가져옴
        Item_getShared();

        // 게시물 고유번호로 포지션 확인
        for(int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_number() == getIntent().getIntExtra("Item_Number", 0)){
                position = i;
            }
        }


        // 뷰 매칭
        timetext = findViewById(R.id.timetext);
        looktext = findViewById(R.id.looktext);
        heart_count = findViewById(R.id.heart_count);
        Comments_Detail = findViewById(R.id.Comments_Detail);
        fix = findViewById(R.id.fix);
        del = findViewById(R.id.del);
        categori_name = findViewById(R.id.categori_name);
        user_name = findViewById(R.id.user_name);
        Buy_Location_Icon = findViewById(R.id.Buy_Location_Icon);
        item_text = findViewById(R.id.item_text);
        user_icon = findViewById(R.id.user_icon);
        Buy_Call_Icon = findViewById(R.id.Buy_Call_Icon);
        price1 = findViewById(R.id.price1);
        itemname = findViewById(R.id.itemname);
        Buy_Image = findViewById(R.id.Buy_Image);
        back = findViewById(R.id.back);
        user = findViewById(R.id.user);
        Comments_Btn = findViewById(R.id.Comments_Btn);
        Buy_Search_Icon = findViewById(R.id.Buy_Search_Icon);

        // 리사이클러뷰 매칭
        My_Sell_Item_Recycle = findViewById(R.id.Buy_My_Item_List_Recycle);
        Comments_RecyclerView = findViewById(R.id.Comments_Recycle);

        // ?
        Comments_RecyclerView.setHasFixedSize(true);
        My_Sell_Item_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 설정
        My_Sell_Item_LayoutManager = new LinearLayoutManager(Buy_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        LayoutManager_Comments = new LinearLayoutManager(Buy_Activity.this, LinearLayoutManager.VERTICAL, false);

        // 레이아웃 적용
        My_Sell_Item_Recycle.setLayoutManager(My_Sell_Item_LayoutManager);
        Comments_RecyclerView.setLayoutManager(LayoutManager_Comments);

        // ?
        CmmAdapter = new CustomAdapter(this , Comments_Array);
//     public Buy_My_Item_List(String buy_Item_Name, String buy_Item_Price, String buy_Item_Img, String buy_Item_Detail, String buy_Categori_Name) {

        // 게시물 수정/삭제  버튼 보이기 / 안보이기
        User_ID = getIntent().getStringExtra("User_ID");
        if(item_db_array.get(position).getItem_writer().equals(User_ID)){
            fix.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);
        }
        else{
            fix.setVisibility(View.INVISIBLE);
            del.setVisibility(View.INVISIBLE);
        }

        // 내 판매목록 갱신
        for(int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_writer().equals(item_db_array.get(position).getItem_writer())){
                if(item_db_array.get(i).getItem_number() == getIntent().getIntExtra("Item_Number", 0)){

                }
                else{
                    my_item_db_array.add(item_db_array.get(i));
                }
            }
        }

        // 게시물 정보 세팅
        timetext.setText(item_db_array.get(position).getitem_create_time());
        looktext.setText(String.valueOf(item_db_array.get(position).getItem_watch()));
        heart_count.setText(String.valueOf(item_db_array.get(position).getItem_heart()));
        categori_name.setText(item_db_array.get(position).getCategory_name());
        price1.setText(item_db_array.get(position).getItem_price()+"원");
        itemname.setText(item_db_array.get(position).getItem_name());
        item_text.setText(item_db_array.get(position).getItem_detail());
        Buy_Image.setImageBitmap(getBitmap());
        user_name.setText(item_db_array.get(position).getItem_writer());
        //user_icon.setImageURI(Uri.parse(item_db_array.get(position).getSeller_img()));

        // 나의 판매 목록 어뎁터
        My_Sell_Item_Adapter = new Buy_Adapter(my_item_db_array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if (obj != null) {
                    int position = (int) obj;

                    Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((Buy_Adapter) My_Sell_Item_Adapter).getData(position).getItem_number());
                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", User_ID);

                    startActivity(intent);
                    finish();
                }
            }
        });
        My_Sell_Item_Recycle.setAdapter(My_Sell_Item_Adapter);

       // CmmAdapter = new Comments_Adapter( Comments_Array , new View.OnClickListener(){
       //         @Override
       //         public void onClick(View v) {
       //             Object obj = v.getTag();
       //             if(obj != null){
       //                 int position = (int)obj;
       //                 Comments_position = position;
       //                 ((Comments_Adapter)CmmAdapter).getData(position);
                        //Intent intent = new Intent(Buy_Activity.this, Category_Cpu_Activity.class);
                        //intent.putExtra("Category_Name", ((Comments_Adapter)CmmAdapter).getData(position).getItem_Name());
                        //startActivity(intent);
       //             }
       //         }
       //     });
        //Comments_RecyclerView.setAdapter(CmmAdapter);

        // 댓글 입력 버튼
        Comments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comments comments = new Comments(Comments_Detail.getText().toString(),"User",getURLForResource(R.drawable.user_icon));
                Comments_Array.add(comments);
                Comments_Detail.setText(null);
                CmmAdapter.notifyDataSetChanged();
            }
        });

        // 위치 선택
        Buy_Location_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPackageList()) {
                    ComponentName compName = new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    Intent intent23 = new Intent(Intent.ACTION_MAIN);
                    intent23.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent23.setComponent(compName);
                    startActivity(intent23);
                }
                else{
                    String url = "market://details?id=" + "com.danawa.estimate";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        // 전화하기 암시적 인텐트
        Buy_Call_Icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01047256772"));

                try {
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //if(Sell_Activity.Camera_Bitmap != null){
        //    categori_name.setText(getIntent().getStringExtra("Item_Categori"));
        //    price1.setText(getIntent().getStringExtra("Item_Price"));
        //    itemname.setText(getIntent().getStringExtra("Item_Name"));
        //    item_text.setText(getIntent().getStringExtra("Item_Detail"));
        //    Buy_Image.setImageBitmap(Sell_Activity.Camera_Bitmap);
       // }

        // 게시글 삭제
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < item_db_array.size(); i++){
                    if(item_db_array.get(position).getItem_number() == item_db_array.get(i).getItem_number()){
                        item_db_array.remove(i);
                        Item_refreshShared();
                        break;
                    }
                }
                finish();

                // 기존 로직
//                for(int i = 0; i < Main_Activity.test1.size(); i++){
//                    if(Main_Activity.test1.get(Main_Activity.position_number).getItem_Img().equals(Mymenu_Activity.My_Menu_Array.get(i).getItem_Img())){
//                        Mymenu_Activity.My_Menu_Array.remove(i);
//                        break;
//                    }
//                }
//                Main_Activity.test1.remove(Main_Activity.position_number);
//                finish();
            }
        });

        // 수정 버튼
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy_Activity.this, Sell_fix_Activity.class);
                // 게시글 고유 번호
                intent.putExtra("Item_Number", item_db_array.get(position).getItem_number());
                intent.putExtra("User_ID", User_ID);
                startActivity(intent);
                finish();
//                intent.putExtra("Item_Category", categori_name.getText().toString());
//                intent.putExtra("Item_Price", price1.getText().toString());
//                intent.putExtra("Item_Name", itemname.getText().toString());
//                intent.putExtra("Item_Detail", item_text.getText().toString());
//                intent.putExtra("Item_Img", getIntent().getStringExtra("Item_Img"));
            }
        });

        // 검색
        Buy_Search_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        // 메뉴
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy_Activity.this, Mymenu_Activity.class);
                startActivity(intent);
            }
        });

        // 뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




//    // 수정 forResult
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if( requestCode == Sell_fix_Check && resultCode == RESULT_OK){
//
//                itemname.setText(data.getStringExtra("Item_Name"));
//                price1.setText(data.getStringExtra("Item_Price"));
//                item_text.setText(data.getStringExtra("Item_Detail"));
//                categori_name.setText(data.getStringExtra("Item_Categori"));
//                Buy_Image.setImageURI(Uri.parse(data.getStringExtra("Image_Uri")));
//
//            }
//        }

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

    // Drawable String 으로 변환
    private String getURLForResource(int resId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }


    // 앱 설치 유무 판단
    public boolean getPackageList() {
        boolean isExist = false;

        PackageManager pkgMgr = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith("com.google.android.apps.maps")){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
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

    //문자열 비트맵으로 변환
    public Bitmap getBitmap(){
        byte[] decodedByteArray = Base64.decode(item_db_array.get(position).getitem_img(), Base64.NO_WRAP);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        return  decodedBitmap;
    }
    
    @Override
    public void onResume(){
        super.onResume();

        // 판매항목 추가 하는 부분
        // 기존 판매 항복 초기화
        //Buy_Activity.Buy_my_item.clear();
        // 판매항목리스트에 전체 아이템 추가
        // 현재 포지션 아이템 삭제

        //if(Main_Activity.Position_Check == true) {
        //    for(int i = 0; i < Main_Activity.test1.size(); i++){
        //        Buy_My_Item_List test2 = new Buy_My_Item_List(Main_Activity.test1.get(i).getItem_Name(), Main_Activity.test1.get(i).getItem_Price(), Main_Activity.test1.get(i).getItem_Img(), Main_Activity.test1.get(i).getItem_Detail(), Main_Activity.test1.get(i).getCategori_Name());
        //        Buy_my_item.add(test2);
        //    }
        //    //Buy_my_item.remove(Main_Activity.position_number);
        //}
        //else{
        //    for(int i = 0; i < Main_Activity.test1.size(); i++) {
        //        Buy_My_Item_List test3 = new Buy_My_Item_List(Mymenu_Activity.My_Menu_Array.get(i).getItem_Name(), Mymenu_Activity.My_Menu_Array.get(i).getItem_Price(), Mymenu_Activity.My_Menu_Array.get(i).getItem_Img(), Mymenu_Activity.My_Menu_Array.get(i).getItem_Detail(), Mymenu_Activity.My_Menu_Array.get(i).getCategori_Name());
        //        Buy_my_item.add(test3);
        //    }
        //    Buy_my_item.remove(Mymenu_Activity.My_Menu_Position);
        //}
    }
}