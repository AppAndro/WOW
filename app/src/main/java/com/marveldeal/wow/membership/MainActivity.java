package com.marveldeal.wow.membership;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.InterstitialAd;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.marveldeal.wow.R;
import com.marveldeal.wow.ScraperActivity;
import com.marveldeal.wow.VideoListActivity;
import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
/*
  Digits Is A Library Used FOr OTP Authentication. It Provides OTP Auth For Free.
 */
    SweetAlertDialog pDialog;
    LoginManager manager;
    InterstitialAd ad;
    private SessionListener sessionListener = new SessionListener() {
        @Override
        public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String sessionId) {
            if (success) {
                MonetizationManager.fetchAd(MainActivity.this, NativeXAdPlacement.Exit_Ad_From_Application, new OnAdEventV2() {
                    @Override
                    public void onEvent(AdEvent adEvent, AdInfo adInfo, String s) {

                    }
                });
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        MonetizationManager.createSession(getApplicationContext(), "105567", sessionListener);
    }

    @Override
    public void onBackPressed(){
        MonetizationManager.showReadyAd(this, NativeXAdPlacement.Exit_Ad_From_Application, null);
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new LoginManager(this);
        if(manager.getToken()!=null){
            proceed();
        }
        ad = new InterstitialAd(this);
        AdRegistration.setAppKey("0ce5d2febe1e4b4587c5e68a09fceade");
        AdRegistration.enableLogging(true);
        ad.setListener(new AdListener() {
            @Override
            public void onAdLoaded(Ad ad, AdProperties adProperties) {
                MainActivity.this.ad.showAd();
            }
            @Override
            public void onAdFailedToLoad(Ad ad, AdError adError) {}
            @Override
            public void onAdExpanded(Ad ad) {}
            @Override
            public void onAdCollapsed(Ad ad) {}
            @Override
            public void onAdDismissed(Ad ad) {}
        });
        ad.loadAd();
        setContentView(R.layout.activity_login);
        findViewById(R.id.scrape).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScraperActivity.class));
            }
        });
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                manager.register(session, new LoginManager.onRegistrationComplete() {
                    @Override
                    public void onPreStart() {
                        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Logging You In");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                    @Override
                    public void onSuccess(String message) {
                        pDialog.dismiss();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        proceed();
                    }
                });
            }
            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });

    }
    public void proceed(){
        finish();
        startActivity(new Intent(this, VideoListActivity.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
