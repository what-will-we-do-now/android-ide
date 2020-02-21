package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "uk.ac.tees.v8036651.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //TODO: temporary move later to spalsh screen

        File gitFile = getFileStreamPath("git");

        if(!gitFile.exists()) {

            System.out.println("Creating GIT file");
            try {

                InputStream gitIn;
                if(System.getProperty("os.arch").startsWith("arm")) {
                    //the current phone is an ARM phone
                    gitIn = getResources().openRawResource(getResources().getIdentifier("git-arm", "raw", getPackageName()));
                }else if(System.getProperty("os.arch").equals("i686")){
                    //the current phone is an x86 phone

                    return;
                }else{

                    return;
                }
                byte[] buffer = new byte[gitIn.available()];
                gitIn.read(buffer);
                gitIn.close();

                FileOutputStream gitOut = openFileOutput("git", Context.MODE_PRIVATE);
                gitOut.write(buffer);
                gitOut.close();

                File file = getFileStreamPath("git");
                file.setExecutable(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        if ((Username.getText().toString().equals("")) && (Password.getText().toString().equals("")))
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
