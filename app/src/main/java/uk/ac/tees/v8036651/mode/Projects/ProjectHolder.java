package uk.ac.tees.v8036651.mode.Projects;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.v8036651.mode.R;

public class ProjectHolder extends RecyclerView.ViewHolder{

    public TextView projectName;
    public TextView projectPath;

    public ProjectHolder(View itemView) {
        super(itemView);
        projectName = (TextView) itemView.findViewById(R.id.project_name);
        projectPath = (TextView) itemView.findViewById(R.id.project_location);
    }
}
