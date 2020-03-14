package uk.ac.tees.v8036651.mode.GitTools;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.List;

import uk.ac.tees.v8036651.mode.Projects.Project;
import uk.ac.tees.v8036651.mode.R;

public class GitChangedFileAdapter extends RecyclerView.Adapter<GitChangedFileHolder> {

    private List<GitChangedFile> changedFiles;
    private RecyclerView recyclerView;

    public GitChangedFileAdapter(List<GitChangedFile> changedFiles){
        this.changedFiles = changedFiles;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public GitChangedFileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_git_add_file, parent, false);

        return new GitChangedFileHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GitChangedFileHolder holder, int position) {
        holder.file.setText(changedFiles.get(position).getFile());
        holder.status.setText(changedFiles.get(position).getStatus().toString());

        if(changedFiles.get(position).getStatus().equals(GitChangedFile.FileStatus.ADDED) || changedFiles.get(position).getStatus().equals(GitChangedFile.FileStatus.REMOVED)){
            holder.checkBox.setChecked(true);
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private boolean ignore = false;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!ignore) {
                    ignore = true;
                    if (isChecked) {
                        AddCommand gadd = Project.openedProject.getGit().add();
                        gadd.addFilepattern(changedFiles.get(position).getFile());
                        try {
                            gadd.call();
                            Log.d("git", "Added file " + changedFiles.get(position).getFile());
                        } catch (GitAPIException e) {
                            Log.e("git", "Could not add file!", e);
                            holder.checkBox.setChecked(false);
                        }
                    }else{
                        ResetCommand greset = Project.openedProject.getGit().reset();
                        greset.addPath(changedFiles.get(position).getFile());
                        try {
                            greset.call();
                            Log.d("git", "Reset file " + changedFiles.get(position).getFile());
                        } catch (GitAPIException e) {
                            Log.e("git", "Could not reset file!", e);
                            holder.checkBox.setChecked(true);
                        }
                    }
                    ignore = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return changedFiles.size();
    }
}