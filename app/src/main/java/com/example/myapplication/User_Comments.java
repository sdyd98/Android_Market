package com.example.myapplication;

class User_Comments {
    String comment_user_name;
    String comment_user_icon;
    String comment_user_comments;
    String comment_user_id;

    public User_Comments(String comment_user_name, String comment_user_icon, String comment_user_comments, String comment_user_id) {
        this.comment_user_name = comment_user_name;
        this.comment_user_icon = comment_user_icon;
        this.comment_user_comments = comment_user_comments;
        this.comment_user_id = comment_user_id;
    }

    public String getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(String comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public void setComment_user_name(String comment_user_name) {
        this.comment_user_name = comment_user_name;
    }

    public String getComment_user_icon() {
        return comment_user_icon;
    }

    public void setComment_user_icon(String comment_user_icon) {
        this.comment_user_icon = comment_user_icon;
    }

    public String getComment_user_comments() {
        return comment_user_comments;
    }

    public void setComment_user_comments(String comment_user_comments) {
        this.comment_user_comments = comment_user_comments;
    }
}
