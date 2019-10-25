package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Inside_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String Login_User_Id;
    // 채팅 유저 데이터 어레이
    private ArrayList<Chat_Inside_User_Data> mDataset = null;

    Chat_Inside_Adapter(ArrayList<Chat_Inside_User_Data> dataList, String Login_User_Id)
    {
        this.Login_User_Id = Login_User_Id;
        mDataset = dataList;
    }

    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 뷰 선언
        View view;

        // context 생성
        Context context = parent.getContext();

        // 일플레이터 생성
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 뷰 타입에 따라서 달라짐

        // 로그인한 유저와 viewType이 같다면
        if(viewType == 1){
            view = inflater.inflate(R.layout.chat_inside_me, parent, false);
            return new RightViewHolder(view);
        }

        else{
            view = inflater.inflate(R.layout.chat_inside_another, parent, false);
            return new LeftViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(viewHolder instanceof LeftViewHolder){
            ((LeftViewHolder) viewHolder).image.setImageURI(Uri.parse(mDataset.get(position).getChat_Inside_User_Img()));
            ((LeftViewHolder) viewHolder).content.setText(mDataset.get(position).getChat_Inside_User_Message());
            ((LeftViewHolder) viewHolder).name.setText(mDataset.get(position).getChat_Inside_User_Id());
        }
        else{
            ((RightViewHolder) viewHolder).content.setText(mDataset.get(position).getChat_Inside_User_Message());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 여기서 반환 하는 타입에 따라서 if문 달라짐
    @Override
    public int getItemViewType(int position) {
        int check = 0;
        if(mDataset.get(position).getChat_Inside_User_Id().equals(Login_User_Id)){
            check = 1;
        }
        return check;
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        CircleImageView image;

        LeftViewHolder(View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.Another_Name);
            content = itemView.findViewById(R.id.Chat_Inside_Another_Message);
            image = itemView.findViewById(R.id.Chat_Inside_Another_Icon);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView name;
        CircleImageView image;

        RightViewHolder(View itemView)
        {
            super(itemView);

            content = itemView.findViewById(R.id.Chat_Inside_Me_Message);
        }
    }
}