package uk.ac.tees.v8036651.mode.Projects;

import android.util.Log;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Project {

    public static Project openedProject;

    private String name;
    private File root;
    private Git git;

    private File lastFile;
    private Properties prop;

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

        prop = new Properties();

        try {
            prop.loadFromXML(new FileInputStream(new File(root, "project.settings")));

            if(prop.getProperty("lastFile") != null) {
                lastFile = new File(root, prop.getProperty("lastFile"));
            }

        } catch (IOException e) {
            Log.w("project", "Could not load project.settings file", e);
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

    public boolean hasLastFile(){
        return lastFile != null;
    }

    public File getLastFile(){
        return lastFile;
    }

    /**
     * IMPORTANT NOTE: the file passed MUST be within the project directory, otherwise this may do who knows what.
     * @param file the file which was last opened
     * @throws IOException if the config file could not be saved
     */
    public void setLastFile(File file) throws IOException {
        lastFile = file;
        prop.setProperty("lastFile", file.getAbsolutePath().substring(root.getAbsolutePath().length()));
        FileOutputStream os = new FileOutputStream(new File(root, "project.settings"));
        prop.storeToXML(os, "");
    }
}