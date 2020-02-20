package uk.ac.tees.v8036651.mode;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeUsername extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
    }

    public void updatePassword (View view)
    {
        EditText Username = (EditText) findViewById(R.id.username);
        EditText Password = (EditText) findViewById(R.id.password);
        EditText NewUsername = (EditText) findViewById(R.id.username3);
        EditText ConfirmNewUsername = (EditText) findViewById(R.id.username4);

//This is to be completley overhauled therefore commented out
 /*       if ((Username.getText() == LoginActivity.username) && (Password.getText() == LoginActivity.password) && (NewUsername == ConfirmNewUsername))
        {
            LoginActivity.password = NewPassword;
        }
        else
        {

  */
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setMessage("Wrong Password or Username Or New Usernames Do Not Match");
        dlgAlert.setTitle("Error!");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        System.out.println("Error occured!");
        //}
    }
}

