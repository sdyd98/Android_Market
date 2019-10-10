package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Sign_Up_Activity extends AppCompatActivity {

    Button btn_Sign_up_real;
    static ArrayList<ArrayList<String>> person = new ArrayList<ArrayList<String>>();
    static ArrayList<String> person_detail = new ArrayList<>();
    TextInputEditText Sign_Up_Id, Sign_Up_Pw, Sign_Up_Pw2, Sign_Up_Name, Sign_Up_Phone_Number, Sign_Birthday_Number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Sign_Up_Id = findViewById(R.id.Sign_Up_Id);
        Sign_Up_Pw = findViewById(R.id.Sign_Up_Pw);
        Sign_Up_Name = findViewById(R.id.Sign_Up_Name);
        Sign_Up_Pw2 = findViewById(R.id.Sign_Up_Pw2);
        btn_Sign_up_real = findViewById(R.id.btn_Sign_up_real);


        // 회원가입하기 버튼
        btn_Sign_up_real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw_check1 = Sign_Up_Pw.getText().toString();
                String pw_check2 = Sign_Up_Pw2.getText().toString();
                if(pw_check1.equals(pw_check2)) {
                    person_detail.add(Sign_Up_Id.getText().toString());
                    person_detail.add(Sign_Up_Pw.getText().toString());
                    person_detail.add(Sign_Up_Name.getText().toString());
                    person_detail.add(Sign_Up_Phone_Number.getText().toString());
                    person_detail.add(Sign_Birthday_Number.getText().toString());
                    person.add(person_detail);
                    Intent intent = new Intent(Sign_Up_Activity.this, Login_Activity.class);
                    Toast.makeText(Sign_Up_Activity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                }
                else{
                    Toast.makeText(Sign_Up_Activity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}