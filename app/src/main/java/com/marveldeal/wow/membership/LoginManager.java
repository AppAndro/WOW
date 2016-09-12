package com.marveldeal.wow.membership;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.DigitsSession;
import com.marveldeal.wow.Utils;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginManager {
    SharedPreferences prefs;
    Context c;
    public LoginManager(Context c){
        prefs = PreferenceManager.getDefaultSharedPreferences(c);
        this.c = c;
    }
     /*
     I Could have Used AsyncHTTCLient Library But I Have Done It Manually To Show My More Skills.
     */
    public void register(DigitsSession session, onRegistrationComplete listener){
        new RegisterPhone(listener, session).execute();
    }
    public interface onRegistrationComplete {
        void onPreStart();
        void onSuccess(String message);
    }
    public class RegisterPhone extends AsyncTask<String, Void, RegisterPhone.RegisterModel> {
        onRegistrationComplete listener;
        DigitsSession session;
        public RegisterPhone(onRegistrationComplete listener, DigitsSession session){this.listener = listener;this.session=session;}
        @Override
        public void onPreExecute(){
            if(listener!=null){listener.onPreStart();}
        }
        class RegisterModel{
            public String token;
            public String msg;
        }
        @Override
        public RegisterModel doInBackground(String... params){
            try {
                URL url = new URL("http://wowapp.marveldeal.com/api/v1/auth");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "*/*");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
                String param = "email=" + URLEncoder.encode(session.getEmail().address, "UTF-8")+"&token="+ URLEncoder.encode(session.getAuthToken().token, "UTF-8")+"&phone="+ URLEncoder.encode(session.getPhoneNumber(), "UTF-8");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(param);
                writer.flush();
                writer.close();
                os.close();
                JSONObject obj = new JSONObject(Utils.convertStreamToString(conn.getInputStream()));
                RegisterModel model = new RegisterModel();
                model.msg = obj.getString("status");
                model.token = obj.getString("token");
                return model;
            } catch (Exception e) {
                Log.e("MarvelApp", "err", e);
                Crashlytics.logException(e);
            }
            return null;
        }
        @Override
        public void onPostExecute(RegisterModel res){
            if(res!=null) {
                putToken(res.token);
            }
            if(listener!=null) {
                listener.onSuccess(res!=null && res.msg!=null?res.msg:"There Was An Error");
            }
        }
    }
    public String getToken(){
        return prefs.getString("token", null);
    }
    public void putToken(String token){
        prefs.edit().putString("token", token).apply();
    }

}
