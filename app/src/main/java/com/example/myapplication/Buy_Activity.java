package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Buy_Activity extends AppCompatActivity {

    static String User_id_test;

    // 유저 정보를 담을 어레이
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

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

    // 로그인한 유저 img
    String User_img;
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
    // 작성자 position
    private int User_Writer;
    private int Login_User_Position;

    //static ArrayList<Comments> Comments_Array = new ArrayList<>();
    //static ArrayList<Buy_My_Item_List> Buy_my_item = new ArrayList<>();



    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_interface);

        // 아이템 전체 정보 가져옴
        Item_getShared();

        // 유저 전체 정보 가져옴
        User_getShared();

        // 게시물 고유번호로 포지션 확인
        for(int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_number() == getIntent().getIntExtra("Item_Number", 0)){
                position = i;
            }
        }

        // 글 작성자 유저 정보 찾기
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(item_db_array.get(position).getItem_writer())){
                User_Writer = i;
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
        back = findViewById(R.id.Search_Result_Back);
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

        // 게시물 수정/삭제  버튼 보이기 / 안보이기
        User_ID = getIntent().getStringExtra("User_ID");

        // 현재 접속 유저 아이디
        User_id_test = User_ID;

        // ?
        CmmAdapter = new CustomAdapter( this , item_db_array.get(position).getUser_comments());
        Comments_RecyclerView.setAdapter(CmmAdapter);
//     public Buy_My_Item_List(String buy_Item_Name, String buy_Item_Price, String buy_Item_Img, String buy_Item_Detail, String buy_Categori_Name) {

        // 최근본 상품 고유 번호 판단

        // 고유번호 어레이 리스트 사이즈 만큼 반복
        for(int i = 0; i < Mymenu_Activity.My_Menu_Number_Array.size(); i++){
            // 만약 가지고 있는 고유 번호와 현 게시글 고유번호가 같다면
            if(Mymenu_Activity.My_Menu_Number_Array.get(i) == getIntent().getIntExtra("Item_Number", 0)){
                // 일치하는 고유 번호 삭제
                Mymenu_Activity.My_Menu_Number_Array.remove(i);
            }
        }

        // 게시글 작성자와 로그인 유저 아이디가 다를때 게시물 저장
        if(!item_db_array.get(position).getItem_writer().equals(getIntent().getStringExtra("User_ID"))){
            // 내가 본 게시물 번호 저장
            Mymenu_Activity.My_Menu_Number_Array.add(getIntent().getIntExtra("Item_Number", 0));
        }

        // 유저 아이디에 따라 수정 삭제 버튼 보이기
        if(item_db_array.get(position).getItem_id().equals(User_ID)){
            fix.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);
        }
        else{
            fix.setVisibility(View.INVISIBLE);
            del.setVisibility(View.INVISIBLE);
        }

        // 로그인한 유저 position 얻기
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(User_ID)){
                Login_User_Position = i;
            }
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
        try {
            timetext.setText(time()+" 전");
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "에바아아아ㅏ아야아아아ㅏ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        looktext.setText(String.valueOf(item_db_array.get(position).getItem_watch()));
        heart_count.setText(String.valueOf(item_db_array.get(position).getItem_heart()));
        categori_name.setText(item_db_array.get(position).getCategory_name());
        price1.setText(item_db_array.get(position).getItem_price()+"원");
        itemname.setText(item_db_array.get(position).getItem_name());
        item_text.setText(item_db_array.get(position).getItem_detail());
        Buy_Image.setImageURI(Uri.parse(item_db_array.get(position).getitem_img()));
        user_name.setText(item_db_array.get(position).getItem_writer());
        user_icon.setImageURI(Uri.parse(User_Db_ArrayList.get(User_Writer).getUser_icon_img()));

        // 유저 조회수 판단 부분
        for(int i = 0; i < item_db_array.get(position).getUser_sees().size(); i++) {
            // 만약 유저 조회 판단 어레이에 현재 접속 유저 아이디가 있다면
            // 저장된 시간과 현재 시간을 대조해 24시간이 넘었는지 판단하고 넘었으면 조회수 추가 그리고 다시 현재시간 저장
            // 안넘었으면 그냥 넘어감
            if (item_db_array.get(position).getUser_sees().get(i).getSee_user_id().equals(User_ID)){

            }
            // 없다면 판단 어레이에 현재 시간과 유저 아이디 조회수 추가
            else{
                item_db_array.get(position).getUser_sees().get(i).setSee_user_id(User_ID);
                break;
            }
        }

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


        // 댓글 입력 버튼
        Comments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Comments user_comments = new User_Comments(User_Db_ArrayList.get(Login_User_Position).getUser_name(),User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), Comments_Detail.getText().toString(), User_Db_ArrayList.get(Login_User_Position).getUser_id());
                item_db_array.get(position).getUser_comments().add(user_comments);
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

