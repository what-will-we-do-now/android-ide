package uk.ac.tees.cis2003.froyo.GitTools;

public enum GitTaskStatus {
    GENERIC_SUCCESS,
    GENERIC_FAILURE,
    GENERIC_REQUIRES_AUTHENTICATION,

    CLONE_REMOTE_REPOSITORY_NOT_FOUND,

    COMMIT_NO_HEAD,
    COMMIT_NO_MESSAGE,
    COMMIT_UNMERGED_PATH,
    COMMIT_CONCURRENT_UPDATE,
    COMMIT_WRONG_STATE,
    COMMIT_REJECT_COMMIT,
    COMMIT_NO_AUTHOR
}