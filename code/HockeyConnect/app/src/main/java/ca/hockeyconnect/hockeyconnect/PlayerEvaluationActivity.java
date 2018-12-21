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

    ArrayList<SeekBar> seekBars = new ArrayList<SeekBar>();
    ArrayList<ImageButton> helpButtons = new ArrayList<ImageButton>();
    Button saveButton;
    Button cancelButton;
    ArrayList<String> helpStrings = new ArrayList<String>();
    ArrayList<Integer> attributeValues = new ArrayList<Integer>();

    ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
    ArrayAdapter<Attribute> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluation);

        listAdapter = new ArrayAdapter<Attribute>(this, R.layout.list_item_attribute_slider, attributeList);
        setListAdapter(listAdapter);

        final Player thisPlayer = (Player)getIntent().getSerializableExtra("PLAYER");
        final String evaluatorID = getIntent().getStringExtra("EVALUATOR_ID");
        final String tryoutID = getIntent().getStringExtra("TRYOUT_ID");

        // TODO: this is copied from below - needs to be customized accordingly.
        final RequestQueue mRequestQueue0 = Volley.newRequestQueue(this);
        String url0 = String.format("http://192.168.0.160:5000/getEvalCrit/%s", tryoutID);
        StringRequest stringRequest = new StringRequest (Request.Method.GET, url0,
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
        mRequestQueue0.add(stringRequest);

        seekBars[0] = (SeekBar)findViewById(R.id.seekBar0);
        seekBars[1] = (SeekBar)findViewById(R.id.seekBar1);
        seekBars[2] = (SeekBar)findViewById(R.id.seekBar2);
        seekBars[3] = (SeekBar)findViewById(R.id.seekBar3);
        seekBars[4] = (SeekBar)findViewById(R.id.seekBar4);

        saveButton = (Button)findViewById(R.id.buttonSave);
        cancelButton = (Button)findViewById(R.id.buttonCancel);

        helpButtons[0] = (ImageButton)findViewById(R.id.helpButton0);
        helpButtons[1] = (ImageButton)findViewById(R.id.helpButton1);
        helpButtons[2] = (ImageButton)findViewById(R.id.helpButton2);
        helpButtons[3] = (ImageButton)findViewById(R.id.helpButton3);
        helpButtons[4] = (ImageButton)findViewById(R.id.helpButton4);
        helpStrings[0] = "Speed/Explosiveness: How fast a player skates relative to other players.";
        helpStrings[1] = "Hockey Awareness/Instincts/Sense: Reads and reacts/anticipates where the puck is going. A player's ability to assess plays and predict movements.";
        helpStrings[2] = "Compete Level: Work ethic, attacking the puck, strong with the puck, battles with other players.";
        helpStrings[3] = "Puck Handling: Stick work, stick handling, passing, seeing the ice with the puck.";
        helpStrings[4] = "Agility: Pivoting, backwards to forwards and vice-versa, ability to stay on feet in crowded areas.";

        TextView playerNameTextView = (TextView)findViewById(R.id.textViewPlayerName);
        TextView playerNumberTextView = (TextView)findViewById(R.id.textViewPlayerNumber);
        TextView attribute0TextView = (TextView)findViewById(R.id.textViewAttributeName0);
        TextView attribute1TextView = (TextView)findViewById(R.id.textViewAttributeName1);
        TextView attribute2TextView = (TextView)findViewById(R.id.textViewAttributeName2);
        TextView attribute3TextView = (TextView)findViewById(R.id.textViewAttributeName3);
        TextView attribute4TextView = (TextView)findViewById(R.id.textViewAttributeName4);

        final TextView[] attributeValueViews = {(TextView)findViewById(R.id.textViewAttribute0),
                (TextView)findViewById(R.id.textViewAttribute1),
                (TextView)findViewById(R.id.textViewAttribute2),
                (TextView)findViewById(R.id.textViewAttribute3),
                (TextView)findViewById(R.id.textViewAttribute4)};

        // TODO: name & number
        playerNameTextView.setText(thisPlayer.getFullName());
        //playerNumberTextView.setText(R.string.test_player_number_name);
        playerNumberTextView.setText("");
        attribute0TextView.setText(R.string.attribute0_name);
        attribute1TextView.setText(R.string.attribute1_name);
        attribute2TextView.setText(R.string.attribute2_name);
        attribute3TextView.setText(R.string.attribute3_name);
        attribute4TextView.setText(R.string.attribute4_name);

        final RequestQueue mRequestQueue1 = Volley.newRequestQueue(this);
        String url1 = String.format("http://192.168.0.160:5000/getGameEval/%s/%d", tryoutID, thisPlayer.getID());
        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest (Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO: populate sliders
                        try {
                            seekBars[0].setProgress(response.getInt("Speed"));
                            seekBars[1].setProgress(response.getInt("HockeyAwareness"));
                            seekBars[2].setProgress(response.getInt("CompeteLevel"));
                            seekBars[3].setProgress(response.getInt("PuckHandling"));
                            seekBars[4].setProgress(response.getInt("Agility"));
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
        mRequestQueue1.add(jsonObjectRequest1);

        for(int i = 0; i < 5; i++) {
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
        }

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
                String url2 = "http://192.168.0.160:5000/postGameEval";
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("playerID", Integer.toString(thisPlayer.getID()));
                params2.put("evaluatorID", evaluatorID);
                params2.put("tryoutID", tryoutID);
                // TODO: ignore zero values
                params2.put("speed", Integer.toString(attributeValues[0]));
                params2.put("hockeyAwareness", Integer.toString(attributeValues[1]));
                params2.put("competeLevel", Integer.toString(attributeValues[2]));
                params2.put("puckHandling", Integer.toString(attributeValues[3]));
                params2.put("agility", Integer.toString(attributeValues[4]));
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
