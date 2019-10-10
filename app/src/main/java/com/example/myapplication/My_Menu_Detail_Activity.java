package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class My_Menu_Detail_Activity extends AppCompatActivity {

    static final int My_Menu_GET_GALLERY_IMAGE = 500;
    static String My_Menu_User_Icon;
    ImageView My_Menu_Detail_Icon_Back, My_Menu_Detail_User_Icon;
    LinearLayout My_Menu_Detail_Icon_Item, My_Menu_Detail_Icon_Comments, My_Menu_Detail_Icon_Heart, My_Menu_Detail_Icon_Following, My_Menu_Detail_Icon_Followers, My_Menu_Detail_Icon_Certification;
    TextView My_Menu_Detail_User_Name;
    static String User_Name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        My_Menu_Detail_User_Name = findViewById(R.id.My_Menu_Detail_User_Name);
        My_Menu_Detail_User_Icon = findViewById(R.id.My_Menu_Detail_User_Icon);
        My_Menu_Detail_Icon_Certification = findViewById(R.id.My_Menu_Detail_Icon_Certification);
        My_Menu_Detail_Icon_Item = findViewById(R.id.My_Menu_Detail_Icon_Item);
        My_Menu_Detail_Icon_Comments = findViewById(R.id.My_Menu_Detail_Icon_Comments);
        My_Menu_Detail_Icon_Back = findViewById(R.id.My_Menu_Detail_Icon_Back);
        My_Menu_Detail_Icon_Heart = findViewById(R.id.My_Menu_Detail_Icon_Heart);
        My_Menu_Detail_Icon_Following = findViewById(R.id.My_Menu_Detail_Icon_Following);
        My_Menu_Detail_Icon_Followers = findViewById(R.id.My_Menu_Detail_Icon_Followers);

        if(User_Name != null){
            My_Menu_Detail_User_Name.setText(User_Name);
        }

        if(My_Menu_User_Icon != null){
            My_Menu_Detail_User_Icon.setImageURI(Uri.parse(My_Menu_User_Icon));
        }

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
                        User_Name = username;
                        My_Menu_Detail_User_Name.setText(username);
                    }
                });
                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();

            }
        });

        My_Menu_Detail_User_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, My_Menu_GET_GALLERY_IMAGE);
            }
        });

        My_Menu_Detail_Icon_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, My_Item_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Detail_Icon_Comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Comments_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Detail_Icon_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Heart_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Detail_Icon_Following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Following_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Detail_Icon_Followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Followers_Activity.class);
                startActivity(intent);
            }
        });

        My_Menu_Detail_Icon_Certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Certification_Activity.class);
                startActivity(intent);
            }
        });


        My_Menu_Detail_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Menu_Detail_Activity.this, Mymenu_Activity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == My_Menu_GET_GALLERY_IMAGE && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            Uri selectedImageUri = intent.getData();
            My_Menu_User_Icon = selectedImageUri.toString();
            My_Menu_Detail_User_Icon.setImageURI(selectedImageUri);
        }
    }

}