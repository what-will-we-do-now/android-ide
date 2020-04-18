package uk.ac.tees.v8036651.mode.FileViewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.Toast;

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
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_file_viewer);
        rootPath = getProjectDirectory();
    }

    private String currentPath;
    private File[] projectFiles;
    private List<File> filesList;
    private int filesFoundCount;
    private File dir;
    private final TextAdapter textAdapter = new TextAdapter();

    private boolean isFileManagerInitialized = false;

    private boolean[] selection;
    private int selectionCount = 0;
    private boolean longClick = false;

    private int selectedItemIndex;

    private File currentCopied;
    private boolean isCurrentCopiedCut;

    //Runs whenever the view is resumed
    @Override
    protected void onResume(){
        super.onResume();

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
                    if (currentPath.contains(rootPath + "/")){
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

                    selection[position] = !selection[position];
                    textAdapter.setSelection(selection);

                    selectionCount = 0;
                    for (boolean aSelection : selection) {
                        if (aSelection) {
                            selectionCount++;
                            break;
                        }
                    }

                    System.out.println("SELECTION COUNT: " + selectionCount);

                    buttonCheck(position);

                    return true;
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
                            intent.putExtra("OpenFile", filesList.get(position).getAbsolutePath());
                            startActivity(intent);
                        }
                    }
                }
            });

            final Button deleteBtt = findViewById(R.id.delete_btt);
            final Button renameBtt = findViewById(R.id.rename_btt);
            final Button copyBtt = findViewById(R.id.copy_btt);
            final Button cutBtt = findViewById(R.id.cut_btt);
            final Button pasteBtt = findViewById(R.id.paste_btt);

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
                            for (int position = 0; position < projectFiles.length; position++){
                                if(selection[position]){
                                    deleteFileOrFolder(projectFiles[position]);
                                    selection[position] = false;

                                    buttonCheck(position);
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

            renameBtt.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder renameDialog = new AlertDialog.Builder(Screen_FileViewer.this);
                    renameDialog.setTitle("Rename file to: ");
                    final EditText newNameInput = new EditText(Screen_FileViewer.this);

                    String filePath = projectFiles[selectedItemIndex].getAbsolutePath();

                    newNameInput.setText(filePath.substring(filePath.lastIndexOf('/') + 1));
                    newNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    renameDialog.setView(newNameInput);

                    renameDialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newName = new File(filePath).getParent() + '/' + newNameInput.getText();
                            File newFile = new File(newName);
                            new File(filePath).renameTo(new File(newName));
                            refresh();
                        }
                    });

                    renameDialog.show();
                }
            }));

            copyBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.paste_btt).setVisibility(View.VISIBLE);
                    currentCopied = projectFiles[selectedItemIndex];
                    isCurrentCopiedCut = false;
                    Toast.makeText(Screen_FileViewer.this, "File Copied", Toast.LENGTH_LONG).show();
                }
            });

            cutBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.paste_btt).setVisibility(View.VISIBLE);
                    currentCopied = projectFiles[selectedItemIndex];
                    isCurrentCopiedCut = true;
                    Toast.makeText(Screen_FileViewer.this, "File Cut", Toast.LENGTH_LONG).show();
                }
            });

            pasteBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        saveActivity(loadActivity(currentCopied), currentCopied.getName());
                        Toast.makeText(Screen_FileViewer.this, "File Pasted", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (isCurrentCopiedCut){
                        deleteFileOrFolder(currentCopied);
                    }

                    findViewById(R.id.paste_btt).setVisibility(View.GONE);
                    refresh();
                }
            });

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

    //returns string path to the main storage of the project code
    private String getProjectDirectory () {
        return Project.openedProject.getRoot().getAbsolutePath();
    }

    public String loadActivity(File file){
        String ret;

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

        selection = new boolean[filesFoundCount];
        textAdapter.setSelection(selection);
        textAdapter.setData(filesList);
    }

    private void buttonCheck(int position){
        selectionCount = 0;
        for (boolean aSelection : selection) {
            if (aSelection) {
                selectionCount++;
                break;
            }
        }

        if (selectionCount == 1) {
            selectedItemIndex = position;
            findViewById(R.id.rename_btt).setVisibility(View.VISIBLE);
            findViewById(R.id.cut_btt).setVisibility(View.VISIBLE);
            findViewById(R.id.copy_btt).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.rename_btt).setVisibility(View.GONE);
            findViewById(R.id.cut_btt).setVisibility(View.GONE);
            findViewById(R.id.copy_btt).setVisibility(View.GONE);
        }

        if (selectionCount >= 1){
            longClick = true;
            findViewById(R.id.delete_btt).setVisibility(View.VISIBLE);
        }
        else {
            longClick = false;
            findViewById(R.id.delete_btt).setVisibility(View.GONE);
        }
    }
}




