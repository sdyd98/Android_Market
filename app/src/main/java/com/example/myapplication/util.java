package com.example.myapplication;

public class util {

    public void getSpinner_Count(String[] item, String Categori_Name) {
        for (int i = 0; i < item.length; i++) {
            if (item[i].equals(Categori_Name)) {
                int i1 = i;

                break;
            }
        }
    }
}

