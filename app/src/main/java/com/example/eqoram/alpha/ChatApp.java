package com.example.eqoram.alpha;

import android.app.Application;

/**
 * Created by Sven on 11/10/2016.
 */

    public class ChatApp extends Application {
        public static DatabaseHandler db;
        @Override
        public void onCreate() {
        super.onCreate();
        db = new DatabaseHandler(getApplicationContext());
        }
        public void onTerminate() {
        super.onTerminate();
        db.close();
        }
    }
