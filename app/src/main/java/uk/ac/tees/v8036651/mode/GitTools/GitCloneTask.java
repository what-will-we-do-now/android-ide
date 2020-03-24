package uk.ac.tees.v8036651.mode.GitTools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;
import java.net.URL;

import uk.ac.tees.v8036651.mode.R;

public class GitCloneTask extends AsyncTask<Void, Integer, GitTaskStatus> {

    private File downloadDirectory;
    private Context context;
    private URL url;

    public GitCloneTask(File downloadDirectory, Context context, URL url) {
        this.downloadDirectory = downloadDirectory;
        this.context = context;
        this.url = url;
    }

    @Override
    protected GitTaskStatus doInBackground(Void... v) {
        try {
            CloneCommand clone = Git.cloneRepository();
            clone.setURI(url.toString());
            clone.setDirectory(downloadDirectory);
            clone.setCredentialsProvider(CredentialsProvider.getDefault());
            clone.call();
        }catch(TransportException e) {
            Log.w("git", "Repository requires authentication", e);
            purge(downloadDirectory);
            return GitTaskStatus.GENERIC_REQUIRES_AUTHENTICATION;
        } catch(InvalidRemoteException e){
            Log.e("git", "Remote repository not found", e);
            purge(downloadDirectory);
            return GitTaskStatus.CLONE_REMOTE_REPOSITORY_NOT_FOUND;
        } catch (GitAPIException e) {
            //an error occured while processing
            Log.e("git", "Could not clone git repository", e);
            purge(downloadDirectory);
            return GitTaskStatus.GENERIC_FAILURE;
        }
        return GitTaskStatus.GENERIC_SUCCESS;
    }

    @Override
    protected void onPostExecute(GitTaskStatus status) {
        if(status.equals(GitTaskStatus.GENERIC_REQUIRES_AUTHENTICATION)){
            GitTools.requestAuthentication(context, new Runnable() {
                @Override
                public void run() {
                    GitCloneTask gt = new GitCloneTask(downloadDirectory, context, url);
                    gt.execute();
                }
            });
        }else if(status.equals(GitTaskStatus.CLONE_REMOTE_REPOSITORY_NOT_FOUND)){
            Toast.makeText(context, R.string.git_clone_error_remote_not_found, Toast.LENGTH_LONG).show();
        }else if(status.equals(GitTaskStatus.GENERIC_FAILURE)){
            Toast.makeText(context, R.string.git_clone_error, Toast.LENGTH_LONG).show();
        }else if(status.equals(GitTaskStatus.GENERIC_SUCCESS)){
            Toast.makeText(context, R.string.git_clone_success, Toast.LENGTH_LONG).show();
        }
    }

    private void purge(File file){
        if(file.isDirectory()){
            for(File child : file.listFiles()){
                purge(child);
            }
        }
        file.delete();
    }
}