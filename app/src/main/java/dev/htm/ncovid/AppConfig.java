package dev.htm.ncovid;

import android.app.Application;

import dev.htm.ncovid.util.NetworkHelper;

public class AppConfig extends Application {

    private void initialize() {
        // network helper
        NetworkHelper.setContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }
}