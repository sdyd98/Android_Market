package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.ContactsContract.RawContacts.*;

public class Sell_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 유저 저옵 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 현재 작성자 아이디
    String user_id;
    String user_img;

    // test
    int count;

    // requestcode
    private final int GET_GALLERY_IMAGE = 200;
    private final int CAPTURE_IMAGE = 300;

    static double Hardness;
    static double Latitude;

    // 선언
    ImageView Sell_Back_Btn, Sell_Image_Btn;
    Button Sell_Finish_Btn;
    String Categori_Name;
    EditText Sell_Item_Name, Sell_Item_Price,Sell_Item_Detail;
    LinearLayout Sell_Location_Icon;
    String Image_save;
    Spinner spinner;
    String[] item;
    private int User_Position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

        // 게시글 고유 번호 get
        Item_Count_getShared();

        // 현재 접속 유저 아이디 인텐트 받은거 저장
        user_id = getIntent().getStringExtra("User_ID");

        // 유저 포지션 알아 내기
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            User_Db_ArrayList.get(i).getUser_id().equals(user_id);
            User_Position = i;
        }

        // 아이템 전체 목록 쉐어드 정보 세팅
        Item_getShared();

        // 유저 정보 세팅
        User_getShared();

        // 유저 아이디와 맞는 이미지 저장
        for(int i = 0; i < User_Db_ArrayList.size(); i++){
            if(user_id.equals(User_Db_ArrayList.get(i).getUser_id())){
                user_img = User_Db_ArrayList.get(i).getUser_icon_img();
            }
        }

        // 키보드 자동 올리기
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        // 스피너 item
        item =  new String[]{"카테고리","CPU","GPU","RAM","MB","SSD","HDD","Power","Cooler"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 스피너 매칭 설정
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);

        // 뷰매칭
        Sell_Item_Detail = findViewById(R.id.Sell_Item_detail);
        Sell_Location_Icon = findViewById(R.id.Sell_Location_Icon);
        Sell_Back_Btn = findViewById(R.id.Sell_Back_Btn);
        Sell_Image_Btn = findViewById(R.id.Sell_Image_Btn);
        Sell_Finish_Btn = findViewById(R.id.Sell_Finish_Btn);
        Sell_Item_Name = findViewById(R.id.Sell_Item_Name);
        Sell_Item_Price = findViewById(R.id.Sell_Item_Price);

        // 거래위치
        Sell_Location_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Map_Activity.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼
        Sell_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // 게시글 작성 완료 버튼
        Sell_Finish_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 첫작성시 기본 이미지 drawable 가져오기
                Drawable drawable = getResources().getDrawable(R.drawable.sell3);

                // 기본 이미지 drawalbe 을 bitmap으로 파싱
                Bitmap bitmap2 = ((BitmapDrawable)drawable).getBitmap();

                // 기존 박혀있는 Drawable 이미지 가져오기
                Bitmap bitmap = ((BitmapDrawable)Sell_Image_Btn.getDrawable()).getBitmap();

                // 게시글 작성 완료 버튼 수정 시작
                // 이미지 기입 여부 cheock
