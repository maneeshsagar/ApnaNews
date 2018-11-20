package com.example.maneeshsagar.apnanews;

public class Constants {

    //Old api version
    public static final String BASE_URL_OLD = "https://newsapi.org/v1/";

    //New api version
    public static final String BASE_URL = "https://newsapi.org/v2/";

    //API Key
    public static final String API_KEY = BuildConfig.NEWS_API_KEY;

    //Used to save the instance of the recyclerView
    public static final String RECYCLER_STATE_KEY = "recycler_list_state";

    //Used to save the instance of the title of Toolbar.
    public static final String TITLE_STATE_KEY = "title_state";

    //Used to save the instance of the selected source.
    public static final String SOURCE = "source";

    //Used to save the instance of the title of Toolbar.
    public static final String TITLE_WEBVIEW_KEY = "save_text_webView";

    //Used by intents
    public static final String INTENT_URL = "url";

    public static final String INTENT_HEADLINE = "key_HeadLine";
    public static final String INTENT_DESCRIPTION = "key_description";
    public static final String INTENT_DATE = "key_date";
    public static final String INTENT_IMG_URL = "key_imgURL";
    public static final String INTENT_ARTICLE_URL = "key_URL";

    public static final int GOOGLE_NEWS_INDIA=1;
    public static final int BBC_NEWS=2;
    public static final int THE_HINDU=3;
    public static final int THE_TIMES_OF_INDIA=4;
    public static final int ENTERTAINMENT=5;
    public static final int BUZZFEED=6;
    public static final int MASHABLE=7;
    public static final int MTV_NEWS=8;
    public static final int SPORT=9;
    public static final int BBC_SPORT=10;
    public static final int ESPN_CRIC_INFO=11;
    public static final int TALK_SPORT=12;
    public static final int SCIENCE=13;
    public static final int MEDICAL_NEWS=14;
    public static final int NATIONAL_GEOGRAPHIC=15;
    public static final int TECHNOLOGY=16;
    public static final int CRYPTO_COINS_NEWS=17;
    public static final int ENAGDET=18;
    public static final int THE_NEXT_WEB=19;
    public static final int THE_VERGE=20;
    public static final int TECH_CRUNCH=21;
    public static final int TECH_RADAR=22;
    public static final int GAMING=23;
    public static final int IGN=24;
    public static final int POLYGON=25;
    public static final int MORE_INFO=26;
    public static final int ABOUT_APP=27;
    public static final int OPENSOURCE=28;
    public static final int POWERED_BY=29;
    public static final int CONTACT_US=30;

    public static final String TRANSITION = "transition";
}
