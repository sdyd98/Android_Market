package com.example.myapplication;

class User_DB {
    private String user_icon_img;
    private String user_id;
    private String user_pw;
    private String user_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public User_DB(String user_icon_img, String user_id, String user_pw, String user_name) {
        this.user_icon_img = user_icon_img;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
    }

    public String getUser_icon_img() {
        return user_icon_img;
    }

    public void setUser_icon_img(String user_icon_img) {
        this.user_icon_img = user_icon_img;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }
}
