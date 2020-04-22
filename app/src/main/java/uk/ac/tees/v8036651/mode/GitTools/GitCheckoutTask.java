package uk.ac.tees.v8036651.mode.GitTools;

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitCheckoutTask extends AsyncTask<Void, Integer, GitTaskStatus> {

    private CheckoutCommand command;
    private Runnable postExec;

    public GitCheckoutTask(CheckoutCommand command){
        this(command, null);
    }

    public GitCheckoutTask(CheckoutCommand command, Runnable postExec){
        this.command = command;
        this.postExec = postExec;
    }

    @Override
    protected GitTaskStatus doInBackground(Void... voids) {
        try {
            command.call();
        } catch (GitAPIException e) {
            Log.e("Git", "git error occurred", e);
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
