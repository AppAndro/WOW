package com.marveldeal.wow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexbbb.uploadservice.AbstractUploadServiceReceiver;
import com.alexbbb.uploadservice.ContentType;
import com.alexbbb.uploadservice.UploadRequest;
import com.alexbbb.uploadservice.UploadService;
import com.marveldeal.wow.membership.LoginManager;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UploadVideo extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText tags;
    ImageView thumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);
        thumb = (ImageView)findViewById(R.id.thumb);
        tags = (EditText)findViewById(R.id.tags);
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titl = title.getText().toString();
                String desc = description.getText().toString();
                String tg = tags.getText().toString();
                if (titl.isEmpty()) {
                    Toast.makeText(UploadVideo.this, "Title Can't Be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tg.isEmpty()) {
                    Toast.makeText(UploadVideo.this, "Tags Can't Be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (desc.isEmpty()) {
                    Toast.makeText(UploadVideo.this, "description Cant be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                upload(UploadVideo.this, titl, desc, tg);
            }
        });
        findViewById(R.id.chooseVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideo();
            }
        });
    }
    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video"), 100);
    }
    public void setVideoThumbnail(){
        new AsyncTask<String, Void, Bitmap>(){
            @Override
            public Bitmap doInBackground(String... t){
                Bitmap b = ThumbnailUtils.createVideoThumbnail(t[0], MediaStore.Video.Thumbnails.MICRO_KIND);
                return Utils.scaleBitmap(b, 150, 150);
            }
            @Override
            public void onPostExecute(Bitmap b){
                thumb.setVisibility(View.VISIBLE);
                thumb.setImageBitmap(b);
            }
        }.execute(videoPath);
    }
    public void upload(final Context context, final String title, final String description, final String tags) {
        final UploadRequest request = new UploadRequest(context, "upload-1", "http://wowapp.marveldeal.com/api/v1/upload");
        request.addFileToUpload(videoPath,
                "file",
                "uploadedfile.mp4",
                ContentType.VIDEO_MPEG);
        request.addParameter("title", title);
        request.addParameter("desc", description);
        request.addParameter("tags", tags);
        request.addParameter("token", new LoginManager(context).getToken());
        request.setNotificationConfig(
                android.R.drawable.ic_menu_upload, //Notification icon. You can use your own app's R.drawable.your_resource
                "Uploading File", //You can use your string resource with: context.getString(R.string.your_string)
                "Uploading...",
                "Upload Completed",
                "Upload Failed",
                false);
        try {
            UploadService.startUpload(request);
            showDialog();
        } catch (Exception exc) {}
    }
    @Override
    public void onDestroy(){
        if(pDialog!=null&&pDialog.isShowing()){
            pDialog.dismiss();
        }
        pDialog = null;
        super.onDestroy();
    }
    String videoPath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
            try {
                videoPath = FilePathUtils.getPath(this, data.getData());
            }catch (Exception e){Log.e("Err", "er", e);}
                if(videoPath!=null&&!videoPath.isEmpty()){
                    setVideoThumbnail();
                }
                else{
                  Toast.makeText(UploadVideo.this, "It Seems You Have Selected An InAccessible Or Invalid Video", Toast.LENGTH_LONG).show();
                }
        }
    }
    SweetAlertDialog pDialog;
    public void showDialog(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.getProgressHelper().setInstantProgress(0);
        pDialog.setTitleText("Uploading Video...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private  final BroadcastReceiver uploadReceiver = new AbstractUploadServiceReceiver() {
        public void onProgress(final String uploadId, final int progress) {
            if(pDialog!=null){
                Log.d("WOW", "Pr"+progress);
                pDialog.getProgressHelper().setInstantProgress(progress);
            }
        }
        public void onError(final String uploadId, final Exception exception) {
            if(pDialog!=null&&pDialog.isShowing()){
                pDialog.dismiss();
                Toast.makeText(UploadVideo.this, "Upload Failure", Toast.LENGTH_LONG).show();
            }
        }
        public void onCompleted(final String uploadId, final int serverResponseCode, final String serverResponseMessage) {
            if(pDialog!=null&&pDialog.isShowing()){
                pDialog.dismiss();
                finish();
                startActivity(new Intent(UploadVideo.this, VideoListActivity.class));
                Toast.makeText(UploadVideo.this, "Upload Complete", Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadService.getActionBroadcast());
        registerReceiver(uploadReceiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(uploadReceiver);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload_video, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
