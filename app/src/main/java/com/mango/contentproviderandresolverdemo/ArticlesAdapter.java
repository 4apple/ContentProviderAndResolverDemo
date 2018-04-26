package com.mango.contentproviderandresolverdemo;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.util.LinkedList;

public class ArticlesAdapter {

    private static final String TAG = "ArticlesAdapter";

    private ContentResolver resolver;

    private String[] projection = new String[] {
            Articles.ID,
            Articles.TITLE,
            Articles.ABSTRACT,
            Articles.URL
    };

    public ArticlesAdapter(Context context) {
        resolver = context.getContentResolver();
        Log.i(TAG, "ArticlesAdapter created");
    }

    public long insertArticle(Article article) {
        ContentValues values = new ContentValues();
        values.put(Articles.TITLE, article.getTitle());
        values.put(Articles.ABSTRACT, article.getAbstract());
        values.put(Articles.URL,article.getUrl());

        Uri uri = resolver.insert(Articles.CONTENT_URI, values);
        if (uri.getPathSegments() != null) {
            String itemId = uri.getPathSegments().get(1);
            return Integer.valueOf(itemId).longValue();
        } else {
            return Integer.valueOf(0).longValue();
        }

    }

    public boolean updateArticle(Article article) {
        Uri uri = ContentUris.withAppendedId(Articles.CONTENT_URI, article.getId());

        ContentValues values = new ContentValues();
        values.put(Articles.TITLE, article.getTitle());
        values.put(Articles.ABSTRACT, article.getAbstract());
        values.put(Articles.URL,article.getUrl());

        int count = resolver.update(uri, values, null, null);

        return count > 0;
    }

    public boolean removeArticle(int id) {
        Uri uri = ContentUris.withAppendedId(Articles.CONTENT_URI, id);

        int count = resolver.delete(uri, null,null);

        return count > 0;
    }

    public LinkedList<Article> getAllArticles() {
        LinkedList<Article> articles = new LinkedList<>();



        Cursor cursor = resolver.query(Articles.CONTENT_URI, projection, null,
                null, Articles.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String abs = cursor.getString(2);
                    String url = cursor.getString(3);

                    Article article = new Article(id,title,abs,url);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return  articles;


    }


    public int getArticleCount() {
        int count  = 0;
        ContentProviderClient provider = null;
        try {
            provider = resolver.acquireContentProviderClient(Articles.CONTENT_URI);
            Bundle bundle = provider.call(Articles.METHOD_GET_ITEM_COUNT, null, null);
            count = bundle.getInt(Articles.KEY_ITEM_COUNT, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (provider != null) {
                provider.close();
            }
        }

        return count;
    }

    public Article getArticlesById(int id) {
        Uri uri = ContentUris.withAppendedId(Articles.CONTENT_URI, id);

        Cursor cursor = resolver.query(uri, projection, null, null,
                Articles.DEFAULT_SORT_ORDER);

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        String title = cursor.getString(1);
        String abs  = cursor.getString(2);
        String url = cursor.getString(3);
        cursor.close();
        return new Article(id,title,abs,url);
    }

    public Article getArticleByPos(int pos) {
        Uri uri = ContentUris.withAppendedId(Articles.CONTENT_POS_URI, pos);

        Cursor cursor = resolver.query(uri, projection, null,
                null, Articles.DEFAULT_SORT_ORDER);
        if (null == cursor || !cursor.moveToFirst()) {
            return null;
        }

        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String abs  = cursor.getString(2);
        String url = cursor.getString(3);

        cursor.close();
        return new Article(id, title, abs, url);
    }
}
