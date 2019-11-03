package com.example.myapplication;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    // 재고
    private int stuck = 10;

    // 유저 아이디 어뎁터에 넘길려고 static
    static String User_id_test;

    // 유저 정보를 담을 어레이
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 내상품 어레이 객체 생성
    ArrayList<Item_DB> my_item_db_array = new ArrayList<>();

    // 유저의 채팅방 데이터
    ArrayList<Chat_List> Chat_User_List = new ArrayList<>();

    // 채팅방 전체 데이터
    ArrayList<Chat_List> Chat_List_Array = new ArrayList<>();



//    // 상품 고유 넘버
//    int Item_Number = getIntent().getIntExtra("Item_Number", 0);

    // 상품 position
    int Item_position;

    // 로그인한 유저
    String Login_User_ID;

    // requestcoed
    //final static int Sell_fix_Check = 750;



    // 뷰 선언
    LinearLayout weight_fix, User_hide, Chat_Btn;
    ImageView Buy_Image, back, user, user_icon, User_heart;
    TextView price1, itemname, Buy_Call_Icon, item_text, Buy_Location_Icon, user_name, categori_name, timetext, looktext, heart_count, open_days, User_Follower_Count;
    Button del, fix, Comments_Btn,Buy_Item;
    EditText Comments_Detail;
    CheckBox heart, heart2, Following_Check_Box;


    // 리사이클러뷰 ~
    private RecyclerView Comments_RecyclerView, My_Sell_Item_Recycle;
    private RecyclerView.Adapter CmmAdapter, My_Sell_Item_Adapter;
    private RecyclerView.LayoutManager LayoutManager_Comments, My_Sell_Item_LayoutManager;

    // 작성자 position
    private int User_Writer_Position;
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
        Item_Position_Check();

        // 글 작성자 유저 정보 찾기
        User_Writer_Position_Check();


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
        Comments_Btn = findViewById(R.id.Comments_Btn);
        open_days = findViewById(R.id.open_days);
        heart = findViewById(R.id.heart);
        heart2 = findViewById(R.id.heart2);
        User_hide = findViewById(R.id.User_hide);
        User_heart =findViewById(R.id.User_heart);
        weight_fix = findViewById(R.id.weight_fix);
        Following_Check_Box = findViewById(R.id.Following_Check_Box);
        User_Follower_Count = findViewById(R.id.User_Follower_Count);
        Chat_Btn = findViewById(R.id.layout1);
        Buy_Item = findViewById(R.id.Buy_Item);

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

        // 로그인한 유저 인텐트 받기
        Login_User_ID= getIntent().getStringExtra("User_ID");

        // 현재 접속 유저 아이디
        User_id_test = Login_User_ID;

        // 댓글 어뎁터 붙이기전 아이디 or 이미지 변경 됐으면 변경해주는 부분
        User_Comments_Refresh();

        // 댓글 어뎁터 붙이기
        CmmAdapter = new CustomAdapter( this , item_db_array.get(Item_position).getUser_comments(), Login_User_ID);
        Comments_RecyclerView.setAdapter(CmmAdapter);

        // 최근본 상품 판단
        Item_Watch();

        // 유저 아이디에 따라 수정 삭제 버튼 보이기
        Fix_Del_Button_Check();

        // 로그인한 유저 position 얻기
        Login_User_Position_Check();

        // 채팅방 전체 데이터 불러오기
        getChat_List();

        // 유저의 채팅방 분류
        setUser_Chat_List();

        // 내 판매목록 갱신
        My_Sell_Item_List();

        // 조회수 판단
        See_Count_Check();

        // 알림 창을 타고 들어왔을때만 동작
        if(getIntent().getBooleanExtra("Check_noti", false)) {
            Toast.makeText(getApplicationContext(), "하이하이", Toast.LENGTH_SHORT).show();
            // 알림 창 타고 들어온거 제거
            notificationDel();
        }

        // 게시물 정보 세팅
        try {
            timetext.setText(time()+" 전");
        } catch (ParseException e) {
        }
        looktext.setText(String.valueOf(See_Count()));
        heart_count.setText(String.valueOf(item_db_array.get(Item_position).getUser_selects().size()));
        categori_name.setText(item_db_array.get(Item_position).getCategory_name());
        price1.setText(item_db_array.get(Item_position).getItem_price()+"원");
        itemname.setText(item_db_array.get(Item_position).getItem_name());
        item_text.setText(item_db_array.get(Item_position).getItem_detail());
        Buy_Image.setImageURI(Uri.parse(item_db_array.get(Item_position).getitem_img()));
        user_name.setText(User_Db_ArrayList.get(User_Writer_Position).getUser_name());
        user_icon.setImageURI(Uri.parse(User_Db_ArrayList.get(User_Writer_Position).getUser_icon_img()));
        User_Follower_Count.setText(String.valueOf(User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().size()));
        try {
            open_days.setText(user_open());
        } catch (ParseException e) {
            e.printStackTrace();
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
                    intent.putExtra("User_ID", Login_User_ID);

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

        // 게시글 찜 목록 어레이에 로그인 유저가 있는지 판별
        if(item_db_array.get(Item_position).getUser_selects() != null) {
            for (int i = 0; i < item_db_array.get(Item_position).getUser_selects().size(); i++) {
                if (item_db_array.get(Item_position).getUser_selects().get(i).getUser_name().equals(Login_User_ID)) {
                    heart.setChecked(true);
                    heart2.setChecked(true);
                }
            }
        }

        // 해당 게시글 작성자 팔로우 리스트에 로그인 유저가 있는지 판별
        for(int i = 0;  i < User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().size(); i++){
            if(User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().get(i).equals(Login_User_ID)){
                Following_Check_Box.setChecked(true);
            }
        }

        // 채팅 버튼
        Chat_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상대방 아이디
                String Another_User_ID = User_Db_ArrayList.get(User_Writer_Position).getUser_id();

                if (Chat_User_List.size() != 0) {
                    // 만약 상대방 아이디가 내 채팅방에 있다면 작동 x
                    for (int i = 0; i < Chat_User_List.size(); i++) {
                        if (Chat_User_List.get(i).getChat_Maker_User().equals(Another_User_ID) || Chat_User_List.get(i).getChat_Receiver_User().equals(Another_User_ID)) {

                        }

                        if (i == Chat_User_List.size()) {

                            // 채팅방 객체 생성 ( 로그인 유저 아이디, 상대방 유저 아이디, 채팅방 내용 어레이 리스트, 이미지)
                            Chat_List chat_list = new Chat_List(Login_User_ID, Another_User_ID, User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), User_Db_ArrayList.get(User_Writer_Position).getUser_icon_img(), User_Db_ArrayList.get(User_Writer_Position).getUser_name(), User_Db_ArrayList.get(User_Writer_Position).getUser_name());
                            // 전체 채팅방 어레이에 채팅방 추가
                            Chat_List_Array.add(chat_list);

                            // 채팅방 데이터 쉐어드에 저장
                            setChat_List();

                            // 유저 채팅방 분류
                            setUser_Chat_List();

                            break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();

                    // 채팅방 객체 생성 ( 로그인 유저 아이디, 상대방 유저 아이디, 채팅방 내용 어레이 리스트, 이미지)
                    Chat_List chat_list = new Chat_List(Login_User_ID, Another_User_ID, User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), User_Db_ArrayList.get(User_Writer_Position).getUser_icon_img(), User_Db_ArrayList.get(Login_User_Position).getUser_name(), User_Db_ArrayList.get(User_Writer_Position).getUser_name());
                    // 전체 채팅방 어레이에 채팅방 추가
                    Chat_List_Array.add(chat_list);

                    // 채팅방 데이터 쉐어드에 저장
                    setChat_List();

                    // 유저 채팅방 분류
                    setUser_Chat_List();
                }

                // 인텐트 생성
                Intent intent = new Intent(getApplicationContext(), Chat_Message_Activity.class);

                // 로그인 유저 아이디 인텐트
                intent.putExtra("User_ID", Login_User_ID);

                // 상대방 아이디 인텐트
                intent.putExtra("Another_User_ID", Another_User_ID);

                // 인텐트 전달
                startActivity(intent);
            }
        });

        // 팔로우 체크 박스
        Following_Check_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팔로우 할때
                if(Following_Check_Box.isChecked()){
                    // 로그인한 유저 팔로잉 어레이에 작성자 아이디 추가
                    User_Db_ArrayList.get(Login_User_Position).getUser_Following().add(item_db_array.get(Item_position).getItem_id());
                    // 작성자 팔로워 리스트에 로그인 유저 아이디 추가
                    User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().add(Login_User_ID);

                    User_Follower_Count.setText(String.valueOf(User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().size()));

                    // 알림 객체 생성
                    Allim_Db allim_db = new Allim_Db(item_db_array.get(Item_position).getitem_img(), User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), "팔로우 하였습니다!", time_check(), User_Db_ArrayList.get(Login_User_Position).getUser_name(), item_db_array.get(Item_position).getItem_number());

                    // 알림 객체 추가
                    User_Db_ArrayList.get(User_Writer_Position).getAllim_db().add(allim_db);

                    User_Save_Shared();

                    // 토스트 메시지
                    Toast.makeText(getApplicationContext(), "팔로우 되었습니다 !", Toast.LENGTH_SHORT).show();
                }
                // 팔로우 취소
                else{
                    // 로그인한 유저 팔로잉 어레이에서 작성자 아이디 삭제

                    // 로그인한 유저에 팔로잉 어레이 만큼 반복
                    for(int i = 0; i < User_Db_ArrayList.get(Login_User_Position).getUser_Following().size(); i++){
                        // 팔로잉 어레이에 작성자에 아이디가 있다면
                        if(User_Db_ArrayList.get(Login_User_Position).getUser_Following().get(i).equals(item_db_array.get(Item_position).getItem_id())){
                            // 작성자 아이디 삭제
                            User_Db_ArrayList.get(Login_User_Position).getUser_Following().remove(i);
                            break;
                        }
                    }

                    // 작성자 팔로워 리스트에서 로그인 유저 아이디 삭제

                    // 작성자 팔로워 어레이 만큼 반복
                    for(int i = 0; i < User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().size(); i++){
                        // 팔로워 어레이에 로그인한 유저 아이디가 있다면
                        if(User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().get(i).equals(Login_User_ID)){
                            // 유저아이디 삭제
                            User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().remove(i);
                            break;
                        }
                    }

                    // 토스트 메시지
                    Toast.makeText(getApplicationContext(), "팔로우가 취소되었습니다.", Toast.LENGTH_SHORT).show();

                    User_Follower_Count.setText(String.valueOf(User_Db_ArrayList.get(User_Writer_Position).getUser_Follower().size()));
                }

                // 유저 정보 저장
                User_Save_Shared();
            }
        });

        // 위쪽 체크 박스
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 체크 박스 눌렸을때
                if(heart.isChecked()){

                    // 유저 객체 생성
                    User_Select user_select = new User_Select(Login_User_ID);

                    // 해당 아이템 유저 찜 목록 어레이에 객체 추가
                    item_db_array.get(Item_position).getUser_selects().add(user_select);

                    // 유저 찜 목록 어레이에 해당 게시글 고유 번호 저장
                    User_Db_ArrayList.get(Login_User_Position).getUser_Select().add(item_db_array.get(Item_position).getItem_number());

                    // 다른 버튼 활성화
                    heart2.setChecked(true);

                    Allim_Db allim_db = new Allim_Db(item_db_array.get(Item_position).getitem_img(),User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(),"찜을 하였습니다", time_check(), Login_User_ID, item_db_array.get(Item_position).getItem_number());

                    User_Db_ArrayList.get(User_Writer_Position).getAllim_db().add(allim_db);

                    Toast.makeText(getApplicationContext(), "찜 목록에 추가 되었습니다!!", Toast.LENGTH_SHORT).show();

                    // 아이템 어레이 다시 저장
                    Item_refreshShared();

                    // 유저 정보 저장
                    User_Save_Shared();

                    // 찜 수
                    heart_count.setText(String.valueOf(item_db_array.get(Item_position).getUser_selects().size()));




                }
                // 체크 박스 안눌렸을때
                else{

                    // 해당 게시글 찜 목록 어레이 사이즈 만큼 반복
                    for(int i = 0; i < item_db_array.get(Item_position).getUser_selects().size(); i++){

                        // 게시글 찜목록 어레이에 로그인한 유저 아이디와 일치하는게 있으면
                        if(item_db_array.get(Item_position).getUser_selects().get(i).getUser_name().equals(Login_User_ID)){

                            // 해당 유저 객체 삭제
                            item_db_array.get(Item_position).getUser_selects().remove(i);

                            // 유저 찜목록에서 게시글 삭제
                            for(int j = 0; j < User_Db_ArrayList.get(Login_User_Position).getUser_Select().size(); j++){
                                if(User_Db_ArrayList.get(Login_User_Position).getUser_Select().get(j) == item_db_array.get(Item_position).getItem_number()){
                                    User_Db_ArrayList.get(Login_User_Position).getUser_Select().remove(j);
                                    break;
                                }
                            }

                            Toast.makeText(getApplicationContext(), "찜 목록에서 삭제 되었습니다", Toast.LENGTH_SHORT).show();

                            // 아이템 어레이 다시 저장
                            Item_refreshShared();

                            // 유저 정보 저장
                            User_Save_Shared();

                            // 찜 수
                            heart_count.setText(String.valueOf(item_db_array.get(Item_position).getUser_selects().size()));

                            break;
                        }
                    }

                    // 다른 체크박스 비활성화
                    heart2.setChecked(false);
                }
            }
        });

        // 아래쪽 체크 박스
        heart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heart2.isChecked()){

                    // 유저 객체 생성
                    User_Select user_select = new User_Select(Login_User_ID);

                    // 해당 아이템 유저 찜 목록 어레이에 객체 추가
                    item_db_array.get(Item_position).getUser_selects().add(user_select);

                    // 유저 찜 목록 어레이에 해당 게시글 고유 번호 저장
                    User_Db_ArrayList.get(Login_User_Position).getUser_Select().add(item_db_array.get(Item_position).getItem_number());

                    // 다른 버튼 활성화
                    heart.setChecked(true);

                    Allim_Db allim_db = new Allim_Db(item_db_array.get(Item_position).getitem_img(),User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(),"해당 상품을 찜 하였습니다", time_check(), Login_User_ID, item_db_array.get(Item_position).getItem_number());

                    User_Db_ArrayList.get(User_Writer_Position).getAllim_db().add(allim_db);

                    Toast.makeText(getApplicationContext(), "찜 목록에 추가 되었습니다!!", Toast.LENGTH_SHORT).show();

                    // 아이템 어레이 다시 저장
                    Item_refreshShared();

                    // 유저 정보 저장
                    User_Save_Shared();

                    // 찜 수
                    heart_count.setText(String.valueOf(item_db_array.get(Item_position).getUser_selects().size()));
                }
                else{

                    // 해당 게시글 찜 목록 어레이 사이즈 만큼 반복
                    for(int i = 0; i < item_db_array.get(Item_position).getUser_selects().size(); i++){

                        // 게시글 찜목록 어레이에 로그인한 유저 아이디와 일치하는게 있으면
                        if(item_db_array.get(Item_position).getUser_selects().get(i).getUser_name().equals(Login_User_ID)){

                            // 해당 유저 객체 삭제
                            item_db_array.get(Item_position).getUser_selects().remove(i);

                            // 유저 찜목록에서 게시글 삭제
                            for(int j = 0; j < User_Db_ArrayList.get(Login_User_Position).getUser_Select().size(); j++){
                                if(User_Db_ArrayList.get(Login_User_Position).getUser_Select().get(j) == item_db_array.get(Item_position).getItem_number()){
                                    User_Db_ArrayList.get(Login_User_Position).getUser_Select().remove(j);
                                    break;
                                }
                            }

                            Toast.makeText(getApplicationContext(), "찜 목록에서 삭제 되었습니다", Toast.LENGTH_SHORT).show();

                            // 아이템 어레이 다시 저장
                            Item_refreshShared();

                            // 유저 정보 저장
                            User_Save_Shared();

                            // 찜 수
                            heart_count.setText(String.valueOf(item_db_array.get(Item_position).getUser_selects().size()));

                            break;
                        }
                    }

                    // 다른 체크박스 비활성화
                    heart.setChecked(false);
                }
            }
        });

        // 프로필 사진 버튼
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
                intent.putExtra("Profile_Id", User_Db_ArrayList.get(User_Writer_Position).getUser_id());
                intent.putExtra("User_ID", Login_User_ID);
                startActivity(intent);
            }
        });

        //댓글 입력 버튼
        Comments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Comments user_comments = new User_Comments(User_Db_ArrayList.get(Login_User_Position).getUser_name(),User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), Comments_Detail.getText().toString(), User_Db_ArrayList.get(Login_User_Position).getUser_id());
                item_db_array.get(Item_position).getUser_comments().add(user_comments);
                Comments_Detail.setText(null);
                CmmAdapter.notifyDataSetChanged();

                if(!User_Db_ArrayList.get(Login_User_Position).getUser_id().equals(User_Db_ArrayList.get(User_Writer_Position).getUser_id())) {
                    // 알림 객체 생성
                    Allim_Db allim_db = new Allim_Db(item_db_array.get(Item_position).getitem_img(), User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), "댓글을 달았습니다.", time_check(), User_Db_ArrayList.get(Login_User_Position).getUser_name(), item_db_array.get(Item_position).getItem_number());

                    // 알림 객체 추가
                    User_Db_ArrayList.get(User_Writer_Position).getAllim_db().add(allim_db);

                    User_Save_Shared();
                }
            }
        });

        // 위치 선택
        Buy_Location_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Trade_Map_Activity.class);
                intent.putExtra("Item_Number", item_db_array.get(Item_position).getItem_number());
                startActivity(intent);
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
                    if(item_db_array.get(Item_position).getItem_number() == item_db_array.get(i).getItem_number()){
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
                intent.putExtra("Item_Number", item_db_array.get(Item_position).getItem_number());
                intent.putExtra("User_ID", Login_User_ID);
                startActivity(intent);
//                intent.putExtra("Item_Category", categori_name.getText().toString());
//                intent.putExtra("Item_Price", price1.getText().toString());
//                intent.putExtra("Item_Name", itemname.getText().toString());
//                intent.putExtra("Item_Detail", item_text.getText().toString());
//                intent.putExtra("Item_Img", getIntent().getStringExtra("Item_Img"));
            }
        });

        // 뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
                intent.putExtra("User_ID", Login_User_ID);
                startActivity(intent);
                finish();
            }
        });

    }

    // 액티비티 시작 바로 직전
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

    // 액티비티 정지
    @Override
    protected void onPause() {
        super.onPause();
        Item_refreshShared();
//            Intent intent = new Intent(getApplicationContext(), Category_Cpu_Activity.class);
//            intent.putExtra( "Category_Name", getIntent().getStringExtra("Category_Name"));
//            startActivity(intent);
    }

    // 액티비티 재시작
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

    // 오픈일 구하기
    public String user_open() throws ParseException {
        // 작성 시간
        String reqDateStr = User_Db_ArrayList.get(User_Writer_Position).getUser_open();

        //현재시간 Date
        Date curDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        //요청시간을 Date로 parsing 후 time가져오기
        Date reqDate = dateFormat.parse(reqDateStr);
        long reqDateTime = reqDate.getTime();

        //현재시간을 요청시간의 형태로 format 후 time 가져오기
        curDate = dateFormat.parse(dateFormat.format(curDate));
        long curDateTime = curDate.getTime();

        long realtime = (curDateTime - reqDateTime) / (24 * 60 * 60 * 1000)+1;

        String fixdata = " +"+realtime;

        return fixdata;
    }

    // 게시물 시간 구하기
    public String time() throws ParseException {

        // 작성 시간
        String reqDateStr = item_db_array.get(Item_position).getitem_create_time();

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

        Log.e("check_time", String.valueOf(curDateTime));
        Log.e("check_time", String.valueOf(reqDateTime));
        Log.e("check_time", String.valueOf(minute));

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

    // 조회수
    public int See_Count(){

        int count = 0;

        if(!item_db_array.get(Item_position).getUser_sees().isEmpty()) {
            for (int i = 0; i < item_db_array.get(Item_position).getUser_sees().size(); i++) {
                count = count + item_db_array.get(Item_position).getUser_sees().get(i).getViews_count();
            }
        }
        else{
            count = 0;
        }
        return count;
    }

//    public void User_Plus(){
//        //현재시간 Date
//        Date curDate = new Date();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
//
//        //현재시간을 요청시간의 형태로 format 후 time 가져오기
//        try {
//            curDate = dateFormat.parse(dateFormat.format(curDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
////                long curDateTime = curDate.getTime();
//
//        // 현재 시간을 String 화
//        String now_time = dateFormat.format(curDate.getTime());
//
//        // 유저 판별 객체 생성
//        User_See user_see = new User_See(now_time, 1, User_ID);
//
//        // 유저 추가
//        item_db_array.get(position).getUser_sees().add(user_see);
//    }

    // 게시물 고유번호로 포지션 확인
    public void Item_Position_Check(){
        for(int i = 0; i < item_db_array.size(); i++){
            if(item_db_array.get(i).getItem_number() == getIntent().getIntExtra("Item_Number", 0)){
                Item_position = i;
            }
        }
    }

    // 글 작성자 유저 정보 찾기
    public void User_Writer_Position_Check(){
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
        if(User_Db_ArrayList.get(i).getUser_id().equals(item_db_array.get(Item_position).getItem_id())){
            User_Writer_Position = i;
            break;
            }
        }
    }

    // 댓글 어뎁터 붙이기전 아이디 or 이미지 변경 됐으면 변경해주는 부분
    public void User_Comments_Refresh() {
        for (int i = 0; i < item_db_array.get(Item_position).getUser_comments().size(); i++) {
            for (int j = 0; j < User_Db_ArrayList.size(); j++) {
                if (item_db_array.get(Item_position).getUser_comments().get(i).getComment_user_id().equals(User_Db_ArrayList.get(j).getUser_id())) {
                    item_db_array.get(Item_position).getUser_comments().get(i).setComment_user_name(User_Db_ArrayList.get(j).getUser_name());
                    item_db_array.get(Item_position).getUser_comments().get(i).setComment_user_icon(User_Db_ArrayList.get(j).getUser_icon_img());
                }
            }
        }
    }

    // 최근본 상품 판단
    public void Item_Watch(){

        // 고유번호 어레이 리스트 사이즈 만큼 반복
        for(int i = 0; i < Mymenu_Activity.My_Menu_Number_Array.size(); i++){
            // 만약 가지고 있는 고유 번호와 현 게시글 고유번호가 같다면
            if(Mymenu_Activity.My_Menu_Number_Array.get(i) == getIntent().getIntExtra("Item_Number", 0)){
                // 일치하는 고유 번호 삭제
                Mymenu_Activity.My_Menu_Number_Array.remove(i);
            }
        }

        // 게시글 작성자와 로그인 유저 아이디가 다를때 게시물 저장
        if(!item_db_array.get(Item_position).getItem_id().equals(getIntent().getStringExtra("User_ID"))){
            // 내가 본 게시물 번호 저장
            Mymenu_Activity.My_Menu_Number_Array.add(getIntent().getIntExtra("Item_Number", 0));
        }
    }

    // 유저 아이디에 따라 수정 삭제 버튼 보이기
    public void Fix_Del_Button_Check(){
        if(item_db_array.get(Item_position).getItem_id().equals(Login_User_ID)){
            fix.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);
            User_hide.setVisibility(View.GONE);
            heart.setVisibility(View.INVISIBLE);
            Following_Check_Box.setVisibility(View.GONE);
        }
        
        else{
            fix.setVisibility(View.INVISIBLE);
            del.setVisibility(View.INVISIBLE);
            User_heart.setVisibility(View.INVISIBLE);
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

    // 내 판매목록 갱신
    public void My_Sell_Item_List(){
        for(int i = 0; i < item_db_array.size(); i++){
            // 현 게시글 만든 작성자의 아이디와 모든 아이템에 정보를 가지고
            if(item_db_array.get(i).getItem_id().equals(item_db_array.get(Item_position).getItem_id())){
                // 현 게시글과 고유번호가 다른 게시글정보를 어레이 저장한다
                if(item_db_array.get(i).getItem_number() == getIntent().getIntExtra("Item_Number", 0)){

                }
                else{
                    my_item_db_array.add(item_db_array.get(i));
                }
            }
        }
    }

    // 조회수
    public void See_Count_Check(){
        // 유저 게시글 체크 어레이에 사이즈가 0이 아니면 판별 시작
        if(!item_db_array.get(Item_position).getUser_sees().isEmpty()) {
            boolean User_create = true;
            // 유저 조회수 판단 부분
            for (int i = 0; i < item_db_array.get(Item_position).getUser_sees().size(); i++) {
                // 만약 유저 조회 판단 어레이에 현재 접속 유저 아이디가 있다면
                // 저장된 시간과 현재 시간을 대조해 24시간이 넘었는지 판단하고 넘었으면 조회수 추가 그리고 다시 현재시간 저장
                // 안넘었으면 그냥 넘어감
                if (item_db_array.get(Item_position).getUser_sees().get(i).getSee_user_id().equals(Login_User_ID)) {

                    // 기존 조회 시간
                    String reqDateStr =item_db_array.get(Item_position).getUser_sees().get(i).getTime();

                    //현재시간 Date
                    Date curDate = new Date();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");


                    //요청시간을 Date로 parsing 후 time가져오기
                    Date reqDate = null;
                    try {
                        reqDate = dateFormat.parse(reqDateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long reqDateTime = reqDate.getTime();

                    //현재시간을 요청시간의 형태로 format 후 time 가져오기
                    try {
                        curDate = dateFormat.parse(dateFormat.format(curDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long curDateTime = curDate.getTime();

                    // 현재시간 - 마지막 조회시간 구하기
                    long time = (curDateTime- reqDateTime) / (24 * 60 * 60 * 1000);

//                    Toast.makeText(getApplicationContext(), " 시간 차이 = "+time, Toast.LENGTH_SHORT).show();

                    User_create = false;

                    // 24시간이 지났다면 시간 수정후 조회수 +1
                    if(time > 1){
                        // 현재 시간으로 set
                        item_db_array.get(Item_position).getUser_sees().get(i).setTime(dateFormat.format(curDateTime));
                        // 조회수 + 1
                        item_db_array.get(Item_position).getUser_sees().get(i).setViews_count(item_db_array.get(Item_position).getUser_sees().get(i).getViews_count()+1);
                        break;
                    }
                    // 아니라면 그냥 지나감
                    else{
                        break;
                    }
                }
            }
            // 없다면 판단 어레이에 현재 시간과 유저 아이디 조회수 추가
            if(User_create == true) {
//                Toast.makeText(getApplicationContext(), "새로운 유저 생성", Toast.LENGTH_SHORT).show();
                //현재시간 Date
                Date curDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

                //현재시간을 요청시간의 형태로 format 후 time 가져오기
                try {
                    curDate = dateFormat.parse(dateFormat.format(curDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                long curDateTime = curDate.getTime();

                // 현재 시간을 String 화
                String now_time = dateFormat.format(curDate.getTime());

                // 유저 판별 객체 생성
                User_See user_see = new User_See(now_time, 1, Login_User_ID);

                // 유저 추가
                item_db_array.get(Item_position).getUser_sees().add(user_see);
            }
        }
        // 유저 어레이가 0 이면 유저 추가
        else{
            //현재시간 Date
            Date curDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

            //현재시간을 요청시간의 형태로 format 후 time 가져오기
            try {
                curDate = dateFormat.parse(dateFormat.format(curDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//                long curDateTime = curDate.getTime();

            // 현재 시간을 String 화
            String now_time = dateFormat.format(curDate.getTime());

            // 유저 판별 객체 생성
            User_See user_see = new User_See(now_time, 1, Login_User_ID);

            // 유저 추가
            item_db_array.get(Item_position).getUser_sees().add(user_see);
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

    // 시간 출력
    public String time_check(){

        // 현재시간 가져오기
        SimpleDateFormat format = new SimpleDateFormat ("yyyyMMddHHmm");

        // 캘린더 객체 time 생성
        Calendar time = Calendar.getInstance();

        // 포매팅
        String format_time = format.format(time.getTime());

        // 값 반환
        return format_time;
    }

    // 로그인 유저에 채팅방 분류
    public void setUser_Chat_List(){

        Chat_User_List.clear();

        // 전체 채팅방 목록만큼 반복
        for(int i = 0; i < Chat_List_Array.size(); i++){
            // 만약 채팅방 아이디 목록에 유저의 아이디가 들어가있는 채팅방을 발견 한다면
            if(Chat_List_Array.get(i).getChat_Maker_User().equals(Login_User_ID)||Chat_List_Array.get(i).getChat_Receiver_User().equals(Login_User_ID)){
                // 그채팅방 추가
                Chat_User_List.add(Chat_List_Array.get(i));
            }
        }
    }

    // 채팅방 목록 불러오기
    public void getChat_List(){
        SharedPreferences sharedPreferences = getSharedPreferences("Chat", 0);
        String Chat_List_Array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if (Chat_List_Array!= null) {
            try {

                // Key="Data" 값을 이용해 JsonArray 생성
                JSONArray jsonArray_user_db = new JSONArray(Chat_List_Array);

                // JsonArray 길이 만큼 반복
                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    // JsonArray 값을 data에 저장
                    String data = jsonArray_user_db.optString(i);

                    // Gson 생성
                    Gson gson = new Gson();

                    // data 값을 gson으로 파싱 하여 객체 생성
                    Chat_List chat_list = gson.fromJson(data, Chat_List.class);

                    // 생성된 객체 추가
                    this.Chat_List_Array.add(chat_list);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // 채팅 데이터 저장
    public void setChat_List(){

        // 쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Chat", 0);
        // 쉐어드 저장한다 선언
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 예비 어레이리스트 생성하고 현재 Chat_List_Array를 스트링값으로 파싱하여 저장
        ArrayList<String> saveChat_List_Array = new ArrayList<>();

        // Chat_List_Array 사이즈 만큼 반복
        for (int i = 0; i < this.Chat_List_Array.size(); i++) {
            // gson 생성
            Gson gson = new Gson();
            // Chat_db 에 Chat_List_Array 어레이 안에 있는 객체를 gson으로 파싱하여 저장
            String Chat_Db = gson.toJson(Chat_List_Array.get(i));

            // 예비 어레이에 파싱한 값 저장
            saveChat_List_Array.add(Chat_Db);
        }

        // JsonArray 선언
        JSONArray setJsonArray = new JSONArray();

        // 예비 어레이 리스트 사이즈 만큼 반복
        for (int i = 0; i < saveChat_List_Array.size(); i++) {
            // JsonArray에 데이터 추가
            setJsonArray.put(saveChat_List_Array .get(i));
        }
        // Chat_Array_List 어레이리스트가 null이 아니라면
        if (!this.Chat_List_Array.isEmpty()) {

            // 키값에 데이터 저장
            editor.putString("Data", setJsonArray.toString());

        } else {

            // 빈 값 저장
            editor.putString("Data", null);

        }

        // 저장완료
        editor.commit();
    }

    // 알림 정보 제거
    public void notificationDel(){

        Bundle extras = getIntent().getExtras();

        int id = extras.getInt("notificationId");

        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //노티피케이션 제거
        notificationManager.cancel(id);

        User_Db_ArrayList.get(Login_User_Position).getAllim_db().remove(User_Db_ArrayList.get(Login_User_Position).getAllim_db().size()-1);

        User_Save_Shared();
    }



}
