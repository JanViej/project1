package com;
/**
 * Created by http://giasutinhoc.vn
 */
import android.app.Application;

public class WifiApp extends Application {
    static WifiApp wifiInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        wifiInstance = this;
    }
    public static synchronized WifiApp getInstance() {
        return wifiInstance;
    }
}
