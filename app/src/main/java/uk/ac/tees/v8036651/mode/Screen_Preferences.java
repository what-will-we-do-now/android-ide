package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class Screen_Preferences extends AppCompatActivity {

    private static SharedPreferences.Editor prefEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);

        prefEd = getSharedPreferences("light_mode", MODE_PRIVATE).edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference pattern = findPreference("pattern");
            SwitchPreferenceCompat lightMode = findPreference("light_mode");
            lightMode.setOnPreferenceChangeListener((preference, newValue) -> {
                if(preference.getKey().equals("light_mode")){
                    lightMode.setChecked((Boolean) newValue);
                    if(lightMode.isChecked()){ updateTheme(getString(R.string.light_mode_enabled));}
                    else{ updateTheme(getString(R.string.light_mode_disabled));}
                    Toast.makeText(SettingsFragment.this.getContext(), R.string.info_restart_required, Toast.LENGTH_LONG).show();
                }
                return true;
            });
            pattern.setOnPreferenceClickListener(preference -> {
                if (preference.getKey().equals("pattern")){
                    updatePattern();
                }
                return true;
            });
        }

        private void updateTheme(String summary){
            prefEd.putString("light_mode",summary);
            System.out.println("The summary is " + summary);
            prefEd.commit();
            System.out.println("Committed");
        }

        private void updatePattern() {
            Intent pattern_intent = new Intent(this.getContext(), Screen_ChangePattern.class);
            startActivity(pattern_intent);
        }
    }
}