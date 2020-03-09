package uk.ac.tees.v8036651.mode.GitTools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.net.URL;

import uk.ac.tees.v8036651.mode.R;

public class GitCloneTask extends AsyncTask<Void, Integer, Long> {

    private File downloadDirectory;
    private Context context;
    private CredentialsProvider credentialsProvider;
    private URL url;

    public GitCloneTask(File downloadDirectory, Context context, URL url) {
        this(downloadDirectory, context, url, null);
    }

    public GitCloneTask(File downloadDirectory, Context context, URL url, CredentialsProvider credentialsProvider){
        this.downloadDirectory = downloadDirectory;
        this.context = context;
        this.url = url;
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    protected Long doInBackground(Void... v) {
        try {
            CloneCommand clone = Git.cloneRepository();
            clone.setURI(url.toString());
            clone.setDirectory(downloadDirectory);
            clone.setCredentialsProvider(credentialsProvider);
            clone.call();
        }catch(TransportException e) {
            Log.w("git", "Repository requires authentication", e);
            return -1L;
        } catch(InvalidRemoteException e){
            Log.e("git", "Remote repository not found", e);
            return -3L;
        } catch (GitAPIException e) {
            //an error occured while processing
            Log.e("git", "Could not clone git repository", e);
            return -2L;
        }
        return 1L;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        if(aLong == -1L){
            //the authentication failed... lets retry

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            View dialogue = LayoutInflater.from(context).inflate(R.layout.dialog_git_authentication, null);
            builder.setView(dialogue);

            final EditText username = dialogue.findViewById(R.id.git_authentication_username);
            final EditText password = dialogue.findViewById(R.id.git_authentication_password);

            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GitCloneTask gt = new GitCloneTask(downloadDirectory, context, url, new UsernamePasswordCredentialsProvider(username.getText().toString(), password.getText().toString()));
                    gt.execute();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
        }else if(aLong == -3L){
            Toast.makeText(context, R.string.git_clone_error_remote_not_found, Toast.LENGTH_LONG).show();
        }else if(aLong == -2L){
            Toast.makeText(context, R.string.git_clone_error, Toast.LENGTH_LONG).show();
        }
    }
}
