package com.example.contentresolverdemo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

public class ArticlesAdapter {

    private static final String TAG = "ArticlesAdapter";

    private ContentResolver resolver = null;

    public ArticlesAdapter(Context context) {
        resolver = context.getContentResolver();
    }

}
