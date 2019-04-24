package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class TimerMenuActivity extends AppCompatActivity {

    TextView timerTextView1;
    TextView timerTextView2;
    TextView timeTextView;
    Button saveButton;
    Button cancelButton;
    int timerActivityRequestCode1 = 1000;
    int timerActivityRequestCode2 = 1001;
    static final String timerValueRequestCode = "timer_value";
    long timerMillisecondValue = 0;
    long timerMillisecondValue1 = 0;
    long timerMillisecondValue2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_menu);

        final Player thisPlayer = (Player)getIntent().getSerializableExtra("PLAYER");
        final String playerID = String.valueOf(thisPlayer.getID());
        final String evaluatorID = getIntent().getStringExtra("EVALUATOR_ID");
        final String tryoutID = getIntent().getStringExtra("TRYOUT_ID");

        timerTextView1 = (TextView)findViewById(R.id.timerTextView1);
        timerTextView2 = (TextView)findViewById(R.id.timerTextView2);
        timeTextView = findViewById(R.id.timeTextView);

        Button timerButton1 = (Button)findViewById(R.id.timerButton1);
        Button timerButton2 = (Button)findViewById(R.id.timerButton2);
        saveButton = (Button)findViewById(R.id.buttonSave);
        cancelButton = (Button)findViewById(R.id.buttonCancel);

        timerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TimerMenuActivity.this, TimerActivity.class), timerActivityRequestCode1);
            }
        });
        timerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TimerMenuActivity.this, TimerActivity.class), timerActivityRequestCode2);
            }
        });
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
                if(timerMillisecondValue != 0 && timerMillisecondValue < timerMillisecondValue1 && timerMillisecondValue < timerMillisecondValue2) {
                    finish();
                    return;
                }
                if(timerMillisecondValue1 == 0 && timerMillisecondValue2 == 0) {
                    finish();
                    return;
                }

                final RequestQueue mRequestQueue = Volley.newRequestQueue(currentContext);
                String url = String.format("%s/timedEval", getString(R.string.server_url));
                Map<String, String> params = new HashMap<String, String>();
                // TODO: the intent extras are already saved in global variables
                Player thisPlayer = (Player)getIntent().getSerializableExtra("PLAYER");
                params.put("playerID", Integer.toString(thisPlayer.getID()));
                params.put("evaluator", getIntent().getStringExtra("EVALUATOR_ID"));
                params.put("tryoutID", getIntent().getStringExtra("TRYOUT_ID"));
                long shortestTime = timerMillisecondValue1;
                //choose lowest
                if(timerMillisecondValue1 == 0 || (timerMillisecondValue1 > timerMillisecondValue2 && timerMillisecondValue2 != 0)) {
                    shortestTime = timerMillisecondValue2;
                }
                params.put("duration", Long.toString(shortestTime));
                JSONObject jsonObject = new JSONObject(params);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            finish();
                            return;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Saving Failed", Toast.LENGTH_LONG).show();
                    }
                });

                mRequestQueue.add(jsonObjectRequest);
            }
        });

        // TODO: use single request queue
        final RequestQueue mRequestQueue0 = Volley.newRequestQueue(this);
        String url0 = String.format("%s/getTimedEval/%s/%s/%s", getString(R.string.server_url), tryoutID, evaluatorID, playerID);
        StringRequest stringRequest0 = new StringRequest (Request.Method.GET, url0,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response);
                            timerMillisecondValue = reader.getInt("Duration");

                            int minutes = (int)(timerMillisecondValue / 1000 / 60);
                            int seconds = (int)(timerMillisecondValue / 1000 % 60);
                            int milliSeconds = (int)(timerMillisecondValue % 1000);

                            timeTextView.setText("" + minutes + ":"
                                    + String.format("%02d", seconds) + ":"
                                    + String.format("%03d", milliSeconds));
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
        mRequestQueue0.add(stringRequest0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == timerActivityRequestCode1 && resultCode == RESULT_OK && data != null) {
            timerMillisecondValue1 = data.getLongExtra(timerValueRequestCode, 0);

            int minutes = (int)(timerMillisecondValue1 / 1000 / 60);
            int seconds = (int)(timerMillisecondValue1 / 1000 % 60);
            int milliSeconds = (int)(timerMillisecondValue1 % 1000);

            timerTextView1.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));
        }

        if (requestCode == timerActivityRequestCode2 && resultCode == RESULT_OK && data != null) {
            timerMillisecondValue2 = data.getLongExtra(timerValueRequestCode, 0);

            int minutes = (int)(timerMillisecondValue2 / 1000 / 60);
            int seconds = (int)(timerMillisecondValue2 / 1000 % 60);
            int milliSeconds = (int)(timerMillisecondValue2 % 1000);

            timerTextView2.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));
        }
    }
}
