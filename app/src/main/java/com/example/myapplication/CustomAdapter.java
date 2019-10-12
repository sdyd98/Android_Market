package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User_Comments> mList;
    private Context mContext;

    public class CustomViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener { // 1. 리스너 추가
        protected TextView User_id;
        protected TextView User_Comments;


        public CustomViewHolder(View view) {
            super(view);
            this.User_id = (TextView) view.findViewById(R.id.User_Id);
            this.User_Comments = (TextView) view.findViewById(R.id.User_Comments);

                view.setOnCreateContextMenuListener(this); //2. 리스너 등록
            if(Buy_Activity.User_id_test.equals(User_id.getText().toString())) {
                Toast.makeText(mContext, "트루다 트루", Toast.LENGTH_SHORT).show();
                view.setEnabled(true);
            }
            else{
                Toast.makeText(mContext, "펄스다 펄스 안눌린다", Toast.LENGTH_SHORT).show();
                view.setEnabled(false);
            }

        }



        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가

                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
                MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);

        }


            // 4. 캔텍스트 메뉴 클릭시 동작을 설정
            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case 1001:

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                View view = LayoutInflater.from(mContext)
                                        .inflate(R.layout.edit_box, null, false);
                                builder.setView(view);
                                final Button ButtonSubmit = (Button) view.findViewById(R.id.User_Fix_Btn);
                                final EditText editTextID = (EditText) view.findViewById(R.id.User_Text_Fix);


                                editTextID.setText(mList.get(getAdapterPosition()).getComment_user_comments());

                                final AlertDialog dialog = builder.create();
                                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        String strID = editTextID.getText().toString();
                                        String strEnglish = mList.get(getAdapterPosition()).getComment_user_comments();
                                        String Img = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.user_icon).toString();


                                        //Comments comments = new Comments(strID, strEnglish, Img);
                                        User_Comments user_comments = new User_Comments(strEnglish, Img, strID);

                                        mList.set(getAdapterPosition(), user_comments);
                                        notifyItemChanged(getAdapterPosition());

                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                                break;

                            case 1002:

                                mList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), mList.size());
                                notifyDataSetChanged();
                                break;

                            }
                        return true;
                    }
            };
    }



//    public CustomAdapter(ArrayList<Dictionary> list) {
//        this.mList = list;
//    }

    public CustomAdapter(Context context, ArrayList<User_Comments> list) {
        mList = list;
        mContext = context;
    }




    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comments_view, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.User_id.setText(mList.get(position).getComment_user_name());
        viewholder.User_Comments.setText(mList.get(position).getComment_user_comments());
        //viewholder.User_Img.setText(mList.get(position).getKorean());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}