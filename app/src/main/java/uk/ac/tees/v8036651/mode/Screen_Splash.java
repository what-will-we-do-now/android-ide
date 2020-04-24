package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class Screen_Splash extends AppCompatActivity {
    private final static int SPLASH_TIME_OUT = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        SharedPreferences pref = getSharedPreferences("light_mode", MODE_PRIVATE);
        final String lmSummary = pref.getString("light_mode", "");
        if (lmSummary.equals(getApplicationContext().getString(R.string.settings_theme_summary_disabled))){
            getApplicationContext().setTheme(R.style.darkTheme);
            getApplicationInfo().theme = R.style.darkTheme;
        }
        else if (lmSummary.equals(getApplicationContext().getString(R.string.settings_theme_summary_enabled))){
            getApplicationContext().setTheme(R.style.lightTheme);
            getApplicationInfo().theme = R.style.lightTheme;
        }

        setTheme(getApplicationInfo().theme);


        PluginManager.load(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        File mainCodeDirectory = new File (getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory");
        if(!mainCodeDirectory.exists()){
            mainCodeDirectory.mkdir();
        }

        new Handler().postDelayed(() -> {
            if (checkPref(this, "first")){
                launchWelcome();
            }
            else {
                Intent splashIntent = new Intent(Screen_Splash.this, Screen_Login.class);
                startActivity(splashIntent);
            }
            finish();
        },SPLASH_TIME_OUT);





    }

    public void launchWelcome(){
        Intent welcomeIntent = new Intent(Screen_Splash.this, Screen_Welcome.class);
        startActivity(welcomeIntent);
        changePrefs(this, "first");
        finish();
    }

    public static boolean checkPref(Context context, String prefKey){
        return context.getSharedPreferences("prefs",MODE_PRIVATE).getBoolean(prefKey, true);
    }

    public static void changePrefs(Context context,String key){
        SharedPreferences.Editor editor = context.getSharedPreferences("prefs",MODE_PRIVATE).edit();
        editor.putBoolean(key,false);
        editor.apply();
    }
}