package uk.ac.tees.v8036651.mode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.tees.v8036651.mode.FileViewer.Screen_FileViewer;
import uk.ac.tees.v8036651.mode.GitTools.GitCloneTask;
import uk.ac.tees.v8036651.mode.Projects.Project;

public class Screen_Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_home);

        if(Project.openedProject == null){
            findViewById(R.id.btnGotoCode).setVisibility(View.GONE);
        }else{
            findViewById(R.id.btnGotoCode).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Project.openedProject == null){
            findViewById(R.id.btnGotoCode).setVisibility(View.GONE);
        }else{
            findViewById(R.id.btnGotoCode).setVisibility(View.VISIBLE);
        }
    }

    public void openProjects(View view){
        Intent intent = new Intent(this, Screen_Project_Choose.class);
        startActivity(intent);
    }

    public void createProject(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogue = LayoutInflater.from(this).inflate(R.layout.dialog_project_new, null);

        builder.setView(dialogue);

        final EditText projectName = dialogue.findViewById(R.id.project_name);

        builder.setPositiveButton("Create Project", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File projectFile = new File(new File(getExternalFilesDir(null), "MoDE_Code_Directory"), projectName.getText().toString());

                projectFile.mkdirs();

                Project.openedProject = new Project(projectName.getText().toString(), projectFile);

                //open the file manager
                //TODO add change path to where FileViewer is showing
                startActivity(new Intent(Screen_Home.this, Screen_FileViewer.class));
            }
        });

        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.show();

        projectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    projectName.setError("Project must have name!");
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                }else if((new File(new File(getExternalFilesDir(null), "MoDE_Code_Directory"), s.toString())).exists()){
                    projectName.setError("Project already exists!");
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                }else{
                    projectName.setError(null);
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

        builder.setNegativeButton("Cancel", null);


        final AlertDialog dialog = builder.show();


        //check that the project already doesn't exist!
        projectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(new File(parentFile, s.toString()).exists()){
                    projectName.setError("Project with this name already exists!");
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    projectName.setError(null);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
