package uk.ac.tees.v8036651.mode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import uk.ac.tees.v8036651.mode.FileViewer.Screen_FileViewer;
import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

import android.view.View.OnKeyListener;

public class Screen_IDE extends AppCompatActivity {

    public static String projectsDirectory = "";
    private static String fileName = null;
    public static String editTextContent = "";

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_ide_screen);

        projectsDirectory = getExternalFilesDir(null).getAbsolutePath() + "/MoDE_Code_Directory";

        NumberedTextView txtCode = (NumberedTextView) findViewById(R.id.txtCode);

        txtCode.setText(editTextContent);
        PluginManager.formatText(txtCode, new File("code.java"));

        txtCode.setOnKeyListener(new OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!event.isCanceled() && keyCode == KeyEvent.KEYCODE_TAB && event.getAction() == KeyEvent.ACTION_UP){
                    NumberedTextView txtCode = ((NumberedTextView) v);
                    txtCode.getText().insert(txtCode.getSelectionStart(), "    ");
                }
                return false;
            }
        });

        txtCode.addTextChangedListener(new TextWatcher() {

            boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!ignore) {
                    ignore = true;
                    NumberedTextView txtCode = findViewById(R.id.txtCode);
                    if (count == 1 && s.charAt(start) == '(') {
                        if(txtCode.getText().toString().replaceAll("([^(|^)])", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, ")");
                            txtCode.setSelection(start + count);
                        }
                    }else if (count == 1 && s.charAt(start) == '{') {
                        if(txtCode.getText().toString().replaceAll("([^{|^}])", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, "}");
                            txtCode.setSelection(start + count);
                        }
                    }else if (count == 1 && s.charAt(start) == '[') {
                        if(txtCode.getText().toString().replaceAll("([^\\[|^\\]])", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, "]");
                            txtCode.setSelection(start + count);
                        }
                    }else if (count == 1 && s.charAt(start) == '"') {
                        if(s.length() > start + 1 && s.charAt(start + 1) == '\"'){
                            String pre = txtCode.getText().toString().substring(0, start) + txtCode.getText().toString().substring(start + count);
                            //check if the brackets were balanced before
                            if(pre.replaceAll("[^\"]", "").length() % 2 == 0){
                                txtCode.setText(pre);
                                txtCode.setSelection(start + count);
                            }
                        }else if(txtCode.getText().toString().replaceAll("[^\"]", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, "\"");
                            txtCode.setSelection(start + count);
                        }
                    }else if (count == 1 && s.charAt(start) == '\'') {
                        if(s.length() > start + 1 && s.charAt(start + 1) == '\''){
                            String pre = txtCode.getText().toString().substring(0, start) + txtCode.getText().toString().substring(start + count);
                            //check if the brackets were balanced before
                            if(pre.replaceAll("[^\']", "").length() % 2 == 0){
                                txtCode.setText(pre);
                                txtCode.setSelection(start + count);
                            }
                        }else if(txtCode.getText().toString().replaceAll("[^\']", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, "\'");
                            txtCode.setSelection(start + count);
                        }
                    }else if(count == 1 && s.charAt(start) == ')'){
                        if(s.length() > start + 1 && s.charAt(start + 1) == ')'){
                            String pre = txtCode.getText().toString().substring(0, start) + txtCode.getText().toString().substring(start + count);
                            //check if the brackets were balanced before
                            if(pre.replaceAll("([^(|^)])", "").length() % 2 == 0){
                                txtCode.setText(pre);
                                txtCode.setSelection(start + count);
                            }
                        }
                    }else if(count == 1 && s.charAt(start) == '}'){
                        if(s.length() > start + 1 && s.charAt(start + 1) == '}'){
                            String pre = txtCode.getText().toString().substring(0, start) + txtCode.getText().toString().substring(start + count);
                            //check if the brackets were balanced before
                            if(pre.replaceAll("([^{|^}])", "").length() % 2 == 0){
                                txtCode.setText(pre);
                                txtCode.setSelection(start + count);
                            }
                        }
                    }else if(count == 1 && s.charAt(start) == ']'){
                        if(s.length() > start + 1 && s.charAt(start + 1) == ']'){
                            String pre = txtCode.getText().toString().substring(0, start) + txtCode.getText().toString().substring(start + count);
                            //check if the brackets were balanced before
                            if(pre.replaceAll("([^\\[|^\\]])", "").length() % 2 == 0){
                                txtCode.setText(pre);
                                txtCode.setSelection(start + count);
                            }
                        }
                    }
                    ignore = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!ignore) {
                    ignore = true;
                    PluginManager.formatText((NumberedTextView) findViewById(R.id.txtCode), new File("code.java"));
                    ignore = false;
                }
            }
        });

        txtCode.setHorizontallyScrolling(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ide_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //TODO To be extended when more functionality is added
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_code:
                Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();

                if (fileName == null){
                    final AlertDialog.Builder setFileNameDialog = new AlertDialog.Builder(Screen_IDE.this);
                    View inflater = LayoutInflater.from(this).inflate(R.layout.set_file_name_alert_dialog, null);

                    final EditText input = (EditText) inflater.findViewById(R.id.fileNameEditText);

                    setFileNameDialog.setView(inflater);
                    setFileNameDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fileName = input.getText().toString();

                            try {
                                saveActivity(((NumberedTextView) findViewById(R.id.txtCode)).getText().toString(), fileName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    setFileNameDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    setFileNameDialog.show();
                }


                return true;
            case R.id.settings_nav:
                startActivity(new Intent(Screen_IDE.this, Screen_Settings.class));
                return true;
            case R.id.fileview_nav:
                startActivity(new Intent(Screen_IDE.this, Screen_FileViewer.class));
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //Saves files to specified directory
    public void saveActivity(String data, String fileName) throws Exception{

        File file = new File(projectsDirectory, fileName);

        FileOutputStream output = new FileOutputStream(file);
        OutputStreamWriter out = new OutputStreamWriter(output);
        out.write(data);
        out.flush();
        out.close();
        output.flush();
        output.close();
    }
}
