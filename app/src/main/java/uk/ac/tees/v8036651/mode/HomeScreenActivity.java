package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        //Test
        //Commit
    }

    public void openIDE(View view){
        Intent intent = new Intent(this, Screen_IDE.class);
        startActivity(intent);
    }

    public void openQRScreen(View view){
        Intent qrIntent = new Intent(this, QRScreen.class);
        startActivity(qrIntent);
    }

    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, Screen_Settings.class);
        startActivity(settingsIntent);
    }

    public void openGitHub(View view){
        Intent gitIntent = new Intent(this, GitHubScreen.class);
        startActivity(gitIntent);
    }
}