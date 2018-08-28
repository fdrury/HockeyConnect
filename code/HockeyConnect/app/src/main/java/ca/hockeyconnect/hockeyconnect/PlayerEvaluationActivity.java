package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlayerEvaluationActivity extends AppCompatActivity {

    SeekBar[] seekBars = new SeekBar[5];
    ImageButton[] helpButtons = new ImageButton[5];
    Button saveButton;
    Button cancelButton;
    String[] helpStrings = new String[5];
    int[] attributeValues = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluation);

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
        playerNameTextView.setText(R.string.test_player_name_name);
        playerNumberTextView.setText(R.string.test_player_number_name);
        attribute0TextView.setText(R.string.attribute0_name);
        attribute1TextView.setText(R.string.attribute1_name);
        attribute2TextView.setText(R.string.attribute2_name);
        attribute3TextView.setText(R.string.attribute3_name);
        attribute4TextView.setText(R.string.attribute4_name);

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
                final RequestQueue mRequestQueue = Volley.newRequestQueue(currentContext);
                String url = "http://192.168.0.160:5000/gameEval";
                Map<String, String> params = new HashMap<String, String>();
                Player thisPlayer = (Player)getIntent().getSerializableExtra("PLAYER");
                params.put("playerID", Integer.toString(thisPlayer.getID()));
                params.put("tryoutID", getIntent().getStringExtra("TRYOUT_ID"));
                // TODO: ignore zero values
                params.put("speed", Integer.toString(attributeValues[0]));
                params.put("hockeyAwareness", Integer.toString(attributeValues[1]));
                params.put("competeLevel", Integer.toString(attributeValues[2]));
                params.put("puckHandling", Integer.toString(attributeValues[3]));
                params.put("agility", Integer.toString(attributeValues[4]));
                JSONObject jsonObject = new JSONObject(params);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.POST, url, jsonObject,
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

                mRequestQueue.add(jsonObjectRequest);
            }
        });
    }


}
