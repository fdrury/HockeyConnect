package ca.hockeyconnect.hockeyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerEvaluationActivity extends AppCompatActivity {

    SeekBar[] seekBars = new SeekBar[5];
    ImageButton[] helpButtons = new ImageButton[5];
    TextView timerTextView;
    String[] helpStrings = new String[5];
    int timerActivityRequestCode = 1234;
    static final String timerValueRequestCode = "timer_value";
    long timerMillisecondValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluation);

        seekBars[0] = (SeekBar)findViewById(R.id.seekBar0);
        seekBars[1] = (SeekBar)findViewById(R.id.seekBar1);
        seekBars[2] = (SeekBar)findViewById(R.id.seekBar2);
        seekBars[3] = (SeekBar)findViewById(R.id.seekBar3);
        seekBars[4] = (SeekBar)findViewById(R.id.seekBar4);
        helpButtons[0] = (ImageButton)findViewById(R.id.helpButton0);
        helpButtons[1] = (ImageButton)findViewById(R.id.helpButton1);
        helpButtons[2] = (ImageButton)findViewById(R.id.helpButton2);
        helpButtons[3] = (ImageButton)findViewById(R.id.helpButton3);
        helpButtons[4] = (ImageButton)findViewById(R.id.helpButton4);
        helpStrings[0] = "Speed: Speed/Explosiveness. How fast a player skates relative to other players";
        helpStrings[1] = "Hockey Awareness/Instincts/Sense: Reads and reacts/anticipates where the puck os going A player's ability to assess plays and predict movements";
        helpStrings[2] = "Compete Level: Work ethic, attacking the puck, strong with the puck, battles with other players";
        helpStrings[3] = "Puck Handling: Stick work, stick handling, passing, seeing the ice with the puck.";
        helpStrings[4] = "Agility: Pivoting, backwards to forwards and vice-versa, ability to stay on feet in crowded areas";

        TextView playerNameTextView = (TextView)findViewById(R.id.textViewPlayerName);
        TextView playerNumberTextView = (TextView)findViewById(R.id.textViewPlayerNumber);
        TextView attribute0TextView = (TextView)findViewById(R.id.textViewAttributeName0);
        TextView attribute1TextView = (TextView)findViewById(R.id.textViewAttributeName1);
        TextView attribute2TextView = (TextView)findViewById(R.id.textViewAttributeName2);
        TextView attribute3TextView = (TextView)findViewById(R.id.textViewAttributeName3);
        TextView attribute4TextView = (TextView)findViewById(R.id.textViewAttributeName4);
        timerTextView = (TextView)findViewById(R.id.timerTextView);

        final TextView[] attributeValueViews = {(TextView)findViewById(R.id.textViewAttribute0),
                (TextView)findViewById(R.id.textViewAttribute1),
                (TextView)findViewById(R.id.textViewAttribute2),
                (TextView)findViewById(R.id.textViewAttribute3),
                (TextView)findViewById(R.id.textViewAttribute4)};
        //final TextView attribute0ValueView = (TextView)findViewById(R.id.textViewAttribute0);

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

        Button timerButton = (Button)findViewById(R.id.timerButton);

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PlayerEvaluationActivity.this, TimerActivity.class), timerActivityRequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == timerActivityRequestCode && resultCode == RESULT_OK && data != null) {
            timerMillisecondValue = data.getLongExtra(timerValueRequestCode, 0);

            int minutes = (int)(timerMillisecondValue / 1000 / 60);
            int seconds = (int)(timerMillisecondValue / 1000 % 60);
            int milliSeconds = (int)(timerMillisecondValue % 1000);

            timerTextView.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));
        }
    }


}
