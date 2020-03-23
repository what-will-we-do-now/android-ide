package uk.ac.tees.v8036651.mode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IdeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ide_screen);

        TextView txtCode = (TextView) findViewById(R.id.txtCode);
        //apparently Horizontal Scrolling in XML is not working. Thanks Android
        txtCode.setHorizontallyScrolling(true);

    }
}