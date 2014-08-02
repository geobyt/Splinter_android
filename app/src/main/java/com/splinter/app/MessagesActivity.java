package com.splinter.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.splinter.app.Database.Message;
import com.splinter.app.Service.JsonParser;
import com.splinter.app.Service.WebServiceListener;
import com.splinter.app.Service.WebServiceTask;

import java.util.ArrayList;
import java.util.List;


public class MessagesActivity extends ListActivity implements WebServiceListener {

    WebServiceTask webServiceTask = new WebServiceTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceTask.delegate = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String locationId = extras.getString("LOCATION_ID");
            this.webServiceTask.execute("http://lyraserver.azurewebsites.net/message/" + locationId);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    public void response(String result) {
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
}
