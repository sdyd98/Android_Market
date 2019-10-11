package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Sign_Up_Activity extends AppCompatActivity {


//    static ArrayList<ArrayList<String>> person = new ArrayList<ArrayList<String>>();
//    static ArrayList<String> person_detail = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();



    boolean ID_Check = false;

    TextInputEditText Sign_Up_Id, Sign_Up_Pw, Sign_Up_Pw_Check, Sign_Up_Name;
    ImageView Password_Check_Img;
    Button Sign_Up_Btn, User_Id_Check;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        User_getShared("User","Data", User_Db_ArrayList);


        // 뷰 매칭
        Sign_Up_Id = findViewById(R.id.Sign_Up_Id);
        Sign_Up_Pw = findViewById(R.id.Sign_Up_Pw);
        Sign_Up_Name = findViewById(R.id.Sign_Up_Name);
        Sign_Up_Pw_Check = findViewById(R.id.Sign_Up_Pw_Check);
        Sign_Up_Btn = findViewById(R.id.Sign_Up_Btn);
        Password_Check_Img = findViewById(R.id.Password_Check_Img);
        User_Id_Check = findViewById(R.id.User_Id_Check);


        //비밀번호 실시간 체크 1
            Sign_Up_Pw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!Sign_Up_Pw_Check.getText().toString().equals("")) {
                        if (Sign_Up_Pw.getText().toString().equals(Sign_Up_Pw_Check.getText().toString())) {
                            Password_Check_Img.setImageResource(R.drawable.check);
                        } else {
                            Password_Check_Img.setImageResource(R.drawable.close);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        //비밀번호 실시간 체크 2
        Sign_Up_Pw_Check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Sign_Up_Pw.getText().toString().equals(Sign_Up_Pw_Check.getText().toString())){
                    Password_Check_Img.setImageResource(R.drawable.check);
                }
                else{
                    Password_Check_Img.setImageResource(R.drawable.close);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    public void Check_Id(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(User_Db_ArrayList.size() != 0) {
            for (int i = 0; i < User_Db_ArrayList.size(); i++) {
                if (!User_Db_ArrayList.get(i).getUser_id().equals(Sign_Up_Id.getText().toString())) {
                    ID_Check = true;
                    Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다!", Toast.LENGTH_SHORT).show();
                } else {
                    ID_Check = false;
                    Toast.makeText(getApplicationContext(), "이미 있는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    Sign_Up_Id.requestFocus();
                    imm.showSoftInput(Sign_Up_Id, 0);
                }
            }
        }
        else{
            ID_Check = true;
            Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다!", Toast.LENGTH_SHORT).show();
        }
    }

    // 회원가입하기 버튼
    public void Sign_Up_Btn(View view){
        // 키보드 자동 올리기
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        String name_check = Sign_Up_Name.getText().toString();
        String id_check = Sign_Up_Id.getText().toString();
        String pw_check1 = Sign_Up_Pw.getText().toString();
        String pw_check2 = Sign_Up_Pw_Check.getText().toString();

        // 닉네임 기입 여부 check
        if(name_check.equals("")){
            Toast.makeText(getApplicationContext(), "닉네임을 입력 하세요", Toast.LENGTH_SHORT).show();
            // 포커스 맞추기
            Sign_Up_Name.requestFocus();
            //키보드 올리기
            imm.showSoftInput(Sign_Up_Name, 0);
        }
        // 아이디 기입 여부 check
        if(!name_check.equals("")&&id_check.equals("")){
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            Sign_Up_Id.requestFocus();
            imm.showSoftInput(Sign_Up_Id, 0);
        }

        // 비밀번호 기입 여부 check
        if(!name_check.equals("")&&!id_check.equals("")&&pw_check1.equals("")){
            Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            Sign_Up_Pw.requestFocus();
            imm.showSoftInput(Sign_Up_Pw, 0);
        }

        // 비밀번호 2차확인
        if(!name_check.equals("")&&!id_check.equals("")&&!pw_check1.equals("")&&pw_check2.equals("")||!pw_check1.equals(pw_check2)){
            Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            Sign_Up_Pw_Check.requestFocus();
            imm.showSoftInput(Sign_Up_Pw_Check, 0);
        }

        if(!name_check.equals("")&&!id_check.equals("")&&!pw_check1.equals("")&&!pw_check2.equals("")&&pw_check1.equals(pw_check2)&& ID_Check == false){
            Toast.makeText(getApplicationContext(), "아이디 중복검사를 완료하세요.", Toast.LENGTH_SHORT).show();
        }

        // 회원가입 성공 부분 쉐어드로 데이터 전달
        if(!name_check.equals("")&&!id_check.equals("")&&!pw_check1.equals("")&&!pw_check2.equals("")&&(pw_check1.equals(pw_check2))&& ID_Check == true){
            User_DB user_db = new User_DB(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.user_icon_3).toString() ,Sign_Up_Id.getText().toString(), Sign_Up_Pw.getText().toString());
            User_Db_ArrayList.add(user_db);
            User_setShared("User","Data", User_Db_ArrayList);
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "회원가입 성공!!", Toast.LENGTH_SHORT).show();

        }
    }

    // 유저 정보 쉐어드 get
    public void User_getShared(String Name, String Key, ArrayList<User_DB> User_Db_ArrayList){
        // 쉐어드 이름과 모드 설정
        SharedPreferences sharedPreferences = getSharedPreferences(Name,0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString(Key, "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if(user_db_array !=  null) {
            try {
                JSONArray jsonArray_user_db = new JSONArray(user_db_array);
                for (int i = 0; i < jsonArray_user_db.length(); i++) {
                    String data = jsonArray_user_db.optString(i);
                    Gson gson = new Gson();
                    User_DB user_db = gson.fromJson(data, User_DB.class);
                    User_Db_ArrayList.add(user_db);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                }
            }
        }


    //유저 정보 쉐어드 set
    // 파일이름 , 키값, 저장할 데이터
    public void User_setShared(String Name, String Key, ArrayList<User_DB> user_db_array){
        // 쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences(Name, 0);
        // 쉐어드 저장한다 선언
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 예비 어레이리스트 생성
        // 예비 어레이리스트에 현재 user_db_array 스트링값으로 파싱하여 저장
        ArrayList<String> setUser_Db_Array = new ArrayList<>();
        for(int i = 0; i < user_db_array.size(); i++){
            Gson gson = new Gson();
            String user_db = gson.toJson(User_Db_ArrayList.get(i));
            setUser_Db_Array.add(user_db);
        }

        // Json 선언
        JSONArray setJsonArray = new JSONArray();

        // 예비 어레이리스트 값을 JsonArray에 모두 저장
        for(int i =0; i < setUser_Db_Array.size(); i ++){
            setJsonArray.put(setUser_Db_Array.get(i));
        }

        // 키값에 데이터 저장
        if(!user_db_array.isEmpty()){
            editor.putString(Key, setJsonArray.toString());
        }
        else{
            editor.putString(Key, null);
        }
        editor.commit();
    }

}

