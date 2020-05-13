package uk.ac.tees.cis2003.froyo.Projects;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.cis2003.froyo.R;

public class ProjectHolder extends RecyclerView.ViewHolder{

    public TextView projectName;
    public TextView projectPath;

    public ProjectHolder(View itemView) {
        super(itemView);
        projectName = (TextView) itemView.findViewById(R.id.fragment_project_new_name);
        projectPath = (TextView) itemView.findViewById(R.id.fragment_project_location);
    }
}
