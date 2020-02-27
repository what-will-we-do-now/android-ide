package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;

import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class IdeScreen extends AppCompatActivity {

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
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}