package uk.ac.tees.v8036651.mode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
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
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    if(lightMode.isChecked()){ updateTheme(getString(R.string.settings_theme_summary_enabled));}
                    else{ updateTheme(getString(R.string.settings_theme_summary_disabled));}
                    Toast.makeText(SettingsFragment.this.getContext(), R.string.settings_info_restart_required, Toast.LENGTH_LONG).show();
                }
                return true;
            });
            pattern.setOnPreferenceClickListener(preference -> {
                if (preference.getKey().equals("pattern")){
                    updatePattern();
                }
                return true;
            });
            Preference GitUsername = findPreference("Git_Username");
            GitUsername.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogue = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_git_settings_commit_username, null);
                    builder.setView(dialogue);
                    builder.setPositiveButton(getResources().getString(R.string.answer_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText username = dialogue.findViewById(R.id.git_committer_username);
                            SharedPreferences.Editor gitSettings = getActivity().getSharedPreferences("git", Activity.MODE_PRIVATE).edit();
                            gitSettings.putString("username", username.getText().toString());
                            gitSettings.commit();
                            GitUsername.setSummary(getResources().getString(R.string.settings_git_username_summary_current, getActivity().getSharedPreferences("git", Activity.MODE_PRIVATE).getString("username", "")));
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.answer_cancel), null);
                    builder.show();
                    return true;
                }
            });
            GitUsername.setSummary(getResources().getString(R.string.settings_git_username_summary_current, getActivity().getSharedPreferences("git", Activity.MODE_PRIVATE).getString("username", "")));

            Preference GitEmail = findPreference("Git_Email");
            GitEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogue = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_git_settings_commit_email, null);
                    builder.setView(dialogue);
                    builder.setPositiveButton(getResources().getString(R.string.answer_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText email = dialogue.findViewById(R.id.git_committer_email);
                            SharedPreferences.Editor gitSettings = getActivity().getSharedPreferences("git", Activity.MODE_PRIVATE).edit();
                            gitSettings.putString("email", email.getText().toString());
                            gitSettings.commit();
                            GitEmail.setSummary(getResources().getString(R.string.settings_git_email_summary_current, getActivity().getSharedPreferences("git", Activity.MODE_PRIVATE).getString("email", "")));
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.answer_cancel), null);
                    builder.show();
                    return true;
                }
            });
            GitEmail.setSummary(getResources().getString(R.string.settings_git_email_summary_current, getActivity().getSharedPreferences("git", Activity.MODE_PRIVATE).getString("email", "")));
        }

        private void updateTheme(String summary){
            prefEd.putString("light_mode",summary);
            prefEd.commit();
        }

        private void updatePattern() {
            Intent pattern_intent = new Intent(this.getContext(), Screen_ChangePattern.class);
            startActivity(pattern_intent);
        }
    }
}