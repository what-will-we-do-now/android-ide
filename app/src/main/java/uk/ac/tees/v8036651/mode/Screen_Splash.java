package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Screen_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen__splash);

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
    }
