package com.marveldeal.wow;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class DataConverter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_converter);
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("tests.txt")));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                write(nextLine);
            }
        }
        catch (Exception e){
            Log.e("WOW", "err", e);
        }
    }
    public void write(String[] data){
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/UnitTests");
            if (!dir.exists()) {
                dir.mkdir();
            }
            String code = "Feature: "+data[1]+"\n\n"+"Scenario: "+data[2]+"\n"+data[3];
            String name =  data[1].replaceAll("[^A-Za-z0-9]", "")+ new Random().nextInt(20) + ".feature";
            File file = new File(dir,  name);
            FileOutputStream os = new FileOutputStream(file);
            os.write(code.getBytes());
            os.close();
        }catch (Exception e){Log.e("WOW", "err", e);}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_converter, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
