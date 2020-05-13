package uk.ac.tees.cis2003.froyo.Projects;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.cis2003.froyo.FileViewer.Screen_FileViewer;
import uk.ac.tees.cis2003.froyo.R;
import uk.ac.tees.cis2003.froyo.Screen_IDE;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectHolder>{

    private ArrayList<Project> projects;
    private RecyclerView recyclerView;

    public ProjectAdapter(ArrayList<Project> projects){
        this.projects = projects;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_project, parent, false);

        return new ProjectHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        holder.projectName.setText(projects.get(position).getName());
        holder.projectPath.setText(projects.get(position).getRoot().getAbsolutePath());

        holder.itemView.findViewById(R.id.fragment_project_button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());

                builder.setTitle(recyclerView.getResources().getString(R.string.project_delete_title));

                builder.setPositiveButton(recyclerView.getResources().getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        projects.get(position).delete();
                        projects.remove(position);
                        recyclerView.removeViewAt(position);
                        ProjectAdapter.this.notifyItemRemoved(position);
                        ProjectAdapter.this.notifyItemRangeChanged(position, projects.size());
                        ProjectAdapter.this.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton(recyclerView.getResources().getString(R.string.answer_no), null);

                final AlertDialog dialog = builder.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project.openedProject = new Project(projects.get(position).getName(), projects.get(position).getRoot());


                if(Project.openedProject.hasLastFile()){
                    Intent screenIDE = new Intent(recyclerView.getContext(), Screen_IDE.class);
                    screenIDE.putExtra("OpenFile", Project.openedProject.getLastFile().getAbsolutePath());
                    recyclerView.getContext().startActivity(screenIDE);
                    ((Activity)recyclerView.getContext()).finish();

                }else {
                    recyclerView.getContext().startActivity(new Intent(recyclerView.getContext(), Screen_FileViewer.class));
                    ((Activity)recyclerView.getContext()).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
