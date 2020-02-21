package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class IdeScreen extends AppCompatActivity {

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ide_screen);


        TextView txtCode = (TextView) findViewById(R.id.txtCode);

        txtCode.addTextChangedListener(new TextWatcher() {

            boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ignore) {
                    return;
                }

                ignore = true;
                PluginManager.formatText((TextView) findViewById(R.id.txtCode), null);
                ignore = false;
            }
        });

        txtCode.setHorizontallyScrolling(true);
    }
}