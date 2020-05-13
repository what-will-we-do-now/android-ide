package uk.ac.tees.cis2003.froyo.FileViewer;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import uk.ac.tees.cis2003.froyo.GUI.MapAdapter;
import uk.ac.tees.cis2003.froyo.Projects.Project;
import uk.ac.tees.cis2003.froyo.R;
import uk.ac.tees.cis2003.froyo.plugins.PluginManager;

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
    private List<File> filesList;
    private int filesFoundCount;
    private File dir;
    private final TextAdapter textAdapter = new TextAdapter();

    private boolean isFileManagerInitialized = false;

    private boolean[] selection;
    private int selectionCount = 0;
    private boolean longClick = false;

    private int selectedItemIndex;

    private ArrayList<File> currentCopied = new ArrayList<>();
    private boolean isCurrentCopiedCut;

    //Runs whenever the view is resumed
    @Override
    protected void onResume(){
        super.onResume();

        if (!isFileManagerInitialized) {
            currentPath = rootPath;
            dir = new File(rootPath);
            File[] projectFiles = dir.listFiles();

            final TextView pathOutput = findViewById(R.id.screen_file_viewer_dir_name);
            pathOutput.setText(currentPath.substring(currentPath.lastIndexOf('/') + 1));

            if (projectFiles != null){
               filesFoundCount = projectFiles.length;
            }
            else {
                filesFoundCount = 0;
            }

            final ListView listView = findViewById(R.id.screen_file_viewer_file_list);

            listView.setAdapter(textAdapter);

            filesList = new ArrayList<>();
            //Lists all of the files in the specified directory
            for(int i=0; i < filesFoundCount; i++){
                filesList.add(projectFiles[i]);
            }
            Collections.sort(filesList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if((o1.isDirectory() && o2.isDirectory()) || (o1.isFile() && o2.isFile())) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                    else if(o1.isDirectory() && o2.isFile()){
                        return -1;
                    }
                    else if(o1.isFile() && o2.isDirectory()){
                        return 1;
                    }
                    //this should never happen
                    Log.wtf("Sorter", "NOOOO");
                    return 0;
                }
            });
            textAdapter.setData(filesList);

            selection = new boolean[filesFoundCount];

            //Button to go upwards in directory
            final ImageButton upDirectoryButton = findViewById(R.id.screen_file_viewer_up_directory);
            upDirectoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPath.contains(rootPath + "/")){
                        currentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
                        dir = new File(currentPath);
                        pathOutput.setText(currentPath.substring(currentPath.lastIndexOf('/') + 1));
                    }
                    refresh();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    selection[position] = !selection[position];
                    textAdapter.setSelection(selection);

                    selectedItemIndex = position;
                    buttonCheck();

                    return true;
                }
            });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!longClick){
                        if (filesList.get(position).isDirectory()){
                            currentPath = (currentPath + '/' +filesList.get(position).getName());
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

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("OpenFile",filesList.get(position).getAbsolutePath());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                }
            });

            final Button deleteBtt = findViewById(R.id.screen_file_viewer_button_delete);
            final Button renameBtt = findViewById(R.id.screen_file_viewer_button_rename);
            final Button copyBtt = findViewById(R.id.screen_file_viewer_button_copy);
            final Button cutBtt = findViewById(R.id.screen_file_viewer_button_cut);
            final Button pasteBtt = findViewById(R.id.screen_file_viewer_button_paste);

            //Delete Button
            deleteBtt.setOnClickListener((new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder deteteDialog = new AlertDialog.Builder(Screen_FileViewer.this);
                    deteteDialog.setTitle(getResources().getString(R.string.file_manager_dialog_delete_title));
                    deteteDialog.setMessage(getResources().getString(R.string.file_manager_dialog_delete_description));

                    deteteDialog.setPositiveButton(getResources().getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int position = 0; position < filesList.size(); position++){
                                if(selection[position]){
                                    deleteFileOrFolder(filesList.get(position));
                                    selection[position] = false;
                                    selectedItemIndex = position;
                                }
                            }
                            refresh();
                        }
                    });

                    deteteDialog.setNegativeButton(getResources().getString(R.string.answer_no), null);

                    deteteDialog.show();
                }
            }));

            renameBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder renameDialog = new AlertDialog.Builder(Screen_FileViewer.this);
                    renameDialog.setTitle(getResources().getString(R.string.file_manager_dialog_rename_title));
                    final EditText newNameInput = new EditText(Screen_FileViewer.this);

                    String filePath = filesList.get(selectedItemIndex).getAbsolutePath();

                    newNameInput.setText(filePath.substring(filePath.lastIndexOf('/') + 1));
                    newNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    renameDialog.setView(newNameInput);

                    renameDialog.setPositiveButton(getResources().getString(R.string.answer_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newName = new File(filePath).getParent() + '/' + newNameInput.getText();
                            File newFile = new File(newName);
                            if (Project.openedProject.getLastFile().equals(new File(filePath))){
                                try {
                                    Project.openedProject.setLastFile(newFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            new File(filePath).renameTo(newFile);
                            refresh();
                        }
                    });

                    renameDialog.setNegativeButton(R.string.answer_cancel, null);

                    renameDialog.show();
                }
            });

            copyBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.screen_file_viewer_button_paste).setVisibility(View.VISIBLE);

                    for (int position = 0; position < filesList.size(); position++){
                        if(selection[position]){
                            currentCopied.add(filesList.get(position));
                            selection[position] = false;
                        }
                    }

                    isCurrentCopiedCut = false;
                    Toast.makeText(Screen_FileViewer.this, getResources().getString(R.string.file_manager_message_file_copied), Toast.LENGTH_SHORT).show();
                    refresh();
                }
            });

            cutBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.screen_file_viewer_button_paste).setVisibility(View.VISIBLE);

                    for (int position = 0; position < filesList.size(); position++){
                        if(selection[position]){
                            currentCopied.add(filesList.get(position));
                            selection[position] = false;
                        }
                    }
                    isCurrentCopiedCut = true;
                    Toast.makeText(Screen_FileViewer.this, getResources().getString(R.string.file_manager_message_file_cut), Toast.LENGTH_SHORT).show();
                    refresh();
                }
            });

            pasteBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int filesUnpasted = 0;
                    int filesPasted = 0;
                    int totalFiles = 0;

                    for (File copiedFile : currentCopied){

                        for (File file : filesList){
                            if (file.equals(copiedFile)){
                                filesUnpasted++;
                                break;
                            }
                        }

                        try {
                            saveActivity(loadActivity(copiedFile), copiedFile.getName());
                            filesPasted++;
                        } catch (Exception e) {
                            filesUnpasted++;
                            e.printStackTrace();
                        }

                        if (isCurrentCopiedCut){
                            deleteFileOrFolder(copiedFile);
                        }

                        findViewById(R.id.screen_file_viewer_button_paste).setVisibility(View.GONE);
                    }

                    totalFiles = filesPasted + filesUnpasted;
                    refresh();

                    if (filesUnpasted == 0){
                        Toast.makeText(Screen_FileViewer.this, getResources().getQuantityString(R.plurals.file_manager_message_all_file_pasted, filesPasted, filesPasted), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Screen_FileViewer.this, getResources().getQuantityString(R.plurals.file_manager_message_all_file_pasted, totalFiles, filesPasted, totalFiles), Toast.LENGTH_SHORT).show();
                    }
                    currentCopied.clear();
                }
            });

            currentCopied.clear();
            isFileManagerInitialized = true;
            refresh();
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

                inflater = LayoutInflater.from(Screen_FileViewer.this).inflate(R.layout.dialog_file_new, null);
                newFileDialog.setView(inflater);

                input = inflater.findViewById(R.id.dialog_file_new_name);
                Spinner language = inflater.findViewById(R.id.dialog_file_new_lang_type);
                Spinner template = inflater.findViewById(R.id.dialog_file_new_template);

                ArrayAdapter languageContent = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PluginManager.getProjectTypes());

                language.setAdapter(languageContent);

                language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //ArrayAdapter templateContent = new ArrayAdapter<>(Screen_FileViewer.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(PluginManager.getTemplatesFor(language.getSelectedItem().toString()).keySet()));
                        MapAdapter templateContent = new MapAdapter(Screen_FileViewer.this, android.R.layout.simple_spinner_dropdown_item, PluginManager.getTemplatesFor(language.getSelectedItem().toString()));
                        template.setAdapter(templateContent);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                newFileDialog.setPositiveButton(R.string.answer_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String lang = ((Spinner)language.findViewById(R.id.dialog_file_new_lang_type)).getSelectedItem().toString();
                            String temp = ((Spinner)template.findViewById(R.id.dialog_file_new_template)).getSelectedItem().toString();

                            Map<String, String> values = new HashMap<>();
                            values.put("filename", input.getText().toString());

                            String templateData = PluginManager.getTemplate(lang, temp, values);
                            saveActivity(templateData, input.getText().toString() + "." + PluginManager.getDefaultFileExtensionFor(lang));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        refresh();

                    }
                });
                newFileDialog.setNegativeButton(R.string.answer_cancel, null);
                newFileDialog.show();
                return true;

            case R.id.create_folder:

                final AlertDialog.Builder newFoldereDialog = new AlertDialog.Builder(Screen_FileViewer.this);

                inflater = LayoutInflater.from(Screen_FileViewer.this).inflate(R.layout.dialog_set_directory_name, null);
                input = inflater.findViewById(R.id.dialog_set_directory_name_new_name);

                newFoldereDialog.setView(inflater);
                newFoldereDialog.setPositiveButton(R.string.answer_save, new DialogInterface.OnClickListener() {
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
                newFoldereDialog.setNegativeButton(R.string.answer_cancel, null);
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
        File[] projectFiles = dir.listFiles();

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

        Collections.sort(filesList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if((o1.isDirectory() && o2.isDirectory()) || (o1.isFile() && o2.isFile())) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
                else if(o1.isDirectory() && o2.isFile()){
                    return -1;
                }
                else if(o1.isFile() && o2.isDirectory()){
                    return 1;
                }
                //this should never happen
                Log.wtf("Sorter", "NOOOO");
                return 0;
            }
        });

        selection = new boolean[filesFoundCount];
        textAdapter.setSelection(selection);
        textAdapter.setData(filesList);

        buttonCheck();
    }

    private void buttonCheck(){
        selectionCount = 0;
        for (boolean aSelection : selection) {
            if (aSelection) {
                selectionCount++;
            }
        }

        if (selectionCount == 1) {
            findViewById(R.id.screen_file_viewer_button_rename).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.screen_file_viewer_button_rename).setVisibility(View.GONE);
        }

        if (selectionCount >= 1){
            longClick = true;
            findViewById(R.id.screen_file_viewer_button_delete).setVisibility(View.VISIBLE);
            findViewById(R.id.screen_file_viewer_button_cut).setVisibility(View.VISIBLE);
            findViewById(R.id.screen_file_viewer_button_copy).setVisibility(View.VISIBLE);
        }
        else {
            longClick = false;
            findViewById(R.id.screen_file_viewer_button_delete).setVisibility(View.GONE);
            findViewById(R.id.screen_file_viewer_button_cut).setVisibility(View.GONE);
            findViewById(R.id.screen_file_viewer_button_copy).setVisibility(View.GONE);
        }
    }
}




