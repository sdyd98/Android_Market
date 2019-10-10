package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Chat_Inside_Adapter extends RecyclerView.Adapter<Chat_Inside_Adapter.MyViewHolder> {
    private ArrayList<Chat_Inside_User_Data> mDataset;
    private static View.OnClickListener onClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Chat_Inside_User_Message;
        public TextView Chat_Inside_Another_Message;
        public TextView Chat_Inside_User_id;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            Chat_Inside_User_Message = v.findViewById(R.id.Chat_Inside_Me_Message);
            Chat_Inside_Another_Message = v.findViewById(R.id.Chat_Another_Message);
            Chat_Inside_User_id = v.findViewById(R.id.Chat_Inside_User_Id);
            rootView = v;
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Chat_Inside_Adapter(ArrayList<Chat_Inside_User_Data> myDataset, View.OnClickListener onClick) {
        // 들어온 데이터 저장
        mDataset = myDataset;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Chat_Inside_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_inside_another, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Chat_Inside_Another_Message.setText(mDataset.get(position).getChat_Inside_User_Message());

        if(Chat_Activity.User_Check == true) {
            holder.Chat_Inside_User_id.setText("나");
            holder.Chat_Inside_User_id.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.Chat_Inside_Another_Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else{
            holder.Chat_Inside_User_id.setText("상대방");
            holder.Chat_Inside_User_id.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.Chat_Inside_Another_Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            holder.Chat_Inside_Another_Message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Chat_Inside_User_Data getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }

    public void addChat(Chat_Inside_User_Data chat_inside_user_data ) {
        mDataset.add(chat_inside_user_data);
        notifyItemInserted(mDataset.size()-1);
    }
}