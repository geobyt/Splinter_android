package com.splinter.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.splinter.app.Database.Message;
import com.splinter.app.Service.JsonParser;
import com.splinter.app.Service.WebServiceListener;
import com.splinter.app.Service.WebServicePostTask;
import com.splinter.app.Service.WebServiceTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MessagesActivity extends ListActivity implements WebServiceListener, AddMessage.AddMessageListener {

    String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebServiceTask webServiceTask = new WebServiceTask();
        webServiceTask.delegate = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            locationId = extras.getString("LOCATION_ID");
            webServiceTask.execute("http://lyraserver.azurewebsites.net/message/" + locationId);
        }
        else
        {
            webServiceTask.execute("http://lyraserver.azurewebsites.net/message/2");
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    public void responsePost(String result){
        WebServiceTask webServiceTask = new WebServiceTask();
        webServiceTask.delegate = this;
        webServiceTask.execute("http://lyraserver.azurewebsites.net/message/" + locationId);
    }

    public void responseGet(String result) {
        //TODO: actually update database here
        if (!result.isEmpty()) {
            List<Message> messages = JsonParser.ParseMessagesJson(result);

            List<String> values = new ArrayList<String>();

            for (Message m : messages){
                values.add(m.getDescription());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.rowmessage, R.id.label, values);
            setListAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_addmessage) {
            showAddMessageDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAddMessageDialog() {
        DialogFragment dialog = new AddMessage();
        dialog.show(this.getFragmentManager(), "AddMessage");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        WebServicePostTask webServicePostTask = new WebServicePostTask();
        webServicePostTask.delegate = this;

        String message = ((AddMessage)dialog).messageText.getText().toString();
        JSONObject obj = new JSONObject();
        obj.put("text", message);
        webServicePostTask.execute("http://lyraserver.azurewebsites.net/message/" + locationId, obj.toJSONString());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

}
