package ca.hockeyconnect.hockeyconnect;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ArrayAdapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlayerListActivity extends ListActivity {

    ArrayList<String> PlayerList = new ArrayList<String>();

    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        listAdapter = new ArrayAdapter<String>(this, R.layout.list_item_player, PlayerList);
        setListAdapter(listAdapter);
        //ListView listView = (ListView) findViewById(R.id.listView1);
        //listView.setAdapter(listAdapter);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL hcServer = new URL("192.168.0.160/tryout/1");
                    HttpURLConnection serverConnection = (HttpURLConnection) hcServer.openConnection();
                    serverConnection.setRequestMethod("GET");
                    InputStream responseBody = serverConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if(key.equals("FirstName")) {
                            PlayerList.add(jsonReader.nextString());
                            listAdapter.notifyDataSetChanged();
                        } else {
                            jsonReader.skipValue();
                        }
                    }

                    jsonReader.close();
                    serverConnection.disconnect();
                } catch(Exception e) {
                    // do nothing
                }
            }
        });
    }
}
