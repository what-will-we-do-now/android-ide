package uk.ac.tees.v8036651.mode;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;

import uk.ac.tees.v8036651.mode.plugins.PluginManager;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Screen_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        SharedPreferences pref = getSharedPreferences("light_mode", MODE_PRIVATE);
        final String lmSummary = pref.getString("light_mode", "");
        if (lmSummary.equals(getApplicationContext().getString(R.string.light_mode_disabled))){
            getApplicationContext().setTheme(R.style.darkTheme);
            getApplicationInfo().theme = R.style.darkTheme;
        }
        else if (lmSummary.equals(getApplicationContext().getString(R.string.light_mode_enabled))){
            getApplicationContext().setTheme(R.style.lightTheme);
            getApplicationInfo().theme = R.style.lightTheme;
        }

        setTheme(getApplicationInfo().theme);


        PluginManager.load();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        File mainCodeDirectory = new File (getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory");
        if(!mainCodeDirectory.exists()){
            mainCodeDirectory.mkdir();
        }

        new Handler().postDelayed(() -> {
            Intent splashIntent = new Intent(Screen_Splash.this, Screen_Login.class);
            startActivity(splashIntent);
            finish();
        },SPLASH_TIME_OUT);





    }



}