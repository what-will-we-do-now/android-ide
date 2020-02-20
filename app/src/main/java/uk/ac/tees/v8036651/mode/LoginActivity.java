package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "uk.ac.tees.v8036651.MESSAGE";
    public static String password = "";
    public static String username = "";

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

        if ((Username.getText().toString().equals(username)) && (Password.getText().toString().equals(password)))
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
