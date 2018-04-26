package com.mango.contentproviderandresolverdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ArticleActivity extends Activity implements View.OnClickListener {

    private final static String TAG = ArticleActivity.class.getSimpleName();

    public final static String EDIT_ARTICLE_ACTION = "EDIT_ARTICLE_ACTION";
    public final static int MODIFY_ARTICLE = 1;
    public final static int DELETE_ARTICLE = 2;

    private int articleId = -1;

    private EditText titleEdit = null;
    private EditText abstractEdit = null;
    private EditText urlEdit = null;

    private Button addButton = null;
    private Button modifyButton = null;
    private Button deleteButton = null;
    private Button cancelButton = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);

        titleEdit = findViewById(R.id.edit_article_title);
        abstractEdit = findViewById(R.id.edit_article_abstract);
        urlEdit = findViewById(R.id.edit_article_url);

        addButton = findViewById(R.id.button_add_article);
        addButton.setOnClickListener(this);

        modifyButton = findViewById(R.id.button_modify);
        modifyButton.setOnClickListener(this);

        deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(this);

        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        Intent intent = getIntent();
        articleId = intent.getIntExtra(Articles.ID, -1);

        if(articleId != -1) {
            String title = intent.getStringExtra(Articles.TITLE);
            titleEdit.setText(title);

            String abs = intent.getStringExtra(Articles.ABSTRACT);
            abstractEdit.setText(abs);

            String url = intent.getStringExtra(Articles.URL);
            urlEdit.setText(Articles.URL);

            addButton.setVisibility(View.GONE);
        } else {
            modifyButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        Log.i(TAG,"Activity created");

    }

    @Override
    public void onClick(View v) {
        if (v.equals(addButton)) {
            String title = titleEdit.getText().toString();
            String abs = abstractEdit.getText().toString();
            String url = urlEdit.getText().toString();

            Intent result = new Intent();
            result.putExtra(Articles.TITLE, title);
            result.putExtra(Articles.ABSTRACT, abs);
            result.putExtra(Articles.URL, url);

            setResult(RESULT_OK, result);
            finish();
        } else if(v.equals(modifyButton)) {
            String title = titleEdit.getText().toString();
            String abs = abstractEdit.getText().toString();
            String url = urlEdit.getText().toString();

            Intent result = new Intent();
            result.putExtra(Articles.ID, articleId);
            result.putExtra(Articles.TITLE, title);
            result.putExtra(Articles.ABSTRACT, abs);
            result.putExtra(Articles.URL, url);
            result.putExtra(EDIT_ARTICLE_ACTION, MODIFY_ARTICLE);

            setResult(RESULT_OK, result);
        } else if(v.equals(deleteButton)) {
            Intent result = new Intent();
            result.putExtra(Articles.ID, articleId);
            result.putExtra(EDIT_ARTICLE_ACTION,DELETE_ARTICLE);

            setResult(RESULT_OK, result);
            finish();
        } else if(v.equals(cancelButton)) {
            setResult(RESULT_CANCELED,null);
            finish();
        }
    }
}
