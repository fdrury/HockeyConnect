package ca.hockeyconnect.hockeyconnect;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlayerEvaluationActivity extends ListActivity {

    //ArrayList<SeekBar> seekBars = new ArrayList<SeekBar>();
    //ArrayList<ImageButton> helpButtons = new ArrayList<ImageButton>();
    Button saveButton;
    Button cancelButton;
    //ArrayList<String> helpStrings = new ArrayList<String>();
    //ArrayList<Integer> attributeValues = new ArrayList<Integer>();

    ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
    AttributeAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluation);

        listAdapter = new AttributeAdapter(this, attributeList);
        setListAdapter(listAdapter);

        final Player thisPlayer = (Player)getIntent().getSerializableExtra("PLAYER");
        final String evaluatorID = getIntent().getStringExtra("EVALUATOR_ID");
        final String tryoutID = getIntent().getStringExtra("TRYOUT_ID");

        // setup/declared first but is added to queue on result of following request.
        // TODO: getGameEval SHOULD get the gameEval for the current evaluator.
        String url1 = String.format("%s/getGameEval/%s/%s/%d", getString(R.string.server_url), tryoutID, evaluatorID, thisPlayer.getID());
        final StringRequest stringRequest2 = new StringRequest (Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO: populate sliders
                        try {
                            JSONArray reader = new JSONArray(response);
                            for(int i = 0; i  < reader.length(); i++) {
                                JSONObject attribute = reader.getJSONObject(i);
                                // TODO: this assumes indexing/ordering is correct - is this a safe assumption?
                                attributeList.get(i).setValue(Integer.valueOf(attribute.getString("Value")));
                            }
                            listAdapter.notifyDataSetChanged();
                        } catch(Exception e) {
                            // TODO: catch exception
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: does anything need to be done here?
                    }
                }
        );

        // TODO: only use one RequestQueue
        final RequestQueue mRequestQueue0 = Volley.newRequestQueue(this);
        String url0 = String.format("%s/getEvalCrit/%s", getString(R.string.server_url), tryoutID);
        StringRequest stringRequest1 = new StringRequest (Request.Method.GET, url0,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray reader = new JSONArray(response);
                            for(int i = 0; i  < reader.length(); i++) {
                                JSONObject attribute = reader.getJSONObject(i);
                                attributeList.add(new Attribute(attribute.getString("Name"), attribute.getString("Description"), attribute.getInt("ID")));
                            }
                            listAdapter.notifyDataSetChanged();
                            mRequestQueue0.add(stringRequest2);
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
        mRequestQueue0.add(stringRequest1);

        saveButton = (Button)findViewById(R.id.buttonSave);
        cancelButton = (Button)findViewById(R.id.buttonCancel);

        TextView playerNameTextView = (TextView)findViewById(R.id.textViewPlayerName);
        TextView playerNumberTextView = (TextView)findViewById(R.id.textViewPlayerNumber);

        // TODO: name & number (or just jersey info?)
        playerNameTextView.setText(thisPlayer.getFullName());
        //playerNumberTextView.setText(R.string.test_player_number_name);
        playerNumberTextView.setText("");

        /*for(int i = 0; i < 5; i++) {
            final TextView attributeValueView = attributeValueViews[i];
            final int index = i;
            seekBars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    attributeValues[index] = progress;
                    if (progress == 0)
                        attributeValueView.setText(R.string.not_rated_name);
                    else
                        attributeValueView.setText(Integer.toString(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            helpButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), helpStrings[index], Toast.LENGTH_LONG).show();
                }
            });
        }*/

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Context currentContext = this;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue mRequestQueue2 = Volley.newRequestQueue(currentContext);
                String url2 = String.format("%s/postGameEval", getString(R.string.server_url));
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("playerID", Integer.toString(thisPlayer.getID()));
                params2.put("evaluatorID", evaluatorID);
                params2.put("tryoutID", tryoutID);

                // TODO: add params
                for(int i = 0; i < attributeList.size(); i++) {
                    //params2.put(String.valueOf(attributeList.get(i).getID()), String.valueOf(attributeList.get(i).getValue()));
                    params2.put(String.valueOf(attributeList.get(i).getID()), String.valueOf(attributeList.get(i).getValue()));
                }

                /*params2.put("speed", Integer.toString(attributeValues[0]));
                params2.put("hockeyAwareness", Integer.toString(attributeValues[1]));
                params2.put("competeLevel", Integer.toString(attributeValues[2]));
                params2.put("puckHandling", Integer.toString(attributeValues[3]));
                params2.put("agility", Integer.toString(attributeValues[4]));*/
                JSONObject jsonObject2 = new JSONObject(params2);

                final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest (Request.Method.POST, url2, jsonObject2,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Saving Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                );

                mRequestQueue2.add(jsonObjectRequest2);
            }
        });
    }


}
