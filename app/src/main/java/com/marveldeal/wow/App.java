package com.marveldeal.wow;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class App extends Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "skT5BLMGnm3DexKftpGqmDnbQ";
    private static final String TWITTER_SECRET = "8oGtgKRn6UiuB83EZZAykbjXHTAieaNwHBxdpKFqri9Q1iv5pV";
    // I Used Crashlytics To Track Bugs And Crashes
    // Answers Is Used To Analyze The Apps. To Know The Number Of Active Users & New Users.
    // Digits is Used For OTP Authentication. As I Haven't Bought Any SMS Plan.
    @Override
    public void onCreate(){
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Answers(), new TwitterCore(authConfig), new Digits.Builder().build());
    }
}
