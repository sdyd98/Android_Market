package com.example.myapplication;

import java.util.ArrayList;

class Item_DB {
    // 게시글 고유 번호
    int item_number;
    // 작성자 아이디
    String item_id;
    // 작성자 이름
    String item_writer;
    // 상품 가격
    String item_price;
    // 상품 이름
    String item_name;
    // 게시글 작성 시간
    String item_create_time;
    // 게시글 조회수
    int item_watch;
    // 게시글 좋아요 수
    int item_heart;
    // 게시글 내용
    String item_detail;
    //게시글 댓글
    ArrayList<User_Comments>  user_comments = new ArrayList<>();
    //게시글 조회 판단
    ArrayList<User_See> user_sees = new ArrayList<>();
    //게시글 찜 유저 목록
    ArrayList<User_Select> user_selects = new ArrayList<>();
    //작성자 오픈일
    int item_user_open;
    //작성자 팔로워
    int item_user_follower;
    // 상품 이미지
    String item_img;

    // 카테고리 이름
    String category_name;

    // 판매 유무 확인
    boolean item_state;

    // 판매자 이미지
    String seller_img;

    double Latitude;
    double Hardness;

    public Item_DB(int item_number, String item_writer, String item_price, String item_name, String item_create_time, int item_watch, int item_heart, String item_detail, int item_user_open, int item_user_follower, String item_img, String seller_img, String category_name, String item_id, double Hardeness, double Latitude, boolean item_state) {
        this.item_number = item_number;
        this.item_writer = item_writer;
        this.item_price = item_price;
        this.item_name = item_name;
        this.item_create_time = item_create_time;
        this.item_watch = item_watch;
        this.item_heart = item_heart;
        this.item_detail = item_detail;
        this.item_user_open = item_user_open;
        this.item_user_follower = item_user_follower;
        this.item_img = item_img;
        this.seller_img = seller_img;
        this.category_name = category_name;
        this.item_id = item_id;
        this.Latitude = Latitude;
        this.Hardness = Hardeness;
        this.item_state = item_state;
    }

    public boolean isItem_state() {
        return item_state;
    }

    public void setItem_state(boolean item_state) {
        this.item_state = item_state;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getHardness() {
        return Hardness;
    }

    public void setHardness(double hardness) {
        Hardness = hardness;
    }

    public ArrayList<User_Select> getUser_selects() {
        return user_selects;
    }

    public void setUser_selects(ArrayList<User_Select> user_selects) {
        this.user_selects = user_selects;
    }

    public ArrayList<User_See> getUser_sees() {
        return user_sees;
    }

    public void setUser_sees(ArrayList<User_See> user_sees) {
        this.user_sees = user_sees;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getitem_img(){
        return item_img;
    }

    public String getSeller_img() {
        return seller_img;
    }

    public void setSeller_img(String seller_img) {
        this.seller_img = seller_img;
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

    public String getitem_create_time() {
        return item_create_time;
    }

    public void setitem_create_time(String item_create_time) {
        this.item_create_time= item_create_time;
    }

    public int getItem_watch() {
        return item_watch;
    }

    public void setItem_watch(int item_watch) {
        this.item_watch = item_watch;
    }

    public int getItem_heart() {
        return item_heart;
    }

    public void setItem_heart(int item_heart) {
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

    public int getItem_user_open() {
        return item_user_open;
    }

    public void setItem_user_open(int item_user_open) {
        this.item_user_open = item_user_open;
    }

    public int getItem_user_follower() {
        return item_user_follower;
    }

    public void setItem_user_follower(int item_user_follower) {
        this.item_user_follower = item_user_follower;
    }
}
