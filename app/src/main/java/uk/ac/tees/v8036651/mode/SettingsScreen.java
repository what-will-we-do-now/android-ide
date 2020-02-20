package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
    }

    public void openChangePassword(View view){
        Intent NPintent = new Intent(this, ChangePassword.class);
        startActivity(NPintent);
    }
}
