package com.example.myapplication;

class Chat_Inside_User_Data {
    String Chat_Inside_User_Id;
    String Chat_Inside_User_Message;
    String Chat_Inside_User_Img;
    String Chat_Inside_User_Message_Time;

    public Chat_Inside_User_Data(String chat_Inside_User_Id, String chat_Inside_User_Message, String Chat_Inside_User_Img, String Chat_Inside_User_Message_Time) {
        Chat_Inside_User_Id = chat_Inside_User_Id;
        Chat_Inside_User_Message = chat_Inside_User_Message;
        this.Chat_Inside_User_Img = Chat_Inside_User_Img;
        this.Chat_Inside_User_Message_Time = Chat_Inside_User_Message_Time;
    }

    public String getChat_Inside_User_Message_Time() {
        return Chat_Inside_User_Message_Time;
    }

    public void setChat_Inside_User_Message_Time(String chat_Inside_User_Message_Time) {
        Chat_Inside_User_Message_Time = chat_Inside_User_Message_Time;
    }

    public String getChat_Inside_User_Img() {
        return Chat_Inside_User_Img;
    }

    public void setChat_Inside_User_Img(String chat_Inside_User_Img) {
        Chat_Inside_User_Img = chat_Inside_User_Img;
    }

    public String getChat_Inside_User_Id() {
        return Chat_Inside_User_Id;
    }

    public void setChat_Inside_User_Id(String chat_Inside_User_Id) {
        Chat_Inside_User_Id = chat_Inside_User_Id;
    }

    public String getChat_Inside_User_Message() {
        return Chat_Inside_User_Message;
    }

    public void setChat_Inside_User_Message(String chat_Inside_User_Message) {
        Chat_Inside_User_Message = chat_Inside_User_Message;
    }

}
