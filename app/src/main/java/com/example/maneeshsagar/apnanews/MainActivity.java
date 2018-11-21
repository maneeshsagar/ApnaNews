package com.example.maneeshsagar.apnanews;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.maneeshsagar.apnanews.adapter.DataAdapter;

import com.example.maneeshsagar.apnanews.model.ArticleStructure;
import com.example.maneeshsagar.apnanews.model.MainViewModel;
import com.example.maneeshsagar.apnanews.model.NewsResponse;

import com.example.maneeshsagar.apnanews.widget.CollectionWidget;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener {
    private String[] SOURCE_ARRAY = {"google-news-in", "bbc-news", "the-hindu", "the-times-of-india",
            "buzzfeed", "mashable", "mtv-news", "bbc-sport", "espn-cric-info", "talksport", "medical-news-today",
            "national-geographic", "crypto-coins-news", "engadget", "the-next-web", "the-verge", "techcrunch", "techradar", "ign", "polygon"};
    private String SOURCE;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<ArticleStructure> articleStructure = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Parcelable listState;
    private Typeface montserrat_regular;
    private TextView mTitle;
    public static int   currentVisiblePosition = 0;
    public static final String LIST_STATE="state";
    Parcelable parcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //    startService(new Intent());
        MobileAds.initialize(this, "ca-app-pub-1091132578018230~3201271500");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "open");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivityLaunching");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        layoutManager=new LinearLayoutManager(MainActivity.this);
        createToolbar();
        createRecyclerView();


        SOURCE = SOURCE_ARRAY[0];
        mTitle.setText(R.string.toolbar_default_text);
        onLoadingSwipeRefreshLayout();

        createDrawer(savedInstanceState, toolbar, montserrat_regular);

        loadJSON();

    }

    @Override
    protected void onPause() {
        super.onPause();
        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        currentVisiblePosition = 0;
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setTypeface(montserrat_regular);
    }
    private void createRecyclerView() {
        recyclerView = findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

    }
    private void onLoadingSwipeRefreshLayout() {

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );
    }

    private void loadJSON() {

        if(currentVisiblePosition==0) {
            swipeRefreshLayout.setRefreshing(true);

            ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
            Call<NewsResponse> call = request.getHeadlines(SOURCE, Constants.API_KEY);
            call.enqueue(new Callback<NewsResponse>() {

                @Override
                public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                    if (response.isSuccessful() && response.body().getArticles() != null) {

                        if (!articleStructure.isEmpty()) {
                            articleStructure.clear();
                        }

                        articleStructure = response.body().getArticles();

                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new DataAdapter(MainActivity.this, articleStructure);
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }


                @Override
                public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        else {
              layoutManager.scrollToPosition(currentVisiblePosition);
              recyclerView.setLayoutManager(layoutManager);
              recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_menu:
                openAboutActivity();
                break;
             case R.id.action_save:
               // openSearchActivity();
                 getFromDB();

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void openAboutActivity() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }



    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar, Typeface montserrat_regular) {
        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(0).withName("GENERAL")
                .withTypeface(montserrat_regular).withSelectable(false);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Google News India")
                .withIcon(R.drawable.ic_googlenews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("BBC News")
                .withIcon(R.drawable.ic_bbcnews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("The Hindu")
                .withIcon(R.drawable.ic_thehindu).withTypeface(montserrat_regular);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("The Times of India")
                .withIcon(R.drawable.ic_timesofindia).withTypeface(montserrat_regular);
        SectionDrawerItem item5 = new SectionDrawerItem().withIdentifier(5).withName("ENTERTAINMENT")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Buzzfeed")
                .withIcon(R.drawable.ic_buzzfeednews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("Mashable")
                .withIcon(R.drawable.ic_mashablenews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("MTV News")
                .withIcon(R.drawable.ic_mtvnews).withTypeface(montserrat_regular);
        SectionDrawerItem item9 = new SectionDrawerItem().withIdentifier(9).withName("SPORTS")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(10).withName("BBC Sports")
                .withIcon(R.drawable.ic_bbcsports).withTypeface(montserrat_regular);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("ESPN Cric Info")
                .withIcon(R.drawable.ic_espncricinfo).withTypeface(montserrat_regular);
        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(12).withName("TalkSport")
                .withIcon(R.drawable.ic_talksport).withTypeface(montserrat_regular);
        SectionDrawerItem item13 = new SectionDrawerItem().withIdentifier(13).withName("SCIENCE")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(14).withName("Medical News Today")
                .withIcon(R.drawable.ic_medicalnewstoday).withTypeface(montserrat_regular);
        PrimaryDrawerItem item15 = new PrimaryDrawerItem().withIdentifier(15).withName("National Geographic")
                .withIcon(R.drawable.ic_nationalgeographic).withTypeface(montserrat_regular);
        SectionDrawerItem item16 = new SectionDrawerItem().withIdentifier(16).withName("TECHNOLOGY")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item17 = new PrimaryDrawerItem().withIdentifier(17).withName("Crypto Coins News")
                .withIcon(R.drawable.ic_ccnnews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item18 = new PrimaryDrawerItem().withIdentifier(18).withName("Engadget")
                .withIcon(R.drawable.ic_engadget).withTypeface(montserrat_regular);
        PrimaryDrawerItem item19 = new PrimaryDrawerItem().withIdentifier(19).withName("The Next Web")
                .withIcon(R.drawable.ic_thenextweb).withTypeface(montserrat_regular);
        PrimaryDrawerItem item20 = new PrimaryDrawerItem().withIdentifier(20).withName("The Verge")
                .withIcon(R.drawable.ic_theverge).withTypeface(montserrat_regular);
        PrimaryDrawerItem item21 = new PrimaryDrawerItem().withIdentifier(21).withName("TechCrunch")
                .withIcon(R.drawable.ic_techcrunch).withTypeface(montserrat_regular);
        PrimaryDrawerItem item22 = new PrimaryDrawerItem().withIdentifier(22).withName("TechRadar")
                .withIcon(R.drawable.ic_techradar).withTypeface(montserrat_regular);
        SectionDrawerItem item23 = new SectionDrawerItem().withIdentifier(23).withName("GAMING")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item24 = new PrimaryDrawerItem().withIdentifier(24).withName("IGN")
                .withIcon(R.drawable.ic_ignnews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item25 = new PrimaryDrawerItem().withIdentifier(25).withName("Polygon")
                .withIcon(R.drawable.ic_polygonnews).withTypeface(montserrat_regular);
        SectionDrawerItem item26 = new SectionDrawerItem().withIdentifier(26).withName("MORE INFO")
                .withTypeface(montserrat_regular);
        SecondaryDrawerItem item27 = new SecondaryDrawerItem().withIdentifier(27).withName("About the app")
                .withIcon(R.drawable.ic_info).withTypeface(montserrat_regular);
        SecondaryDrawerItem item28 = new SecondaryDrawerItem().withIdentifier(28).withName("Open Source")
                .withIcon(R.drawable.ic_code).withTypeface(montserrat_regular);
        SecondaryDrawerItem item29 = new SecondaryDrawerItem().withIdentifier(29).withName("Powered by newsapi.org")
                .withIcon(R.drawable.ic_power).withTypeface(montserrat_regular);
        SecondaryDrawerItem item30 = new SecondaryDrawerItem().withIdentifier(30).withName("Contact us")
                .withIcon(R.drawable.ic_mail).withTypeface(montserrat_regular);

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.pk)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9,
                        item10, item11, item12, item13, item14, item15, item16, item17, item18, item19,
                        item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();
                        switch (selected) {
                            case Constants.GOOGLE_NEWS_INDIA:
                                SOURCE = SOURCE_ARRAY[0];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.BBC_NEWS:
                                SOURCE = SOURCE_ARRAY[1];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.THE_HINDU:
                                SOURCE = SOURCE_ARRAY[2];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.THE_TIMES_OF_INDIA:
                                SOURCE = SOURCE_ARRAY[3];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.BUZZFEED:
                                SOURCE = SOURCE_ARRAY[4];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.MASHABLE:
                                SOURCE = SOURCE_ARRAY[5];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.MTV_NEWS:
                                SOURCE = SOURCE_ARRAY[6];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.BBC_SPORT:
                                SOURCE = SOURCE_ARRAY[7];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.ESPN_CRIC_INFO:
                                SOURCE = SOURCE_ARRAY[8];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.TALK_SPORT:
                                SOURCE = SOURCE_ARRAY[9];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.MEDICAL_NEWS:
                                SOURCE = SOURCE_ARRAY[10];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 15:
                                SOURCE = SOURCE_ARRAY[11];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.CRYPTO_COINS_NEWS:
                                SOURCE = SOURCE_ARRAY[12];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.ENAGDET:
                                SOURCE = SOURCE_ARRAY[13];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.THE_NEXT_WEB:
                                SOURCE = SOURCE_ARRAY[14];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.THE_VERGE:
                                SOURCE = SOURCE_ARRAY[15];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.TECH_CRUNCH:
                                SOURCE = SOURCE_ARRAY[16];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.TECH_RADAR:
                                SOURCE = SOURCE_ARRAY[17];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.IGN:
                                SOURCE = SOURCE_ARRAY[18];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.POLYGON:
                                SOURCE = SOURCE_ARRAY[19];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case Constants.ABOUT_APP:
                                openAboutActivity();
                                break;
                            case Constants.OPENSOURCE:
                                Intent browserSource = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/debo1994/MyTimes"));
                                startActivity(browserSource);
                                break;
                            case Constants.POWERED_BY:
                                Intent browserAPI = new Intent(Intent.ACTION_VIEW, Uri.parse("https://newsapi.org/"));
                                startActivity(browserAPI);
                                break;
                            case Constants.CONTACT_US:
                                sendEmail();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }
    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: maneeshsagar97@gmail.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }


    public void getFromDB()
    {
        MainViewModel mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getList().observe(this, new Observer<List<ArticleStructure>>() {
            @Override
            public void onChanged(@Nullable List<ArticleStructure> slist) {
                Collections.reverse(slist);
                adapter = new DataAdapter(MainActivity.this,(ArrayList)slist);
                recyclerView.setAdapter(adapter);


            }
        });

    }
}
