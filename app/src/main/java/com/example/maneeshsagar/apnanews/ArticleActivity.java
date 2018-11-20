package com.example.maneeshsagar.apnanews;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maneeshsagar.apnanews.Database.AppDatabase;
import com.example.maneeshsagar.apnanews.model.ArticleStructure;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ArticleActivity extends AppCompatActivity {
    AppDatabase mDb;
    private String URL;
    private Typeface montserrat_regular;
    private Typeface montserrat_semiBold;
    ArticleStructure articleStructure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
       AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mDb=AppDatabase.getsInstance(getApplicationContext());
        articleStructure=getIntent().getParcelableExtra("Article");

        createToolbar();
        floatingButton();
       assetManager();
       receiveFromDataAdapter(montserrat_regular, montserrat_semiBold);

    }

    private void assetManager() {
        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        montserrat_semiBold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");
    }



    private void receiveFromDataAdapter(Typeface montserrat_regular, Typeface montserrat_semiBold) {
        String headLine = getIntent().getStringExtra(Constants.INTENT_HEADLINE);
        String description = getIntent().getStringExtra(Constants.INTENT_DESCRIPTION);
        String date = getIntent().getStringExtra(Constants.INTENT_DATE);
        String imgURL = getIntent().getStringExtra(Constants.INTENT_IMG_URL);
        URL = getIntent().getStringExtra(Constants.INTENT_ARTICLE_URL);

        TextView content_Headline = findViewById(R.id.content_Headline);
        content_Headline.setText(headLine);
        content_Headline.setTypeface(montserrat_semiBold);

        TextView content_Description = findViewById(R.id.content_Description);
        content_Description.setText(description);
        content_Description.setTypeface(montserrat_regular);

        ImageView collapsingImage = findViewById(R.id.collapsingImage);
        Glide.with(this)
                .load(imgURL)
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .crossFade()
                .into(collapsingImage);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mDb.queryDao().insertArticle(articleStructure );
               Snackbar.make(v, "Added as Favorite", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
           }
       });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * Override the Up/Home Button
             * */
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
