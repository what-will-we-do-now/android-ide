package uk.ac.tees.v8036651.mode.Projects;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import uk.ac.tees.v8036651.mode.FileViewer.Screen_FileViewer;
import uk.ac.tees.v8036651.mode.R;
import uk.ac.tees.v8036651.mode.Screen_IDE;

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

        holder.itemView.findViewById(R.id.project_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());

                builder.setTitle("Are you sure you want to delete this project?");

                builder.setPositiveButton("Delete Project", new DialogInterface.OnClickListener() {
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

                builder.setNegativeButton("Cancel", null);

                final AlertDialog dialog = builder.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project.openedProject = new Project(projects.get(position).getName(), projects.get(position).getRoot());


                if(Project.openedProject.hasLastFile()){

                    String ret;

                    try{
                        InputStream input = new FileInputStream(Project.openedProject.getLastFile());

                        InputStreamReader inp = new InputStreamReader(input);
                        BufferedReader reader = new BufferedReader(inp);
                        String receiveString;
                        StringBuilder str = new StringBuilder();

                        while( (receiveString = reader.readLine()) != null){
                            str.append(receiveString).append("\n");
                        }

                        ret = str.toString();


                        input.close();
                        inp.close();
                        reader.close();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        ret = null;
                    }
                    Screen_IDE.editTextContent = ret;
                    recyclerView.getContext().startActivity(new Intent(recyclerView.getContext(), Screen_IDE.class));

                }else {
                    // TODO add ability to change directory in which file manager is opened
                    recyclerView.getContext().startActivity(new Intent(recyclerView.getContext(), Screen_FileViewer.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
