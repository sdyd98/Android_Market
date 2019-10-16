package com.example.myapplication;

import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private static View.OnClickListener onClickListener;
    private String Word;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Search_text_test;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            Search_text_test = v.findViewById(R.id.Search_text_test);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // 어뎁터 생성 부분
    public Search_Adapter(ArrayList<String> myDataset, View.OnClickListener onClick, String word) {
        // 들어온 데이터 저장m

        // ?? 매개변수랑 변수랑 이름같으니까 안댐
        Word = word;
        mDataset = myDataset;
        onClickListener = onClick;

    }

    // Create new views (invoked by the layout manager)
    // 레이아웃 매칭하는 부분 (레이아웃 전체를 찍어낸다)
    @Override
    public Search_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    // 어느 뷰에 데이터를 넣을지 설정
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // 일부 텍스트를 변경하는법

        // 보여질 텍스트 String 변수에 담는다
        String content = mDataset.get(position);

        // SpannableString 객체 생성
        SpannableString spannableString = new SpannableString(content);

        // 시작점과 끝부분 찾기
        int start = content.indexOf(Word);

        int end = start + Word.length();

        // 보여질 텍스트에 특정 단어가 있다면 setSpan 생성
        if(content.contains(Word)) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.Search_text_test.setText(spannableString);
        }
        else{
            holder.Search_text_test.setText(content);
        }

        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String getData(int position){
        return mDataset.get(position) != null ? mDataset.get(position) : null;
    }
}