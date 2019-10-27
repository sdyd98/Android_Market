package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MusicData;
import com.example.myapplication.R;

import java.util.ArrayList;

public class MusicServiceActivity extends AppCompatActivity {

    // 음악 파일 어레이 리스트
    ArrayList<MusicData> musicData = new ArrayList<>();

    RecyclerView Mp3_Recycle;
    RecyclerView.Adapter Mp3_Apdater;
    RecyclerView.LayoutManager Mp3_LayoutManager;

    Button b2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitem_audio);

        getMusicData();

        b2 = findViewById(R.id.b2);

        // 리사이클러뷰 매칭
        Mp3_Recycle =findViewById(R.id.Mp3_Recycle);

        // 리사이클러뷰 크기 설정
        Mp3_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 설정
        Mp3_LayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL , false);

        // 레이아웃 매니저 셋팅
        Mp3_Recycle.setLayoutManager(Mp3_LayoutManager);

        // 어뎁터 생성
        Mp3_Apdater = new Mp3_Adapter(musicData , new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(
                        getApplicationContext(),//현재제어권자
                        MyService.class); // 이동할 컴포넌트
                startService(intent); // 서비스 시작

            }
        });
        Mp3_Recycle.setAdapter(Mp3_Apdater);

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 서비스 종료하기
                Log.d("test", "액티비티-서비스 종료버튼클릭");
                Intent intent = new Intent(
                        getApplicationContext(),//현재제어권자
                        MyService.class); // 이동할 컴포넌트
                stopService(intent); // 서비스 종료
            }
        });

    }

    // 음악데이터 가져오기
    private void getMusicData() { //1. 음악파일인지 아닌지, 2. 앨범 아이디, 3. 음원명, 4.가수명, 미디어 파일 아이디(?)
        String[] projection = {MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID};
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // content://로 시작하는 content table uri
                projection, // 어떤 column을 출력할 것인지
                null, // 어떤 row를 출력할 것인지
                null, MediaStore.Audio.Media.TITLE + " ASC"); // 어떻게 정렬할 것인지
        if (cursor != null) {
            while (cursor.moveToNext()) {

                try {
                    // MediaStore.Audio.Media.IS_MUSIC 값이 1이면 mp3 음원 파일입니다.
                    // 그리고 밑에는 mp3 metadata 이미지 파일의 uri값을 얻어낸것입니다.
                    // 이렇게 얻어낸 데이터를 arraylist에 저장합니다.
                    if (cursor.getInt(0) != 0) {
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(cursor.getString(1)));

                        MusicData data = new MusicData();
                        data.setMusicImg(uri);
                        data.setMusicTitle(cursor.getString(2));
                        data.setSinger(cursor.getString(3));
                        data.setAlbumId(cursor.getString(1));
                        data.setMusicId(cursor.getString(4));

                        musicData.add(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private void init(){
//
//        // 어뎁터 선언
//        adapter = new MusicAdapter(mContext, android.R.layout.simple_list_item_1, list);
//        adapter.setMusicBtnListener(new MusicAdapter.btnClickListener() {
//            @Override
//            public void MusicBtnClick(int position) {
//                ListenMusic(position);
//            }
//        });
//        musicListView = findViewById(R.id.menuList);
//        musicListView.setAdapter(adapter);
//        musicListView.setOnItemClickListener(this);
//    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//    }
}

