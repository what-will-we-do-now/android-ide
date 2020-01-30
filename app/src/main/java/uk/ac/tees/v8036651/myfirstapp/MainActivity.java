package uk.ac.tees.v8036651.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "uk.ac.tees.v8036651.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user hits log in.
     */
    public void logIn (View view)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText Username = (EditText) findViewById(R.id.Username);
        String message = Username.getText().toString();
        EditText Password = (EditText) findViewById(R.id.Password);
        message = message + "|" + Password.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
        System.out.println("Worked");
    }
}
