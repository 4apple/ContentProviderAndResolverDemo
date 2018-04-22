package com.mango.contentproviderandresolverdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final static String TAG = "MainActivity";

    private final static int ADD_ARTICAL_ACTIVITY = 1;
    private final static int EDIT_ARTICAL_ACTIVITY = 2;

    private ArticlesAdapter aa = null;
    private ArticleAdapter adapter = null;
    private ArticleObserver observer = null;

    private ListView articleList = null;
    private Button addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aa = new ArticlesAdapter(this);

        articleList = findViewById(R.id.listview_article);
        adapter = new ArticleAdapter(this);
        articleList.setAdapter(adapter);
        articleList.setOnItemClickListener(this);

        addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(this);
        Log.i(TAG, "Main activity created");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        if (v.equals(addButton)) {
            Intent intent = new Intent(this, ArticleActivity.class);
            startActivityForResult(intent, ADD_ARTICAL_ACTIVITY);
        }
    }

    private class ArticleObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public ArticleObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            adapter.notifyAll();
        }
    }

    private class ArticleAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ArticleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return aa.getArticleCount();
        }

        @Override
        public Object getItem(int position) {
            return aa.getArticlesByPos(position);
        }

        @Override
        public long getItemId(int position) {
            Article a = aa.getArticlesByPos(position);
            if (a == null) {
                return -1;
            } else {
                return a.getId();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article article = (Article)getItem(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item, null);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.textview_article_title);
            titleView.setText("Title: " + article.getTitle());

            TextView  abstractView = (TextView) convertView.findViewById(R.id.textview_article_abstract);
            abstractView.setText("Abstract: " + article.getAbstract());

            TextView urlView = (TextView)convertView.findViewById(R.id.textview_article_url);
            urlView.setText("Url: " + article.getUrl());

            return convertView;
        }


    }
}
