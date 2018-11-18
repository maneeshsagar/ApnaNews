package com.example.maneeshsagar.apnanews.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.maneeshsagar.apnanews.model.ArticleStructure;


/**
 * Created by maneeshsagar on 27-07-2018.
 */
@Database(entities ={ ArticleStructure.class},version = 1,exportSchema = false)
public  abstract class AppDatabase extends RoomDatabase {

    public static final  Object lock=new Object();
    public static AppDatabase sInstance;
    public static final String DATABASE_NAME="article";


    public  static AppDatabase getsInstance(Context context)
    {
        if(sInstance==null)
        {
            synchronized (lock){

                sInstance= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();

            }
        }
        return sInstance;
    }
    public abstract QueryDao queryDao();
}
