package uk.ac.tees.cis2003.froyo.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    /**
     * This function returns the first location at which the two strings aren't the same
     * This function is based on https://stackoverflow.com/a/12089970/5008816
     * @param v1 the first string to be compared
     * @param v2 the second string to be compared
     * @return -1 if difference was not found, 0 based index of the first difference otherwise.
     */
    public static int indexOfDifference(String v1, String v2){
        if(v1 == null || v2 == null){
            return -1;
        }
        if(v1.equals(v2)){
            return -1;
        }
        int x;
        for(x = 0;  x < v1.length() && x < v2.length(); x++){
            if(v1.charAt(x) != v2.charAt(x)){
                return x;
            }
        }
        if(x < v2.length() || x < v1.length()){
            return x;
        }
        return -1;
    }

    /**
     * This function returns the number of the lines which is present in the text.
     * Note if file ends with a new line it will count as extra line.
     * @param text the text to check number of lines
     * @return number of lines in the text
     */
    public static int numberOfLines(String text){
        return text.split("\n").length + (text.endsWith("\n") ? 1 : 0);
    }

    /**
     * This function returns list of indexes at which the given regex match started in the given string.
     * @param text the text to be searched in.
     * @param regex the regex to be used.
     * @return list of indexes at which all regex matches started.
     */
    public static ArrayList<Integer> getIndexesOf(String text, String regex){
        Matcher matcher = Pattern.compile(regex).matcher(text);
        ArrayList<Integer> offsets = new ArrayList<>();
        while(matcher.find()){
            int offset = matcher.start();

            offsets.add(offset);
        }
        return offsets;
    }
}