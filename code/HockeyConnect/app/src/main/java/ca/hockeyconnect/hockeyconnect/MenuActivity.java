package ca.hockeyconnect.hockeyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button timedEvaluationsButton = (Button)findViewById(R.id.timedEvaluationsButton);
        Button gameEvaluationsButton = (Button)findViewById(R.id.gameEvaluationsButton);

        timedEvaluationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PlayerListActivity.class);
                intent.putExtra("EVALUATION_TYPE", "TIMED");
                intent.putExtra("TRYOUT_ID", getIntent().getStringExtra("TRYOUT_ID"));
                startActivity(intent);
            }
        });

        gameEvaluationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PlayerListActivity.class);
                intent.putExtra("EVALUATION_TYPE", "GAME");
                intent.putExtra("TRYOUT_ID", getIntent().getStringExtra("TRYOUT_ID"));
                startActivity(intent);
            }
        });
    }
}
