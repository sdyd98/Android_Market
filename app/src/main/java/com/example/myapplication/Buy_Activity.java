package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Buy_Activity extends AppCompatActivity {

    final static int Sell_fix_Check = 750;
    ImageView Buy_Image, back, user, Buy_Search_Icon, user_icon;
    TextView price1, itemname, Buy_Call_Icon, item_text, Buy_Location_Icon, user_name, categori_name;
    Button del, fix, Comments_Btn;
    EditText Comments_Detail;

    private RecyclerView Comments_RecyclerView, My_Sell_Item_Recycle;
    private RecyclerView.Adapter CmmAdapter, My_Sell_Item_Adapter;
    private RecyclerView.LayoutManager LayoutManager_Comments, My_Sell_Item_LayoutManager;

    static ArrayList<Comments> Comments_Array = new ArrayList<>();
    static ArrayList<Buy_My_Item_List> Buy_my_item = new ArrayList<>();


    private String getURLForResource(int resId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_interface);

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
        back = findViewById(R.id.back);
        user = findViewById(R.id.user);
        Comments_Btn = findViewById(R.id.Comments_Btn);
        Buy_Search_Icon = findViewById(R.id.Buy_Search_Icon);

        My_Sell_Item_Recycle = findViewById(R.id.Buy_My_Item_List_Recycle);
        Comments_RecyclerView = findViewById(R.id.Comments_Recycle);

        Comments_RecyclerView.setHasFixedSize(true);
        My_Sell_Item_Recycle.setHasFixedSize(true);

        My_Sell_Item_LayoutManager = new LinearLayoutManager(Buy_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        LayoutManager_Comments = new LinearLayoutManager(Buy_Activity.this, LinearLayoutManager.VERTICAL, false);

        My_Sell_Item_Recycle.setLayoutManager(My_Sell_Item_LayoutManager);
        Comments_RecyclerView.setLayoutManager(LayoutManager_Comments);

        CmmAdapter = new CustomAdapter(this , Comments_Array);
//     public Buy_My_Item_List(String buy_Item_Name, String buy_Item_Price, String buy_Item_Img, String buy_Item_Detail, String buy_Categori_Name) {


        My_Sell_Item_Adapter = new Buy_Adapter(Buy_my_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        Comments_RecyclerView.setAdapter(CmmAdapter);

        if(My_Menu_Detail_Activity.User_Name != null){
            user_name.setText(My_Menu_Detail_Activity.User_Name);
        }

        if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
            user_icon.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
        }

        Comments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comments comments = new Comments(Comments_Detail.getText().toString(),"User",getURLForResource(R.drawable.user_icon));
                Comments_Array.add(comments);
                Comments_Detail.setText(null);
                CmmAdapter.notifyDataSetChanged();
            }
        });

        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy_Activity.this, Sell_fix_Activity.class);
                intent.putExtra("Item_Category", categori_name.getText().toString());
                intent.putExtra("Item_Price", price1.getText().toString());
                intent.putExtra("Item_Name", itemname.getText().toString());
                intent.putExtra("Item_Detail", item_text.getText().toString());
                intent.putExtra("Item_Img", getIntent().getStringExtra("Item_Img"));
                startActivityForResult(intent, Sell_fix_Check);
            }
        });

        Buy_Location_Icon.setOnClickListener(new View.OnClickListener() {
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

        String test2_img = getIntent().getStringExtra("Item_Img");

        if(test2_img != null){
                categori_name.setText(getIntent().getStringExtra("Item_Categori"));
                price1.setText(getIntent().getStringExtra("Item_Price")+"원");
                itemname.setText(getIntent().getStringExtra("Item_Name"));
                item_text.setText(getIntent().getStringExtra("Item_Detail"));
                Buy_Image.setImageURI(Uri.parse(test2_img));
        }

        //if(Sell_Activity.Camera_Bitmap != null){
        //    categori_name.setText(getIntent().getStringExtra("Item_Categori"));
        //    price1.setText(getIntent().getStringExtra("Item_Price"));
        //    itemname.setText(getIntent().getStringExtra("Item_Name"));
        //    item_text.setText(getIntent().getStringExtra("Item_Detail"));
        //    Buy_Image.setImageBitmap(Sell_Activity.Camera_Bitmap);
       // }

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int i = 0; i < Main_Activity.test1.size(); i++){
                    if(Main_Activity.test1.get(Main_Activity.position_number).getItem_Img().equals(Mymenu_Activity.My_Menu_Array.get(i).getItem_Img())){
                        Mymenu_Activity.My_Menu_Array.remove(i);
                        break;
                    }
                }
                Main_Activity.test1.remove(Main_Activity.position_number);
                finish();
            }
        });

        Buy_Search_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buy_Activity.this, Mymenu_Activity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == Sell_fix_Check && resultCode == RESULT_OK){

                itemname.setText(data.getStringExtra("Item_Name"));
                price1.setText(data.getStringExtra("Item_Price"));
                item_text.setText(data.getStringExtra("Item_Detail"));
                categori_name.setText(data.getStringExtra("Item_Categori"));
                Buy_Image.setImageURI(Uri.parse(data.getStringExtra("Image_Uri")));

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
    public void onResume(){
        super.onResume();
        // 판매항목 추가 하는 부분
        // 기존 판매 항복 초기화
        Buy_Activity.Buy_my_item.clear();
        // 판매항목리스트에 전체 아이템 추가
        // 현재 포지션 아이템 삭제

        if(Main_Activity.Position_Check == true) {
            for(int i = 0; i < Main_Activity.test1.size(); i++){
                Buy_My_Item_List test2 = new Buy_My_Item_List(Main_Activity.test1.get(i).getItem_Name(), Main_Activity.test1.get(i).getItem_Price(), Main_Activity.test1.get(i).getItem_Img(), Main_Activity.test1.get(i).getItem_Detail(), Main_Activity.test1.get(i).getCategori_Name());
                Buy_my_item.add(test2);
            }
            Buy_my_item.remove(Main_Activity.position_number);
        }
        else{
            for(int i = 0; i < Main_Activity.test1.size(); i++) {
                Buy_My_Item_List test3 = new Buy_My_Item_List(Mymenu_Activity.My_Menu_Array.get(i).getItem_Name(), Mymenu_Activity.My_Menu_Array.get(i).getItem_Price(), Mymenu_Activity.My_Menu_Array.get(i).getItem_Img(), Mymenu_Activity.My_Menu_Array.get(i).getItem_Detail(), Mymenu_Activity.My_Menu_Array.get(i).getCategori_Name());
                Buy_my_item.add(test3);
            }
            Buy_my_item.remove(Mymenu_Activity.My_Menu_Position);
        }
    }
}