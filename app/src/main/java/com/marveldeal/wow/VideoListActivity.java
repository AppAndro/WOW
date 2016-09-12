package com.marveldeal.wow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.marveldeal.wow.membership.LoginManager;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import im.ene.lab.toro.Toro;
public class VideoListActivity extends AppCompatActivity {
    RecyclerView recycler;
    VideosAdapter adapter;
    SweetAlertDialog pDialog;
    LoginManager manager;
    WowAPI api;
    @Override protected void onDestroy() {
        Toro.detach(this);
        if(pDialog!=null&&pDialog.isShowing()){
            pDialog.dismiss();
        }
        pDialog = null;
        super.onDestroy();
    }
    @Override public void onResume() {
        super.onResume();
        Toro.register(recycler);
    }
    @Override public void onPause() {
        Toro.unregister(recycler);
        super.onPause();
    }
    @Override
    public void onStart(){
        super.onStart();
        startLoading();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toro.attach(this);
        setContentView(R.layout.activity_video_list);
        recycler = (RecyclerView)findViewById(R.id.recycler);
        api = new WowAPI();
        manager = new LoginManager(this);
        adapter = new VideosAdapter(this);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVideo();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(VideoListActivity.this, WowPlayerActivity.class);
                i.putExtra("url", adapter.getDataAt(position).video_URL);
                startActivity(i);
            }
        });
    }
    public void startLoading(){
        pDialog = new SweetAlertDialog(VideoListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading Videos");
        pDialog.setCancelable(false);
        pDialog.show();
        api.loadVideos(manager.getToken(), new WowAPI.onVideosLoaded() {
            @Override
            public void onVideoLoaded(List<WowAPI.VideoUploadModel> list) {
                pDialog.dismiss();
                if(list!=null) {
                    if (list.size() > 0) {
                        adapter.set(list);
                    } else {
                        setNoVideos();
                    }
                }
                else {
                    pDialog.setTitleText("There Is An Unknown Error");
                    pDialog.setConfirmText("Ok");
                    pDialog.setContentText("There Is An Unknown Error.");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    });
                    pDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                }
            }
        });
    }
    public void setNoVideos(){
        recycler.setVisibility(View.GONE);
        findViewById(R.id.no_app).setVisibility(View.VISIBLE);
        findViewById(R.id.add_app).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     addVideo();
                    }
                }
        );
    }
    public void addVideo(){
        startActivity(new Intent(VideoListActivity.this, UploadVideo.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.scrape_action){
            startActivity(new Intent(VideoListActivity.this, ScraperActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
