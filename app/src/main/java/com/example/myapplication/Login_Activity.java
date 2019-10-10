package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Login_Activity extends AppCompatActivity {

    TextInputEditText id, password;
    Button btn_login, btn_sign_up;
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();
    Sign_Up_Activity sign_up_activity = new Sign_Up_Activity();


//    int check_id_num;
//    boolean check_id_value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 카메라 접근 권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(Login_Activity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        // 저장소 접근 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_sign_up = findViewById(R.id.btn_sign_up);



        //회원가입 버튼
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Sign_Up_Activity.class);
                startActivity(intent);
            }
        });



        // 로그인 버튼
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String check_id = id.getText().toString();
//                String check_password = password.getText().toString();
//                check_id_value = false;
//
//
//
//                for (int i = 0; i < Sign_Up_Activity.person.size(); i++) {
//                    if (Sign_Up_Activity.person.get(i).get(0).equals(check_id)) {
//                        check_id_num = i;
//                        check_id_value = true;
//                        break;
//                    }
//                    System.out.println("아이디 확인==================="+Sign_Up_Activity.person.get(i).get(0));
//                    System.out.println("사이즈확인==================="+Sign_Up_Activity.person.size());
//                }
//                if (check_id_value == true) {
//                    //로그인 성공
//                    if (check_id.equals(Sign_Up_Activity.person.get(check_id_num).get(0)) && check_password.equals(Sign_Up_Activity.person.get(check_id_num).get(1))) {
//                        Intent intent = new Intent(Login_Activity.this, Main_Activity.class);
//                        startActivity(intent);
//                        Toast.makeText(Login_Activity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
//                    } else if (!check_id.equals("sdyd98") && !check_password.equals("123")) {
//                        Toast.makeText(Login_Activity.this, "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
//                    } else if (!check_id.equals("sdyd98")) {
//                        Toast.makeText(Login_Activity.this, "아이디를 확인하세요.", Toast.LENGTH_SHORT).show();
//                    } else if (!check_password.equals("123")) {
//                        Toast.makeText(Login_Activity.this, "비밀번호를 확인하세요..", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    Toast.makeText(Login_Activity.this, "아이디를 확인하세요---------.", Toast.LENGTH_SHORT).show();
//                    System.out.println("확인확인확인확인확인확인확인확인확인확인확인확인확인확인확인"+check_id);
//                    System.out.println("길이길이길이길이길이길이길이길이길이길이길이길이길이길이길이"+check_id.length());
//                }
//            }
//        });
    }


    public void Login_Btn(View view){

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

        String User_Id = id.getText().toString();
        String User_PW = password.getText().toString();

        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(User_Id)){
                if(User_Db_ArrayList.get(i).getUser_pw().equals(User_PW)){
                    Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그인 성공!!", Toast.LENGTH_SHORT).show();
                    break;
                }
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(i+1 == User_Db_ArrayList.size()){
                Toast.makeText(getApplicationContext(), "아이디를 확인하세요.", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}