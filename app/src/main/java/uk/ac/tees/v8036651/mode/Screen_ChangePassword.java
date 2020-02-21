package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Screen_ChangePassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
    }

    public void updatePassword(View view) {
        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);

        String actualUsername = pref.getString("username", "");
        String actualPassword = pref.getString("password", "");

        EditText Username = (EditText) findViewById(R.id.username);
        EditText Password = (EditText) findViewById(R.id.password);
        EditText NewPassword = (EditText) findViewById(R.id.password2);
        EditText ConfirmNewPassword = (EditText) findViewById(R.id.password3);

//This is to be completley overhauled therefore commented out
        if ((Username.getText().toString().equals(actualUsername)) && (NewPassword.getText().toString().equals(ConfirmNewPassword.getText().toString())) && (Password.getText().toString().equals(actualPassword)) ) {
            SharedPreferences.Editor prefedit = pref.edit();
            prefedit.putString("password", NewPassword.getText().toString());
            prefedit.commit();
            System.out.println("Update");

            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Password Updated");
            dlgAlert.setTitle("Updated!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
        else {


            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Wrong Password or Username Or New Passwords Do Not Match");
            dlgAlert.setTitle("Error!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            System.out.println("Error occured!");
            //}
        }
    }
}