//    //문자열 비트맵으로 변환
//    public Bitmap getBitmap(){
//        byte[] decodedByteArray = Base64.decode(item_db_array.get(position).getitem_img(), Base64.NO_WRAP);
//        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
//        return  decodedBitmap;
//    }

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

    // 게시물 시간 구하기
    public String time() throws ParseException {

        // 작성 시간
        String reqDateStr = item_db_array.get(position).getitem_create_time();

        //현재시간 Date
        Date curDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");


        //요청시간을 Date로 parsing 후 time가져오기
        Date reqDate = dateFormat.parse(reqDateStr);
        long reqDateTime = reqDate.getTime();

        //현재시간을 요청시간의 형태로 format 후 time 가져오기
        curDate = dateFormat.parse(dateFormat.format(curDate));
        long curDateTime = curDate.getTime();

        //분으로 표현
        long minute = (curDateTime - reqDateTime) / (60 * 1000);

        long hour = (curDateTime - reqDateTime) / (60 * 60 * 1000);

        long day = (curDateTime - reqDateTime) / (24 * 60 * 60 * 1000);

        long week = day/7;

        long month = (day+1)/30;

        long years = month/12;

        String realtime;
        String time = null;

        if(minute < 60){
            realtime = String.valueOf(minute);
            time = realtime+"분";
        }
        else if(hour < 24){
            realtime = String.valueOf(hour);
            time = realtime+"시간";
        }
        else if(day < 7){
            realtime = String.valueOf(day);
            time = realtime+"일";
        }
        else if(week < 5){
            realtime = String.valueOf(week);
            Toast.makeText(getApplicationContext()," 날짜 차이 = "+(curDateTime - reqDateTime)+" 변환 = "+ (curDateTime - reqDateTime) / (7 * 24 * 60 * 60 * 1000), Toast.LENGTH_LONG).show();
            time = realtime+"주";
        }
        else if(month < 12){
            realtime = String.valueOf(month);
            Toast.makeText(getApplicationContext()," 날짜 차이 = "+(curDateTime - reqDateTime)+" 변환 = "+ (curDateTime - reqDateTime) / (31 * 24 * 60 * 60 * 1000), Toast.LENGTH_LONG).show();
            time = realtime+"달";
        }
        else if (years < 100){
            realtime = String.valueOf(years);
            time = realtime+"년";
        }
        // 분 60 * 1000;
        // 시간 60 * 60 * 1000;
        // 일 24 * 60 * 60 * 1000;
        // 년 365 * 24 * 60 * 60 * 1000;

        return time;
    }
    
    @Override
    public void onResume(){
        super.onResume();

        User_Db_ArrayList.clear();

        // 유저 전체 정보 가져옴
        User_getShared();

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

    @Override
    protected void onPause() {
        super.onPause();
        Item_refreshShared();
//            Intent intent = new Intent(getApplicationContext(), Category_Cpu_Activity.class);
//            intent.putExtra( "Category_Name", getIntent().getStringExtra("Category_Name"));
//            startActivity(intent);
        }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
        intent.putExtra("Item_Number", getIntent().getIntExtra("Item_Number", 0));
        intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
