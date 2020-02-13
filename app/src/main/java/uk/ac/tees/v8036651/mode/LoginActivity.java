package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "uk.ac.tees.v8036651.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //test
    /**
     * Called when the user hits log in.
     */
    public void logIn (View view)
    {
        //Intent intent = new Intent(this, HomeScreenActivity.class);
        Intent intent = new Intent(this, HomeScreenActivity.class);
        EditText Username = (EditText) findViewById(R.id.Username);
        String message = Username.getText().toString();
        EditText Password = (EditText) findViewById(R.id.Password);
        message = message + "|" + Password.getText().toString();

        if ((Username.getText().toString().equals("Admin")) && (Password.getText().toString().equals("Admin")))
        {
            startActivity(intent);
            System.out.println("Worked");
        }
        else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Wrong Password or Username");
            dlgAlert.setTitle("Error!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            System.out.println("Error occured!");
        }
    }
}