//                Log.e("checkimg", "기본이미지 비트맵 string"+bitmap.toString());
//                Log.e("checkimg", "원래 박혀있는 이미지 비트맵"+bitmap2);

                // 사진 기입 여부
                if(bitmap == bitmap2){
                    Toast.makeText(getApplicationContext(), "사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
                }

                // 상품명 기입 여부 check
                if(bitmap != bitmap2 && Sell_Item_Name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Sell_Item_Name.requestFocus();
                    imm.showSoftInput(Sell_Item_Name, 0);
                }

                // 카테고리 선택 여부 check
                if(bitmap != bitmap2 && !Sell_Item_Name.getText().toString().equals("") && Categori_Name.equals("카테고리")){
                    Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

                // 가격 기입 여부 check
                if(bitmap != bitmap2 && !Sell_Item_Name.getText().toString().equals("") && !Categori_Name.equals("카테고리") && Sell_Item_Price.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Sell_Item_Price.requestFocus();
                    imm.showSoftInput(Sell_Item_Price, 0);
                }

                // 거래 지역 선택 check 부분

                // APi 만들어야함 -----------------

                //

                // 상품정보 기입 여부 check
                if(bitmap != bitmap2 && !Sell_Item_Name.getText().toString().equals("") && !Categori_Name.equals("카테고리") && !Sell_Item_Price.getText().toString().equals("") && Sell_Item_Detail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "상품정보를 입력해주세요..", Toast.LENGTH_SHORT).show();
                    Sell_Item_Price.requestFocus();
                    imm.showSoftInput(Sell_Item_Detail, 0);
                }

                // 상품 등록 정보 쉐어드 저장 부분
                // 상품 정보
                // 1. 게시글 고유번호
                // 2. 작성자
                // 3. 이미지
                // 4. 가격
                // 5. 제목
                // 6. 작성 시간
                // 7. 조회수
                // 8. 좋아요 수
                // 9. 내용
                // 10. 거래위치
                // 11. 카테고리
                // 12. 댓글 내용
                // 13. 작성자 오픈일
                // 14. 작성자 팔로워
                // 15. 작성자 판매 목록 ??

                // 상품 등록 정보 쉐어드 저장 부분
                if(bitmap != bitmap2 && !Sell_Item_Name.getText().toString().equals("") && !Categori_Name.equals("카테고리") && !Sell_Item_Price.getText().toString().equals("") && !Sell_Item_Detail.getText().toString().equals("")){
                    Item_setShared();
                    Item_Count_setShared();
                    Toast.makeText(getApplicationContext(), "게시글 등록 완료! ==== "+count, Toast.LENGTH_SHORT).show();
                    finish();
                }
                // 수정 끝


//                // 기존 로직
//               if(Sell_Item_Name.getText().toString().equals("") || Sell_Item_Price.getText().toString().equals("") || Sell_Item_Detail.getText().toString().equals("") || (Categori_Name.equals("") || Categori_Name.equals("카테고리"))){
//                    Toast.makeText(Sell_Activity.this, "모두 작성하세요.", Toast.LENGTH_SHORT).show();
//                }
//                else{
////                    Main_Activity.Main_Image.setImageURI(Image_save);
//                    Intent intent = new Intent(Sell_Activity.this, Main_Activity.class);
//                    intent.putExtra("Item_Name",Sell_Item_Name.getText().toString());
//                    intent.putExtra("Item_Price",Sell_Item_Price.getText().toString());
//                    intent.putExtra("Item_Detail",Sell_Item_Detail.getText().toString());
//                    intent.putExtra("Item_Categori",Categori_Name);
//                   Log.e("text_check", Sell_Item_Detail.getText().toString()); //문제없음
//                   Log.e("text_check", Categori_Name);  // 문제없음
//                    if(Image_save != null) {
//                        intent.putExtra("Image_Uri", Image_save.toString());
//                    }
//                    setResult( RESULT_OK, intent );
//                    finish();
//                }
            }
        });

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
                Toast.makeText(Sell_Activity.this, PhotoModels[item]+"가 선택되었습니다", Toast.LENGTH_SHORT).show();
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



    // startActivityForResult 값 받아오기
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // 카메라 사진일때
        if(resultCode == RESULT_OK  && requestCode == CAPTURE_IMAGE){

            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            if(bitmap != null){
            Sell_Image_Btn.setImageURI(getImageUri(getApplicationContext(), bitmap));
            Image_save = getImageUri(getApplicationContext(), bitmap).toString();
            }
        }

        // 갤러리 사진일때
        else if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
                Image_save = intent.getData().toString();
                Sell_Image_Btn.setImageURI(intent.getData());
        }
    }


    // 구글 맵 암시적 인텐트 (삭제 예정)
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



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
        Categori_Name = item[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Categori_Name = "";
    }



//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode ==  && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri selectedImageUri = data.getData();
//            Image_save = selectedImageUri;
//            Sell_Image_Btn.setImageURI(selectedImageUri);
//
//        }
//
//        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
//            Uri selectedImageUri = data.getData();
//            Image_save = selectedImageUri;
//            Sell_Image_Btn.setImageURI(selectedImageUri);
//        }
//    }

    // 아이템 갯수 쉐어드 get
    public void Item_Count_getShared(){
        SharedPreferences sharedPreferences = getSharedPreferences("count", 0);
        count = sharedPreferences.getInt("item_count", 0);
    }

    // 아이템 갯수 쉐어드 set
    public void Item_Count_setShared(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("count", 0);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        if(count < 0){
            editor2.putInt("item_count", 1);
        }
        else{
            editor2.putInt("item_count", count+1);
        }

        editor2.commit();
    }


    // 아이템 정보 set 쉐어드
    public void Item_setShared(){

        // 쉐어드 이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //기존 어레이에 추가후  -> 하나씩 꺼내어 객체 문자화 - > 예비 어레이에 담고 - > 예비 어레이 문자화 - > 쉐어드 저장

        // 아이템 객체 생성
        Item_DB item_db = new Item_DB(count, User_Db_ArrayList.get(User_Position).getUser_name() , Sell_Item_Price.getText().toString(), Sell_Item_Name.getText().toString(), time(), 0, 0, Sell_Item_Detail.getText().toString(), 0, 0, Image_save, user_img, Categori_Name, user_id, Hardness, Latitude, true);
        // 기존 아이템 정보 어레이에 데이터 추가
        item_db_array.add(item_db);

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

    // 유저 정보 쉐어드 get
    public void User_getShared(){
        // 쉐어드 이름과 모드 설정
        SharedPreferences sharedPreferences = getSharedPreferences("User",0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString("Data", "");

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

    // bitmap 이미지 uri 변환
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

//    // 비트맵을 String 변환
//    public String getBitmap_String(Bitmap bitmap)
//    {
//        // 객체 생성
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        // 비트맵 이미지 변환
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//
//        // 바이트 배열에 입력
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//
//        // 스트링 값 반환
//        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
//    }

}