package uk.ac.tees.cis2003.froyo.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;

public class FileUtils {

    /**
     * This function reads a file and returns string which is equivalent to the content of the file.
     *
     * @param file the file to be read
     * @return the content of the file as a string.
     * @throws IOException if the file could not be opened for reading.
     */
    public static String loadFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String receiveString;
        StringBuilder str = new StringBuilder();

        while ((receiveString = reader.readLine()) != null) {
            str.append(receiveString).append("\n");
        }
        reader.close();
        return str.toString();
    }

    /**
     * This function saves data to a file.
     *
     * @param path the path for the file to be saved at
     * @param data the data to be saved.
     * @throws IOException if the file could not be opened for writing.
     */
    public static void saveToFile(String path, String data) throws IOException {
        saveToFile(new File(path), data);
    }

    /**
     * This function saves data to a file.
     *
     * @param file the file for data to be saved to.
     * @param data the data to be saved.
     * @throws IOException if the file could not be opened for writing.
     */
    public static void saveToFile(File file, String data) throws IOException {
        //ensures that the parent directories exists, otherwise crash may happen.
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));

        out.write(data);

        out.flush();
        out.close();
    }

    /**
     * This function deletes a file or a directory recursively.
     *
     * @param file the file / directory to be deleted.
     */
    public static void purge(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                purge(child);
            }
        }
        file.delete();
    }

    /**
     * This function copies file to target location.
     *
     * @param file   the file to be copied.
     * @param target the new file which should be copied into.
     * @throws IOException if the source file couldn't be opened to read or
     *                     target file couldn't be opened to write
     */
    public static void copyFile(File file, File target) throws IOException {

        //ensure that parent of target exists
        File parent = target.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }


        if(file.isDirectory()){
            for(File rfile : file.listFiles()){
                copyFile(rfile, new File(target, rfile.getName()));
            }
        } else {
            //only copy file if the target isn't the same file
            if (!file.getAbsolutePath().equals(target.getAbsolutePath())) {
                saveToFile(target, loadFromFile(file));
            }
        }
    }

    /**
     * This function moves file from one location to another.
     *
     * @param file   the file to be moved.
     * @param target the new location for the file to be moved to.
     * @throws IOException if the source file couldn't be opened to read or
     *                     target file couldn't be opened to write
     */
    public static void moveFile(File file, File target) throws IOException {
        copyFile(file, target);
        purge(file);
    }

    /**
     * This is used to sort files. The sort should have two sorting conditions.
     * First this will sort the files by directories first and files second.
     * Secondly this will sort each directories and files to be sorted in alphabetical order.
     * Example result:
     * [folder] A2
     * [folder] B
     * [file] A1
     * [file] C
     *
     */
    public static final Comparator<File> SORT_TYPE_NAME = (o1, o2) -> {
        if ((o1.isDirectory() && o2.isDirectory()) || (o1.isFile() && o2.isFile())) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        } else if (o1.isDirectory() && o2.isFile()) {
            return -1;
        } else if (o1.isFile() && o2.isDirectory()) {
            return 1;
        }
        //this should never happen
        Log.wtf("Sorter", "NOOOO");
        return 0;
    };
}