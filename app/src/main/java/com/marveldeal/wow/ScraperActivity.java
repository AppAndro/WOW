package com.marveldeal.wow;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ScraperActivity extends AppCompatActivity {
    TextView txt;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraper);
        txt = (TextView)findViewById(R.id.tv);
        txt.setMovementMethod(new ScrollingMovementMethod());
        new ScraperTask().execute();
    }
    public class ScraperTask extends AsyncTask<Void, Void, String>{
        @Override
        public void onPreExecute(){
            pDialog = new SweetAlertDialog(ScraperActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Scraping.... Data");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        public String doInBackground(Void... pa){
            try {
                JSONObject obj = new JSONObject();
                JSONArray arr = new JSONArray();
                Document doc = Jsoup.connect("http://www.wowsport.io/").get();
                Elements section1 = doc.select(".section-1");
                for(int i = 0;i<section1.size();i+=1){
                    JSONObject feature = new JSONObject();
                    Element el = section1.get(i);
                    Element text =  el.getElementsByClass("text-container").get(0);
                    String img = el.getElementsByTag("img").get(0).attr("src");
                    String title = text.getElementsByTag("h4").text();
                    String sub =  text.getElementsByClass("content").get(0).text();
                    feature.put("url", img);
                    feature.put("title", title);
                    feature.put("subtitle", sub);
                    arr.put(feature);
                }
                obj.put("data",arr);
                return obj.toString();
            } catch (Exception e){
                Log.e("MarvelAds", "err", e);
            }
            return null;
        }
        @Override
        public void onPostExecute(String json){
            if(pDialog!=null&&pDialog.isShowing()){pDialog.dismiss();}
           txt.setText(json);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scraper, menu);
        return true;
    }

}
