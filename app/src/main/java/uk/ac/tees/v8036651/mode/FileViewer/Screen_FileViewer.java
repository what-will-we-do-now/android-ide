package uk.ac.tees.v8036651.mode.FileViewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.ac.tees.v8036651.mode.Projects.Project;
import uk.ac.tees.v8036651.mode.R;
import uk.ac.tees.v8036651.mode.Screen_IDE;

public class Screen_FileViewer extends AppCompatActivity {

    private String rootPath;

    @Override
    protected void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        setContentView(R.layout.screen_file_viewer);
        rootPath = getCodeDirectory();
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

    private String currentPath;
    private File[] projectFiles;
    private List<File> filesList;
    private int filesFoundCount;
    private File dir;
    private final TextAdapter textAdapter = new TextAdapter();

    private boolean isFileManagerInitialized = false;

    private boolean[] selection;
    private boolean longClick = false;

    //Runs whenever the view is resumed
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if (!isFileManagerInitialized) {
            currentPath = rootPath;
            dir = new File(rootPath);
            projectFiles = dir.listFiles();

            final TextView pathOutput = findViewById(R.id.dir_name);
            pathOutput.setText(currentPath.substring(currentPath.lastIndexOf('/') + 1));

            if (projectFiles != null){
               filesFoundCount = projectFiles.length;
            }
            else {
                filesFoundCount = 0;
            }

            final ListView listView = findViewById(R.id.file_list);

            listView.setAdapter(textAdapter);

            filesList = new ArrayList<>();
            //Lists all of the files in the specified directory
            for(int i=0; i < filesFoundCount; i++){
                filesList.add(projectFiles[i]);
            }
            textAdapter.setData(filesList);

            selection = new boolean[filesFoundCount];

            //Button to go upwards in directory
            final ImageButton upDirectoryButton = findViewById(R.id.up_directory);
            upDirectoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPath.contains(rootPath)){
                        currentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
                        dir = new File(currentPath);
                        pathOutput.setText(currentPath.substring(currentPath.lastIndexOf('/') + 1));
                        refresh();
                    }
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    longClick = true;

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
                        findViewById(R.id.delete_btt).setEnabled(true);
                    } else {
                        findViewById(R.id.delete_btt).setEnabled(false);
                        longClick = true;
                    }

                    return false;
                }
            });


            final Intent intent = new Intent(this, Screen_IDE.class);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!longClick){
                        if (projectFiles[position].isDirectory()){
                            currentPath = (currentPath + '/' + projectFiles[position].getName());
                            dir = new File(currentPath);
                            pathOutput.setText(currentPath.substring(currentPath.lastIndexOf('/') + 1));
                            refresh();
                        }

                        else {

                            try {
                                Project.openedProject.setLastFile(filesList.get(position));
                            } catch (IOException e) {
                                Log.e("File Manager", "Failed to save new project configuration file", e);
                            }

                            Screen_IDE.editTextContent =(loadActivity(filesList.get(position).getAbsolutePath()));
                            startActivity(intent);
                        }
                    }
                }
            });

            final Button deleteBtt = findViewById(R.id.delete_btt);

            //TODO Delete Button
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
                                        findViewById(R.id.delete_btt).setEnabled(true);
                                    } else {
                                        findViewById(R.id.delete_btt).setEnabled(false);
                                    }
                                }
                            }

                            refresh();
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
        else {
            refresh();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_viewer_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //TODO To be expanded
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        View inflater;
        final EditText input;

        switch (item.getItemId()){
            case R.id.create_file:

                final AlertDialog.Builder newFileDialog = new AlertDialog.Builder(Screen_FileViewer.this);

                inflater = LayoutInflater.from(Screen_FileViewer.this).inflate(R.layout.set_file_name_alert_dialog, null);
                input = inflater.findViewById(R.id.fileNameEditText);

                newFileDialog.setView(inflater);
                newFileDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            saveActivity("", input.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        refresh();

                    }
                });
                newFileDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newFileDialog.show();
                return true;

            case R.id.create_folder:

                final AlertDialog.Builder newFoldereDialog = new AlertDialog.Builder(Screen_FileViewer.this);

                inflater = LayoutInflater.from(Screen_FileViewer.this).inflate(R.layout.set_directory_name_alret_dialog, null);
                input = inflater.findViewById(R.id.directoryNameEditText);

                newFoldereDialog.setView(inflater);
                newFoldereDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            final File newDirectory = new File(currentPath + '/' + input.getText().toString());
                            if (!newDirectory.exists()){
                                newDirectory.mkdir();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        refresh();

                    }
                });
                newFoldereDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newFoldereDialog.show();
                return true;

            case R.id.refresh_menu_option:
                refresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void deleteFileOrFolder (File fileOrFolder){
        if (fileOrFolder.isDirectory()){
            if (Objects.requireNonNull(fileOrFolder.list()).length == 0){
                fileOrFolder.delete();
            }
            else{
                String[] files = fileOrFolder.list();
                for (String temp: Objects.requireNonNull(files)) {
                    File fileToDelete = new File(fileOrFolder, temp);
                    deleteFileOrFolder(fileToDelete);
                }
                if (Objects.requireNonNull(fileOrFolder.list()).length ==0){
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
    private String getCodeDirectory () {
        File directory = new File(Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath() + "/MoDE_Code_Directory");
        return directory.getAbsolutePath();
    }

    public String loadActivity(String fileName){
        String ret;

        File file = new File(fileName);

        try{
            InputStream input = new FileInputStream(file);

            InputStreamReader inp = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inp);
            String receiveString;
            StringBuilder str = new StringBuilder();

            while( (receiveString = reader.readLine()) != null){
                str.append(receiveString).append("\n");
            }

            ret = str.toString();


            input.close();
            inp.close();
            reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
            ret = null;
        }

        return ret;
    }

    //Saves files to specified directory
    public void saveActivity(String data, String fileName) throws Exception{

        File file = new File(currentPath, fileName);

        FileOutputStream output = new FileOutputStream(file);
        OutputStreamWriter out = new OutputStreamWriter(output);
        out.write(data);
        out.flush();
        out.close();
        output.flush();
        output.close();
    }

    private void refresh (){
        projectFiles = dir.listFiles();

        if (projectFiles == null) {
            filesFoundCount = 0;
        }
        else {
            filesFoundCount = projectFiles.length;
        }

        filesList.clear();
        for(int i=0; i < filesFoundCount; i++){
            filesList.add(projectFiles[i]);
        }
        textAdapter.setData(filesList);
    }
}




