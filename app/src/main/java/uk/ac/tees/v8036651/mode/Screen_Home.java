package uk.ac.tees.v8036651.mode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.tees.v8036651.mode.GitTools.GitCloneTask;

public class Screen_Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_home);
        //Test
        //Commit
    }

    public void openIDE(View view){
        Intent intent = new Intent(this, Screen_IDE.class);
        startActivity(intent);
    }

    public void openQRScreen(View view){
        Intent qrIntent = new Intent(this, Screen_QR.class);
        startActivity(qrIntent);
    }

    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, Screen_Settings.class);
        startActivity(settingsIntent);
    }

    public void gitClone(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogue = LayoutInflater.from(this).inflate(R.layout.dialog_git_clone, null);

        builder.setView(dialogue);

        final EditText projectName = dialogue.findViewById(R.id.git_clone_project_name);
        final EditText gitLink = dialogue.findViewById(R.id.git_clone_url);

        final File parentFile = new File(getExternalFilesDir(null), "MoDE_Code_Directory");


        builder.setPositiveButton("Clone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //clone the git project
                try {
                    GitCloneTask gt = new GitCloneTask(new File(parentFile, projectName.getText().toString()), Screen_Home.this, new URL(gitLink.getText().toString()));
                    gt.execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Home.this, R.string.git_clone_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user pressed cancel
            }
        });

        builder.show();
    }
}
