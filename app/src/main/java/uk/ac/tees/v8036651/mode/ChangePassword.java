package uk.ac.tees.v8036651.mode;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
    }

    public void updatePassword (View view)
    {
        EditText Username = (EditText) findViewById(R.id.username);
        EditText Password = (EditText) findViewById(R.id.password);
        EditText NewPassword = (EditText) findViewById(R.id.password2);
        EditText ConfirmNewPassword = (EditText) findViewById(R.id.password3);

//This is to be completley overhauled therefore commented out
 /*       if ((Username.getText() == LoginActivity.username) && (Password.getText() == LoginActivity.password) && (NewPassword == ConfirmNewPassword))
        {
            LoginActivity.password = NewPassword;
        }
        else
        {

  */
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
