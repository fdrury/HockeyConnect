package ca.hockeyconnect.hockeyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEvaluator;
    EditText editTextTryout;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEvaluator = (EditText) findViewById(R.id.evaluatorIDEditText);
        editTextTryout = (EditText) findViewById(R.id.tryoutIDEditText);
        submitButton = (Button)findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("EVALUATOR_ID", editTextEvaluator.getText().toString());
                intent.putExtra("TRYOUT_ID", editTextTryout.getText().toString());
                startActivity(intent);
            }
        });
    }
}
