package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import uk.ac.tees.v8036651.mode.R;

public class Screen_ChangePattern extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_changepattern);

        final PatternLockView patternLockView = findViewById(R.id.patternView);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {

            public void onStarted() {

            }

            public void onProgress(List progressPattern) {

            }

            public void onComplete(List pattern) {
                Log.d(getClass().getName(), "Pattern complete: " +
                        PatternLockUtils.patternToString(patternLockView, pattern));

            }

            public void onCleared() {

            }
        });
    }

    public void updatePattern (View view)
    {
        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        final SharedPreferences.Editor prefEdit = pref.edit();


        final PatternLockView patternLockView = findViewById(R.id.patternView);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {

            public void onStarted() {

            }

            public void onProgress(List progressPattern) {

            }

            public void onComplete(List pattern) {
                Log.d(getClass().getName(), "Pattern complete: " +
                        PatternLockUtils.patternToString(patternLockView, pattern));
                prefEdit.putString("UpdatedPattern",pattern.toString());

            }

            public void onCleared() {

            }
        });

        String newPattern = pref.getString("UpdatedPattern", "");
        System.out.println(newPattern);

        prefEdit.putString("pattern", newPattern);
        prefEdit.commit();
        System.out.println("Updated");

        Intent intent = new Intent(this,Screen_Home.class);
        startActivity(intent);

    }
}
