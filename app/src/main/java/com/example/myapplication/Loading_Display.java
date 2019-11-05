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

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class Loading_Display extends AppCompatActivity {

    ProgressBar progressBar;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_display);

        LottieAnimationView Lottie_Loding = findViewById(R.id.Lottie_Loading);

        Start_Lottie(Lottie_Loding, "loading.json");

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                finish();
            }
        }, 3500);


    }

    // 로티 시작 메소드
    public void Start_Lottie(LottieAnimationView lottieAnimationView, String json){

        // 파일 정하기
        lottieAnimationView.setAnimation(json);

        // 반복 횟수
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);

        // 시작
        lottieAnimationView.playAnimation();
    }
}
