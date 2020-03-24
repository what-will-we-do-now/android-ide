package uk.ac.tees.v8036651.mode.GitTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RejectCommitException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

import uk.ac.tees.v8036651.mode.R;

public class GitCommitTask extends AsyncTask<Void, Integer, GitTaskStatus> {


    private Context context;
    private CommitCommand commitCommand;

    public GitCommitTask(Context context, CommitCommand commitCommand) {
        this.context = context;
        this.commitCommand = commitCommand;
    }

    @Override
    protected GitTaskStatus doInBackground(Void... voids) {
        SharedPreferences preferences = context.getSharedPreferences("git", Context.MODE_PRIVATE);
        commitCommand.setAuthor(preferences.getString("git.author", ""), preferences.getString("git.email", ""));

        if(commitCommand.getAuthor() == null || commitCommand.getAuthor().getEmailAddress() == null || commitCommand.getAuthor().getName() == null){
            return GitTaskStatus.COMMIT_NO_AUTHOR;
        }

        //we expect device owner to be both author and committer
        commitCommand.setCommitter(commitCommand.getAuthor());
        try {
            commitCommand.call();
        } catch (NoHeadException e) {
            Log.e("git", "Unable to commit; repo doesn't have HEAD", e);
            return GitTaskStatus.COMMIT_NO_HEAD;
        } catch (NoMessageException e) {
            Log.e("git", "Unable to commit; No commit message", e);
            return GitTaskStatus.COMMIT_NO_MESSAGE;
        } catch (UnmergedPathsException e) {
            Log.e("git", "Unable to commit; Unmerged paths in index", e);
            return GitTaskStatus.COMMIT_UNMERGED_PATH;
        } catch (ConcurrentRefUpdateException e) {
            Log.e("git", "Unable to commit; HEAD is currently being updated by another process", e);
            return GitTaskStatus.COMMIT_CONCURRENT_UPDATE;
        } catch (WrongRepositoryStateException e) {
            Log.e("git", "Unable to commit; The repository is in wrong state", e);
            return GitTaskStatus.COMMIT_WRONG_STATE;
        } catch (RejectCommitException e){
            Log.e("git", "Unable to commit; The commit was rejected by commit hooks", e);
            return GitTaskStatus.COMMIT_REJECT_COMMIT;
        } catch (GitAPIException e) {
            Log.e("git", "Unable to commit", e);
            return GitTaskStatus.GENERIC_FAILURE;
        }
        return GitTaskStatus.GENERIC_SUCCESS;
    }

    @Override
    protected void onPostExecute(GitTaskStatus status) {
        super.onPostExecute(status);

        switch(status){
            case GENERIC_SUCCESS:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_successful), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_REJECT_COMMIT:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_reject_commit), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_WRONG_STATE:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_wrong_state), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_CONCURRENT_UPDATE:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_concurrent_update), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_UNMERGED_PATH:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_unmerged_path), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_NO_MESSAGE:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_no_message), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_NO_HEAD:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_no_head), Toast.LENGTH_LONG).show();
                break;
            case COMMIT_NO_AUTHOR:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_no_author), Toast.LENGTH_LONG).show();
                break;
            case GENERIC_FAILURE:
            default:
                Toast.makeText(context, context.getResources().getString(R.string.git_commit_message_failure), Toast.LENGTH_LONG).show();
                break;
        }

    }
}