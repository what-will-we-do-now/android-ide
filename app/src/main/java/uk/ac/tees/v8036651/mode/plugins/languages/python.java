package uk.ac.tees.v8036651.mode.plugins.languages;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.v8036651.mode.plugins.ColorInfo;
import uk.ac.tees.v8036651.mode.plugins.Plugin;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class python extends Plugin {

    private static final ArrayList<String> TOKENS;
    static{
        TOKENS = new ArrayList<>();
        TOKENS.add("def");
        TOKENS.add("class");

        TOKENS.add("if");
        TOKENS.add("elif");
        TOKENS.add("else");
        TOKENS.add("break");
        TOKENS.add("continue");
        TOKENS.add("while");
        TOKENS.add("for");
        TOKENS.add("return");
        TOKENS.add("pass");

        TOKENS.add("and");
        TOKENS.add("not");
        TOKENS.add("or");
        TOKENS.add("del");
        TOKENS.add("in");
        TOKENS.add("is");

        TOKENS.add("True");
        TOKENS.add("False");
        TOKENS.add("None");

        TOKENS.add("nonlocal");
        TOKENS.add("global");

        TOKENS.add("from");
        TOKENS.add("import");
        TOKENS.add("as");

        TOKENS.add("try");
        TOKENS.add("except");
        TOKENS.add("finally");

        TOKENS.add("with");
        TOKENS.add("lambda");
        TOKENS.add("raise");
        TOKENS.add("yield");
        TOKENS.add("assert");
    }

    public python() {
        registerSupportedFiletype("py");
        registerTemplate("EMPTY", "Empty python file");
        registerTemplate("EMPTY_MAIN", "Python with main statement");
    }

    @Override
    public ColorInfo[] formatText(String code, String type) {
        ArrayList<ColorInfo> formattedCode = new ArrayList<>();

        for(String token : TOKENS) {
            ArrayList<Integer> offsets = getOffsetsFor(code, "\\b" + token + "\\b");
            for(int off : offsets){
                formattedCode.add(new ColorInfo(off, token.length(), PluginManager.COLOR_KEYWORD, 5));
            }
        }

        /**
         * Matches singleline comments starting with #
         * NOTE: Python doesn't have multiline comments
         */
        Matcher matcherSinglelineComments = Pattern.compile("\\#.*").matcher(code);

        while(matcherSinglelineComments.find()){
            int offset = matcherSinglelineComments.start();
            formattedCode.add(new ColorInfo(offset, matcherSinglelineComments.end() - offset, PluginManager.COLOR_COMMENT, 0));
        }

        /**
         * Matches all digits
         */
        Matcher matcherDigits = Pattern.compile("[0-9]+").matcher(code);

        while(matcherDigits.find()){
            int offset = matcherDigits.start();
            formattedCode.add(new ColorInfo(offset, matcherDigits.end() - offset, PluginManager.COLOR_NUMBER, 10));
        }

        /**
         * Matches singleline strings
         */

        Matcher matcherSingleStrings = Pattern.compile("(\"(.*?)\")|(\'(.*?)\')").matcher(code);
        while(matcherSingleStrings.find()){
            int offset = matcherSingleStrings.start();
            formattedCode.add(new ColorInfo(offset, matcherSingleStrings.end() - offset, PluginManager.COLOR_STRING, 1));
        }

        /**
         * Matches multiline strings
         *
         */
        Matcher matcherMultiStrings = Pattern.compile("\\\"\\\"\\\"([\\S\\s]*?)\\\"\\\"\\\"").matcher(code);
        while(matcherMultiStrings.find()){
            int offset = matcherMultiStrings.start();
            formattedCode.add(new ColorInfo(offset, matcherMultiStrings.end() - offset, PluginManager.COLOR_STRING, 1));
        }

        ColorInfo[] codeFinal = new ColorInfo[formattedCode.size()];

        for(int x = 0; x < formattedCode.size(); x++){
            codeFinal[x] = formattedCode.get(x);
        }

        return codeFinal;
    }

    @Override
    public String getName() {
        return "Python";
    }

    @Override
    public String getMainTemplateID() {
        return "EMPTY_MAIN";
    }

    @Override
    public String getTemplate(String templateID, Map<String, String> values) {
        if("EMPTY_MAIN".equals(templateID)){
            return "if __name__ == \"__main__\":\n" +
                    "  #Enter your code here\n" +
                    "  pass\n";
        }else if("EMPTY".equals(templateID)){
            return "";
        }
        Log.wtf("Plugin XML", "TemplateID not found");
        return "";
    }

    @Override
    public String getDefaultFileExtension() {
        return "py";
    }

    private ArrayList<Integer> getOffsetsFor(String code, String findRegex){
        Matcher matcher = Pattern.compile(findRegex).matcher(code);
        ArrayList<Integer> offsets = new ArrayList<>();
        while(matcher.find()){
            int offset = matcher.start();

            offsets.add(offset);
        }
        return offsets;
    }
}
