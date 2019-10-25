package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Message_Activity extends AppCompatActivity {

    // 채팅방 전체 데이터
    ArrayList<Chat_List> Chat_List_Array = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 채팅방에 채팅 내용 어레이
    ArrayList<Chat_Inside_User_Data> Chat_Inside_User_Data_Array = new ArrayList<>();

    // 뷰 선언
    EditText Chat_User_Input_Message;
    ImageView Chat_Back_Btn, Chat_Push_Message;
    CircleImageView Chat_User_Img;
    TextView Chat_user_Name;
    RecyclerView Chat_Inside_Recycle;
    RecyclerView.Adapter Chat_Inside_Adapter;
    RecyclerView.LayoutManager Chat_Inside_LayoutManager;

    // 로그인 유저 아이디
    private String Login_User_Id;

    // 로그인 유저 포지션
    private int Login_User_Position;

    // 상대방 유저 아이디
    private String Another_User_Id;

    // 채팅방 내용 포지션
    private int Chat_Message_List_Position;
    private int Another_User_Position;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_inside);

        // 로그인 유저 아이디 인텐트 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 채팅 상대방 아이디
        Another_User_Id = getIntent().getStringExtra("Another_User_ID");

        // 채팅방 모든 리스트 가져오기
        getChat_List();

        // 채팅 내용 가져오기
        getChat_Message();

        // 유저 정보 가져오기
        User_getShared();

        // 유저 포지션 체크
        User_Position_Check();

        // 상대방 포지션 체크
        Another_User_Position_Check();

        // 뷰 매칭
        Chat_Back_Btn= findViewById(R.id.Chat_Back);
        Chat_User_Input_Message = findViewById(R.id.Chat_User_Input_Message);
        Chat_Push_Message = findViewById(R.id.Chat_Push_Message);
        Chat_Inside_Recycle = findViewById(R.id.Chat_Inside_Recycle);
        Chat_User_Img = findViewById(R.id.Chat_User_Img);
        Chat_user_Name = findViewById(R.id.Chat_user_Name);

        //채팅방 기본정보 셋팅
        Chat_User_Img.setImageURI(Uri.parse(User_Db_ArrayList.get(Another_User_Position).getUser_icon_img()));
        Chat_user_Name.setText(User_Db_ArrayList.get(Another_User_Position).getUser_name());

        // 리사이클러뷰 사이즈
        Chat_Inside_Recycle.setHasFixedSize(true);

        // 레이아웃 생성
        Chat_Inside_LayoutManager = new LinearLayoutManager(Chat_Message_Activity.this, LinearLayoutManager.VERTICAL, false);

        // 레이아웃 설정
        Chat_Inside_Recycle.setLayoutManager(Chat_Inside_LayoutManager);

        // 어뎁터 생성
        Chat_Inside_Adapter = new Chat_Inside_Adapter(Chat_Inside_User_Data_Array, Login_User_Id);

        // 어뎁터 적용
        Chat_Inside_Recycle.setAdapter(Chat_Inside_Adapter);

        // 화면 맨밑으로 내리기
        Chat_Inside_Recycle.scrollToPosition(Chat_Inside_User_Data_Array.size()-1);

        // 채팅입력창
        Chat_User_Input_Message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    // 화면 맨밑으로 내리기
                Chat_Inside_Recycle.scrollToPosition(Chat_Inside_User_Data_Array.size()-1);
                return false;
            }
        });

        // 채팅 메시지 보내기 버튼
        Chat_Push_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Chat_User_Input_Message.getText().toString().equals("")) {
                    // 유저 객체 생성 (로그인 아이디, 채팅 입력 내용, 로그인 유저 이미지)
                    Chat_Inside_User_Data chat = new Chat_Inside_User_Data(Login_User_Id, Chat_User_Input_Message.getText().toString(), User_Db_ArrayList.get(Login_User_Position).getUser_icon_img(), time());

                    // 채팅방 내용 추가
                    Chat_Inside_User_Data_Array.add(chat);

                    // 텍스트 입력창 지우기
                    Chat_User_Input_Message.setText(null);

                    // 채팅방 내용 새로고침
                    Chat_Inside_Adapter.notifyDataSetChanged();

                    Chat_Inside_Recycle.scrollToPosition(Chat_Inside_User_Data_Array.size() - 1);

                    Chat_List_Array.get(Chat_Message_List_Position).setChat_Check(false);
                }
                else{

                    // 키보드 올리기 위해 선언
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    Chat_User_Input_Message.requestFocus();

                    imm.showSoftInput(Chat_User_Input_Message, 0);

                    Toast.makeText(getApplicationContext(), "메시지를 입력해주세요!", Toast.LENGTH_SHORT).show();

                }
            }
        });


        // 뒤로가기 버튼
        Chat_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 채팅방 내용 찾기
    public void getChat_Message(){
        // 채팅방 전체 목록 만큼 반복
        for(int i = 0; i < Chat_List_Array.size(); i++){
            // 채팅방 만든 유저 채팅 받은 유저 이름이 일치하면
            if(Chat_List_Array.get(i).getChat_Maker_User().equals(Login_User_Id) && Chat_List_Array.get(i).getChat_Receiver_User().equals(Another_User_Id) ||Chat_List_Array.get(i).getChat_Maker_User().equals(Another_User_Id) && Chat_List_Array.get(i).getChat_Receiver_User().equals(Login_User_Id)){
                // 채팅방 포지션
                Chat_Message_List_Position = i;
                // 채팅방 내용 어레이만큼 반복
                for(int j = 0; j < Chat_List_Array.get(i).getChat_Inside_User_Data_Array().size(); j++){
                    // 어뎁터 용 채팅방 내용 어레이에 채팅 내용 객체 추가
                    Chat_Inside_User_Data_Array.add(Chat_List_Array.get(i).getChat_Inside_User_Data_Array().get(j));
                }
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 채팅방 내용 저장
        setChat_Message();

        // 채팅 데이터 저장
        setChat_List();

    }

    // 채팅방 목록 불러오기
    public void getChat_List(){

        //쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Chat", 0);

        // 키값 정해주고
        String Chat_List_Array = sharedPreferences.getString("Data", "");

        // JsonArray를 이용해서 Chat_List형 어레이리스트에 담는과정
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

    // 채팅방 내용 저장
    public void setChat_Message(){
        Chat_List_Array.get(Chat_Message_List_Position).getChat_Inside_User_Data_Array().clear();

        for(int i = 0; i < Chat_Inside_User_Data_Array.size(); i++){
            Chat_List_Array.get(Chat_Message_List_Position).getChat_Inside_User_Data_Array().add(Chat_Inside_User_Data_Array.get(i));
        }
    }

    // 유저 포지션 확인
    public void User_Position_Check(){
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_Id)){
                Login_User_Position = i;
                break;
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

    // 상대방 포지션 확인
    public void Another_User_Position_Check(){
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Another_User_Id)){
                Another_User_Position = i;
                break;
            }
        }
    }

    // 시간 출력
    public String time(){

        // 현재시간 가져오기
        SimpleDateFormat format = new SimpleDateFormat ("yyyyMMddHHmm");

        // 캘린더 객체 time 생성
        Calendar time = Calendar.getInstance();

        // 포매팅
        String format_time = format.format(time.getTime());

        // 값 반환
        return format_time;
    }
}