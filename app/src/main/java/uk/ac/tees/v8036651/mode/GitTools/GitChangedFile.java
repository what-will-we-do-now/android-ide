package uk.ac.tees.v8036651.mode.GitTools;

public class GitChangedFile {

    public enum FileStatus{
        ADDED("Added"),
        CHANGED("Changed"),
        MISSING("Missing"),
        MODIFIED("Modified"),
        REMOVED("Removed"),
        UNCOMMITTED_CHANGES("Uncommitted changes"),
        UNTRACKED("Untracked");

        private final String name;

        private FileStatus(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    }

    private String file;
    private FileStatus status;

    public GitChangedFile(String file, FileStatus status) {
        this.file = file;
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public FileStatus getStatus() {
        return status;
    }
}