package uk.ac.tees.v8036651.mode.FileViewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.ac.tees.v8036651.mode.R;

public class Screen_FileViewer extends AppCompatActivity {

    private Object ArrayList;

    @Override
    protected void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        setContentView(R.layout.activity_file_viewer);

        final String rootPath;

        final Button back = findViewById(R.id.back);
        final Button choose = findViewById(R.id.choose);

        back.setOnClickListener(new View.OnClickListener() {
            //TODO for going up in file directory
            @Override
            public void onClick(View v) {

            }
        });

        choose.setOnClickListener((new View.OnClickListener(){
            //TODO for choosing a file
            //TODO move the trigger to when file manager item is clicked
            @Override
            public void onClick(View v) {

            }
        }));
    }

    //Arbitrary token
    private static final int REQUEST_PERMISSIONS = 1234;

    //Permissions for the manager to write to and read external storage
    private static final String[] PERMISSIONS  = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private static final int PERMISSIONS_COUNT = PERMISSIONS.length;

    /**
     * Checks for permissions to write and read external storage
     * @return true if permissions are denied
     */
    //Version is already checked in onResume method, so this method won't run on devices with android 23 and below
    @SuppressLint("NewApi")
    private boolean arePermissionsDenied(){
        int currentPerms = 0;
        while (currentPerms < PERMISSIONS_COUNT){
            if (checkSelfPermission(PERMISSIONS[currentPerms]) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
            currentPerms++;
        }
        return false;
    }

    private boolean isFileManagerInitialized = false;

    //Runs whenever the view is resumed
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if (!isFileManagerInitialized) {
            final String rootPath = getCodeDirectory();
            final File dir = new File(rootPath);
            final File[] projectFiles = dir.listFiles();
            final TextView pathOutput = findViewById(R.id.pathOutput);
            pathOutput.setText(rootPath);

            final int filesFoundCount;

            if (projectFiles != null){
               filesFoundCount = projectFiles.length;
            }
            else {
                filesFoundCount = 0;
            }



            final ListView fileList = findViewById(R.id.fileList);
            final TextAdapter textAdapter = new TextAdapter();
            fileList.setAdapter(textAdapter);

            List<String> filesList = new ArrayList<>();

            //TODO DELETE only used for testing purposes
            for(int i=0; i < filesFoundCount; i++){
                filesList.add(String.valueOf(projectFiles[i].getAbsolutePath()));
            }

            textAdapter.setData(filesList);

            isFileManagerInitialized = true;
        }
    }

    //TODO check if this code is neccessary
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String[] permissions, final int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length>0){
            if (arePermissionsDenied()){
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }
            else {
                onResume();
            }
        }
    }

    private String getCodeDirectory (){
        File directory;
        if (Environment.getExternalStorageState() == null){
            directory = new File(Environment.getDataDirectory() + "/MoDE_Code_Directory/");
            if (!directory.exists()){
                directory.mkdir();
                return directory.getPath();
            }
            return directory.getPath();
        }
        else{
            directory = new File(Environment.getExternalStorageDirectory() + "/MoDE_Code_Directory/");
            if (!directory.exists()){
                directory.mkdir();
                return directory.getPath();
            }
            return directory.getPath();
        }
    }
}

