package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Chat_Message_Activity extends AppCompatActivity {

    Switch Chat_User_Check_Switch;
    EditText Chat_User_Input_Message;
    ImageView Chat_Back_Btn, Chat_Push_Message;
    RecyclerView Chat_Inside_Recycle;
    RecyclerView.Adapter Chat_Inside_Adapter;
    RecyclerView.LayoutManager Chat_Inside_LayoutManager;
    static ArrayList<Chat_Inside_User_Data> Chat_Inside_Array = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_inside);

        Chat_User_Check_Switch = findViewById(R.id.User_Check_Switch);
        Chat_Back_Btn= findViewById(R.id.Chat_Back);
        Chat_User_Input_Message = findViewById(R.id.Chat_User_Input_Message);
        Chat_Push_Message = findViewById(R.id.Chat_Push_Message);
        Chat_Inside_Recycle = findViewById(R.id.Chat_Inside_Recycle);
        Chat_Inside_Recycle.setHasFixedSize(true);
        Chat_Inside_LayoutManager = new LinearLayoutManager(Chat_Message_Activity.this, LinearLayoutManager.VERTICAL, false);
        Chat_Inside_Recycle.setLayoutManager(Chat_Inside_LayoutManager);

        Chat_User_Check_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked == true){
                    Chat_Activity.User_Check = true;
                    Toast.makeText(Chat_Message_Activity.this, "채팅 입력 : Me", Toast.LENGTH_SHORT).show();
                }
                else{
                    Chat_Activity.User_Check = false;
                    Toast.makeText(Chat_Message_Activity.this, "채팅 입력 : Another", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Chat_Inside_Adapter = new Chat_Inside_Adapter(Chat_Inside_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Chat_Inside_Recycle.setAdapter(Chat_Inside_Adapter);

        Chat_Push_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat_Inside_User_Data chat = new Chat_Inside_User_Data("User", Chat_User_Input_Message.getText().toString());
                //Chat_Inside_Array.add(chat);
                Toast.makeText(Chat_Message_Activity.this, "ㅎㅇㅎㅇ"+chat.Chat_Inside_User_Message, Toast.LENGTH_SHORT).show();
                Chat_User_Input_Message.setText(null);
                ((Chat_Inside_Adapter)Chat_Inside_Adapter).addChat(chat);
            }
        });


        Chat_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}