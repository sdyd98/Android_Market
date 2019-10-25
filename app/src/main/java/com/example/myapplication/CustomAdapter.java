package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    // List 받을곳
    private ArrayList<User_Comments> mList;
    private Context mContext;
    private String Login_User_ID;

    public class CustomViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener { // 1. 리스너 추가
        // 뷰 선언
        protected TextView User_id;
        protected TextView User_Comments;
        protected CircleImageView User_Img;
        protected LinearLayout Comments_touch;


        public CustomViewHolder(View view) {
            // 뷰 매칭
            super(view);
            this.User_id = (TextView) view.findViewById(R.id.User_Id);
            this.User_Comments = (TextView) view.findViewById(R.id.User_Comments);
            this.User_Img = view.findViewById(R.id.User_Img);
            this.Comments_touch = view.findViewById(R.id.Comments_touch);

            //2. 리스너 등록
            Comments_touch.setOnCreateContextMenuListener(this);
        }



        // 메뉴 만들기
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가
            String Check = mList.get(getAdapterPosition()).getComment_user_id();
            // 작성자 , 로그인 유저 판별
            if(Buy_Activity.User_id_test.equals(Check)) {
                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
                MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);
            }
        }


            // 4. 캔텍스트 메뉴 클릭시 동작을 설정
            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            // 편집
                            case 1001:
                                // 다이얼 로그 선언
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                View view = LayoutInflater.from(mContext)
                                        .inflate(R.layout.edit_box, null, false);
                                builder.setView(view);
                                // 완료 버튼
                                final Button ButtonSubmit = (Button) view.findViewById(R.id.User_Fix_Btn);
                                // 텍스트 입력
                                final EditText editTextID = (EditText) view.findViewById(R.id.User_Text_Fix);
                                // 댓글 내용 가져옴
                                editTextID.setText(mList.get(getAdapterPosition()).getComment_user_comments());

                                // 생성
                                final AlertDialog dialog = builder.create();
                                // 완료 버튼 누르면
                                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        // 텍스트 내용
                                        String User_Comments = editTextID.getText().toString();
                                        // 유저 닉네임
                                        String User_Name = mList.get(getAdapterPosition()).getComment_user_name();
                                        // 유저 이미지
                                        String Img = mList.get(getAdapterPosition()).getComment_user_icon();
                                        // 유저 아이디
                                        String User_ID = mList.get(getAdapterPosition()).getComment_user_id();


                                        //Comments comments = new Comments(strID, strEnglish, Img);
                                        // 유저 객체 새로 생성
                                        User_Comments user_comments = new User_Comments(User_Name, Img, User_Comments, User_ID);

                                        // 정보 변경
                                        mList.set(getAdapterPosition(), user_comments);
                                        // 새로고침
                                        notifyItemChanged(getAdapterPosition());

                                        // 다이얼로그 삭제
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                                break;

                            case 1002:

                                // 위치 정보 삭제
                                mList.remove(getAdapterPosition());
                                // 포지션 값도 삭제
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), mList.size());
                                break;

                            }
                        return true;
                    }
            };
    }



//    public CustomAdapter(ArrayList<Dictionary> list) {
//        this.mList = list;
//    }

    public CustomAdapter(Context context, ArrayList<User_Comments> list, String login_User_ID) {
        mList = list;
        mContext = context;
        Login_User_ID = login_User_ID;
    }




    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comments_view, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        viewholder.User_Img.setImageURI(Uri.parse(mList.get(position).getComment_user_icon()));
        viewholder.User_id.setText(mList.get(position).getComment_user_name());
        viewholder.User_Comments.setText(mList.get(position).getComment_user_comments());
        //viewholder.User_Img.setText(mList.get(position).getKorean());

        viewholder.User_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Profile_Activity.class);
                intent.putExtra("User_ID", Login_User_ID);
                intent.putExtra("Profile_Id", mList.get(position).getComment_user_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}