package uk.ac.tees.cis2003.froyo.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtils {

    /**
     * This function reads a file and returns string which is equivalent to the content of the file.
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
     * @param path the path for the file to be saved at
     * @param data the data to be saved.
     * @throws IOException if the file could not be opened for writing.
     */
    public static void saveToFile(String path, String data) throws IOException {
        saveToFile(new File(path), data);
    }

    /**
     * This function saves data to a file.
     * @param file the file for data to be saved to.
     * @param data the data to be saved.
     * @throws IOException if the file could not be opened for writing.
     */
    public static void saveToFile(File file, String data) throws IOException {
        //ensures that the parent directories exists, otherwise crash may happen.
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));

        out.write(data);

        out.flush();
        out.close();
    }
}