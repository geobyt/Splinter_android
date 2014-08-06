package com.splinter.app.Service;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by geo on 8/5/14.
 */
public class WebServicePostTask extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Void doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);

        try {
            httppost.setEntity(new StringEntity(params[1]));
            httppost.addHeader("content-type", "application/x-www-form-urlencoded");

            Log.d("WebServicePostTask", "attempting to post message");

            HttpResponse response = httpclient.execute(httppost);

            Log.d("WebServicePostTask", "posting message succeeded");

        } catch (ClientProtocolException e) {
            Log.d("WebServicePostTask", e.getMessage());
        } catch (IOException e) {
            Log.d("WebServicePostTask", e.getMessage());
        } catch (Exception e) {
            Log.d("WebServicePostTask", e.getMessage());
        }

        return null;
    }
}
