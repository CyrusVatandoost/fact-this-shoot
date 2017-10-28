package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import classes.Category;
import classes.CategoryLoader;
import classes.CategoryLoaderSingleton;

public class Loading extends AppCompatActivity {

    private CategoryLoader categoryLoader;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");

        TextView num = (TextView)findViewById(R.id.textNum);
        num.setText(categoryName);

        categoryLoader = CategoryLoaderSingleton.getInstance(null).getCategoryLoader();

        ProgressBar progressLoading = (ProgressBar)findViewById(R.id.progressLoading);
        loadCategory(categoryName);
        progressLoading.setVisibility(View.GONE);
        play();

    }

    public void loadCategory(String categoryName) {
        category = categoryLoader.getCategory(categoryName);
    }

    public void play() {
        Intent intent = new Intent(this, Loading.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}
