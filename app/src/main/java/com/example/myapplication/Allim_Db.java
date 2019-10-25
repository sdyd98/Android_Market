package com.example.myapplication;

class Allim_Db {
    String Allim_Item_Img;
    String Allim_User_Img;
    String Allim_Ments;
    String Allim_Time;
    String Allim_User_Name;
    int Item_Number;

    public Allim_Db(String allim_Item_Img, String allim_User_Img, String allim_Ments, String allim_Time, String allim_User_Name, int item_Number) {
        Allim_Item_Img = allim_Item_Img;
        Allim_User_Img = allim_User_Img;
        Allim_Ments = allim_Ments;
        Allim_Time = allim_Time;
        Allim_User_Name = allim_User_Name;
        Item_Number = item_Number;
    }

    public int getItem_Number() {
        return Item_Number;
    }

    public void setItem_Number(int item_Number) {
        Item_Number = item_Number;
    }

    public String getAllim_User_Name() {
        return Allim_User_Name;
    }

    public void setAllim_User_Name(String allim_User_Name) {
        Allim_User_Name = allim_User_Name;
    }

    public String getAllim_Item_Img() {
        return Allim_Item_Img;
    }

    public void setAllim_Item_Img(String allim_Item_Img) {
        Allim_Item_Img = allim_Item_Img;
    }

    public String getAllim_User_Img() {
        return Allim_User_Img;
    }

    public void setAllim_User_Img(String allim_User_Img) {
        Allim_User_Img = allim_User_Img;
    }

    public String getAllim_Ments() {
        return Allim_Ments;
    }

    public void setAllim_Ments(String allim_Ments) {
        Allim_Ments = allim_Ments;
    }

    public String getAllim_Time() {
        return Allim_Time;
    }

    public void setAllim_Time(String allim_Time) {
        Allim_Time = allim_Time;
    }
}
