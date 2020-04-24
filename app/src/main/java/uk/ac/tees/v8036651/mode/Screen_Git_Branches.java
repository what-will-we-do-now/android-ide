package uk.ac.tees.v8036651.mode;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.util.List;

import uk.ac.tees.v8036651.mode.GitTools.GitBranchAdapter;
import uk.ac.tees.v8036651.mode.Projects.Project;

public class Screen_Git_Branches extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_git_branches);

        RecyclerView recyclerView = findViewById(R.id.git_branch_choose);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get list of branches
        if(Project.openedProject.getGit() != null){
            ListBranchCommand localListBranch = Project.openedProject.getGit().branchList();
            ListBranchCommand remoteListBranch = Project.openedProject.getGit().branchList();
            remoteListBranch.setListMode(ListBranchCommand.ListMode.REMOTE);
            //listBranch.setListMode(ListBranchCommand.ListMode.ALL);
            try {
                List<Ref> localBranches = localListBranch.call();
                List<Ref> remoteBranches = remoteListBranch.call();

                RecyclerView.Adapter adapter = new GitBranchAdapter(localBranches, remoteBranches);
                recyclerView.setAdapter(adapter);

            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
    }
}
