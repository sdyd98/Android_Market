package com.example.myapplication;

class User_DB {
    private String user_icon_img;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_open;
    private boolean auto_login;


    public User_DB(String user_icon_img, String user_id, String user_pw, String user_name, String user_open, boolean auto_login) {
        this.user_icon_img = user_icon_img;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_open = user_open;
        this.auto_login = auto_login;
    }

    public boolean isAuto_login() {
        return auto_login;
    }

    public void setAuto_login(boolean auto_login) {
        this.auto_login = auto_login;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getUser_open() {
        return user_open;
    }

    public void setUser_open(String user_open) {
        this.user_open = user_open;
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
