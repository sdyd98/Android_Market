package com.example.myapplication;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

import java.util.List;

public class Sell_fix_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    ImageView Sell_Back_Btn, Sell_Image_Btn, Sell_test;
    private final int GET_GALLERY_IMAGE = 200;
    private final int CAPTURE_IMAGE = 300;
    Button Sell_Finish_Btn;
    String Categori_Name;
    EditText Sell_Item_Name, Sell_Item_Price,Sell_Item_Detail;
    LinearLayout Sell_Location_Icon;
    Uri Image_save;
    Spinner spinner;
    String[] item;
    static Bitmap Camera_Bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_fix);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        item =  new String[]{"카테고리","CPU","GPU","RAM","MB","SSD","HDD","Power","Cooler"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        Sell_Item_Detail = findViewById(R.id.Sell_Item_detail);
        Sell_Location_Icon = findViewById(R.id.Sell_Location_Icon);
        Sell_Back_Btn = findViewById(R.id.Sell_Back_Btn);
        Sell_Image_Btn = findViewById(R.id.Sell_Image_Btn);
        Sell_Finish_Btn = findViewById(R.id.Sell_Finish_Btn);
        Sell_Item_Name = findViewById(R.id.Sell_Item_Name);
        Sell_Item_Price = findViewById(R.id.Sell_Item_Price);


        //getIntent().getStringExtra("Item_Detail")
        Sell_Item_Detail.setText(getIntent().getStringExtra("Item_Detail"));
        Sell_Item_Price.setText(getIntent().getStringExtra("Item_Price"));
        Sell_Item_Name.setText(getIntent().getStringExtra("Item_Name"));
        Sell_Image_Btn.setImageURI(Uri.parse(getIntent().getStringExtra("Item_Img")));
        Categori_Name = getIntent().getStringExtra("Item_Category");

        for(int i = 0; i < item.length; i++){
            if (item[i].equals(Categori_Name)){
                spinner.setSelection(i);
                break;
            }
        }





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

        Sell_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Sell_Finish_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(Sell_Item_Name.getText().toString().equals("") || Sell_Item_Price.getText().toString().equals("") || Sell_Item_Detail.getText().toString().equals("") || (Categori_Name.equals("") || Categori_Name.equals("카테고리"))){
                    Toast.makeText(Sell_fix_Activity.this, "모두 작성하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
//                    Main_Activity.Main_Image.setImageURI(Image_save);
                    Intent intent = new Intent(Sell_fix_Activity.this, Buy_Activity.class);
                    intent.putExtra("Item_Name",Sell_Item_Name.getText().toString());
                    intent.putExtra("Item_Price",Sell_Item_Price.getText().toString());
                    intent.putExtra("Item_Detail",Sell_Item_Detail.getText().toString());
                    intent.putExtra("Item_Categori",Categori_Name);
                    if(Image_save != null) {
                        intent.putExtra("Image_Uri", Image_save.toString());
                    }
                    else{
                        intent.putExtra("Image_Uri", getIntent().getStringExtra("Item_Img"));
                    }

                    Main_Activity.test1.get(Main_Activity.position_number).setItem_Name(Sell_Item_Name.getText().toString());
                    Main_Activity.test1.get(Main_Activity.position_number).setItem_Price(Sell_Item_Price.getText().toString());
                    Main_Activity.test1.get(Main_Activity.position_number).setItem_Detail(Sell_Item_Detail.getText().toString());
                    if(Image_save != null) {
                        Main_Activity.test1.get(Main_Activity.position_number).setItem_Img(Image_save.toString());
                    }
                    else{
                        Main_Activity.test1.get(Main_Activity.position_number).setItem_Img(getIntent().getStringExtra("Item_Img"));
                    }
                    Main_Activity.test1.get(Main_Activity.position_number).setItem_Categori_Name(Categori_Name);


                    setResult( RESULT_OK, intent );
                    finish();
                }
            }
        });

    }

    public void photoDialogRadio(View v){
        final CharSequence[] PhotoModels = {"갤러리에서 가져오기", "직접찍어서 가져오기"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

        alt_bld.setTitle("사진 가져오기");
        alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(Sell_fix_Activity.this, PhotoModels[item]+"가 선택되었습니다", Toast.LENGTH_SHORT).show();
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
}