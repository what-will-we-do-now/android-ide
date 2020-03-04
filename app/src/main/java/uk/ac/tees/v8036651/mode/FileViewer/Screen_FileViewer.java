package uk.ac.tees.v8036651.mode.FileViewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.R;
import uk.ac.tees.v8036651.mode.Screen_IDE;

public class Screen_FileViewer extends AppCompatActivity {

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

    private File[] projectFiles;
    private List<String> filesList;
    private int filesFoundCount;

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
            projectFiles = dir.listFiles();

            final TextView pathOutput = findViewById(R.id.pathOutput);
            pathOutput.setText(rootPath.substring(rootPath.lastIndexOf('/') + 1));

            if (projectFiles != null){
               filesFoundCount = projectFiles.length;
            }
            else {
                filesFoundCount = 0;
            }

            final ListView fileList = findViewById(R.id.fileList);
            final TextAdapter textAdapter = new TextAdapter();
            fileList.setAdapter(textAdapter);

            filesList = new ArrayList<>();
            //Lists all of the files in the specified directory
            for(int i=0; i < filesFoundCount; i++){
                filesList.add(String.valueOf(projectFiles[i].getName()));
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


            final Intent intent = new Intent(this, Screen_IDE.class);
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Screen_IDE.editTextContent =(loadActivity("MoDE_Code_Directory/" + filesList.get(position)));
                    startActivity(intent);
                }
            });

            //Buttons management and actions
            final Button backBtt = findViewById(R.id.back);
            final Button deleteBtt = findViewById(R.id.delete);

            //TODO for going up in file directory
            backBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //Delete Button
            deleteBtt.setOnClickListener((new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder deteteDialog = new AlertDialog.Builder(Screen_FileViewer.this);
                    deteteDialog.setTitle("Confirmation");
                    deteteDialog.setMessage("Do you want to delete this file?");

                    deteteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < projectFiles.length; i++){
                                if(selection[i]){
                                    deleteFileOrFolder(projectFiles[i]);
                                    selection[i] = false;

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
                                }
                            }

                            projectFiles = dir.listFiles();
                            filesFoundCount = projectFiles.length;
                            filesList.clear();
                            for(int i=0; i < filesFoundCount; i++){
                                filesList.add(String.valueOf(projectFiles[i].getAbsolutePath()));
                            }
                            textAdapter.setData(filesList);
                        }
                    });

                    deteteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    deteteDialog.show();
                }
            }));

            isFileManagerInitialized = true;
        }
    }

    private void deleteFileOrFolder (File fileOrFolder){
        if (fileOrFolder.isDirectory()){
            if (fileOrFolder.list().length == 0){
                fileOrFolder.delete();
            }
            else{
                String files[] = fileOrFolder.list();
                for (String temp:files) {
                    File fileToDelete = new File(fileOrFolder, temp);
                    deleteFileOrFolder(fileToDelete);
                }
                if (fileOrFolder.list().length ==0){
                    fileOrFolder.delete();
                }
            }
        }
        else {
            fileOrFolder.delete();
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
                return directory.getAbsolutePath();
            }
            return directory.getAbsolutePath();
    }

    public String loadActivity(String fileName){
        String ret = "Did not save";

        File file = new File(getExternalFilesDir(null), fileName);

        try{
            InputStream input = new FileInputStream(file);

            if(input != null){
                InputStreamReader inp = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(inp);
                String receiveString = "";
                StringBuilder str = new StringBuilder();

                while( (receiveString = reader.readLine()) != null){
                    str.append(receiveString + "\n");
                }

                ret = str.toString();


                input.close();
                inp.close();
                reader.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            ret = null;
        }

        return ret;
    }
}



