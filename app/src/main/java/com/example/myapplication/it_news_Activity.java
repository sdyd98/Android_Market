package com.example.myapplication;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class it_news_Activity extends AppCompatActivity {

    String json;

    Thread network_thread;

    private static final String TAG = "it_news_Activity";

    // 버퍼 입력
    BufferedReader br;

    // 긴 스트링 붙일때 쓰는것
    StringBuilder searchResult;// String 객체 + String 객체

    // 뉴스 정보 어레이 리스트
    ArrayList<New_DB> new_dbs = new ArrayList<>();

    // 뷰선언
    ImageView Keyword_Icon_Back;

    // 리사이클러뷰
    RecyclerView it_news_Recycle;
    RecyclerView.Adapter it_news_Adapter;
    RecyclerView.LayoutManager it_news_LayoutManager;

    // 로그인 아이디
    private String Login_User_Id;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.it_news);

        searchNaver("컴퓨터 부품");

        // 로그인 아이디 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 리사이클러뷰 매칭
        it_news_Recycle = findViewById(R.id.It_news);

        // 리사이클러뷰 크기 설정
        it_news_Recycle.setHasFixedSize(true);

        //레이아웃 매니저 생성
        it_news_LayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        // 레이아웃 매니저 셋팅
        it_news_Recycle.setLayoutManager(it_news_LayoutManager);

        // 뷰매칭
        Keyword_Icon_Back = findViewById(R.id.Keyword_Icon_Back);

        network_thread.start();

        try {
            network_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "onCreate: "+new_dbs.toString());

        // 뉴스 리사이클러뷰 어뎁터
        it_news_Adapter = new it_news_Adapter(new_dbs , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 포지션 값 가져오기
                Object obj = v.getTag();
                // 예외처리
                if(obj != null) {
                    // 포지션 값 저장
                    int position = (int)obj;
                    // 인텐트 생성
                    Intent intent = new Intent(getApplicationContext(), web_view_Activity.class);
                    // 현재 접속 유저 아이디 인텐트
                    //intent.putExtra("User_ID", Login_User_Id);
                    intent.putExtra("URL", ((it_news_Adapter)it_news_Adapter).getData(position).getLink());
                    // 인텐트 전송
                    startActivity(intent);
                }
            }
        });
        // 게시글 어뎁터 set
        it_news_Recycle.setAdapter(it_news_Adapter);

        // 뒤로가기 버튼
        Keyword_Icon_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void searchNaver(final String searchObject) { // 검색어 = searchObject로 ;
        final String clientId = "oaD6cWFmRlAA12InuUQJ";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "oGdRaLREuk";//애플리케이션 클라이언트 시크릿값";
        final int display = 20; // 보여지는 검색결과의 수

        //TODO:물어보기

        // 네트워크 연결은 Thread 생성 필요
       network_thread = new Thread() {

            @Override
            public void run() {
                try {

                    // 검색어 UTF-8로 인코딩 해서 저장
                    String text = URLEncoder.encode(searchObject, "UTF-8");

                    // text = 검색어, display = 출력수
                    String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=" + display + "&";

                    // url 생성
                    URL url = new URL(apiURL);

                    // 위에서 생성한 url을 토대로 연결
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    // ?
                    con.setRequestMethod("GET");

                    // 설정
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                    // 연결
                    con.connect();

                    // 응답 코드 저장
                    int responseCode = con.getResponseCode();

                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    // 스트링 빌더
                    searchResult = new StringBuilder();

                    String inputLine;

                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }

                    // 버퍼 리더 닫기
                    br.close();

                    // http 연결 종료
                    con.disconnect();

                    // json 데이터
                    String data = searchResult.toString();

                    System.out.println(data);

                    String[] array;

                    // \" 기준으로 나누어 저장
                    array = data.split("\"");
                    String title = null;
                    String link = null;
                    String description = null;
                    String bloggername = null;
                    String postdate = null;

                    for (int i = 0; i < array.length; i++) {
                        if (array[i].equals("title"))
                            title = removeTag(array[i + 2]);
                        if (array[i].equals("originallink"))
                            link = removeTag(array[i + 2]);
                        if (array[i].equals("description"))
                            description = removeTag(array[i + 2]);
                        if (array[i].equals("bloggername"))
                            bloggername = removeTag(array[i + 2]);
                        if (array[i].equals("pubDate")) {
                            postdate = removeTag(array[i + 2]);
                            new_dbs.add(new New_DB(title, link, description, bloggername, postdate));
                        }
                    }

                    Log.d( "test", "run: "+new_dbs.get(0).getTitle()+new_dbs.get(0).getDescription());

                } catch (Exception e) {
                }
            }
        };
    }



    // 정규 표현식
    public String removeTag(String html) throws Exception {
        String Result = html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        return Result.replaceAll("&quot;","\"");
    }
}