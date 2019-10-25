package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Category_MyAdapter extends RecyclerView.Adapter<Category_MyAdapter.MyViewHolder> {
    private ArrayList<Category_Profile> mDataset;
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // 아이템뷰 id 매칭
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Category_titile;
        public ImageView Category_img;
        public LinearLayout Category_Icon;
        public ConstraintLayout Tlqkf2;
        public View rootView;


        // 아이템뷰 뷰 매칭
        public MyViewHolder(View v) {
            super(v);

            Category_img = v.findViewById(R.id.Icon_Cpu);
            Category_titile = v.findViewById(R.id.Text_Cpu);
            Category_Icon = v.findViewById(R.id.Category_Icon);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Category_MyAdapter(ArrayList<Category_Profile> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Category_MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categori_icon, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Category_titile.setText(mDataset.get(position).getItem_Name());
        holder.Category_img.setImageURI(Uri.parse(mDataset.get(position).getItem_Img()));
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Category_Profile getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }
}