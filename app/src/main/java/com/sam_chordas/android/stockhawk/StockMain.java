package com.sam_chordas.android.stockhawk;

import android.app.Application;
import android.content.Context;

import de.greenrobot.event.EventBus;

/**
 * Application class for the StockHawk
 * to get context from non-activity classes
 */

public class StockMain extends Application
{
    private static StockMain instance;

    public static StockMain getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate()
    {
        EventBus myEventBus = EventBus.getDefault();
        instance = this;
        super.onCreate();
    }
}
