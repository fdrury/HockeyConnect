package ca.hockeyconnect.hockeyconnect;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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

        PlayerList.add("test1");
        listAdapter.notifyDataSetChanged();

        //RequestQueue mRequestQueue;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        /*// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();*/

        String url = "http://192.168.0.160:5000/tryout/1";

        Log.d("start", "starting test");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                PlayerList.add(response.toString());
                listAdapter.notifyDataSetChanged();
                Log.d("response", "response received");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        mRequestQueue.add(jsonObjectRequest);

        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
            try {
                URL hcServer = new URL("192.168.0.160/tryout/1");
                HttpURLConnection serverConnection = (HttpURLConnection) hcServer.openConnection();
                serverConnection.setRequestMethod("GET");
                InputStream responseBody = serverConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                PlayerList.add("test2");
                listAdapter.notifyDataSetChanged();

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
        });*/
    }
}
