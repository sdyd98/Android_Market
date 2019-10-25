package com.example.myapplication;

import java.util.ArrayList;

public class Chat_List {

    // 채팅방 만든 유저 아이디
    String Chat_Maker_User;

    // 채팅방 만든 유저 이름
    String Chat_Maker_Name;

    // 채팅방 만든 유저 이미지
    String Chat_Maker_User_Img;

    // 채팅 걸린 유저 아이디
    String Chat_Receiver_User;

    // 채팅 걸린 유저 이름
    String Chat_Receiver_Name;

    // 채팅 걸린 유저 이미지
    String Chat_Receiver_User_Img;

    // 채팅 읽었는지 판단
    boolean Chat_Check;

    // 유저 들이한 채팅 데이터 어레이 리스트
    ArrayList<Chat_Inside_User_Data> Chat_Inside_User_Data_Array = new ArrayList<>();

    public Chat_List(String chat_Maker_User, String chat_Receiver_User, String Chat_Maker_User_Img, String Chat_Receiver_User_Img,String Chat_Maker_Name, String Chat_Receiver_Name) {
        Chat_Maker_User = chat_Maker_User;
        Chat_Receiver_User = chat_Receiver_User;
        this.Chat_Maker_User_Img = Chat_Maker_User_Img;
        this.Chat_Receiver_User_Img = Chat_Receiver_User_Img;
        this.Chat_Maker_Name = Chat_Maker_Name;
        this.Chat_Receiver_Name = Chat_Receiver_Name;
    }

    public boolean isChat_Check() {
        return Chat_Check;
    }

    public void setChat_Check(boolean chat_Check) {
        Chat_Check = chat_Check;
    }

    public String getChat_Maker_Name() {
        return Chat_Maker_Name;
    }

    public void setChat_Maker_Name(String chat_Maker_Name) {
        Chat_Maker_Name = chat_Maker_Name;
    }

    public String getChat_Receiver_Name() {
        return Chat_Receiver_Name;
    }

    public void setChat_Receiver_Name(String chat_Receiver_Name) {
        Chat_Receiver_Name = chat_Receiver_Name;
    }

    public String getChat_Maker_User_Img() {
        return Chat_Maker_User_Img;
    }

    public void setChat_Maker_User_Img(String chat_Maker_User_Img) {
        Chat_Maker_User_Img = chat_Maker_User_Img;
    }

    public String getChat_Receiver_User_Img() {
        return Chat_Receiver_User_Img;
    }

    public void setChat_Receiver_User_Img(String chat_Receiver_User_Img) {
        Chat_Receiver_User_Img = chat_Receiver_User_Img;
    }

    public String getChat_Maker_User() {
        return Chat_Maker_User;
    }

    public void setChat_Maker_User(String chat_Maker_User) {
        Chat_Maker_User = chat_Maker_User;
    }

    public String getChat_Receiver_User() {
        return Chat_Receiver_User;
    }

    public void setChat_Receiver_User(String chat_Receiver_User) {
        Chat_Receiver_User = chat_Receiver_User;
    }

    public ArrayList<Chat_Inside_User_Data> getChat_Inside_User_Data_Array() {
        return Chat_Inside_User_Data_Array;
    }

    public void setChat_Inside_User_Data_Array(ArrayList<Chat_Inside_User_Data> chat_Inside_User_Data_Array) {
        Chat_Inside_User_Data_Array = chat_Inside_User_Data_Array;
    }
}
