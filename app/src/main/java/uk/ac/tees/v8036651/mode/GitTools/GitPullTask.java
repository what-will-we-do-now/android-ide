package uk.ac.tees.v8036651.mode.GitTools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;

import uk.ac.tees.v8036651.mode.R;

public class GitPullTask extends AsyncTask<Void, Integer, GitTaskStatus> {

    private Context context;
    private PullCommand pull;

    public GitPullTask(Context context, PullCommand pull) {
        this.context = context;
        this.pull = pull;
    }

    @Override
    protected GitTaskStatus doInBackground(Void... voids) {
        try {
            pull.setCredentialsProvider(CredentialsProvider.getDefault());
            pull.call();
        } catch(TransportException e){
            Log.e("git", "Repository requires authentication", e);
            return GitTaskStatus.GENERIC_REQUIRES_AUTHENTICATION;
        } catch (GitAPIException e) {
            Log.e("git", "Unable to pull", e);
            return GitTaskStatus.GENERIC_FAILURE;
        }
        return GitTaskStatus.GENERIC_SUCCESS;
    }

    @Override
    protected void onPostExecute(GitTaskStatus status) {
        super.onPostExecute(status);
        if(status.equals(GitTaskStatus.GENERIC_SUCCESS)){
            Toast.makeText(context, context.getResources().getString(R.string.git_pull_message_successful), Toast.LENGTH_LONG).show();
        }else if(status.equals(GitTaskStatus.GENERIC_FAILURE)){
            Toast.makeText(context, context.getResources().getString(R.string.git_pull_message_failure), Toast.LENGTH_LONG).show();
        }else if(status.equals(GitTaskStatus.GENERIC_REQUIRES_AUTHENTICATION)){
            GitTools.requestAuthentication(context, new Runnable() {
                @Override
                public void run() {
                    GitPullTask gpt = new GitPullTask(context, pull);
                    gpt.execute();
                }
            });
        }
    }
}