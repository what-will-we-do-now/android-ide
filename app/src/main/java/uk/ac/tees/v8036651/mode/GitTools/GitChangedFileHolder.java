package uk.ac.tees.v8036651.mode.GitTools;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.v8036651.mode.R;

public class GitChangedFileHolder extends RecyclerView.ViewHolder {

    public CheckBox checkBox;
    public TextView file;
    public TextView status;

    public GitChangedFileHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        file = (TextView) itemView.findViewById(R.id.git_changed_path);
        status = (TextView) itemView.findViewById(R.id.git_changed_status);
    }
}
