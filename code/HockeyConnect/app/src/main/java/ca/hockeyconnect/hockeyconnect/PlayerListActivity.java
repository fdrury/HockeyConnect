package ca.hockeyconnect.hockeyconnect;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayerListActivity extends ListActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Player> PlayerList = new ArrayList<Player>();
    ArrayAdapter<Player> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        String tryoutID = getIntent().getStringExtra("TRYOUT_ID");

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        listAdapter = new ArrayAdapter<Player>(this, R.layout.list_item_player, PlayerList);
        setListAdapter(listAdapter);

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        String url = String.format("%s/tryout/%s", getString(R.string.server_url), tryoutID);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // restart activity
                        finish();
                        startActivity(getIntent());
                    }
                }
        );

        StringRequest stringRequest = new StringRequest (Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray reader = new JSONArray(response);
                            for(int i = 0; i  < reader.length(); i++) {
                                JSONObject player = reader.getJSONObject(i);
                                PlayerList.add(new Player(player.getString("FirstName"), player.getString("LastName"), player.getInt("ID")));
                            }
                            Collections.sort(PlayerList, new Comparator<Player>() {
                                @Override
                                public int compare(Player o1, Player o2) {
                                    return o1.getLastName().compareTo(o2.getLastName());
                                }
                            });
                            listAdapter.notifyDataSetChanged();
                        } catch(Exception e) {
                            // handle exception
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }
        );

        mRequestQueue.add(stringRequest);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Class nextClass;
        if(getIntent().getStringExtra("EVALUATION_TYPE").equals("GAME")) {
            nextClass = PlayerEvaluationActivity.class;
        } else if(getIntent().getStringExtra("EVALUATION_TYPE").equals("TIMED")) {
            nextClass = TimerMenuActivity.class;
        } else if(getIntent().getStringExtra("EVALUATION_TYPE").equals("JERSEY")) {
            nextClass = JerseySelectionActivity.class;
        } else {
            return;
        }
        Intent intent = new Intent(PlayerListActivity.this, nextClass);
        intent.putExtra("PLAYER", PlayerList.get(position));
        intent.putExtra("EVALUATOR_ID", getIntent().getStringExtra("EVALUATOR_ID"));
        intent.putExtra("TRYOUT_ID", getIntent().getStringExtra("TRYOUT_ID"));
        startActivity(intent);
    }
}
