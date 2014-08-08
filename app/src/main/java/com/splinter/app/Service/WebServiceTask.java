package com.splinter.app.Service;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by geo on 7/22/14.
 */
public class WebServiceTask extends AsyncTask<String, Void, String> {
    public WebServiceListener delegate = null;

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;

        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
            Log.w("WebServiceTask", e.getMessage());
        } catch (IOException e) {
            //TODO Handle problems..
            Log.w("WebServiceTask", e.getMessage());
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.responseGet(result);
    }
}
