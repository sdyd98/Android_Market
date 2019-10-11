package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Main_Activity extends AppCompatActivity {

    BackPressCloseHandler backPressCloseHandler;

    // drawable 파일 string 변환 메소드
    private String getURLForResource(int resId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }

    //위치 확인 변수
    static int position_number;
    private RecyclerView recyclerView, Category_recycle;
    private RecyclerView.Adapter mAdapter,cAdapter;
    private RecyclerView.LayoutManager layoutManager, LayoutManager_Category_recycle;
    // 넘겨줄 데이터 세팅
    static ArrayList<Item_Profile> test1 = new ArrayList<>();
    static ArrayList<Category_Profile> Category_ArrayList = new ArrayList<>();
    Category_Profile CPU = new Category_Profile("CPU",getURLForResource(R.drawable.cpu));
    Category_Profile GPU = new Category_Profile("GPU",getURLForResource(R.drawable.gpu));
    Category_Profile RAM = new Category_Profile("RAM",getURLForResource(R.drawable.ram));
    Category_Profile MB = new Category_Profile("MB",getURLForResource(R.drawable.mb));
    Category_Profile SSD = new Category_Profile("SSD",getURLForResource(R.drawable.ssd));
    Category_Profile HDD = new Category_Profile("HDD",getURLForResource(R.drawable.hdd));
    Category_Profile POWER = new Category_Profile("POWER",getURLForResource(R.drawable.power));
    Category_Profile COOLER = new Category_Profile("COOLER",getURLForResource(R.drawable.cooler));

    static final int REQUEST_TEST = 100;
    static final int REQUEST_TEST2 = 50;
    String img;
    static boolean Position_Check;

    LinearLayout Main_Icon_Search_Btn;
    ImageView Icon_Cpu, Main_Icon_Sell,Main_Icon_Chat,Main_My_Menu, Main_My_Item;
    ImageView Main_Category_btn, Main_ad, Main_app;
    TextView Main_User_Name;
    ImageView Main_Image;
    String Item_Detail, Categori_Name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        backPressCloseHandler = new BackPressCloseHandler(this);




        // 카테고리 더미값 추가
        Category_ArrayList.add(CPU);
        Category_ArrayList.add(GPU);
        Category_ArrayList.add(RAM);
        Category_ArrayList.add(MB);
        Category_ArrayList.add(SSD);
        Category_ArrayList.add(HDD);
        Category_ArrayList.add(POWER);
        Category_ArrayList.add(COOLER);


        Main_User_Name = findViewById(R.id.Main_User_Name);
        Main_app = findViewById(R.id.Main_app);
        Main_ad = findViewById(R.id.Main_ad);
        Main_My_Item = findViewById(R.id.Main_My_Item);
        Main_Icon_Search_Btn = findViewById(R.id.Main_Icon_Search_Btn);
        Icon_Cpu = findViewById(R.id.Icon_Cpu);
        Main_Icon_Sell = findViewById(R.id.Main_Icon_Sell);
        Main_Icon_Chat = findViewById(R.id.Main_Icon_Chat);
        Main_My_Menu = findViewById(R.id.Main_My_Menu);
        Main_Category_btn = findViewById(R.id.Main_Category_btn);



        // 리사이클러뷰 매칭
        recyclerView =  findViewById(R.id.My_TestRe);
        Category_recycle = findViewById(R.id.Category_Recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // ??
        recyclerView.setHasFixedSize(true);
        Category_recycle.setHasFixedSize(true);

        // 레이아웃 매니저 출력방식 설정
        // use a linear layout manager
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        LayoutManager_Category_recycle = new LinearLayoutManager(Main_Activity.this, LinearLayoutManager.HORIZONTAL, false);

        Category_recycle.setLayoutManager(LayoutManager_Category_recycle);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        // 모든 게시글 어뎁터 생성
        mAdapter = new MyAdapter(test1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null) {
                    int position = (int)obj;
                    ((MyAdapter)mAdapter).getData(position);
                    position_number = position;
                    Intent intent = new Intent(Main_Activity.this, Buy_Activity.class);
                    Position_Check = true;
                    intent.putExtra("Item_Name", ((MyAdapter)mAdapter).getData(position).getItem_Name());
                    intent.putExtra("Item_Price", ((MyAdapter)mAdapter).getData(position).getItem_Price());
                    intent.putExtra("Item_Img", ((MyAdapter)mAdapter).getData(position).getItem_Img());
                    intent.putExtra("Item_Detail", ((MyAdapter)mAdapter).getData(position).getItem_Detail());
                    intent.putExtra("Item_Categori", ((MyAdapter)mAdapter).getData(position).getCategori_Name());
                    //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                    // 하나씩 넘길수도 있고
                    // 한번에 넘길수도 있다 (Serializable)

                    Item_Profile My_Menu_Item_Profile = new Item_Profile(((MyAdapter)mAdapter).getData(position).getItem_Name(), ((MyAdapter)mAdapter).getData(position).getItem_Price(), ((MyAdapter)mAdapter).getData(position).getItem_Img(), ((MyAdapter)mAdapter).getData(position).getItem_Detail(), ((MyAdapter)mAdapter).getData(position).getCategori_Name());
                    for(int i = 0; i < Mymenu_Activity.My_Menu_Array.size(); i++){
                        if(Mymenu_Activity.My_Menu_Array.get(i).getItem_Img().equals(My_Menu_Item_Profile.getItem_Img())){
                            Mymenu_Activity.My_Menu_Array.remove(i);
                        }
                    }
                    Mymenu_Activity.My_Menu_Array.add(0, My_Menu_Item_Profile);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);


        // 카테고리 목록 어뎁터 생성
        cAdapter = new Category_MyAdapter(Category_ArrayList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    ((Category_MyAdapter)cAdapter).getData(position);
                    Intent intent = new Intent(Main_Activity.this, Category_Cpu_Activity.class);
                    intent.putExtra("Category_Name", ((Category_MyAdapter)cAdapter).getData(position).getItem_Name());
                    startActivity(intent);

                }
            }
        });
        Category_recycle.setAdapter(cAdapter);










        Main_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPackageList()) {
                    ComponentName compName = new ComponentName("com.danawa.estimate", "com.danawa.estimate.activity.IntroActivity");
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

        Main_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://quasarzone.co.kr/"));
                startActivity(intent);
            }
        });



        Main_My_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, My_Item_Activity.class);
                startActivity(intent);
            }
        });

        Main_Icon_Search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        Main_My_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Mymenu_Activity.class);
                startActivityForResult(intent, REQUEST_TEST2);
            }
        });

        Main_Icon_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });

        Main_Icon_Sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Sell_Activity.class);
                startActivityForResult(intent, REQUEST_TEST);
            }
        });

        //카테고리 버튼
        Main_Category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Category_Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == REQUEST_TEST2){
            if(My_Menu_Detail_Activity.User_Name != null){
                Main_User_Name.setText(My_Menu_Detail_Activity.User_Name);
            }
        }
         else {   // RESULT_CANCEL
        Toast.makeText(Main_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
    }

        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                String Item_Name = data.getStringExtra("Item_Name");
                String Item_Price = data.getStringExtra("Item_Price");
                Item_Detail = data.getStringExtra("Item_Detail");
                Categori_Name =  data.getStringExtra("Item_Categori");
                String Image_uri = data.getStringExtra("Image_Uri");
                img = Image_uri;

                Item_Profile a = new Item_Profile(Item_Name,Item_Price,img,Item_Detail,Categori_Name);
                test1.add(0, a);
                mAdapter.notifyDataSetChanged();


                //Item_Name1.setText(Item_Name);
                //Item_Price1.setText(Item_Price);
                //if(Main_Image != null && img != null) {
                // Uri uri = Uri.parse(Image_uri);
                //    Main_Image.setImageURI(uri);
                //}

                if(Sell_Activity.Camera_Bitmap != null) {
                    Main_Image.setImageBitmap(Sell_Activity.Camera_Bitmap);
                }

                //Main_Image.setVisibility(View.VISIBLE);
                //Item_Name1.setVisibility(View.VISIBLE);
                //Item_Price1.setVisibility(View.VISIBLE);

            } else {   // RESULT_CANCEL
                Toast.makeText(Main_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
//        } else if (requestCode == REQUEST_ANOTHER) {
//            ...
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
                if(mApps.get(i).activityInfo.packageName.startsWith("com.danawa.estimate")){
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        Toast.makeText(Main_Activity.this, "카운트 :"+test1.size(), Toast.LENGTH_SHORT).show();
    }
}




