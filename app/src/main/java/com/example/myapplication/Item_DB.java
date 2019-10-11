package com.example.myapplication;

import java.util.ArrayList;

class Item_DB {
    // 게시글 고유 번호
    int item_number;
    // 작성자 이름
    String item_writer;
    // 상품 가격
    String item_price;
    // 상품 이름
    String item_name;
    // 게시글 작성 시간
    String item_create_tiem;
    // 게시글 조회수
    String item_watch;
    // 게시글 좋아요 수
    String item_heart;
    // 게시글 내용
    String item_detail;
    //게시글 댓글
    ArrayList<User_Comments>  user_comments = new ArrayList<>();
    //작성자 오픈일
    String item_user_open;
    //작성자 팔로워
    String item_user_follower;
    // 상품 이미지
    String item_img;

    public Item_DB(int item_number, String item_writer, String item_price, String item_name, String item_create_tiem, String item_watch, String item_heart, String item_detail, String item_user_open, String item_user_follower, String item_img) {
        this.item_number = item_number;
        this.item_writer = item_writer;
        this.item_price = item_price;
        this.item_name = item_name;
        this.item_create_tiem = item_create_tiem;
        this.item_watch = item_watch;
        this.item_heart = item_heart;
        this.item_detail = item_detail;
        this.item_user_open = item_user_open;
        this.item_user_follower = item_user_follower;
        this.item_img = item_img;
    }
    public String getitem_img(){
        return item_img;
    }

    public void setItem_img(String item_img){
        this.item_img = item_img;
    }

    public int getItem_number() {
        return item_number;
    }

    public void setItem_number(int item_number) {
        this.item_number = item_number;
    }

    public String getItem_writer() {
        return item_writer;
    }

    public void setItem_writer(String item_writer) {
        this.item_writer = item_writer;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_create_tiem() {
        return item_create_tiem;
    }

    public void setItem_create_tiem(String item_create_tiem) {
        this.item_create_tiem = item_create_tiem;
    }

    public String getItem_watch() {
        return item_watch;
    }

    public void setItem_watch(String item_watch) {
        this.item_watch = item_watch;
    }

    public String getItem_heart() {
        return item_heart;
    }

    public void setItem_heart(String item_heart) {
        this.item_heart = item_heart;
    }

    public String getItem_detail() {
        return item_detail;
    }

    public void setItem_detail(String item_detail) {
        this.item_detail = item_detail;
    }

    public ArrayList<User_Comments> getUser_comments() {
        return user_comments;
    }

    public void setUser_comments(ArrayList<User_Comments> user_comments) {
        this.user_comments = user_comments;
    }

    public String getItem_user_open() {
        return item_user_open;
    }

    public void setItem_user_open(String item_user_open) {
        this.item_user_open = item_user_open;
    }

    public String getItem_user_follower() {
        return item_user_follower;
    }

    public void setItem_user_follower(String item_user_follower) {
        this.item_user_follower = item_user_follower;
    }
}
