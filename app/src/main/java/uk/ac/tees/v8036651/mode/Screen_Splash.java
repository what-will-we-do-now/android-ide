package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Screen_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initGit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen__splash);

        File mainCodeDirectory = new File (getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory");
        if(!mainCodeDirectory.exists()){
            mainCodeDirectory.mkdir();
        }

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent splashIntent = new Intent(Screen_Splash.this, Screen_Login.class);
                    startActivity(splashIntent);
                    finish();
                }},SPLASH_TIME_OUT);
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
    }

