package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class Screen_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        final String pattern1 = pref.getString("pattern", "");
        final Intent intent = new Intent(this, Screen_Home.class);
        if (pattern1.equals("")) {
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.screen_login);

            final PatternLockView patternLockView = findViewById(R.id.patternView);
            patternLockView.addPatternLockListener(new PatternLockViewListener() {

                public void onStarted() {}

                public void onProgress(List progressPattern) {}

                public void onComplete(List pattern) {
                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(pattern1.toString())) {
                        Toast.makeText(Screen_Login.this, getResources().getString(R.string.pattern_entry_correct), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Screen_Login.this, getResources().getString(R.string.pattern_entry_incorrect), Toast.LENGTH_LONG).show();
                    }

                }

                public void onCleared() {}
            });
        }
    }
}