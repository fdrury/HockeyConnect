package ca.hockeyconnect.hockeyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
