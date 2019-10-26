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

public class Rank_Adapter extends RecyclerView.Adapter<Rank_Adapter.MyViewHolder> {

    // 아이템 정보 객체 어레이를 받는다
    private ArrayList<String> mDataset;
    // 클릭 리스너 선언
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // 뷰홀더 선언
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Rank_Number;
        public TextView Rank_Text;

        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            Rank_Number = v.findViewById(R.id.Rank_Number);
            Rank_Text = v.findViewById(R.id.Rank_Text);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Rank_Adapter(ArrayList<String> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Rank_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rank_item_view, parent, false);
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
        holder.Rank_Number.setText(String.valueOf(position + 1));
        holder.Rank_Text.setText(mDataset.get(position));

        // position 값 획득
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 포지션 값 반환
    public String getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }
}