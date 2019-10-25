package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Follower_Adapter extends RecyclerView.Adapter<Follower_Adapter.MyViewHolder> {

    // 유저 정보 객체 어레이를 받는다
    private ArrayList<User_DB> mDataset;

    // 아이템 정보 객체 어레이를 받는다
    private ArrayList<Item_DB> Item_db_array;

    // 클릭 리스너 선언
    private static View.OnClickListener onClickListener;





    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // 뷰홀더 선언
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Follower_Item_Count;
        public TextView Follower_Follower_Count;
        public TextView Follower_Name;
        public CircleImageView Follower_Img;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);

            Follower_Item_Count = v.findViewById(R.id.Follow_Item_Count);
            Follower_Follower_Count = v.findViewById(R.id.Follow_Following_Count);
            Follower_Name= v.findViewById(R.id.Follow_Name);
            Follower_Img = v.findViewById(R.id.Follow_Img);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Follower_Adapter(ArrayList<User_DB> myDataset, View.OnClickListener onClick, ArrayList<Item_DB> item_db_array) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
        Item_db_array = item_db_array;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Follower_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follwer_view, parent, false);
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


        // 상품 갯수
        int item_count = 0;

        // 해당 유저 상품 올린수 판단
        for(int i = 0; i < Item_db_array.size(); i++){
            if(Item_db_array.get(i).getItem_id().equals(mDataset.get(position).getUser_id())){
                item_count++;
            }
        }

        holder.Follower_Item_Count.setText("상품 ("+item_count+")");
        holder.Follower_Follower_Count.setText("팔로워 ("+mDataset.get(position).getUser_Follower().size()+")");
        holder.Follower_Name.setText(mDataset.get(position).getUser_name());
        holder.Follower_Img.setImageURI(Uri.parse(mDataset.get(position).getUser_icon_img()));
        // position 값 획득
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 포지션 값 반환
    public User_DB getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }

}