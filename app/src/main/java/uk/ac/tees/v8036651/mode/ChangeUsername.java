package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeUsername extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username);
    }

    public void updateUsername (View view)
    {


        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);

        String actualUsername = pref.getString("username", "");
        String actualPassword = pref.getString("username", "");


        EditText Username = (EditText) findViewById(R.id.username);
        EditText Password = (EditText) findViewById(R.id.password);
        EditText NewUsername = (EditText) findViewById(R.id.username3);
        EditText ConfirmNewUsername = (EditText) findViewById(R.id.username4);

//This is to be completley overhauled therefore commented out
        if ((Username.getText().toString().equals(actualUsername)) && (Password.getText().toString().equals(actualPassword) && (NewUsername.getText().toString().equals(ConfirmNewUsername.getText().toString()))))
        {
            SharedPreferences.Editor prefedit = pref.edit();
            prefedit.putString("username", NewUsername.getText().toString());
            prefedit.commit();
            System.out.println("Updated");
        }
        else
        {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Wrong Password or Username Or New Usernames Do Not Match");
            dlgAlert.setTitle("Error!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            System.out.println("Error occured!");
        }
    }
}

