package uk.ac.tees.v8036651.mode.FileViewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

        //TODO delete - testing
        try {
            String rootPath = getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdir();
            }
            File f = new File(rootPath + "/mttext.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            FileOutputStream out = new FileOutputStream(f);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




        setContentView(R.layout.activity_file_viewer);

        final String rootPath;


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

    private boolean[] selection;

    //Runs whenever the view is resumed
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if (!isFileManagerInitialized) {
            String rootPath = null;
            try {
                rootPath = getCodeDirectory();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final File dir = new File(rootPath);
            final File[] projectFiles = dir.listFiles();

            final TextView pathOutput = findViewById(R.id.pathOutput);
            pathOutput.setText(rootPath.substring(rootPath.lastIndexOf('/') + 1));

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

            final List<String> filesList = new ArrayList<>();

            //Lists all of the files in the specified directory
            for(int i=0; i < filesFoundCount; i++){
                filesList.add(String.valueOf(projectFiles[i].getAbsolutePath()));
            }

            textAdapter.setData(filesList);

            selection = new boolean[projectFiles.length];


            fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    selection[position] = !selection[position];
                    textAdapter.setSelection(selection);

                    boolean isAnySelected = false;
                    for (boolean aSelection : selection) {
                        if (aSelection) {
                            isAnySelected = true;
                            break;
                        }
                    }

                    if (isAnySelected) {
                        findViewById(R.id.delete).setEnabled(true);
                    } else {
                        findViewById(R.id.delete).setEnabled(false);
                    }

                    return false;
                }
            });

            //TODO get
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            //Buttons management and actions
            final Button backBtt = findViewById(R.id.back);
            final Button deleteBtt = findViewById(R.id.delete);

            backBtt.setOnClickListener(new View.OnClickListener() {
                //TODO for going up in file directory
                @Override
                public void onClick(View v) {

                }
            });

            deleteBtt.setOnClickListener((new View.OnClickListener(){
                //TODO for choosing a file
                //TODO move the trigger to when file manager item is clicked
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder deteteDialog = new AlertDialog.Builder(Screen_FileViewer.this);
                    deteteDialog.setTitle("Confirm");
                    deteteDialog.setMessage("Do you want to delete this file?");
                    deteteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < projectFiles.length; i++){
                                
                            }
                        }
                    });
                    deteteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
            }));

            isFileManagerInitialized = true;
        }
    }

    //checks for permissions
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

    //returns string path to the main storage of the app
    private String getCodeDirectory () throws IOException {
        File directory;
            directory = new File(getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory");
            if (!directory.exists()){
                System.out.println(directory.mkdir());
                return directory.getAbsolutePath();
            }
            return directory.getAbsolutePath();
    }
}



