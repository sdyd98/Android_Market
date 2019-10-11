package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Sell_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    Main_Activity main_activity = new Main_Activity();
    String user_id = main_activity.user_id;

    // requestcode
    private final int GET_GALLERY_IMAGE = 200;
    private final int CAPTURE_IMAGE = 300;

    static Bitmap Camera_Bitmap;

    // 선언
    ImageView Sell_Back_Btn, Sell_Image_Btn;
    Button Sell_Finish_Btn;
    String Categori_Name;
    EditText Sell_Item_Name, Sell_Item_Price,Sell_Item_Detail;
    LinearLayout Sell_Location_Icon;
    Uri Image_save;
    Spinner spinner;
    String[] item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

        Item_getShared();

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

        // 구글맵 위치 따오는 부분 (수정해야함)
        Sell_Location_Icon.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(), "게시글 등록 완료!", Toast.LENGTH_SHORT).show();
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

    public void photoDialogRadio(View v){
        final CharSequence[] PhotoModels = {"갤러리에서 가져오기", "직접찍오서 가져오기"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

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



    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK  && requestCode == CAPTURE_IMAGE){
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            if(bitmap != null){
            Sell_Image_Btn.setImageBitmap(bitmap);
            Camera_Bitmap = bitmap;
            Image_save = null;
            }
        }
        else if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            Uri selectedImageUri = intent.getData();
            Image_save = selectedImageUri;
            Sell_Image_Btn.setImageURI(selectedImageUri);
            Camera_Bitmap = null;
        }
    }


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

    // 아이템 정보 set 쉐어드
    public void Item_setShared(){
        // 쉐어드 이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //기존 어레이에 추가후  -> 하나씩 꺼내어 객체 문자화 - > 예비 어레이에 담고 - > 예비 어레이 문자화 - > 쉐어드 저장
        ArrayList<String> item_db_string_array = new ArrayList<>();
        // 아이템 객체 생성
        Item_DB item_db = new Item_DB(item_db_array.size()+1, user_id, Sell_Item_Price.getText().toString(), Sell_Item_Name.getText().toString(), "0", "0", "0", Sell_Item_Detail.getText().toString(), "0", "0", Image_save.toString());
        // 기존 아이템 정보 어레이에 데이터 추가
        item_db_array.add(item_db);

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
}