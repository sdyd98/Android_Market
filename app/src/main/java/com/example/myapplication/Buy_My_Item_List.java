package com.example.myapplication;

public class Buy_My_Item_List {
    private String Buy_Item_Name;
    private String Buy_Item_Price;
    private String Buy_Item_Img;
    private String Buy_Item_Detail;
    private String Buy_Categori_Name;

    public Buy_My_Item_List(String buy_Item_Name, String buy_Item_Price, String buy_Item_Img, String buy_Item_Detail, String buy_Categori_Name) {
        Buy_Item_Name = buy_Item_Name;
        Buy_Item_Price = buy_Item_Price;
        Buy_Item_Img = buy_Item_Img;
        Buy_Item_Detail = buy_Item_Detail;
        Buy_Categori_Name = buy_Categori_Name;
    }

    public String getBuy_Item_Name() {
        return Buy_Item_Name;
    }

    public void setBuy_Item_Name(String buy_Item_Name) {
        Buy_Item_Name = buy_Item_Name;
    }

    public String getBuy_Item_Price() {
        return Buy_Item_Price;
    }

    public void setBuy_Item_Price(String buy_Item_Price) {
        Buy_Item_Price = buy_Item_Price;
    }

    public String getBuy_Item_Img() {
        return Buy_Item_Img;
    }

    public void setBuy_Item_Img(String buy_Item_Img) {
        Buy_Item_Img = buy_Item_Img;
    }

    public String getBuy_Item_Detail() {
        return Buy_Item_Detail;
    }

    public void setBuy_Item_Detail(String buy_Item_Detail) {
        Buy_Item_Detail = buy_Item_Detail;
    }

    public String getBuy_Categori_Name() {
        return Buy_Categori_Name;
    }

    public void setBuy_Categori_Name(String buy_Categori_Name) {
        Buy_Categori_Name = buy_Categori_Name;
    }
}
