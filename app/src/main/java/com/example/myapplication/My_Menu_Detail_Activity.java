package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Menu_Detail_Activity extends AppCompatActivity {

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // requestcode
    private final int GET_GALLERY_IMAGE = 200;
    private final int CAPTURE_IMAGE = 300;

//    String My_Menu_User_Icon;
    CircleImageView My_Menu_Detail_User_Icon;
    ImageView My_Menu_Detail_Icon_Back;
    TextView My_Menu_Detail_User_Name;
    private int User_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        My_Menu_Detail_User_Name = findViewById(R.id.My_Menu_Detail_User_Name);
        My_Menu_Detail_User_Icon = findViewById(R.id.My_Menu_Detail_User_Icon);
        My_Menu_Detail_Icon_Back = findViewById(R.id.My_Menu_Detail_Icon_Back);

//        if(User_Name != null){
//            My_Menu_Detail_User_Name.setText(User_Name);
//        }
//
//        if(My_Menu_User_Icon != null){
//            My_Menu_Detail_User_Icon.setImageURI(Uri.parse(My_Menu_User_Icon));
//        }
        User_getShared("User", "Data", User_Db_ArrayList);


        // 유저 정보에서 로그인 할때 아이디와 같은 유저정보 파악
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(getIntent().getStringExtra("User_ID"))){
                User_position = i;
                break;
            }
        }

        My_Menu_Detail_User_Icon.setImageURI(Uri.parse(User_Db_ArrayList.get(User_position).getUser_icon_img()));
        My_Menu_Detail_User_Name.setText(User_Db_ArrayList.get(User_position).getUser_name());


        My_Menu_Detail_User_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(My_Menu_Detail_Activity.this);
                alert.setTitle("닉네임 변경");
                alert.setMessage("변경할 닉네임 입력");
                final EditText name = new EditText(My_Menu_Detail_Activity.this);
                alert.setView(name);
                alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String username = name.getText().toString();
                        My_Menu_Detail_User_Name.setText(username);
                        User_Db_ArrayList.get(User_position).setUser_name(username);
                        User_setShared("User", "Data", User_Db_ArrayList);
//                        My_Menu_Detail_User_Icon.setImageDrawable();
                    }
                });
                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();

            }
        });

//        My_Menu_Detail_User_Icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, My_Menu_GET_GALLERY_IMAGE);
//            }
//        });

        My_Menu_Detail_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Mymenu_Activity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // 유저 정보 쉐어드 get
    public void User_getShared(String Name, String Key, ArrayList<User_DB> User_Db_ArrayList) {
        // 쉐어드 이름과 모드 설정
        SharedPreferences sharedPreferences = getSharedPreferences(Name, 0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString(Key, "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if (user_db_array != null) {
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

    // startActivityForResult 값 받아오기
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // 카메라 사진일때
        if(resultCode == RESULT_OK  && requestCode == CAPTURE_IMAGE){
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            if(bitmap != null){
                My_Menu_Detail_User_Icon.setImageURI(getImageUri(getApplicationContext(), bitmap));
                User_Db_ArrayList.get(User_position).setUser_icon_img(getImageUri(getApplicationContext(), bitmap).toString());
                User_setShared("User", "Data", User_Db_ArrayList);
            }
        }

        // 갤러리 사진일때
        else if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            My_Menu_Detail_User_Icon.setImageURI(intent.getData());
            User_Db_ArrayList.get(User_position).setUser_icon_img(intent.getData().toString());
            User_setShared("User", "Data", User_Db_ArrayList);
        }
    }

    // bitmap 이미지 uri 변환
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    // 사진 가져올 선택 다이얼로그
    public void photoDialogRadio(View v){
        // 항목 설정
        final CharSequence[] PhotoModels = {"갤러리에서 가져오기", "직접찍어서 가져오기"};
        // 다이얼로그 선언
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

        // 타이틀 설정
        alt_bld.setTitle("사진 가져오기");
        alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), PhotoModels[item]+"가 선택되었습니다", Toast.LENGTH_SHORT).show();
                if (item == 0){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, GET_GALLERY_IMAGE);
                    dialog.dismiss();
                }
                else{
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAPTURE_IMAGE);
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    //유저 정보 쉐어드 set
    // 파일이름 , 키값, 저장할 데이터
    public void User_setShared(String Name, String Key, ArrayList<User_DB> user_db_array) {
        // 쉐어드 선언
        SharedPreferences sharedPreferences = getSharedPreferences(Name, 0);
        // 쉐어드 저장한다 선언
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 예비 어레이리스트 생성
        // 예비 어레이리스트에 현재 user_db_array 스트링값으로 파싱하여 저장
        ArrayList<String> setUser_Db_Array = new ArrayList<>();
        for (int i = 0; i < user_db_array.size(); i++) {
            Gson gson = new Gson();
            String user_db = gson.toJson(User_Db_ArrayList.get(i));
            setUser_Db_Array.add(user_db);
        }

        // Json 선언
        JSONArray setJsonArray = new JSONArray();

        // 예비 어레이리스트 값을 JsonArray에 모두 저장
        for (int i = 0; i < setUser_Db_Array.size(); i++) {
            setJsonArray.put(setUser_Db_Array.get(i));
        }

        // 키값에 데이터 저장
        if (!user_db_array.isEmpty()) {
            editor.putString(Key, setJsonArray.toString());
        } else {
            editor.putString(Key, null);
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}