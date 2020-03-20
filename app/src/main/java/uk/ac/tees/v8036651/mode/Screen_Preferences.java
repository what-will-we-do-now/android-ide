package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class Screen_Preferences extends AppCompatActivity {

    private static SharedPreferences.Editor prefEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Update_Theme(this);

        prefEd = getSharedPreferences("light_mode", MODE_PRIVATE).edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference pattern = findPreference("pattern");
            SwitchPreferenceCompat lightMode = findPreference("light_mode");
            Preference.OnPreferenceClickListener onClick = preference -> {
                if (preference.getKey().equals("pattern")){
                    updatePattern();
                }
                return true;
            };
            Preference.OnPreferenceChangeListener onChange = (preference, newValue) -> {
                if(preference.getKey().equals("light_mode")){
                    lightMode.setChecked((Boolean) newValue);
                    if(lightMode.isChecked()){ updateTheme(getString(R.string.light_mode_enabled));}
                    if(!lightMode.isChecked()){ updateTheme(getString(R.string.light_mode_disabled));}
                }
                return true;
            };
            lightMode.setOnPreferenceChangeListener(onChange);
            pattern.setOnPreferenceClickListener(onClick);
        }

        protected static void updateTheme(String summary){
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