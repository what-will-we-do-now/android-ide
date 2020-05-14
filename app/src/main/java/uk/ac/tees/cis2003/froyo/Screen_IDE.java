package uk.ac.tees.cis2003.froyo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

import uk.ac.tees.cis2003.froyo.FileViewer.Screen_FileViewer;
import uk.ac.tees.cis2003.froyo.GUI.NumberedTextView;
import uk.ac.tees.cis2003.froyo.GitTools.GitBranchCreateTask;
import uk.ac.tees.cis2003.froyo.GitTools.GitPullTask;
import uk.ac.tees.cis2003.froyo.GitTools.GitPushTask;
import uk.ac.tees.cis2003.froyo.Projects.Project;
import uk.ac.tees.cis2003.froyo.Utils.FileUtils;

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

        SearchView search = findViewById(R.id.screen_ide_search);
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                SharedPreferences pref = getSharedPreferences("search_engine", MODE_PRIVATE);
                if(pref.getString("engine", "google.com").equalsIgnoreCase("google.com")){
                    openCustomTab("https://google.com/search?q=" + query);
                }else {
                    openCustomTab("https://stackoverflow.com/search?q=" + query);
                }

                search.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search.setVisibility(View.GONE);
                return false;
            }
        });

        projectsDirectory = getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory";

        NumberedTextView txtCode = findViewById(R.id.screen_ide_code);

        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("OpenFile")){

            fileName = getIntent().getStringExtra("OpenFile");

            File file = new File(fileName);
            txtCode.setFileEdited(file);
            try{
                txtCode.setText(FileUtils.loadFromFile(file));
                txtCode.clearUndoHistory();
            }
            catch(Exception e){
                Log.e("IDE", "Unable to read file", e);
                Toast.makeText(this, getResources().getString(R.string.ide_message_file_open_error), Toast.LENGTH_LONG).show();
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

        menu.getItem(0).setVisible(((NumberedTextView)findViewById(R.id.screen_ide_code)).isUndoAvailable());

        menu.getItem(1).setVisible(saveAvailable);
        menu.getItem(1).setEnabled(saveAvailable);

        Menu subMenu = menu.getItem(2).getSubMenu();
        subMenu.getItem(1).setVisible(((NumberedTextView)findViewById(R.id.screen_ide_code)).isRedoAvailable());
        if(Project.openedProject.hasGitSupport()){
            subMenu.getItem(5).setVisible(true);
            subMenu.getItem(6).setVisible(true);
            subMenu.getItem(7).setVisible(true);
            // temporarily disabled
            subMenu.getItem(8).setVisible(false);
            subMenu.getItem(9).setVisible(false);
            // end of temporarily disabled
            subMenu.getItem(10).setVisible(true);
            subMenu.getItem(4).setVisible(false);
        }else{
            subMenu.getItem(5).setVisible(false);
            subMenu.getItem(6).setVisible(false);
            subMenu.getItem(7).setVisible(false);
            subMenu.getItem(8).setVisible(false);
            subMenu.getItem(9).setVisible(false);
            subMenu.getItem(10).setVisible(false);
            subMenu.getItem(4).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                //open the new file

                NumberedTextView txtCode = findViewById(R.id.screen_ide_code);

                fileName = data.getStringExtra("OpenFile");

                File file = new File(fileName);
                txtCode.setFileEdited(file);
                try{
                    txtCode.setText(FileUtils.loadFromFile(file));
                    txtCode.clearUndoHistory();
                }
                catch(Exception e){
                    Log.e("IDE", "Unable to read file", e);
                    Toast.makeText(this, getResources().getString(R.string.ide_message_file_open_error), Toast.LENGTH_LONG).show();
                }

                //there were no changes
                saveAvailable = false;
            }
        }else if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                //refresh the file

                NumberedTextView txtCode = findViewById(R.id.screen_ide_code);

                File file = new File(fileName);
                txtCode.setFileEdited(file);
                try{
                    txtCode.setText(FileUtils.loadFromFile(file));
                    txtCode.clearUndoHistory();
                }
                catch(Exception e){
                    Log.e("IDE", "Unable to read file", e);
                    Toast.makeText(this, getResources().getString(R.string.ide_message_file_open_error), Toast.LENGTH_LONG).show();
                }

                //there were no changes
                saveAvailable = false;
            }
        }
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
                Intent fileManager = new Intent(Screen_IDE.this, Screen_FileViewer.class);
                startActivityForResult(fileManager, 0);
                return true;
            case R.id.git_init_nav:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(getResources().getString(R.string.git_init_message_enable));

                builder.setPositiveButton(getResources().getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Project.openedProject.gitInit();
                        invalidateOptionsMenu();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.answer_no), null);

                builder.show();
                return true;
            case R.id.screen_commit_commit:
                SharedPreferences pref = getSharedPreferences("git", MODE_PRIVATE);
                if(pref.getString("username", "").equals("") || pref.getString("email", "").equals("")) {
                    //the committer username and email is not set
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle(getResources().getString(R.string.git_commit_message_no_author_title));
                    builder2.setMessage(getResources().getString(R.string.git_commit_message_no_author_description));
                    builder2.setPositiveButton(getResources().getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Screen_IDE.this, Screen_Preferences.class));
                        }
                    });
                    builder2.setNegativeButton(getResources().getString(R.string.answer_no), null);
                    builder2.show();

                }else{
                    startActivity(new Intent(Screen_IDE.this, Screen_Git_Commit.class));
                }
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
                            .setPositiveButton(getResources().getString(R.string.answer_save_continue), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveChanges();
                                    PullCommand gpull = Project.openedProject.getGit().pull();
                                    GitPullTask gpullt = new GitPullTask(Screen_IDE.this, gpull, new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.screen_ide_code)).setText(FileUtils.loadFromFile(new File(fileName)));
                                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.screen_ide_code)).clearUndoHistory();
                                                saveAvailable = false;
                                            } catch (IOException e) {
                                                Log.e("IDE", "Unable to read file", e);
                                                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_message_file_open_error), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    gpullt.execute();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.answer_discard_continue), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PullCommand gpull = Project.openedProject.getGit().pull();
                                    GitPullTask gpullt = new GitPullTask(Screen_IDE.this, gpull, new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.screen_ide_code)).setText(FileUtils.loadFromFile(new File(fileName)));
                                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.screen_ide_code)).clearUndoHistory();
                                                saveAvailable = false;
                                            } catch (IOException e) {
                                                Log.e("IDE", "Unable to read file", e);
                                                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_message_file_open_error), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    gpullt.execute();
                                }
                            })
                            .setNeutralButton(getResources().getString(R.string.answer_cancel), null)
                            .show();
                }else {
                    PullCommand gpull = Project.openedProject.getGit().pull();
                    GitPullTask gpullt = new GitPullTask(this, gpull, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.screen_ide_code)).setText(FileUtils.loadFromFile(new File(fileName)));
                                ((NumberedTextView)Screen_IDE.this.findViewById(R.id.screen_ide_code)).clearUndoHistory();
                                saveAvailable = false;
                            } catch (IOException e) {
                                Log.e("IDE", "Unable to read file", e);
                                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_message_file_open_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    gpullt.execute();
                }
                return true;
            case R.id.git_nav:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                View dialogue = LayoutInflater.from(this).inflate(R.layout.dialog_git, null);
                builder2.setView(dialogue);
                AlertDialog alertDialog = builder2.create();
                dialogue.findViewById(R.id.dialog_git_branch_new).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Screen_IDE.this);
                        View dialogue = LayoutInflater.from(Screen_IDE.this).inflate(R.layout.dialog_git_branch_new, null);

                        builder.setView(dialogue);
                        builder.setPositiveButton(getResources().getString(R.string.git_branch_create), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CreateBranchCommand cbc = Project.openedProject.getGit().branchCreate();
                                try {
                                    cbc.setStartPoint(Project.openedProject.getGit().getRepository().getFullBranch());
                                    cbc.setName(((EditText)dialogue.findViewById(R.id.dialog_git_branch_new_name)).getText().toString());
                                } catch (IOException e) {
                                    Log.e("Git", "Failed to create new branch", e);
                                }

                                GitBranchCreateTask gbct = new GitBranchCreateTask(cbc);
                                gbct.execute();
                            }
                        });
                        builder.setNeutralButton(getResources().getString(R.string.answer_cancel), null);
                        builder.show();
                        alertDialog.dismiss();
                    }
                });
                dialogue.findViewById(R.id.dialog_git_checkout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Screen_IDE.this, Screen_Git_Branches.class));
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            case R.id.dialog_git_checkout:
                StatusCommand sc = Project.openedProject.getGit().status();
                try {
                    Status status = sc.call();
                    if(status.isClean()){
                        startActivityForResult(new Intent(Screen_IDE.this, Screen_Git_Branches.class), 1);
                    }else{
                        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.git_checkout_message_unclean_title)).setMessage(getResources().getString(R.string.git_checkout_message_unclean_message)).setPositiveButton(getResources().getString(R.string.answer_ok), null).show();
                    }
                } catch (GitAPIException e) {
                    Log.e("Git", "Error when getting branch status", e);
                }
                return true;
            case R.id.search:
                findViewById(R.id.screen_ide_search).setVisibility(View.VISIBLE);
                findViewById(R.id.screen_ide_search).requestFocus();
                findViewById(R.id.screen_ide_search).requestFocusFromTouch();

                return true;
            case R.id.undo:
                ((NumberedTextView)findViewById(R.id.screen_ide_code)).undo();
                return true;
            case R.id.redo:
                ((NumberedTextView)findViewById(R.id.screen_ide_code)).redo();
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
                    .setMessage(getResources().getString(R.string.ide_close_no_save_message))
                    .setPositiveButton(getResources().getString(R.string.answer_save_close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveChanges();
                            finish();
                        }
                    }).setNegativeButton(getResources().getString(R.string.answer_discard_close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNeutralButton(getResources().getString(R.string.answer_cancel), null)
                    .show();
        }else{
            super.onBackPressed();
        }
    }

    private void saveChanges(){

        Toast.makeText(this, getResources().getString(R.string.ide_message_saving), Toast.LENGTH_SHORT).show();

        if (fileName == null){
            final AlertDialog.Builder setFileNameDialog = new AlertDialog.Builder(Screen_IDE.this);
            View inflater = LayoutInflater.from(this).inflate(R.layout.dialog_set_file_name, null);

            final EditText input = (EditText) inflater.findViewById(R.id.dialog_set_file_name_new_name);

            setFileNameDialog.setView(inflater);
            setFileNameDialog.setPositiveButton(R.string.answer_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fileName = new File(Project.openedProject.getRoot(), input.getText().toString()).getAbsolutePath();

                    try {
                        FileUtils.saveToFile(fileName, ((NumberedTextView) findViewById(R.id.screen_ide_code)).getText().toString());
                        saveAvailable = false;
                        invalidateOptionsMenu();
                    } catch (Exception e) {
                        Log.e("IDE", "Unable to save file", e);
                        Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_message_file_save_error), Toast.LENGTH_LONG).show();
                    }
                }
            });
            setFileNameDialog.setNegativeButton(R.string.answer_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            setFileNameDialog.show();
        }else{
            try {
                FileUtils.saveToFile(fileName, ((NumberedTextView) findViewById(R.id.screen_ide_code)).getText().toString());
                saveAvailable = false;
                invalidateOptionsMenu();
            } catch (Exception e) {
                Log.e("IDE", "Unable to save file", e);
                Toast.makeText(Screen_IDE.this, getResources().getString(R.string.ide_message_file_save_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    //Custom Tab Shenanigans
    void openCustomTab(String url)
    {
        // Use CustomeTabsIntent.Builder to configure CustomTabsIntent
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolabr color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customeTabsIntent = builder.build();
        //and launch the desired url with CustomeTabsIntent.launchUrl()
        customeTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
