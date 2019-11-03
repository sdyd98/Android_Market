package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Login_Activity extends AppCompatActivity {

    // 세션 콜백 선언
    private SessionCallback sessionCallback;

    // 뷰 선언
    TextInputEditText id, password;
    Button btn_login, btn_sign_up;

    // 쉐어드 정보를 담을 어레이
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

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

        // 뷰매칭
        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        // 유저 정보 가져오기
        getUser_Shared();

        // 자동 로그인
        Auto_Login();

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

    // 액티비티 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    // 액티비티 종료
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    // 액티비티 재시작
    @Override
    protected void onRestart() {
        super.onRestart();

        // 기존 유저 어레이 리스트 초기화
        User_Db_ArrayList.clear();

        // 유저 정보를 가져옴
        getUser_Shared();
    }

    // 로그인 버튼
    public void Login_Btn(View view){

        // 에딧 텍스트에 입력된 값 가져오기
        String User_Id = id.getText().toString();
        String User_PW = password.getText().toString();

        // 유저정보 어레이 크기만큼 ID PW 중복검사
        for(int i = 0; i < User_Db_ArrayList.size(); i++){

            // 유저정보 어레이에 일치하는 아이디가 있다면 실행
            if(User_Db_ArrayList.get(i).getUser_id().equals(User_Id)){

                // 그 유저 비밀번호가 일치하는지 판별
                if(User_Db_ArrayList.get(i).getUser_pw().equals(User_PW)){

                    // 메인화면으로 인텐트 생성
                    Intent intent = new Intent(getApplicationContext(), Main_Activity.class);

                    // 로그인 아이디 전달
                    intent.putExtra("User_ID", User_Id);

                    // 인텐트 전송
                    startActivity(intent);

                    // 로그인 성공 메시지
                    Toast.makeText(getApplicationContext(), "로그인 성공!!", Toast.LENGTH_SHORT).show();

                    // 자동로그인 쉐어드
                    setAuto_Login(User_Id, i);

                    // 현재 액티비티 종료
                    finish();

                    // for문 종료
                    break;
                }

                // 비밀번호가 일치하지 않을때
                else{

                    // 로그인 실패 메시지
                    Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();

                    // for문 종료
                    break;

                }
            }

            // for문을 다 돌렸는데 일치하는 아이디를 못찾았을때 (수정 필요)
            if(i+1 == User_Db_ArrayList.size()){
                Toast.makeText(getApplicationContext(), "아이디를 확인하세요.", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    // 유저 정보 가져오기
    public void getUser_Shared(){

        // 쉐어드 파일이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("User",0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if(user_db_array !=  null) {
            try {

                // JsonArray로 파싱된 user_db_array (스트링)값으로 JsonArray 생성
                JSONArray jsonArray_user_db = new JSONArray(user_db_array);

                // JsonArray 길이 만큼 반복
                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    // data 변수에 JsonArray 값을 넣는다
                    String data = jsonArray_user_db.optString(i);

                    // gson 생성
                    Gson gson = new Gson();

                    // user 객체 생성 gson으로 data를 파싱
                    User_DB user_db = gson.fromJson(data, User_DB.class);

                    // 파싱된 데이터를 기존 어레이에 추가
                    User_Db_ArrayList.add(user_db);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    // 자동 로그인 메소드
    public void Auto_Login(){
        // 쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("User",0);

        String auto_login = sharedPreferences.getString("auto", "");

        // 쉐어드에 데이터가 있다면 실행
        if(!auto_login.equals("")){

            // 유저 정보 어레이 사이즈만큼 반복
            for(int i = 0; i < User_Db_ArrayList.size(); i++){

                // auto_login 변수로 일치하는 유저 어레이에서 찾기
                if(User_Db_ArrayList.get(i).getUser_id().equals(auto_login)){

                    // 키값에 저장된 데이터가 자동로그인 여부가 true인지 판별하고 true면 자동 로그인
                    if(User_Db_ArrayList.get(i).isAuto_login()){

                        // 자동 로그인 메시지
                        Toast.makeText(getApplicationContext(), "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();

                        // 액티비티 종료
                        finish();

                        // Main으로 인텐트 생성
                        Intent intent = new Intent(getApplicationContext(), Main_Activity.class);

                        // 아이디값 인텐트
                        intent.putExtra("User_ID", auto_login);

                        System.out.println("한번더 지나감1");

                        // 인텐트 전송
                        startActivity(intent);

                        break;
                    }
                }
            }
        }
    }

    // 자동 로그인 설정 메소드
    public void setAuto_Login(String User_Id, int User_Position){

        // 쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("User", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Key="auto" User 아이디 저장
        editor.putString("auto", User_Id);

        // 저장 완료
        editor.apply();

        // 로그인한 유저 정보에 Auto Login 옵션 true
        User_Db_ArrayList.get(User_Position).setAuto_login(true);

        // 유저 정보 저장
        User_Save_Shared();

    }

    // 콜백 클래스
    private class SessionCallback implements ISessionCallback {
        // 세션 열림
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                }

                // 로그인 성공했을때
                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), Main_Activity.class);

                    if(Kakao_ID_Check(String.valueOf(result.getId()))){

                    }
                    else{

                        //현재시간 Date
                        Date curDate = new Date();

                        // 포멧팅 형태
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

                        //현재시간을 요청시간의 형태로 format 후 time 가져오기
                        try {
                            curDate = dateFormat.parse(dateFormat.format(curDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // 아이디 생성 날짜 입력
                        String curDateTime = dateFormat.format(curDate.getTime());

                        // 첫작성시 기본 이미지 drawable 가져오기
                        Drawable drawable = getResources().getDrawable(R.drawable.user_icon_3);

                        // 기본 이미지 drawalbe 을 bitmap으로 파싱
                        Bitmap bitmap2 = ((BitmapDrawable)drawable).getBitmap();

                        // 유저 객체 생성
                        User_DB user_db = new User_DB(getImageUri(getApplicationContext(), bitmap2).toString(), String.valueOf(result.getId()), "",result.getKakaoAccount().getProfile().getNickname(), curDateTime, false, 0);

                        // 생성된 객체 어레이에 추가
                        User_Db_ArrayList.add(user_db);

                    }

                    int user_position = 0;
                    for(int i = 0; i < User_Db_ArrayList.size(); i++){
                        if(User_Db_ArrayList.get(i).getUser_id().equals(String.valueOf(result.getId()))){
                            user_position = i;
                            break;
                        }
                    }

                    // auto 로그인이 false면 실행
                    if(!User_Db_ArrayList.get(user_position).isAuto_login()){
                        // 자동로그인 쉐어드
                        setAuto_Login(String.valueOf(result.getId()), user_position);

                        // 로그인 아이디 전달
                        intent.putExtra("User_ID", String.valueOf(result.getId()));

                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Toast.makeText(getApplicationContext(), "문제 발생 발생", Toast.LENGTH_SHORT).show();
        }
    }

    // bitmap 이미지 uri 변환
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // 카카오 계정 확인
    public boolean Kakao_ID_Check(String kakao_id){
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(kakao_id)){
                return true;
            }
        }
        return false;
    }
}