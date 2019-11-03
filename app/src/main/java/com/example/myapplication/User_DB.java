package com.example.myapplication;

import java.util.ArrayList;

class User_DB {
    private String user_icon_img;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_open;
    private boolean auto_login;
    private ArrayList<Integer> User_Select = new ArrayList<>();
    private ArrayList<String> User_Follower = new ArrayList<>();
    private ArrayList<String> User_Following = new ArrayList<>();
    private ArrayList<Allim_Db> allim_db = new ArrayList<>();
    private ArrayList<Keyword_DB> User_Keyword = new ArrayList<>();
    private int Mymoney;


    public User_DB(String user_icon_img, String user_id, String user_pw, String user_name, String user_open, boolean auto_login, int Mymoney) {
        this.user_icon_img = user_icon_img;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_open = user_open;
        this.auto_login = auto_login;
        this.Mymoney = Mymoney;
    }

    public int getMymoney() {
        return Mymoney;
    }

    public void setMymoney(int mymoney) {
        Mymoney = mymoney;
    }

    public ArrayList<Keyword_DB> getUser_Keyword() {
        return User_Keyword;
    }

    public void setUser_Keyword(ArrayList<Keyword_DB> user_Keyword) {
        User_Keyword = user_Keyword;
    }

    public ArrayList<Allim_Db> getAllim_db() {
        return allim_db;
    }

    public void setAllim_db(ArrayList<Allim_Db> allim_db) {
        this.allim_db = allim_db;
    }

    public ArrayList<String> getUser_Follower() {
        return User_Follower;
    }

    public void setUser_Follower(ArrayList<String> user_Follower) {
        User_Follower = user_Follower;
    }

    public ArrayList<String> getUser_Following() {
        return User_Following;
    }

    public void setUser_Following(ArrayList<String> user_Following) {
        User_Following = user_Following;
    }

    public ArrayList<Integer> getUser_Select() {
        return User_Select;
    }

    public void setUser_Select(ArrayList<Integer> user_Select) {
        User_Select = user_Select;
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
