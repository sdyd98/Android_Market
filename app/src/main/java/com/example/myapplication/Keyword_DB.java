package com.example.myapplication;

class Keyword_DB {
    String Keyword;
    int MinPrice;
    int MaxPrice;

    public Keyword_DB(String keyword, int minPrice, int maxPrice) {
        Keyword = keyword;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }

    public int getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(int minPrice) {
        MinPrice = minPrice;
    }

    public int getMaxPrice() {
        return MaxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        MaxPrice = maxPrice;
    }
}
