package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.Parser;

import java.util.ArrayList;

public class Chat_Activity extends AppCompatActivity {

    // 유저의 채팅방 데이터
    ArrayList<Chat_List> Chat_User_List = new ArrayList<>();

    // 채팅방 전체 데이터
    ArrayList<Chat_List> Chat_List_Array = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 뷰선언
    ImageView Chat_Icon_Back_Btn, Chat_Plus;

    // 리사이클러뷰 선언
    RecyclerView Chat_Recycle;
    RecyclerView.LayoutManager Chat_LayoutManager;
    RecyclerView.Adapter Chat_Adapter;

    // 로그인한 유저 아이디
    private String Login_User_ID;

    // 상대판 유저 아이디
    private String Another_User_ID;

    // 로그인한 유저 포지션
    private int Login_User_position;

    // 상대방 유저 포지션
    private int Another_User_Position;


    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // 로그인 유저 아이디
        Login_User_ID = getIntent().getStringExtra("User_ID");

        // 채팅방 전체 데이터 불러오기
        getChat_List();

        // 유저의 채팅방 분류
        setUser_Chat_List();

        // 유저 쉐어드 가져오기
        User_getShared();

        // 유저 포지션 체크
        User_Position_Check();

        // 뷰 매칭
        Chat_Icon_Back_Btn = findViewById(R.id.Chat_Icon_Back_Btn);
        Chat_Plus = findViewById(R.id.Chat_Plus);
        Chat_Recycle = findViewById(R.id.Chat_Recycle);

        // 리사이클러뷰 사이즈
        Chat_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 생성
        Chat_LayoutManager = new LinearLayoutManager(Chat_Activity.this, LinearLayoutManager.VERTICAL, false);

        // 레이아웃 설정
        Chat_Recycle.setLayoutManager(Chat_LayoutManager);

        // 어뎁터 생성
        Chat_Adapter = new Chat_Adapter(Chat_User_List, new View.OnClickListener() {
            @Override
            // 채팅방 클릭
            public void onClick(View v) {
                Object obj = v.getTag();
                if (obj != null) {
                    int position = (int) obj;
                    Intent intent = new Intent(Chat_Activity.this, Chat_Message_Activity.class);

                    // 로그인 유저 아이디 인텐트
                    intent.putExtra("User_ID", Login_User_ID);

                    String Another_User_ID;

                    // 상대방 아이디 확인
                    if (((Chat_Adapter) Chat_Adapter).getData(position).getChat_Maker_User().equals(Login_User_ID)) {
                        Another_User_ID = ((Chat_Adapter) Chat_Adapter).getData(position).getChat_Receiver_User();
                    } else {
                        Another_User_ID = ((Chat_Adapter) Chat_Adapter).getData(position).getChat_Maker_User();
                    }

                    // 상대방 아이디 인텐트
                    intent.putExtra("Another_User_ID", Another_User_ID);

                    if(((Chat_Adapter) Chat_Adapter).Chat_Count(position) == 0){
                        // 채팅방 확인 유무
                        ((Chat_Adapter) Chat_Adapter).getData(position).setChat_Check(false);
                    }
                    else{
                        // 채팅방 확인 유무
                        ((Chat_Adapter) Chat_Adapter).getData(position).setChat_Check(true);
                    }
                    startActivity(intent);
                }
            }
        }, Login_User_ID);
        Chat_Recycle.setAdapter(Chat_Adapter);


