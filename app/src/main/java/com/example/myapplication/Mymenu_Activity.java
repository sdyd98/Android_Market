package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Mymenu_Activity extends AppCompatActivity {

    public static Context CONTEXT;
    static final int TEST_DATA = 20;
    static final int TEST_DATA1 = 30;

    ImageView My_Menu_Icon_Close;
    ImageView My_Menu_Icon_User;
    TextView My_Menu_Icon_Chat, My_Menu_Icon_Allim, My_Menu_Icon_Keyword_Allim, My_Menu_Icon_Heart, My_Menu_User_Name;
    LinearLayout Item_Layout, Comments_Layout, Heart_Layout, Following_Layout, Followers_Layout;

    RecyclerView My_Menu_Recycle;
    RecyclerView.Adapter My_Menu_Adapter;
    RecyclerView.LayoutManager My_Menu_LayoutManager;

    static int My_Menu_Position;
    static ArrayList<Item_Profile> My_Menu_Array = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = this;
        setContentView(R.layout.mymenu);

        My_Menu_User_Name = findViewById(R.id.My_Menu_User_Name);
        My_Menu_Icon_Allim = findViewById(R.id.My_Menu_Icon_Allim);
        My_Menu_Icon_Chat = findViewById(R.id.My_Menu_Icon_Chat);
        Item_Layout = findViewById(R.id.Item_Layout);
        Comments_Layout = findViewById(R.id.Comments_Layout);
        Heart_Layout = findViewById(R.id.Heart_Layout);
        Following_Layout = findViewById(R.id.Following_Layout);
        Followers_Layout = findViewById(R.id.Follower_Layout);
        My_Menu_Icon_Close = findViewById(R.id.My_Menu_Icon_Close);
        My_Menu_Icon_User = findViewById(R.id.My_Menu_Icon_User);
        My_Menu_Icon_Keyword_Allim = findViewById(R.id.My_Menu_Icon_Keyword_Allim);
        My_Menu_Icon_Heart = findViewById(R.id.My_Menu_Icon_Heart);

        My_Menu_Recycle = findViewById(R.id.My_Menu_Recycle);
        My_Menu_Recycle.setHasFixedSize(true);
        My_Menu_LayoutManager = new LinearLayoutManager(Mymenu_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        My_Menu_Recycle.setLayoutManager(My_Menu_LayoutManager);

        My_Menu_Adapter = new My_Menu_Adapter(My_Menu_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null) {
                    int position = (int)obj;
                    My_Menu_Position = position;
                    ((My_Menu_Adapter)My_Menu_Adapter).getData(position);
                    Intent intent = new Intent(Mymenu_Activity.this, Buy_Activity.class);
                    intent.putExtra("Item_Name", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getItem_Name());
                    intent.putExtra("Item_Price", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getItem_Price());
                    intent.putExtra("Item_Img", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getItem_Img());
                    intent.putExtra("Item_Detail", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getItem_Detail());
                    intent.putExtra("Item_Categori", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getCategori_Name());
                    //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                    // 하나씩 넘길수도 있고
                    // 한번에 넘길수도 있다 (Serializable)
                    startActivity(intent);
                }
            }
        });
        My_Menu_Recycle.setAdapter(My_Menu_Adapter);

        if(My_Menu_Detail_Activity.User_Name != null){
            My_Menu_User_Name.setText(My_Menu_Detail_Activity.User_Name);
        }

        if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
            My_Menu_Icon_User.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
        }

        My_Menu_Icon_Allim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, allim_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Icon_Keyword_Allim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Keyword_allim_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Icon_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Heart_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Icon_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });

        Followers_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Followers_Activity.class);
                startActivity(intent);
            }
        });

        Following_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Following_Activity.class);
                startActivity(intent);
            }
        });

        Comments_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Comments_Activity.class);
                startActivity(intent);
            }
        });

        Heart_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Heart_Activity.class);
                startActivity(intent);
            }
        });

        Item_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, My_Item_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Icon_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, My_Menu_Detail_Activity.class);
                startActivityForResult(intent, TEST_DATA);
            }
        });


        My_Menu_Icon_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Main_Activity.class);
                setResult( RESULT_OK, intent);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TEST_DATA) {
            if(My_Menu_Detail_Activity.User_Name != null){
                My_Menu_User_Name.setText(My_Menu_Detail_Activity.User_Name);
            }
            if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
                My_Menu_Icon_User.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
            }
        }
    }

}
