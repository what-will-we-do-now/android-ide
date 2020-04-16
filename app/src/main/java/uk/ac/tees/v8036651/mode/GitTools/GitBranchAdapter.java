package uk.ac.tees.v8036651.mode.GitTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8036651.mode.Projects.Project;
import uk.ac.tees.v8036651.mode.R;

public class GitBranchAdapter extends RecyclerView.Adapter<GitBranchHolder> {

    private List<Ref> branches;
    private List<Ref> localBranches;
    private List<Ref> remoteBranches;
    private RecyclerView recyclerView;

    public GitBranchAdapter(List<Ref> localBranches, List<Ref> remoteBranches){
        this.localBranches = localBranches;
        this.remoteBranches = remoteBranches;
        this.branches = new ArrayList<>();
        branches.addAll(localBranches);
        branches.addAll(remoteBranches);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public GitBranchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_git_branch, parent, false);
        return new GitBranchHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GitBranchHolder holder, int position) {
        holder.branchName.setText(Repository.shortenRefName(branches.get(position).getName()));
        holder.location.setText(remoteBranches.contains(branches.get(position)) ? "Remote" : "Local");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(localBranches.contains(branches.get(position))) {
                    // change the branch
                    CheckoutCommand checkout = Project.openedProject.getGit().checkout();
                    checkout.setName(branches.get(position).getName());
                    try {
                        checkout.call();
                        ((Activity) recyclerView.getContext()).finish();
                    } catch (GitAPIException e) {
                        e.printStackTrace();
                    }
                }else{
                    //checkout requires downloading new branch

                    AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                    View dialogue = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.dialog_git_branch_checkout_remote, null);

                    builder.setView(dialogue);
                    ((EditText)dialogue.findViewById(R.id.git_checkout_name)).setText(branches.get(position).getName().replace("ref/remote/origin/", ""));
                    builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CheckoutCommand checkout = Project.openedProject.getGit().checkout();
                            checkout.setCreateBranch(true);
                            checkout.setName(((EditText)dialogue.findViewById(R.id.git_checkout_name)).getText().toString());
                            checkout.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK);
                            checkout.setStartPoint(branches.get(position).getName());
                            GitCheckoutTask gct = new GitCheckoutTask(checkout);
                            gct.execute();
                            ((Activity) recyclerView.getContext()).finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }
}
