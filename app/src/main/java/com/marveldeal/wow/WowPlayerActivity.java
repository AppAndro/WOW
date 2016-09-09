package com.marveldeal.wow;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

public class WowPlayerActivity extends AppCompatActivity implements EasyVideoCallback {
    private EasyVideoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wowplayer_activity);
        String url = getIntent().getStringExtra("url");
        if(url==null||url.isEmpty()){
            Toast.makeText(this, "No Video To Play", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        player = (EasyVideoPlayer) findViewById(R.id.player);
        player.setCallback(this);
        player.setSource(Uri.parse(url));
    }
    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }
    @Override
    public void onPreparing(EasyVideoPlayer player) {
        // TODO handle if needed
    }
    @Override
    public void onPrepared(EasyVideoPlayer player) {
        // TODO handle
    }

    @Override
    public void onBuffering(int percent) {
        // TODO handle if needed
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        // TODO handle
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed
    }
}