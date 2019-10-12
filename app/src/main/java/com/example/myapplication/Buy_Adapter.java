package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Buy_Adapter extends RecyclerView.Adapter<Buy_Adapter.MyViewHolder> {
    private ArrayList<Item_DB> mDataset;
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ImageView_title;
        public TextView TextView_Price;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            ImageView_title = v.findViewById(R.id.Buy_My_Item_List_Img);
            TextView_Price = v.findViewById(R.id.Buy_My_Item_List_Price);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Buy_Adapter(ArrayList<Item_DB> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장

        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Buy_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_my_item_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

            //문자열 비트맵으로 변환
            byte[] decodedByteArray = Base64.decode(mDataset.get(position).getitem_img(), Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            holder.ImageView_title.setImageBitmap(decodedBitmap);
            holder.TextView_Price.setText(mDataset.get(position).getItem_price()+"원");

        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Item_DB getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }
}