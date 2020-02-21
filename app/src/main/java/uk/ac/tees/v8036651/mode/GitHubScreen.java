package uk.ac.tees.v8036651.mode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GitHubScreen extends AppCompatActivity {

    private static final int CREATE_FILE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_screen);
    }

    public void doSaveActivity(View view){
        saveActivity();
    }

    public void saveActivity(){
        System.out.println("It entered");
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, "Testing the file" );
        intent.putExtra(Intent.EXTRA_TITLE, "invoice");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
        System.out.println("Check storage");
    }
}
