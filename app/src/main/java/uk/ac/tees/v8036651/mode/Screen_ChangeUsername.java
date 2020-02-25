package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Screen_ChangeUsername extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_change_username);
    }

    public void updateUsername (View view)
    {


        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);

        String actualUsername = pref.getString("username", "");
        String actualPassword = pref.getString("password", "");


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

            Intent intent = new Intent(this, Screen_Home.class);
            startActivity(intent);

            Context context = getApplicationContext();
            CharSequence text = "Username Updated";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
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

