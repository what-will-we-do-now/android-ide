package uk.ac.tees.cis2003.froyo.GitTools;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.cis2003.froyo.R;

public class GitChangedFileHolder extends RecyclerView.ViewHolder {

    public CheckBox checkBox;
    public TextView file;
    public TextView status;

    public GitChangedFileHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.fragment_git_add_file_selection);
        file = (TextView) itemView.findViewById(R.id.fragment_git_add_file_changed_path);
        status = (TextView) itemView.findViewById(R.id.fragment_git_add_file_changed_status);
    }
}
