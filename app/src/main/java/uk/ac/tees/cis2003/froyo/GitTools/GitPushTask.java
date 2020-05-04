package uk.ac.tees.cis2003.froyo.GitTools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;

import uk.ac.tees.cis2003.froyo.R;

public class GitPushTask extends AsyncTask<Void, Integer, GitTaskStatus> {

    private Context context;
    private PushCommand push;

    public GitPushTask(Context context, PushCommand push) {
        this.context = context;
        this.push = push;
    }

    @Override
    protected GitTaskStatus doInBackground(Void... voids) {
        push.setCredentialsProvider(CredentialsProvider.getDefault());
        try {
            push.call();
        } catch(TransportException e){
            Log.e("git", "Repository requires authentication", e);
            return GitTaskStatus.GENERIC_REQUIRES_AUTHENTICATION;
        } catch (GitAPIException e) {
            Log.e("git", "Unable to push", e);
            return GitTaskStatus.GENERIC_FAILURE;
        }
        return GitTaskStatus.GENERIC_SUCCESS;
    }

    @Override
    protected void onPostExecute(GitTaskStatus taskStatus) {
        super.onPostExecute(taskStatus);
        if(taskStatus.equals(GitTaskStatus.GENERIC_SUCCESS)) {
            Toast.makeText(context, context.getResources().getString(R.string.git_push_message_successful), Toast.LENGTH_LONG).show();
        }else if(taskStatus.equals(GitTaskStatus.GENERIC_FAILURE)){
            Toast.makeText(context, context.getResources().getString(R.string.git_push_message_failure), Toast.LENGTH_LONG).show();
        }else if(taskStatus.equals(GitTaskStatus.GENERIC_REQUIRES_AUTHENTICATION)){
            GitTools.requestAuthentication(context, new Runnable() {
                @Override
                public void run() {
                    GitPushTask gpt = new GitPushTask(context, push);
                    gpt.execute();
                }
            });
        }
    }
}