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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class allim_Adapter extends RecyclerView.Adapter<allim_Adapter.MyViewHolder> {
    private ArrayList<Allim_Db> mDataset;
    private static View.OnClickListener onClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public CircleImageView Allim_User_Img;
        public TextView Allim_Ment;
        public TextView Allim_Time;
        public ImageView Allim_Item_Img;
        private TextView Allim_User_Id;

        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            Allim_Item_Img = v.findViewById(R.id.Allim_Item_Img);
            Allim_Ment = v.findViewById(R.id.Allim_Ment);
            Allim_Time = v.findViewById(R.id.Allim_Time);
            Allim_User_Img = v.findViewById(R.id.Allim_User_Img);
            Allim_User_Id = v.findViewById(R.id.Allim_User_Name);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public allim_Adapter(ArrayList<Allim_Db> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public allim_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.allim_veiw, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Allim_User_Img.setImageURI(Uri.parse(mDataset.get(position).getAllim_User_Img()));
        holder.Allim_Ment.setText(mDataset.get(position).getAllim_Ments());
        holder.Allim_Item_Img.setImageURI(Uri.parse(mDataset.get(position).getAllim_Item_Img()));
        holder.Allim_User_Id.setText(mDataset.get(position).getAllim_User_Name()+" 님이");
        try {
            holder.Allim_Time.setText(time(position)+" 전");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Allim_Db getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }

    // 게시물 시간 구하기
    private String time(int position) throws ParseException {

        // 작성 시간
        String reqDateStr = mDataset.get(position).getAllim_Time();

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
}