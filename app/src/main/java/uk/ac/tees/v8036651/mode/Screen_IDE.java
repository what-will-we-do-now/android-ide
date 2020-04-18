package uk.ac.tees.v8036651.mode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import uk.ac.tees.v8036651.mode.FileViewer.Screen_FileViewer;
import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.GitTools.GitPullTask;
import uk.ac.tees.v8036651.mode.GitTools.GitPushTask;
import uk.ac.tees.v8036651.mode.Projects.Project;

public class Screen_IDE extends AppCompatActivity {

    public static String projectsDirectory = "";
    private static String fileName = null;

    private boolean saveAvailable = false;

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_ide);

        projectsDirectory = getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory";

        NumberedTextView txtCode = findViewById(R.id.txtCode);

        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("OpenFile")){

            fileName = getIntent().getStringExtra("OpenFile");

            File file = new File(getIntent().getStringExtra("OpenFile"));
            txtCode.setFileEdited(file);
            try{
                txtCode.setText(loadFile(file));
            }
            catch(Exception e){
                Log.e("IDE", "Unable to read file", e);
                Toast.makeText(this, getResources().getString(R.string.ide_file_open_error), Toast.LENGTH_LONG).show();
            }
        }
        txtCode.setHorizontallyScrolling(true);

        txtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Screen_IDE.this.saveAvailable = true;
                Screen_IDE.this.invalidateOptionsMenu();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ide_toolbar_menu, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        menu.getItem(0).setVisible(saveAvailable);
        menu.getItem(0).setEnabled(saveAvailable);

        Menu subMenu = menu.getItem(1).getSubMenu();

        if(Project.openedProject.hasGitSupport()){
            subMenu.getItem(3).setVisible(true);
            subMenu.getItem(4).setVisible(true);
            subMenu.getItem(5).setVisible(true);
            subMenu.getItem(6).setVisible(true);
            subMenu.getItem(2).setVisible(false);
        }else{
            subMenu.getItem(3).setVisible(false);
            subMenu.getItem(4).setVisible(false);
            subMenu.getItem(5).setVisible(false);
            subMenu.getItem(6).setVisible(false);
            subMenu.getItem(2).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    //TODO To be extended when more functionality is added
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_code:
                saveChanges();

                return true;
            case R.id.settings_nav:
                startActivity(new Intent(Screen_IDE.this, Screen_Preferences.class));
                return true;
            case R.id.fileview_nav:
                startActivity(new Intent(Screen_IDE.this, Screen_FileViewer.class));
                return true;
            case R.id.git_init_nav:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Do you want to enable Git version control?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Project.openedProject.gitInit();
                        invalidateOptionsMenu();
                    }
                });
                builder.setNegativeButton("No", null);

                builder.show();
                return true;
            case R.id.git_commit:
                startActivity(new Intent(Screen_IDE.this, Screen_Git_Commit.class));
                return true;
            case R.id.git_push:
                PushCommand gpush = Project.openedProject.getGit().push();
                GitPushTask gpusht = new GitPushTask(this, gpush);
                gpusht.execute();
                return true;
            case R.id.git_pull:
                if(saveAvailable){
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.ide_continue_save_title))
                            .setMessage(getResources().getString(R.string.ide_continue_save_message))
                            .setPositiveButton(getResources().getString(R.string.ide_continue_save_save), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveChanges();
                                    PullCommand gpull = Project.openedProject.getGit().pull();
                                    GitPullTask gpullt = new GitPullTask(Screen_IDE.this, gpull, new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.txtCode)).setText(loadFile(new File(fileName)));
                                                saveAvailable = false;
                                            } catch (IOException e) {
                                                Log.e("IDE", "Unable to read file", e);
                                                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_file_open_error), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    gpullt.execute();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.ide_continue_save_continue), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PullCommand gpull = Project.openedProject.getGit().pull();
                                    GitPullTask gpullt = new GitPullTask(Screen_IDE.this, gpull, new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.txtCode)).setText(loadFile(new File(fileName)));
                                                saveAvailable = false;
                                            } catch (IOException e) {
                                                Log.e("IDE", "Unable to read file", e);
                                                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_file_open_error), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    gpullt.execute();
                                }
                            })
                            .setNeutralButton(getResources().getString(R.string.ide_continue_save_cancel), null)
                            .show();
                }else {
                    PullCommand gpull = Project.openedProject.getGit().pull();
                    GitPullTask gpullt = new GitPullTask(this, gpull, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.txtCode)).setText(loadFile(new File(fileName)));
                                saveAvailable = false;
                            } catch (IOException e) {
                                Log.e("IDE", "Unable to read file", e);
                                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_file_open_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    gpullt.execute();
                }
                return true;
            case R.id.git_nav:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(saveAvailable) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.ide_close_no_save_title))
                    .setMessage(getResources().getString(R.string.ide_close_no_save_text))
                    .setPositiveButton(getResources().getString(R.string.ide_close_option_yes_and_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveChanges();
                            finish();
                        }
                    }).setNegativeButton(getResources().getString(R.string.ide_close_option_yes_and_discard), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNeutralButton(getResources().getString(R.string.ide_close_option_no), null)
                    .show();
        }else{
            super.onBackPressed();
        }
    }

    private void saveChanges(){

        Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();

        if (fileName == null){
            final AlertDialog.Builder setFileNameDialog = new AlertDialog.Builder(Screen_IDE.this);
            View inflater = LayoutInflater.from(this).inflate(R.layout.set_file_name_alert_dialog, null);

            final EditText input = (EditText) inflater.findViewById(R.id.fileNameEditText);

            setFileNameDialog.setView(inflater);
            setFileNameDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fileName = new File(Project.openedProject.getRoot(), input.getText().toString()).getAbsolutePath();

                    try {
                        saveActivity(((NumberedTextView) findViewById(R.id.txtCode)).getText().toString(), fileName);
                        saveAvailable = false;
                        invalidateOptionsMenu();
                    } catch (Exception e) {
                        Log.e("IDE", "Unable to save file", e);
                        Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_file_save_error), Toast.LENGTH_LONG).show();
                    }
                }
            });
            setFileNameDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            setFileNameDialog.show();
        }else{
            try {
                saveActivity(((NumberedTextView) findViewById(R.id.txtCode)).getText().toString(), fileName);
                saveAvailable = false;
                invalidateOptionsMenu();
            } catch (Exception e) {
                Log.e("IDE", "Unable to save file", e);
                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_file_save_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String loadFile(File file) throws IOException {
        InputStream input = new FileInputStream(file);

        InputStreamReader inp = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(inp);
        String receiveString;
        StringBuilder str = new StringBuilder();

        while( (receiveString = reader.readLine()) != null){
            str.append(receiveString).append("\n");
        }

        String ret = str.toString();


        input.close();
        inp.close();
        reader.close();
        return ret;
    }

    //Saves files to specified directory
    public void saveActivity(String data, String fileName) throws Exception{

        File file = new File(fileName);

        FileOutputStream output = new FileOutputStream(file);
        OutputStreamWriter out = new OutputStreamWriter(output);
        out.write(data);
        out.flush();
        out.close();
        output.flush();
        output.close();
    }

    //Stackoverflow search
    public void stackOverflowSearch ()
    {

    }

    //Google Search
    public void searchUsingGoogle()
    {

    }
}
