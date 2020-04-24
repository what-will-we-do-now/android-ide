package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Screen_Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_welcome);


    }

    public void openLogin(View view){
        Intent welcomeIntent = new Intent(this, Screen_Login.class);
        startActivity(welcomeIntent);
        finish();
    }
}
