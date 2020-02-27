package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Junk_Screen_Git extends AppCompatActivity {

    private static final int CREATE_FILE = 988;
    private static final int WRITE_REQUEST_CODE = 45;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.junk_screen_git);
    }

    public void doSaveActivity(View view) throws Exception {
        /*
        System.out.println("It Entered");
        FileInputStream ins = new FileInputStream(getApplicationContext().getFilesDir()+"/file.txt");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int size;
        byte[] buffer = new byte[1024];
        System.out.println("Buffer created");
        try{
            while ((size = ins.read(buffer, 0, 1024)) >= 0){
                output.write(buffer, 0, size);
            }
            ins.close();
            buffer = output.toByteArray();
            String fileName = "Test";
            FileOutputStream fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(buffer);
            fos.close();
        }
        catch(Exception fu){
            fu.printStackTrace();
        }
        System.out.println("Check Storage");
        */

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "example.txt");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Writing to file!");
        } catch (IOException e) {
            //Handle exception
        }

        //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("config.txt", Context.MODE_PRIVATE));
        //outputStreamWriter.write("Hello!!");
        //outputStreamWriter.close();
    }

    public void saveActivity(ContextWrapper con) throws Exception {
        System.out.println("Entered");
        File file = new File(con.getFilesDir(), "Test");
        String filename = "TestFile";
        String fileContents = "This is a test";
        InputStream is;

        FileInputStream fis = con.openFileInput(filename);
        InputStreamReader inp = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder str = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(inp)) {
            String line = reader.readLine();
            while(line != null){
                str.append(line).append('\n');
                line = reader.readLine();
            }
        }
        catch(Exception ex){

        }
        System.out.println("Buffered Reader created");
        try(FileOutputStream fos = con.openFileOutput(filename, Context.MODE_PRIVATE)){
            file.createNewFile();
            file.setWritable(true);
            file.setReadable(true);
            fos.write(toByteArray(new ByteArrayInputStream(fileContents.getBytes(StandardCharsets.UTF_8))));
            
        }
        catch(Exception ex){
        }

    }

    public void oldeSaveActivity(){
        System.out.println("It entered");

        Intent saveIntent = new Intent();
        saveIntent.setAction(Intent.ACTION_CREATE_DOCUMENT);
        saveIntent.putExtra(Intent.EXTRA_TITLE,"Testing the void");
        saveIntent.putExtra(Intent.EXTRA_TEXT, "Testing the file" );

        saveIntent.addCategory(Intent.CATEGORY_OPENABLE);
        saveIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        saveIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        saveIntent.setType("text/plain");
        System.out.println(saveIntent.getExtras());
        System.out.println(saveIntent.toString());
        System.out.println(saveIntent.describeContents());
        startActivityForResult(saveIntent,WRITE_REQUEST_CODE);
        System.out.println(saveIntent.describeContents());
        System.out.println("Check storage");

        /*

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.putExtra(Intent.EXTRA_TEXT, "Testing the file" );
        intent.putExtra(Intent.EXTRA_TITLE, "invoice");
        intent.setType("text/plain");
        System.out.println(intent.getExtras());

        startActivityForResult(intent, CREATE_FILE);
        System.out.println("Check storage");
        */
    }

    public byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i;
        byte[] data = new byte[1024];
        while((i = is.read(data, 0, data.length)) != -1){
            buffer.write(data, 0, 1);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
