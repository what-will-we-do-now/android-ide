package uk.ac.tees.v8036651.mode;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import uk.ac.tees.v8036651.mode.Projects.Project;

public class Screen_git_commit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_git_commit);

        TextView branchName = findViewById(R.id.git_branch_name);
        try {
            branchName.setText(getResources().getString(R.string.git_names_branch_display, Project.openedProject.getGit().getRepository().getBranch()));
        } catch (IOException e) {
            Log.e("git", "Unable to retrieve branch name", e);
        }
    }
}
