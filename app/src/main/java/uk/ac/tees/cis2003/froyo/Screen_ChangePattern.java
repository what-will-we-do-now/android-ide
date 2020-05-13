package uk.ac.tees.cis2003.froyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class Screen_ChangePattern extends AppCompatActivity {

    private SharedPreferences.Editor prefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);

        prefEdit = getSharedPreferences("mode", Context.MODE_PRIVATE).edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_changepattern);

        final PatternLockView patternLockView = findViewById(R.id.screen_login_pattern);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {

            public void onStarted() {}

            public void onProgress(List progressPattern) {}

            public void onComplete(List pattern) {
                String textpattern = PatternLockUtils.patternToString(patternLockView, pattern);
                prefEdit.putString("pattern",textpattern);

            }

            public void onCleared() {}
        });
    }

    public void updatePattern (View view)
    {
        prefEdit.commit();
        finish();
    }

    public void resetPattern (View view)
    {
        prefEdit.putString("pattern", "");
        prefEdit.commit();
        finish();
    }
}
