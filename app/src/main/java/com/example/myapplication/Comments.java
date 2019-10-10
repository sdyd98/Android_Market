package com.example.myapplication;

public class Comments {
    private String Comments_Detail;
    private String Comments_Id;
    private String Comments_Img;

    public Comments(String comments_Detail, String comments_Id, String comments_Img) {
        Comments_Detail = comments_Detail;
        Comments_Id = comments_Id;
        Comments_Img = comments_Img;
    }

    public String getComments_Detail() {
        return Comments_Detail;
    }

    public void setComments_Detail(String comments_Detail) {
        Comments_Detail = comments_Detail;
    }

    public String getComments_Id() {
        return Comments_Id;
    }

    public void setComments_Id(String comments_Id) {
        Comments_Id = comments_Id;
    }

    public String getComments_Img() {
        return Comments_Img;
    }

    public void setComments_Img(String comments_Img) {
        Comments_Img = comments_Img;
    }
}
