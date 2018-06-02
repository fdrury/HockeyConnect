package ca.hockeyconnect.hockeyconnect;

import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        ImageButton helpButton = (ImageButton)findViewById(R.id.helpButtonTimer);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "The time that it takes a player to skate from one goal line to the other from a standstill", Toast.LENGTH_LONG).show();
            }
        });

        /*chronometer = (Chronometer)findViewById(R.id.chronometer);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
            }
        });

        final Button startStopButton = (Button)findViewById(R.id.startStopButton);

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning){
                    chronometer.stop();
                    timerRunning = false;
                    startStopButton.setText("RE-START");
                    startStopButton.setBackgroundColor(startStopButton.getContext().getResources().getColor(R.color.colorGreen));
                }
                else{
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    timerRunning = true;
                    startStopButton.setText("STOP");
                    startStopButton.setBackgroundColor(startStopButton.getContext().getResources().getColor(R.color.colorRed));
                }
            }
        });*/
    }
}
