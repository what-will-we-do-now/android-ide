package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import uk.ac.tees.v8036651.mode.FileViewer.Screen_FileViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class Screen_IDE extends AppCompatActivity {

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ide_screen);

        NumberedTextView txtCode = (NumberedTextView) findViewById(R.id.txtCode);

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
                        if(txtCode.getText().toString().replaceAll("[^\"]", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, "\"");
                            txtCode.setSelection(start + count);
                        }
                    }else if (count == 1 && s.charAt(start) == '\'') {
                        if(txtCode.getText().toString().replaceAll("[^\']", "").length() % 2 != 0) {
                            txtCode.getText().insert(start + count, "\'");
                            txtCode.setSelection(start + count);
                        }
                    }
                    ignore = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!ignore) {
                    if (s.toString().contains("\n")) {
                        ignore = true;
                        PluginManager.formatText((NumberedTextView) findViewById(R.id.txtCode), new File("code.java"));
                        ignore = false;
                    }
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

    //To be extended when more functionality is added
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_code:
                Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();


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

    public void saveActivity(String data, String fileName) throws Exception{

        FileOutputStream output = openFileOutput(fileName, MODE_PRIVATE);
        OutputStreamWriter out = new OutputStreamWriter(output);
        out.write(data);
        out.flush();
        out.close();
        output.flush();
        output.close();
    }

    public String loadActivity(String fileName){
        String ret = "Did not save";

        try{
            InputStream input = openFileInput(fileName);

            if(input != null){
                InputStreamReader inp = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(inp);
                String receiveString = "";
                StringBuilder str = new StringBuilder();

                while( (receiveString = reader.readLine()) != null){
                    str.append(receiveString);
                }

                ret = str.toString();


                input.close();
                inp.close();
                reader.close();
            }
        }
        catch(Exception e){
            ret = null;
        }

        return ret;
    }
}
