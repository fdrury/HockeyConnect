package ca.hockeyconnect.hockeyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    private boolean timerRunning = false;
    private long millisecondTime, startTime, timeBuff, updateTime = 0L;
    int seconds, minutes, milliSeconds;

    Handler handler;

    TextView textView;

    ImageButton helpButton;
    Button saveButton;
    Button startStopButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        handler = new Handler();

        helpButton = (ImageButton)findViewById(R.id.helpButtonTimer);
        cancelButton = (Button)findViewById(R.id.buttonCancel);
        saveButton = (Button)findViewById(R.id.buttonSave);
        startStopButton = (Button)findViewById(R.id.startStopButton);

        textView = (TextView)findViewById(R.id.textViewTimer);

        saveButton.setEnabled(false);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "The time that it takes a player to skate from one goal line to the other from a standstill", Toast.LENGTH_LONG).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: send data back
                Intent output = new Intent();
                output.putExtra(TimerMenuActivity.timerValueRequestCode, millisecondTime);
                setResult(RESULT_OK, output);
                finish();
            }
        });

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning){
                    // TODO: stop timer
                    timeBuff += millisecondTime;
                    handler.removeCallbacks(runnable);
                    timerRunning = false;
                    saveButton.setEnabled(true);
                    startStopButton.setText("RE-START");
                    // TODO: use tint instead
                    startStopButton.setBackgroundColor(startStopButton.getContext().getResources().getColor(R.color.colorGreen));
                }
                else{
                    millisecondTime = 0L ;
                    timeBuff = 0L ;
                    updateTime = 0L ;
                    seconds = 0 ;
                    minutes = 0 ;
                    milliSeconds = 0 ;
                    // TODO: start timer
                    startTime = SystemClock.uptimeMillis();
                    handler.post(runnable);
                    saveButton.setEnabled(false);
                    timerRunning = true;
                    startStopButton.setText("STOP");
                    // TODO: use tint instead
                    startStopButton.setBackgroundColor(startStopButton.getContext().getResources().getColor(R.color.colorRed));
                }
            }
        });


    }

    public Runnable runnable = new Runnable() {

        @Override
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;

            updateTime = timeBuff + millisecondTime;

            seconds = (int) (updateTime / 1000);

            minutes = seconds / 60;

            seconds = seconds % 60;

            milliSeconds = (int) (updateTime % 1000);

            textView.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));

            handler.postDelayed(this, 0);
        }

    };
}
