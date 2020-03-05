package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class Screen_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        PluginManager.load(this);

        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        String pattern = pref.getString("pattern", "");

        Intent intent = new Intent(this, Screen_Home.class);
        System.out.println("Pattern is:");
        System.out.println(pattern);
        if (pattern.equals("")) {
            startActivity(intent);
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
                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(pattern.toString())) {
                        Toast.makeText(Screen_Login.this, "Welcome back!", Toast.LENGTH_LONG).show();
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
