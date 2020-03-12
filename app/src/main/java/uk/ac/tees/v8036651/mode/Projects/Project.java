package uk.ac.tees.v8036651.mode.Projects;

import android.util.Log;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;

public class Project {

    private String name;
    private File root;
    private Git git;

    public Project(String name, File root) {
        this(name, root, null);
    }

    public Project(String name, File root, Git git) {
        this.name = name;
        this.root = root;
        if(git == null){
            try {
                this.git = Git.open(root);
            } catch (IOException e) {
                Log.w("project", "An error occured while trying to open local git repository. (Most likely not a repository)", e);
                this.git = null;
            }
        }else {
            this.git = git;
        }
    }

    public boolean hasGitSupport(){
        return git != null;
    }

    public String getName() {
        return name;
    }

    public File getRoot() {
        return root;
    }

    public Git getGit() {
        return git;
    }
}