package uk.ac.tees.v8036651.mode.GitTools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import uk.ac.tees.v8036651.mode.R;

public class GitCommitTask extends AsyncTask<CommitCommand, Integer, Integer> {


    private Context context;
    private int totalCommits = 0;
    private int successfulCommits = 0;

    public GitCommitTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(CommitCommand... commits) {
        totalCommits = commits.length;
        for(CommitCommand commit : commits){
            try {
                commit.call();
                successfulCommits++;
            } catch (GitAPIException e) {
                Log.e("git", "Unable to commit", e);
            }
        }
        return successfulCommits;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(successfulCommits == totalCommits) {
            Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_successful, successfulCommits), Toast.LENGTH_LONG).show();
        }else if(successfulCommits == 0){
            Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_failure, totalCommits), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_partial, successfulCommits, (totalCommits - successfulCommits)), Toast.LENGTH_LONG).show();
        }

    }
}