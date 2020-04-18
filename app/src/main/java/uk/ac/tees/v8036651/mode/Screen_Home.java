package uk.ac.tees.v8036651.mode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.v8036651.mode.FileViewer.Screen_FileViewer;
import uk.ac.tees.v8036651.mode.GitTools.GitCloneTask;
import uk.ac.tees.v8036651.mode.Projects.Project;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class Screen_Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
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
        if(Project.openedProject == null || Project.openedProject.getLastFile() == null){
            findViewById(R.id.btnGotoCode).setVisibility(View.GONE);
        }else{
            findViewById(R.id.btnGotoCode).setVisibility(View.VISIBLE);
        }

    }

    public void openProjects(View view){
        Intent intent = new Intent(this, Screen_Project_Choose.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        startActivity(intent);
    }

    public void createProject(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogue = LayoutInflater.from(this).inflate(R.layout.dialog_project_new, null);

        builder.setView(dialogue);

        // fill in list of project types
        Spinner projectTypes = dialogue.findViewById(R.id.project_type);

        ArrayAdapter content = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PluginManager.getProjectTypes());

        projectTypes.setAdapter(content);

        final EditText projectName = dialogue.findViewById(R.id.project_name);

        builder.setPositiveButton("Create Project", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File projectFile = new File(new File(getExternalFilesDir(null), "MoDE_Code_Directory"), projectName.getText().toString());

                projectFile.mkdirs();

                Project.openedProject = new Project(projectName.getText().toString(), projectFile);

                String projectLanguage = ((Spinner)dialogue.findViewById(R.id.project_type)).getSelectedItem().toString();

                try {
                    Project.openedProject.setLanguage(projectLanguage);
                } catch (IOException e) {
                    Log.e("Project", "Unable to set language", e);
                }

                boolean createMain = ((CheckBox) dialogue.findViewById(R.id.project_main_make)).isChecked();

                if(createMain){

                    String filename = ((EditText) dialogue.findViewById(R.id.project_main_name)).getText().toString();

                    //TODO remove hardcoded JAVA and get the file extension from Plugin Manager
                    File mainFile = new File(Project.openedProject.getSrc(), filename + ".java");

                    Map<String, String> values = new HashMap<>();

                    values.put("filename", filename);

                    FileOutputStream output = null;
                    try {
                        output = new FileOutputStream(mainFile);
                        OutputStreamWriter out = new OutputStreamWriter(output);
                        out.write(PluginManager.getDefaultTemplate(projectLanguage, values));
                        out.flush();
                        out.close();
                        output.flush();
                        output.close();
                        Project.openedProject.setLastFile(mainFile);
                    } catch (IOException e) {
                        Log.e("Project","Unable to automatically create default file", e);
                        startActivity(new Intent(Screen_Home.this, Screen_FileViewer.class));
                    }

                    Intent screenIDE = new Intent(Screen_Home.this, Screen_IDE.class);
                    screenIDE.putExtra("OpenFile", mainFile.getAbsolutePath());
                    startActivity(screenIDE);
                }else {
                    //open the file manager
                    //TODO add change path to where FileViewer is showing
                    startActivity(new Intent(Screen_Home.this, Screen_FileViewer.class));
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.show();

        // add validation for project name already in use
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
        intent.putExtra("OpenFile", Project.openedProject.getLastFile().getAbsolutePath());
        startActivity(intent);
    }

    public void openQRScreen(View view){
        Intent qrIntent = new Intent(this, Screen_QR.class);
        startActivity(qrIntent);
    }

    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, Screen_Preferences.class);
        startActivity(settingsIntent);
    }

    public void openSearch(View view){

        Intent intentSearch = new Intent(this, Screen_Search.class);
        startActivity(intentSearch);
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


        //check that the project name isn't already taken.
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
