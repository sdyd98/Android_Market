package com.example.myapplication;

class User_See {
    String see_user_id;
    String time;
    int views_count;

    public User_See(String time, int views_count, String see_user_id) {
        this.time = time;
        this.views_count = views_count;
        this.see_user_id = see_user_id;
    }

    public String getSee_user_id() {
        return see_user_id;
    }

    public void setSee_user_id(String see_user_id) {
        this.see_user_id = see_user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }
}
