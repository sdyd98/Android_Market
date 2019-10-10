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

public class Comments_Adapter extends RecyclerView.Adapter<Comments_Adapter.MyViewHolder> {
    private ArrayList<Comments> mDataset;
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView User_Id;
        public TextView User_Comments;
        public ImageView User_Img;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            User_Id = v.findViewById(R.id.User_Id);
            User_Comments = v.findViewById(R.id.User_Comments);
            User_Img = v.findViewById(R.id.User_Img);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Comments_Adapter(ArrayList<Comments> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장m
        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Comments_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.User_Id.setText(mDataset.get(position).getComments_Id());
        holder.User_Comments.setText(mDataset.get(position).getComments_Detail());
        holder.User_Img.setImageURI(Uri.parse(mDataset.get(position).getComments_Img()));

        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Comments getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }
}