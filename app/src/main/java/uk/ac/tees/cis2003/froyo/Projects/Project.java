package uk.ac.tees.cis2003.froyo.Projects;

import android.util.Log;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

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
    private File src;
    private String language;

    private File lastFile;
    private Properties prop;

    public Project(String name, File root) {
        this(name, root, null);
    }

    public Project(String name, File root, Git git) {
        this.name = name;
        this.root = root;
        this.src = new File(root, "src");
        if(!this.src.exists()){
            if(!this.src.mkdirs()){
                Log.w("project", "Could not create src directory in the project.");
            }
        }
        if(git == null){
            try {
                this.git = Git.open(root);
            } catch (IOException e) {
                Log.w("project", "An error occurred while trying to open local git repository. (Most likely not a repository)", e);
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
            if(prop.getProperty("language") != null){
                language = prop.getProperty("language");
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

    public File getSrc(){ return src; }

    public Git getGit() {
        return git;
    }

    public boolean hasLastFile(){
        return lastFile != null;
    }

    public String getLanguage(){ return language; }

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

    public void setLanguage(String language) throws IOException {
        this.language = language;
        prop.setProperty("language", language);
        FileOutputStream os = new FileOutputStream(new File(root, "project.settings"));
        prop.storeToXML(os, "");
    }

    public void delete(){
        if(openedProject != null) {
            if (openedProject.getName().equals(this.name)) {
                openedProject = null;
            }
        }
        purge(root);
    }

    private void purge(File file){
        if(file.isDirectory()){
            for(File child : file.listFiles()){
                purge(child);
            }
        }
        file.delete();
    }

    public void gitInit(){
        if(git == null){
            InitCommand ginit = Git.init();
            ginit.setDirectory(root);
            try {
                git = ginit.call();
            } catch (GitAPIException e) {
                Log.e("git", "Failed initialising Git repository", e);
            }
        }else{
            Log.w("project", "Attempting to init git on existing repository");
        }
    }
}