package com.example.widget_example.model;

import java.util.ArrayList;

public class EventBusResults {

    int mResult;
    public ArrayList<Articles> mArticlesArrayList;

    public EventBusResults(int resultCode, ArrayList<Articles> articlesArrayList){
        this.mResult = resultCode;
        this.mArticlesArrayList = articlesArrayList;
    }

    public int getmResult() {
        return mResult;
    }

    public ArrayList<Articles> getmArticlesArrayList() {
        return mArticlesArrayList;
    }
}
