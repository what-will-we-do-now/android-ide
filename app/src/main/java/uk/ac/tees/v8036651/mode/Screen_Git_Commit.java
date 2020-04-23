package uk.ac.tees.v8036651.mode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.GitTools.GitChangedFile;
import uk.ac.tees.v8036651.mode.GitTools.GitChangedFileAdapter;
import uk.ac.tees.v8036651.mode.GitTools.GitChangedFileHolder;
import uk.ac.tees.v8036651.mode.GitTools.GitCommitTask;
import uk.ac.tees.v8036651.mode.Projects.Project;

public class Screen_Git_Commit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_git_commit);

        TextView branchName = findViewById(R.id.git_branch_name);
        try {
            branchName.setText(getResources().getString(R.string.git_names_branch_display, Project.openedProject.getGit().getRepository().getBranch()));
        } catch (IOException e) {
            Log.e("git", "Unable to retrieve branch name", e);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.git_files_changed);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get list of files which have changes in working directory



        StatusCommand gstatus = Project.openedProject.getGit().status();
        try {
            Status status = gstatus.call();

            List<GitChangedFile> gitFiles = new ArrayList<>();


            //the file was already added to index via `git add` command
            for(String file: status.getAdded()){
                if(!status.getChanged().contains(file)) {
                    gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.ADDED));
                }
            }

            //the file was already added to index via `git add` command
            for(String file: status.getChanged()){
                gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.CHANGED));
            }

            //file was deleted from file system but not from git index
            for(String file: status.getMissing()){
                gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.MISSING));
            }

            for(String file: status.getModified()){
                gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.MODIFIED));
            }

            //the file was removed via `git rm` command
            for(String file: status.getRemoved()){
                gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.REMOVED));
            }

            /* Commented out as resulted in duplicates. I am unsure what is the difference between Uncommitted Changes and any of: modified, added, changed
            for(String file: status.getUncommittedChanges()){
                if(!status.getAdded().contains(file)) {
                    gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.UNCOMMITTED_CHANGES));
                }
            }*/

            for(String file: status.getUntracked()){
                gitFiles.add(new GitChangedFile(file, GitChangedFile.FileStatus.UNTRACKED));
            }

            RecyclerView.Adapter adapter = new GitChangedFileAdapter(gitFiles);
            recyclerView.setAdapter(adapter);

        } catch (GitAPIException e) {
            Log.e("git", "Could not get status", e);
        }

        Button commit = (Button)findViewById(R.id.git_commit);
        NumberedTextView commitMessage = (NumberedTextView) findViewById(R.id.git_commit_message);


        commitMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    commitMessage.setError(getResources().getString(R.string.git_commit_validation_empty_commit_message));
                    commit.setEnabled(false);
                }else{
                    commitMessage.setError(null);
                    commit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //by default the commit message would be empty
        commitMessage.setError(getResources().getString(R.string.git_commit_validation_empty_commit_message));
        commit.setEnabled(false);
    }


    public void selectAll(View view){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.git_files_changed);
        for(int x = 0; x < recyclerView.getChildCount(); x++){
            ((GitChangedFileHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(x))).checkBox.setChecked(true);
        }
    }

    public void deselectAll(View view){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.git_files_changed);
        for(int x = 0; x < recyclerView.getChildCount(); x++){
            ((GitChangedFileHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(x))).checkBox.setChecked(false);
        }
    }

    public void commit(View view){
        CommitCommand gcommit = Project.openedProject.getGit().commit();
        gcommit.setMessage(((NumberedTextView) findViewById(R.id.git_commit_message)).getText().toString());
        GitCommitTask gct = new GitCommitTask(Screen_Git_Commit.this, gcommit);
        gct.execute();
        finish();
    }
}