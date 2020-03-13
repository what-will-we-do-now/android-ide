package uk.ac.tees.v8036651.mode;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import uk.ac.tees.v8036651.mode.Projects.Project;
import uk.ac.tees.v8036651.mode.Projects.ProjectAdapter;

public class Screen_Project_Choose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_project_choose);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.project_list);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //get list of all projects

        File file = new File(getExternalFilesDir(null), "MoDE_Code_Directory");

        ArrayList<Project> listOfProjects = new ArrayList<>();

        for(File f : file.listFiles()) {
            if (f.isDirectory()) {
                listOfProjects.add(new Project(f.getName(), f));
            }
        }

        RecyclerView.Adapter adapter = new ProjectAdapter(listOfProjects);
        recyclerView.setAdapter(adapter);
    }
}
