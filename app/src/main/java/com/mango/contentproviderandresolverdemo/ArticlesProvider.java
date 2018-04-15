package com.mango.contentproviderandresolverdemo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


import java.nio.BufferUnderflowException;
import java.util.HashMap;

/**
 * class
 * package com.mango.contentproviderandresolverdemo
 *
 * @author swd1
 * @date 18-4-10
 */
public class ArticlesProvider extends ContentProvider {

    private static final String LOG_TAG = "ArticlesProvider";

    private static final String DB_NAME = "Articles.db";
    private static final String DB_TABLE = "ArticlesTable";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE = "CREATE TABLE " + DB_NAME +
            " (" + Articles.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Articles.TITLE + " TEXT NOT NULL, " +
            Articles.ABSTRACT + " TEXT NOT NULL, " +
            Articles.URL + " TEXT NOT NULL);";

    private static final UriMatcher uriMatcher;
    private static final HashMap<String, String> articleProjectionMap;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Articles.AUTHORITY, "item", Articles.ITEM);
        uriMatcher.addURI(Articles.AUTHORITY, "item/#", Articles.ITEM_ID);
        uriMatcher.addURI(Articles.AUTHORITY, "pos/#", Articles.ITEM_POS);
    }

    static {
        articleProjectionMap = new HashMap<>();
        articleProjectionMap.put(Articles.ID, Articles.ID);
        articleProjectionMap.put(Articles.TITLE, Articles.TITLE);
        articleProjectionMap.put(Articles.ABSTRACT, Articles.ABSTRACT);
        articleProjectionMap.put(Articles.URL, Articles.URL);
    }

    private DBHelper dbHelper = null;
    private ContentResolver resolver = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        resolver = context.getContentResolver();

        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);

        Log.i(LOG_TAG, "Articles Provider Create");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;

        switch(uriMatcher.match(uri)){
            case Articles.ITEM: {
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(articleProjectionMap);
                break;
            }
            case Articles.ITEM_ID:{
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(articleProjectionMap);
                sqlBuilder.appendWhere(Articles.ID + "=" + id);
                break;
            }
            case Articles.ITEM_POS:{
                String pos = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(articleProjectionMap);
                limit = ", 1";
                break;
            }
            default:{
                throw new IllegalArgumentException("Error Uri: " + uri);
            }

        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null,
                null,TextUtils.isEmpty(sortOrder)? Articles.DEFAULT_SORT_ORDER : sortOrder,
                limit);
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if (method.equals(Articles.METHOD_GET_ITEM_COUNT)) {
            return getItemCount();
        }

        throw new IllegalArgumentException("Error method call: " + method);
    }

    private Bundle getItemCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DB_TABLE, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getCount();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Articles.KEY_ITEM_COUNT, count);

        cursor.close();
        db.close();

        return bundle;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Articles.ITEM:
                return Articles.CONTENT_TYPE;
            case Articles.ITEM_ID:
            case Articles.ITEM_POS:
                return Articles.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) != Articles.ITEM) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert(DB_TABLE, Articles.ID, values);
        if (id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for " + uri);
        }

        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);

        return newUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case Articles.ITEM: {
                count = db.delete(DB_TABLE, selection, selectionArgs);
                break;
            }
            case Articles.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE,Articles.ID + "=" + id
                + (!TextUtils.isEmpty(selection)? " and (" + selection + ")" : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)){
            case Articles.ITEM: {
                count = db.update(DB_TABLE, values, selection,selectionArgs);
                break;
            }
            case Articles.ITEM_ID:{
                String id = uri.getPathSegments().get(1);
                count = db.update(DB_TABLE,values,Articles.ID + "=" + id
                +(!TextUtils.isEmpty(selection) ? " and (" + selection + ")" : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);
        return count;
    }

    private static class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
