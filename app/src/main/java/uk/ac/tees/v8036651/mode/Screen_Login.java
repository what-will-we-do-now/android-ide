package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class Screen_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        PluginManager.load(this);

        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        final String pattern1 = pref.getString("pattern", "");
        final Intent intent = new Intent(this, Screen_Home.class);
        System.out.println("Pattern is: " + pattern1);
        if (pattern1.equals("")) {
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.screen_login);

            final PatternLockView patternLockView = findViewById(R.id.patternView);
            patternLockView.addPatternLockListener(new PatternLockViewListener() {

                public void onStarted() {

                }

                public void onProgress(List progressPattern) {

                }

                public void onComplete(List pattern) {
                    Log.d(getClass().getName(), "Pattern complete: " +
                            PatternLockUtils.patternToString(patternLockView, pattern));
                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(pattern1.toString())) {
                        Toast.makeText(Screen_Login.this, "Welcome back!", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Screen_Login.this, "Incorrect password", Toast.LENGTH_LONG).show();
                    }

                }

                public void onCleared() {

                }
            });
        }

    }
}
