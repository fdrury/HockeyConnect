package ca.hockeyconnect.hockeyconnect;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.0.160:5000/tryout/1";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray reader = new JSONArray(response);
                    for(int i = 0; i  < reader.length(); i++) {
                        JSONObject player = reader.getJSONObject(i);
                        String name = player.getString("FirstName") + " " + player.getString("LastName");
                        PlayerList.add(name);
                        listAdapter.notifyDataSetChanged();
                    }
                } catch(Exception e) {
                    // handle exception
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

        mRequestQueue.add(stringRequest);
    }
}
