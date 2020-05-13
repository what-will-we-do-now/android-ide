package uk.ac.tees.cis2003.froyo.GitTools;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.cis2003.froyo.R;

public class GitBranchHolder extends RecyclerView.ViewHolder {

    public TextView branchName;
    public TextView location;
    public ImageButton delete;

    public GitBranchHolder(View itemView){
        super(itemView);
        branchName = itemView.findViewById(R.id.fragment_git_branch_choose_name);
        location = itemView.findViewById(R.id.fragment_git_branch_choose_location);
        delete = itemView.findViewById(R.id.fragment_git_branch_button_delete);
    }
}