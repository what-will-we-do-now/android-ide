package uk.ac.tees.v8036651.mode.FileViewer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8036651.mode.R;

public class Screen_FileViewer extends AppCompatActivity {

    private Object ArrayList;

    @Override
    protected void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        setContentView(R.layout.activity_file_viewer);

        final Button back = findViewById(R.id.back);
        final Button choose = findViewById(R.id.choose);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        choose.setOnClickListener((new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        }));

        final ListView fileList = findViewById(R.id.fileList);
        final TextAdapter textAdapter = new TextAdapter();
        fileList.setAdapter(textAdapter);

        List<String> example = new ArrayList<>();

        for(int i=0; i < 100; i++){
            example.add(String.valueOf(i));
        }

        textAdapter.setData(example);
    }
}

