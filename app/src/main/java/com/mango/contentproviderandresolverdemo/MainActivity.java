package com.mango.contentproviderandresolverdemo;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";

    private final static int ADD_ARTICAL_ACTIVITY = 1;
    private final static int EDIT_ARTICAL_ACTIVITY = 2;

    private ArticlesAdapter aa = null;
    private ArticleAdapter adapter = null;
    private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            return aa.getArticlesById(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article article = (Article)getItem(position);

            convertView title = 

            return null;
        }


    }
}
