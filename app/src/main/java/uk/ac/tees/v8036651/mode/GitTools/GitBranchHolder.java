package uk.ac.tees.v8036651.mode.GitTools;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.v8036651.mode.R;

public class GitBranchHolder extends RecyclerView.ViewHolder {

    public TextView branchName;
    public TextView location;

    public GitBranchHolder(View itemView){
        super(itemView);
        branchName = itemView.findViewById(R.id.git_branch_choose_name);
        location = itemView.findViewById(R.id.git_branch_choose_location);
    }
}