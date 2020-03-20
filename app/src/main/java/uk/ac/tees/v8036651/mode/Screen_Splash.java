package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Switch;

import java.io.File;

public class Screen_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkTheme();
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

    protected void checkTheme(){
        SharedPreferences pref = getSharedPreferences("lightMode", MODE_PRIVATE);
        final String lmSumm = pref.getString("lightMode", "");
        System.out.println("String value of Light Mode is: " + lmSumm);
        System.out.println();
        System.out.println();
        System.out.println();
        if (lmSumm.equals(R.string.light_mode_disabled)){
            setTheme(R.style.darkTheme);
        }
        else if (lmSumm.equals(R.string.light_mode_enabled)){
            setTheme(R.style.lightTheme);
        }
    }
}