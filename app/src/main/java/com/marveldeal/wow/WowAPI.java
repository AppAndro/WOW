package com.marveldeal.wow;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilson mani on 08/09/2016.
 */
public class WowAPI {
    public class VideoUploadModel{
        String datetime;
        String title;
        String description;
        String tags;
        String video_URL;
        public VideoUploadModel(String datetime, String title, String description, String tags, String video_URL){
            this.datetime = datetime;
            this.title = title;
            this.description = description;
            this.tags = tags;
            this.video_URL = video_URL;
        }
    }
    public interface onVideosLoaded{
         void onVideoLoaded(List<VideoUploadModel> list);
    }
    onVideosLoaded listener;
    String token;
    public void loadVideos(String token, onVideosLoaded listener){
        this.listener = listener;
        this.token = token;
        new LoadVideos().execute();
    }
    class LoadVideos extends AsyncTask<Void, Void, List<VideoUploadModel>>{
        @Override
        public List<VideoUploadModel> doInBackground(Void... p){
            try {
                URL url = new URL("http://wowapp.marveldeal.com/api/v1/getVideos");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "*/*");
                conn.setRequestProperty("Host", "wowapp.marveldeal.com");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
                String param = "token="+ URLEncoder.encode(token, "UTF-8");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(param);
                writer.flush();
                writer.close();
                os.close();
                JSONArray obj = new JSONObject(Utils.convertStreamToString(conn.getInputStream())).getJSONArray("videos");
                List<VideoUploadModel> list = new ArrayList<VideoUploadModel>();
                for(int i =0;i<obj.length();i+=1){
                    JSONObject object = obj.getJSONObject(i);
                    VideoUploadModel model = new VideoUploadModel(object.getString("datetime"), object.getString("title"), object.getString("description"), object.getString("tags"), object.getString("videoURL"));
                    list.add(model);
                }
                return list;
            } catch (Exception e) {
                Log.e("WOW", "err", e);
            }
            return null;
        }
        @Override
        public void onPostExecute(List<VideoUploadModel> list){
            if(listener!=null) {
                listener.onVideoLoaded(list);
            }
        }
    }
}
