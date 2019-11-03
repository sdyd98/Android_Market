package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Loading_Display extends AppCompatActivity {

    ProgressBar progressBar;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_display);


        progressBar = findViewById(R.id.progressBar);
        Loading loading = new Loading();

        loading.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.arg1 == 100) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    // 로딩 스레드
    class Loading extends Thread {
        @Override
        public void run() {
            super.run();

            for(int i = 0; i <=100; i++){
                progressBar.setProgress(i);
                Message msg = handler.obtainMessage();
                msg.arg1 = i;
                handler.sendMessage(msg);
                try{
                    Thread.sleep(20);
                }catch (InterruptedException e){

                }
            }
        }

    }
}
