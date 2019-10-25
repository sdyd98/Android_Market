package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Select_Adapter extends RecyclerView.Adapter<Select_Adapter.MyViewHolder> {

    // 아이템 정보 객체 어레이를 받는다
    private ArrayList<Item_DB> mDataset;
    private ArrayList<User_DB> User_DB_Array;
    private ArrayList<Item_DB> Item_db_array;
    Context Select_context;
    String Login_User_Id;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // 뷰홀더 선언
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView Item_Img;
        public CheckBox Heart_Check_Box;


        public MyViewHolder(View v) {
            super(v);

            Item_Img = v.findViewById(R.id.Select_Img);
            Heart_Check_Box = v.findViewById(R.id.Select_Check_Box);


            v.setClickable(true);
            v.setEnabled(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Select_Adapter(ArrayList<Item_DB> myDataset, Context context, String user_id, ArrayList<User_DB> User_DB_array, ArrayList<Item_DB> item_db_array) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        Select_context = context;
        Login_User_Id = user_id;
        User_DB_Array = User_DB_array;
        Item_db_array = item_db_array;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Select_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // 데이터 추가
        holder.Item_Img.setImageURI(Uri.parse(mDataset.get(position).getitem_img()));
        holder.Heart_Check_Box.setChecked(true);

        holder.Item_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Buy_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                intent.putExtra("Item_Number", mDataset.get(position).getItem_number());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

        // 체크 박스 눌렀을때
       holder.Heart_Check_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저 어레이 크기 만큼 반복
                for(int i = 0; i < User_DB_Array.size(); i++){
                    // 로그인한 아이디와 어레이에 있는 아이디가 일치하면
                    if(User_DB_Array.get(i).getUser_id().equals(Login_User_Id)){
                        //유저 찜 목록 어레이 만큼 반복
                        for(int j = 0; j < User_DB_Array.get(i).getUser_Select().size(); j++){
                            // 유저 찜 목록 게시글 고유 넘버와 현재 클릭한 고유 넘버가 같다면
                            if(User_DB_Array.get(i).getUser_Select().get(j) == mDataset.get(position).getItem_number()){
                                // 유저 찜 목록에서 그 고유 넘버 삭제
                                User_DB_Array.get(i).getUser_Select().remove(j);

                                // 아이템 전체 목록 갯수 만큼 반복
                                for (int i1 = 0; i1 < Item_db_array.size(); i1++){
                                    // 만약 현재 게시물 고유 넘버와 일치하는 게시물을 찾았다면
                                    if(Item_db_array.get(i1).getItem_number() == mDataset.get(position).getItem_number()){
                                        // 그 게시물에 유저 찜 목록 어레이 만큼 반복
                                        for (int i2 = 0; i2 < Item_db_array.get(i1).getUser_selects().size(); i2++){
                                            // 찜 목록에서 유저 아이디와 같은 게시물을 찾았다면
                                            if(Item_db_array.get(i1).getUser_selects().get(i2).getUser_name().equals(Login_User_Id)){
                                                // 삭제
                                                Item_db_array.get(i1).getUser_selects().remove(i2);

                                                Toast.makeText(v.getContext(), "찜 목록에서 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                                            }
                                        }


                                    }
                                }

                                // 찜 목록 어레이 어뎁터 도 삭제
                                mDataset.remove(position);

                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 포지션 값 반환
    public Item_DB getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }


}