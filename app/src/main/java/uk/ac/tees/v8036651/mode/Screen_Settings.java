package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Screen_Settings extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);
    }

    public void openChangePassword(View view){
        Intent NPintent = new Intent(this, Screen_ChangePassword.class);
        startActivity(NPintent);
    }

    public void openChangeUsername(View view){
        Intent NUintent = new Intent(this, Screen_ChangeUsername.class);
        startActivity(NUintent);
    }
}
