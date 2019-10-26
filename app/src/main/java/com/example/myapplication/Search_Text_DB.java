package com.example.myapplication;

class Search_Text_DB {
    String Search_Text;
    int Search_Count;

    public Search_Text_DB(String search_Text, int search_Count) {
        Search_Text = search_Text;
        Search_Count = search_Count;
    }

    public String getSearch_Text() {
        return Search_Text;
    }

    public void setSearch_Text(String search_Text) {
        Search_Text = search_Text;
    }

    public int getSearch_Count() {
        return Search_Count;
    }

    public void setSearch_Count(int search_Count) {
        Search_Count = search_Count;
    }
}
