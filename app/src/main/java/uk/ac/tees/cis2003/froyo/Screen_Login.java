package uk.ac.tees.cis2003.froyo;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class Screen_Login extends AppCompatActivity {
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);


        SharedPreferences pref = getSharedPreferences("mode", Context.MODE_PRIVATE);
        final String pattern1 = pref.getString("pattern", "");
        final Intent intent = new Intent(this, Screen_Home.class);
        if (pattern1.equals(""))
        {
            startActivity(intent);
            finish();
        }



        else
            {
            setContentView(R.layout.screen_login);

            final PatternLockView patternLockView = findViewById(R.id.patternView);
            patternLockView.addPatternLockListener(new PatternLockViewListener() {

                public void onStarted()
                {

                }

                public void onProgress(List progressPattern)
                {

                }

                public void onComplete(List pattern)
                {
                    if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(pattern1.toString()))
                    {
                        Toast.makeText(Screen_Login.this, getResources().getString(R.string.pattern_entry_correct), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();
                    }
                    else
                        {
                            Toast.makeText(Screen_Login.this, getResources().getString(R.string.pattern_entry_incorrect), Toast.LENGTH_LONG).show();
                        }

                }

                public void onCleared()
                {

                }
            });
        }



        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if(!fingerprintManager.isHardwareDetected())
            {
                System.out.println("Finger print check unsupported as no hardware available!");
            }
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            {
                System.out.println("Permission to use fingerprint not granted!");
            }
            else if (!keyguardManager.isKeyguardSecure())
            {
                System.out.println("Please add a lock to your device first!");
            }
            else if (!fingerprintManager.hasEnrolledFingerprints())
            {
                System.out.println("Please add a fingerprint to your device first!");
            }
            else
            {
                System.out.println("Fingerprint is able to work on this device!");
                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, null);

                if(fingerprintManager.equals(true))
                {
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}