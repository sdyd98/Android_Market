package com.example.myapplication;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> {
    private final String Login_User_Id;
    private ArrayList<Chat_List> mDataset;
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Chat_User_Id;
        public TextView Chat_Last;
        public TextView Chat_Time;
        public TextView Chat_Count;
        public CircleImageView Chat_User_Img;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            Chat_User_Id = v.findViewById(R.id.Chat_User_Id);
            Chat_User_Img = v.findViewById(R.id.Chat_User_Icon);
            Chat_Last = v.findViewById(R.id.Chat_Last);
            Chat_Time = v.findViewById(R.id.Chat_Time);
            Chat_Count = v.findViewById(R.id.Chat_Count);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Chat_Adapter(ArrayList<Chat_List> myDataset, View.OnClickListener onClick, String Login_User_Id) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
        this.Login_User_Id = Login_User_Id;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Chat_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_view, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String Chat_Name;
        String Chat_Img;

        //로그인 유저 판단
        if(mDataset.get(position).getChat_Receiver_User().equals(Login_User_Id)){
            Chat_Name = mDataset.get(position).getChat_Maker_Name();
            Chat_Img = mDataset.get(position).getChat_Maker_User_Img();
        }
        else{
            Chat_Name = mDataset.get(position).getChat_Receiver_Name();
            Chat_Img = mDataset.get(position).getChat_Receiver_User_Img();
        }
        // 채팅방 목록 채팅 상대 정보 셋팅
        holder.Chat_User_Id.setText(Chat_Name);
        holder.Chat_User_Img.setImageURI(Uri.parse(Chat_Img));

        if(mDataset.get(position).getChat_Inside_User_Data_Array().size() == 0){
            holder.Chat_Last.setVisibility(View.INVISIBLE);
            holder.Chat_Time.setVisibility(View.INVISIBLE);
            holder.Chat_Count.setVisibility(View.INVISIBLE);
        }
        else{
            holder.Chat_Last.setVisibility(View.VISIBLE);
            holder.Chat_Time.setVisibility(View.VISIBLE);
            holder.Chat_Count.setVisibility(View.VISIBLE);

            // 마지막 채팅 표시하기
            holder.Chat_Last.setText(mDataset.get(position).getChat_Inside_User_Data_Array().get(mDataset.get(position).getChat_Inside_User_Data_Array().size() - 1).getChat_Inside_User_Message());
            try {
                // 마지막 메세지 시간 구하기
                holder.Chat_Time.setText(time(mDataset.get(position).getChat_Inside_User_Data_Array().get(mDataset.get(position).getChat_Inside_User_Data_Array().size()-1).getChat_Inside_User_Message_Time())+" 전");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 채팅온 메세지가 0이라면
        if(Chat_Count(position) == 0){
            holder.Chat_Count.setVisibility(View.INVISIBLE);
        }
        // 0이 아니라면
        else{
            if(mDataset.get(position).isChat_Check()){
                holder.Chat_Count.setVisibility(View.INVISIBLE);
            }
            else{
                holder.Chat_Count.setVisibility(View.VISIBLE);
                holder.Chat_Count.setText(String.valueOf(Chat_Count(position)));
            }
        }


        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Chat_List getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }

    // 게시물 시간 구하기
    public String time(String Last_Message_Time) throws ParseException {

        // 작성 시간
        String reqDateStr = Last_Message_Time;

        //현재시간 Date
        Date curDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");


        //요청시간을 Date로 parsing 후 time가져오기
        Date reqDate = dateFormat.parse(reqDateStr);
        long reqDateTime = reqDate.getTime();

        //현재시간을 요청시간의 형태로 format 후 time 가져오기
        curDate = dateFormat.parse(dateFormat.format(curDate));
        long curDateTime = curDate.getTime();

        //분으로 표현
        long minute = (curDateTime - reqDateTime) / (60 * 1000);

        long hour = (curDateTime - reqDateTime) / (60 * 60 * 1000);

        long day = (curDateTime - reqDateTime) / (24 * 60 * 60 * 1000);

        long week = day/7;

        long month = (day+1)/30;

        long years = month/12;

        Log.e("check_time", String.valueOf(curDateTime));
        Log.e("check_time", String.valueOf(reqDateTime));
        Log.e("check_time", String.valueOf(minute));

        String realtime;
        String time = null;

        if(minute < 60){
            realtime = String.valueOf(minute);
            time = realtime+"분";
        }
        else if(hour < 24){
            realtime = String.valueOf(hour);
            time = realtime+"시간";
        }
        else if(day < 7){
            realtime = String.valueOf(day);
            time = realtime+"일";
        }
        else if(week < 5){
            realtime = String.valueOf(week);
            time = realtime+"주";
        }
        else if(month < 12){
            realtime = String.valueOf(month);
            time = realtime+"달";
        }
        else if (years < 100){
            realtime = String.valueOf(years);
            time = realtime+"년";
        }
        // 분 60 * 1000;
        // 시간 60 * 60 * 1000;
        // 일 24 * 60 * 60 * 1000;
        // 년 365 * 24 * 60 * 60 * 1000;

        return time;
    }

    // 체팅온 카운트 계산
    public int Chat_Count(int position) {
        int Chat_Last_Position = 0;
        int Chat_Count = 0;

        // 만약 채팅방 안에있는 채팅내용이 한개라면
        if(mDataset.get(position).getChat_Inside_User_Data_Array().size() == 1){
            // 만약 채팅방 내용 첫번째 유저 아이디가 로그인한 유저 라면
            if(mDataset.get(position).getChat_Inside_User_Data_Array().get(0).getChat_Inside_User_Id().equals(Login_User_Id)){
                System.out.println("첫번째 채팅내용이 로그인한 유저");
                // 메시지 온 갯수를 0 이라고 반환
                Chat_Count = 0;
            }
            // 만약 유저가 아니라면
            else{
                System.out.println("첫번째 채팅 내용 유저 아님");
                // 어레이 사이즈만큼 반환
                Chat_Count = mDataset.get(position).getChat_Inside_User_Data_Array().size();
            }
        }
        // 채팅방 내용이 2개이상이라면
        else {
            // 채팅방 내용을 역순으로 크기만큼 반복
            for (int i = mDataset.get(position).getChat_Inside_User_Data_Array().size() - 1; i >= 0; i--) {
                // 만약 채팅 내용 유저 아이디가 로그인한 유저랑 같다면
                if (mDataset.get(position).getChat_Inside_User_Data_Array().get(i).getChat_Inside_User_Id().equals(Login_User_Id)) {
                    // 해당 포지션 저장
                    Chat_Last_Position = i;
                    Chat_Count = mDataset.get(position).getChat_Inside_User_Data_Array().size() - (Chat_Last_Position + 1);
                    break;
                }

                // for 문 다돌렸는데 못찾았다면
                if( i == mDataset.get(position).getChat_Inside_User_Data_Array().size()-1){
                    // 채팅방 전체 크기를 반환
                    Chat_Count = mDataset.get(position).getChat_Inside_User_Data_Array().size();
                }
            }
        }
            return Chat_Count;
        }
    }

