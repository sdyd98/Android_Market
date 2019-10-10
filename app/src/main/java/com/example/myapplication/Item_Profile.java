package com.example.myapplication;

public class Item_Profile {

    private String Item_Name;
    private String Item_Price;
    private String Item_Img;
    private String Item_Detail;
    private String Categori_Name;

    public Item_Profile(String Item_Name, String Item_Price, String Item_Img, String Item_Detail, String Categori_Name){
        this.Item_Name = Item_Name;
        this.Item_Price = Item_Price;
        this.Item_Img = Item_Img;
        this.Item_Detail = Item_Detail;
        this.Categori_Name = Categori_Name;
    }

    public String getItem_Name(){
        return Item_Name;
    }

    public String getItem_Price(){
        return Item_Price;
    }

    public String getItem_Img(){
        return Item_Img;
    }

    public String getItem_Detail(){
        return Item_Detail;
    }

    public String getCategori_Name(){
        return Categori_Name;
    }

    public void setItem_Name(String setItem_Name){
        this.Item_Name = setItem_Name;
    }

    public void setItem_Price(String setItem_Price){
        this.Item_Price = setItem_Price;
    }

    public void setItem_Img(String setItem_Img){
        this.Item_Img = setItem_Img;
    }

    public void setItem_Detail(String setItem_Detail){
        this.Item_Detail = setItem_Detail;
    }

    public void setItem_Categori_Name(String setCategori_Name){
        this.Categori_Name = setCategori_Name;
    }

}
