package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.DocumentsContract;

import java.io.File;
import java.io.FileOutputStream;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class SaveActivity {
    private static final int CREATE_FILE = 1;

    private void createFile(Uri pickerInitialUri) {
        System.out.println("It entered");
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
        System.out.println("Check storage");
    }

    private void startActivityForResult(Intent intent, int createFile) {
    }
}
