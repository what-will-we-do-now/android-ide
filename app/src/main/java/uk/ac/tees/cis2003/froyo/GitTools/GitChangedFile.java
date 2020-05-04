package uk.ac.tees.cis2003.froyo.GitTools;

import android.content.Context;

import uk.ac.tees.cis2003.froyo.R;

public class GitChangedFile {

    public enum FileStatus{
        ADDED("Added", R.string.git_file_status_added),
        CHANGED("Changed", R.string.git_file_status_changed),
        MISSING("Missing", R.string.git_file_status_missing),
        MODIFIED("Modified", R.string.git_file_status_modified),
        REMOVED("Removed", R.string.git_file_status_removed),
        UNCOMMITTED_CHANGES("Uncommitted changes", R.string.git_file_status_uncommitted_changes),
        UNTRACKED("Untracked", R.string.git_file_status_untracked);

        private final String fallbackName;
        private final int resource;

        private FileStatus(String name, int resource){
            this.fallbackName = name;
            this.resource = resource;
        }

        /**
         * This function should not be used as it will return unlocalized names.
         * Use toString(Context context) instead
         * @return unlocalized file status
         */
        @Deprecated
        @Override
        public String toString(){
            return fallbackName;
        }

        public String toString(Context context){
            return context.getResources().getString(resource);
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