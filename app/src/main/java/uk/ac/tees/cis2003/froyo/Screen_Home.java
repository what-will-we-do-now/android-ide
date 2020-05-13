package uk.ac.tees.cis2003.froyo;

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
import android.widget.CompoundButton;
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

import uk.ac.tees.cis2003.froyo.FileViewer.Screen_FileViewer;
import uk.ac.tees.cis2003.froyo.GitTools.GitCloneTask;
import uk.ac.tees.cis2003.froyo.Projects.Project;
import uk.ac.tees.cis2003.froyo.plugins.PluginManager;

public class Screen_Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_home);
        if(Project.openedProject == null){
            findViewById(R.id.screen_home_return_project).setVisibility(View.GONE);
        }else{
            findViewById(R.id.screen_home_return_project).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Project.openedProject == null || Project.openedProject.getLastFile() == null){
            findViewById(R.id.screen_home_return_project).setVisibility(View.GONE);
        }else{
            findViewById(R.id.screen_home_return_project).setVisibility(View.VISIBLE);
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

        // fill in list of project types
        Spinner projectTypes = dialogue.findViewById(R.id.fragment_project_new_type);
        CheckBox mainCreate = dialogue.findViewById(R.id.fragment_project_new_main_make);
        mainCreate.setChecked(true);
        EditText mainName = dialogue.findViewById(R.id.fragment_project_new_main_name);
        mainName.setText(getResources().getString(R.string.project_new_default_main_name));

        ArrayAdapter content = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PluginManager.getProjectTypes());

        projectTypes.setAdapter(content);

        final EditText projectName = dialogue.findViewById(R.id.fragment_project_new_name);

        builder.setPositiveButton(getResources().getString(R.string.answer_create_project), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File projectFile = new File(new File(getExternalFilesDir(null), "MoDE_Code_Directory"), projectName.getText().toString());

                projectFile.mkdirs();

                Project.openedProject = new Project(projectName.getText().toString(), projectFile);

                String projectLanguage = ((Spinner)dialogue.findViewById(R.id.fragment_project_new_type)).getSelectedItem().toString();

                try {
                    Project.openedProject.setLanguage(projectLanguage);
                } catch (IOException e) {
                    Log.e("Project", "Unable to set language", e);
                }

                boolean createMain = ((CheckBox) dialogue.findViewById(R.id.fragment_project_new_main_make)).isChecked();

                if(createMain){

                    String filename = ((EditText) dialogue.findViewById(R.id.fragment_project_new_main_name)).getText().toString();

                    File mainFile = new File(Project.openedProject.getSrc(), filename + "." + PluginManager.getDefaultFileExtensionFor(projectLanguage));

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
                    startActivity(new Intent(Screen_Home.this, Screen_FileViewer.class));
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.answer_cancel), null);

        final AlertDialog dialog = builder.show();

        // add validation for project name already in use
        projectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    projectName.setError(getResources().getString(R.string.project_new_error_no_name));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else if((new File(new File(getExternalFilesDir(null), "MoDE_Code_Directory"), s.toString())).exists()){
                    projectName.setError(getResources().getString(R.string.project_new_error_name_in_use));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    projectName.setError(null);
                    //only enable if main name is valid or is not being created
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(mainName.getVisibility() != View.VISIBLE || mainName.getError() == null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        mainCreate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    dialog.findViewById(R.id.fragment_project_new_main_name_text).setVisibility(View.GONE);
                    mainName.setError(null);
                    mainName.setVisibility(View.GONE);
                    //trigger text watcher to check if main name is allowed
                    projectName.setText(projectName.getText());
                }else{
                    dialog.findViewById(R.id.fragment_project_new_main_name_text).setVisibility(View.VISIBLE);
                    mainName.setVisibility(View.VISIBLE);
                    //trigger text watcher to check if main name is allowed
                    mainName.setText(mainName.getText());
                    projectName.setText(projectName.getText());
                }
            }
        });



        mainName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    mainName.setError(getResources().getString(R.string.project_new_error_main_no_name));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    mainName.setError(null);
                    //only enable if project name is valid
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(projectName.getError() == null);
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

    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, Screen_Preferences.class);
        startActivity(settingsIntent);
    }

    public void gitClone(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogue = LayoutInflater.from(this).inflate(R.layout.dialog_git_clone, null);

        builder.setView(dialogue);

        final EditText projectName = dialogue.findViewById(R.id.dialog_git_clone_project_name);
        final EditText gitLink = dialogue.findViewById(R.id.dialog_git_clone_url);

        final File parentFile = new File(getExternalFilesDir(null), "MoDE_Code_Directory");



        builder.setPositiveButton(getResources().getString(R.string.answer_clone), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //clone the git project
                try {
                    GitCloneTask gt = new GitCloneTask(new File(parentFile, projectName.getText().toString()), Screen_Home.this, new URL(gitLink.getText().toString()));
                    gt.execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Home.this, R.string.git_clone_message_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.answer_cancel), null);


        final AlertDialog dialog = builder.show();


        //check that the project name isn't already taken.
        projectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(new File(parentFile, s.toString()).exists()){
                    projectName.setError(getResources().getString(R.string.project_new_error_name_in_use));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    projectName.setError(null);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
