package com.splinter.app.Service;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by geo on 8/5/14.
 */
public class WebServicePostTask extends AsyncTask<String, Void, String> {
    public WebServiceListener delegate = null;

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        String responseString = "";

        try {
            /*
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addTextBody("text", params[1], ContentType.APPLICATION_FORM_URLENCODED);

            httppost.setEntity(entity.build());
            */

            httppost.setEntity(new StringEntity(params[1]));
            httppost.addHeader("content-type", "application/json");

            Log.d("WebServicePostTask", "attempting to post message");

            HttpResponse response = httpclient.execute(httppost);
            responseString = String.valueOf(response.getStatusLine().getStatusCode());
            Log.d("WebServicePostTask", "posting message succeeded");

        } catch (ClientProtocolException e) {
            Log.d("WebServicePostTask", e.getMessage());
        } catch (IOException e) {
            Log.d("WebServicePostTask", e.getMessage());
        } catch (Exception e) {
            Log.d("WebServicePostTask", e.getMessage());
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.responsePost(result);
    }
}
