package uk.ac.tees.cis2003.froyo.GitTools;

import android.os.AsyncTask;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitBranchCreateTask extends AsyncTask<Void, Integer, GitTaskStatus> {

    private CreateBranchCommand command;
    private Runnable postExec;

    public GitBranchCreateTask(CreateBranchCommand command){
        this(command, null);
    }

    public GitBranchCreateTask(CreateBranchCommand command, Runnable postExec){
        this.command = command;
        this.postExec = postExec;
    }
    @Override
    protected GitTaskStatus doInBackground(Void... voids) {
        try {
            command.call();
        } catch (GitAPIException e) {
            return GitTaskStatus.GENERIC_FAILURE;
        }
        return GitTaskStatus.GENERIC_SUCCESS;
    }

    @Override
    protected void onPostExecute(GitTaskStatus taskStatus) {
        super.onPostExecute(taskStatus);
        if(postExec != null){
            postExec.run();
        }
    }
}
