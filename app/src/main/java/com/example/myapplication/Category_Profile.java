package com.example.myapplication;

public class Category_Profile {

    private String Item_Name;
    private String Item_Img;


    public Category_Profile(String Item_Name, String Item_Img){
        this.Item_Name = Item_Name;
        this.Item_Img = Item_Img;
    }

    public String getItem_Name(){
        return Item_Name;
    }

    public String getItem_Img(){
        return Item_Img;
    }

    public void setItem_Name(String setItem_Name){
        this.Item_Name = setItem_Name;
    }

    public void setItem_Img(String setItem_Img){
        this.Item_Img = setItem_Img;
    }

}
