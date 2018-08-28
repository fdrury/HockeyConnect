package ca.hockeyconnect.hockeyconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TimerMenuActivity extends AppCompatActivity {

    TextView timerTextView1;
    TextView timerTextView2;
    Button saveButton;
    Button cancelButton;
    int timerActivityRequestCode1 = 1000;
    int timerActivityRequestCode2 = 1001;
    static final String timerValueRequestCode = "timer_value";
    long timerMillisecondValue1 = 0;
    long timerMillisecondValue2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_menu);



        /*final RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.160:5000/timedEval";
        Map<String, String> params = new HashMap<String, String>();
        params.put("playerID", getIntent().getStringExtra("PLAYER"));
        params.put("tryoutID", getIntent().getStringExtra("TRYOUT_ID"));
        // TODO: ignore zero values
        long shortestTime = timerMillisecondValue1 < timerMillisecondValue2 ? timerMillisecondValue1 : timerMillisecondValue2; //choose lowest
        params.put("duration", Long.toString(shortestTime));
        JSONObject jsonObject = new JSONObject(params);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSONResponse", response.toString());
                String reader = response.toString();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Saving Failed", Toast.LENGTH_LONG).show();
            }
        });*/

        timerTextView1 = (TextView)findViewById(R.id.timerTextView1);
        timerTextView2 = (TextView)findViewById(R.id.timerTextView2);

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
                final RequestQueue mRequestQueue = Volley.newRequestQueue(currentContext);
                String url = "http://192.168.0.160:5000/timedEval";
                Map<String, String> params = new HashMap<String, String>();
                Player thisPlayer = (Player)getIntent().getSerializableExtra("PLAYER");
                params.put("playerID", Integer.toString(thisPlayer.getID()));
                params.put("tryoutID", getIntent().getStringExtra("TRYOUT_ID"));
                // TODO: ignore zero values
                long shortestTime = timerMillisecondValue1 < timerMillisecondValue2 ? timerMillisecondValue1 : timerMillisecondValue2; //choose lowest
                params.put("duration", Long.toString(shortestTime));
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
                });

                // TODO: adjust RetryPolicy
                int socketTimeout = 30000; // 30 seconds
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonObjectRequest.setRetryPolicy(policy);
                mRequestQueue.add(jsonObjectRequest);
            }
        });
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
