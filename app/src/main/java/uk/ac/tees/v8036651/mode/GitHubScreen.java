package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GitHubScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_screen);
    }

    public void doSaveActivity(View view){
        Intent intent = new Intent(this, SaveActivity.class);
        startActivity(intent);
    }
}
