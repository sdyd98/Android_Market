package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Chat_Activity extends AppCompatActivity {

    ImageView Chat_Icon_Back_Btn, Chat_Plus;
    ArrayList<Chat_User_Data> Chat_User_Data = new ArrayList<>();
    RecyclerView Chat_Recycle;
    RecyclerView.LayoutManager Chat_LayoutManager;
    RecyclerView.Adapter Chat_Adapter;
    static boolean User_Check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        Chat_Icon_Back_Btn = findViewById(R.id.Chat_Icon_Back_Btn);
        Chat_Plus = findViewById(R.id.Chat_Plus);
        Chat_Recycle = findViewById(R.id.Chat_Recycle);

        Chat_Recycle.setHasFixedSize(true);

        Chat_LayoutManager = new LinearLayoutManager(Chat_Activity.this, LinearLayoutManager.VERTICAL, false);

        Chat_Recycle.setLayoutManager(Chat_LayoutManager);

        Chat_Adapter = new Chat_Adapter(Chat_User_Data, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    ((Chat_Adapter)Chat_Adapter).getData(position);
                    Intent intent = new Intent(Chat_Activity.this, Chat_Message_Activity.class);
                    startActivity(intent);
                }
            }
        });
        Chat_Recycle.setAdapter(Chat_Adapter);


        Chat_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat_Activity.this);

                View view = LayoutInflater.from(Chat_Activity.this)
                        .inflate(R.layout.chat_match_view, null, false);
                builder.setView(view);

                final Button ButtonSubmit = view.findViewById(R.id.Chat_User_Btn);
                final EditText editTextID = view.findViewById(R.id.Chat_User_Input_Id);

                final AlertDialog dialog = builder.create();

                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Chat_User_Data chat_user_data = new Chat_User_Data(editTextID.getText().toString(), Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.user_icon).toString());
                        Chat_User_Data.add(chat_user_data);
                        Toast.makeText(Chat_Activity.this, chat_user_data.getChat_User_Data(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Chat_Icon_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}