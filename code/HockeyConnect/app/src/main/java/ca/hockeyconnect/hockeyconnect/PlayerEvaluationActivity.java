package ca.hockeyconnect.hockeyconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PlayerEvaluationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluation);

        TextView playerNameTextView = (TextView)findViewById(R.id.textViewPlayerName);
        TextView playerNumberTextView = (TextView)findViewById(R.id.textViewPlayerNumber);
        TextView attribute0TextView = (TextView)findViewById(R.id.textViewAttributeName0);
        TextView attribute1TextView = (TextView)findViewById(R.id.textViewAttributeName1);
        TextView attribute2TextView = (TextView)findViewById(R.id.textViewAttributeName2);
        TextView attribute3TextView = (TextView)findViewById(R.id.textViewAttributeName3);
        TextView attribute4TextView = (TextView)findViewById(R.id.textViewAttributeName4);

        playerNameTextView.setText(R.string.test_player_name_name);
        playerNumberTextView.setText(R.string.test_player_number_name);
        attribute0TextView.setText(R.string.attribute0_name);
        attribute1TextView.setText(R.string.attribute1_name);
        attribute2TextView.setText(R.string.attribute2_name);
        attribute3TextView.setText(R.string.attribute3_name);
        attribute4TextView.setText(R.string.attribute4_name);
    }
}