        // 채팅방 생성 ( 이미 만들어져 있으면 생성되면 안됨 )
        Chat_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat_Activity.this);

                // 인플레이터
                View view = LayoutInflater.from(Chat_Activity.this)
                        .inflate(R.layout.chat_match_view, null, false);
                builder.setView(view);

                // 버튼 생성과 매칭
                final Button ButtonSubmit = view.findViewById(R.id.Chat_User_Btn);
                final EditText editTextID = view.findViewById(R.id.Chat_User_Input_Id);

                // 다이얼로그 생성
                final AlertDialog dialog = builder.create();

                // 채팅 하기 버튼
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // 상대방 아이디
                        Another_User_ID = editTextID.getText().toString();

                        if (User_Check(Another_User_ID) == true) {
                            if (Chat_User_List.size() != 0) {
                                // 만약 상대방 아이디가 내 채팅방에 있다면 작동 x
                                for (int i = 0; i < Chat_User_List.size(); i++) {
                                    if (Chat_User_List.get(i).getChat_Maker_User().equals(Another_User_ID) || Chat_User_List.get(i).getChat_Receiver_User().equals(Another_User_ID)) {



                                        // 채팅방 안으로 인텐트 생성
                                        Intent intent = new Intent(getApplicationContext(), Chat_Message_Activity.class);

                                        // 로그인 유저 아이디 인텐트
                                        intent.putExtra("User_ID", Login_User_ID);

                                        // 상대방 아이디 인텐트
                                        intent.putExtra("Another_User_ID", Another_User_ID);

                                        startActivity(intent);

                                        break;
                                    }

                                    if (i == Chat_User_List.size() - 1) {

                                        Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();

                                        // 상대방 포지션 체크
                                        Another_User_Position_Check();


                                        // 채팅방 객체 생성 ( 로그인 유저 아이디, 상대방 유저 아이디, 채팅방 내용 어레이 리스트, 이미지)
                                        Chat_List chat_list = new Chat_List(Login_User_ID, editTextID.getText().toString(), User_Db_ArrayList.get(Login_User_position).getUser_icon_img(), User_Db_ArrayList.get(Another_User_Position).getUser_icon_img(), User_Db_ArrayList.get(Login_User_position).getUser_name(), User_Db_ArrayList.get(Another_User_Position).getUser_name());
                                        // 전체 채팅방 어레이에 채팅방 추가
                                        Chat_List_Array.add(chat_list);

                                        // 채팅방 데이터 쉐어드에 저장
                                        setChat_List();

                                        // 유저 채팅방 분류
                                        setUser_Chat_List();

                                        // 어뎁터 새로고침
                                        Chat_Adapter.notifyDataSetChanged();

                                        break;
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();
                                // 상대방 포지션 체크
                                Another_User_Position_Check();

                                // 채팅방 객체 생성 ( 로그인 유저 아이디, 상대방 유저 아이디, 채팅방 내용 어레이 리스트, 이미지)
                                Chat_List chat_list = new Chat_List(Login_User_ID, editTextID.getText().toString(), User_Db_ArrayList.get(Login_User_position).getUser_icon_img(), User_Db_ArrayList.get(Another_User_Position).getUser_icon_img(), User_Db_ArrayList.get(Login_User_position).getUser_name(), User_Db_ArrayList.get(Another_User_Position).getUser_name());
                                // 전체 채팅방 어레이에 채팅방 추가
                                Chat_List_Array.add(chat_list);

                                // 채팅방 데이터 쉐어드에 저장
                                setChat_List();

                                // 유저 채팅방 분류
                                setUser_Chat_List();

                                // 어뎁터 새로고침
                                Chat_Adapter.notifyDataSetChanged();

                            }

                            // 다이얼 로그 종료
                            dialog.dismiss();
                        } else {
                            if(editTextID.getText().toString().equals(Login_User_ID)){
                                Toast.makeText(getApplicationContext(), "자기 자신을 초대 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "해당 유저가 없습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

        // 뒤로가기
        Chat_Icon_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 액티비티 재시작
    @Override
    protected void onRestart() {
        super.onRestart();

        Chat_List_Array.clear();

        Chat_List_Array.clear();

        // 채팅방 전체 데이터 불러오기
        getChat_List();

        // 유저의 채팅방 분류
        setUser_Chat_List();

        Chat_Adapter.notifyDataSetChanged();

    }

    // 액티비티 정지
    @Override
    protected void onPause() {
        super.onPause();

        // 채팅방 목록 저장
        setChat_List();
    }

    // 로그인 유저에 채팅방 분류
    public void setUser_Chat_List() {

        Chat_User_List.clear();

        // 전체 채팅방 목록만큼 반복
        for (int i = 0; i < Chat_List_Array.size(); i++) {
            // 만약 채팅방 아이디 목록에 유저의 아이디가 들어가있는 채팅방을 발견 한다면
            if (Chat_List_Array.get(i).getChat_Maker_User().equals(Login_User_ID) || Chat_List_Array.get(i).getChat_Receiver_User().equals(Login_User_ID)) {
                // 그채팅방 추가
                Chat_User_List.add(Chat_List_Array.get(i));
            }
        }
    }

    // 채팅방 목록 불러오기
    public void getChat_List() {
        SharedPreferences sharedPreferences = getSharedPreferences("Chat", 0);
        String Chat_List_Array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if (Chat_List_Array != null) {
            try {

                // Key="Data" 값을 이용해 JsonArray 생성
                JSONArray jsonArray_user_db = new JSONArray(Chat_List_Array);

                //public Node(Tag tag, Mark startMark, Mark endMark) {

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
    public void setChat_List() {

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
            setJsonArray.put(saveChat_List_Array.get(i));
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

    // 유저 포지션 확인
    public void User_Position_Check() {
        for (int i = 0; i < User_Db_ArrayList.size(); i++) {
            if (User_Db_ArrayList.get(i).getUser_id().equals(Login_User_ID)) {
                Login_User_position = i;
                break;
            }
        }
    }

    // 상대방 포지션 확인
    public void Another_User_Position_Check() {
        for (int i = 0; i < User_Db_ArrayList.size(); i++) {
            if (User_Db_ArrayList.get(i).getUser_id().equals(Another_User_ID)) {
                Another_User_Position = i;
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

    // 해당 유저 있는지 판별
    public boolean User_Check(String User_Name) {
        boolean User_Check = true;

        // 전체 유저 목록 만큼 반복
        for (int i = 0; i < User_Db_ArrayList.size(); i++) {
            // 매개변수로 받은 유저 이름이 있다면
            if (User_Db_ArrayList.get(i).getUser_id().equals(User_Name) && !User_Db_ArrayList.get(i).getUser_id().equals(Login_User_ID)) {
                User_Check = true;
                break;
            }
            if (i == User_Db_ArrayList.size() - 1) {
                User_Check = false;
            }
        }

            return User_Check;
        }
    }



