package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        initGit();

        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        String actualUsername = pref.getString("username", "");
        String actualPassword = pref.getString("password", "");
        System.out.println("username: " + actualUsername);
        System.out.println("password: " + actualPassword);
        if ((actualUsername.equals("")) && (actualPassword.equals("")))
        {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
        }else {
            setContentView(R.layout.activity_login);
        }
    }

    private void initGit(){

        //TODO: temporary move later to spalsh screen

        File gitFile = getFileStreamPath("git");

        if(!gitFile.exists()) {

            System.out.println("Creating GIT file");
            try {

                InputStream gitIn;
                if(System.getProperty("os.arch").startsWith("arm")) {
                    //the current phone is an ARM phone
                    gitIn = getResources().openRawResource(getResources().getIdentifier("git_arm", "raw", getPackageName()));
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
        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        String actualUsername = pref.getString("username", "");
        String actualPassword = pref.getString("password", "");
        Intent intent = new Intent(this, HomeScreenActivity.class);
        //Intent intent = new Intent(this, HomeScreenActivity.class);
        EditText Username = (EditText) findViewById(R.id.Username);
        EditText Password = (EditText) findViewById(R.id.Password);

        if ((Username.getText().toString().equals(actualUsername)) && (Password.getText().toString().equals(actualPassword)))
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
