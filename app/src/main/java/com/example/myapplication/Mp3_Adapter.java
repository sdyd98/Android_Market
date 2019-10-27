package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Mp3_Adapter extends RecyclerView.Adapter<Mp3_Adapter.MyViewHolder> {

    // 아이템 정보 객체 어레이를 받는다
    private ArrayList<MusicData> mDataset;
    // 클릭 리스너 선언
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // 뷰홀더 선언
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt_music_title;
        public TextView txt_singer_name;
        public ImageView imgMusic;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            txt_music_title = v.findViewById(R.id.txt_music_title);
            txt_singer_name = v.findViewById(R.id.txt_singer_name);
            imgMusic = v.findViewById(R.id.imgMusic);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Mp3_Adapter(ArrayList<MusicData> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Mp3_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mp3_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // 데이터 추가
        holder.txt_music_title.setText(mDataset.get(position).getMusicTitle());
        holder.txt_singer_name.setText(mDataset.get(position).getSinger());
        holder.imgMusic.setImageURI(Uri.parse(mDataset.get(position).getMusicImg().toString()));
        // position 값 획득
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 포지션 값 반환
    public MusicData getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }
}