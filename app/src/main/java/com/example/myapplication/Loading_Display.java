package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Loading_Display extends AppCompatActivity {
    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_display);

        Loading loading = new Loading();

        loading.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
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

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }

            handler.sendEmptyMessage(0);
        }

    }
}
