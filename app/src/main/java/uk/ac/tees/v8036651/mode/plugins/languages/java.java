/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.tees.v8036651.mode.plugins.languages;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.v8036651.mode.plugins.ColorInfo;
import uk.ac.tees.v8036651.mode.plugins.Plugin;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

/**
 *
 * @author v8073331
 */
public class java extends Plugin{

    private static final ArrayList<String> TOKENS;
    static{
        TOKENS = new ArrayList<>();
        TOKENS.add("public");
        TOKENS.add("private");
        TOKENS.add("protected");
        TOKENS.add("static");
        TOKENS.add("final");

        TOKENS.add("class");
        TOKENS.add("interface");
        TOKENS.add("enum");
        TOKENS.add("abstract");
        TOKENS.add("import");
        TOKENS.add("package");
        TOKENS.add("extends");
        TOKENS.add("implements");
        TOKENS.add("default");
        TOKENS.add("synchronized");
        TOKENS.add("volatile");
        TOKENS.add("throw");
        TOKENS.add("throws");
        TOKENS.add("native");

        TOKENS.add("void");
        TOKENS.add("int");
        TOKENS.add("float");
        TOKENS.add("long");
        TOKENS.add("double");
        TOKENS.add("boolean");
        TOKENS.add("char");
        TOKENS.add("short");
        TOKENS.add("null");

        TOKENS.add("transient");
        TOKENS.add("instanceof");
        TOKENS.add("assert");
        TOKENS.add("if");
        TOKENS.add("else");
        TOKENS.add("case");
        TOKENS.add("switch");
        TOKENS.add("super");
        TOKENS.add("this");
        TOKENS.add("return");
        TOKENS.add("for");
        TOKENS.add("while");
        TOKENS.add("break");
        TOKENS.add("continue");
        TOKENS.add("do");

        TOKENS.add("true");
        TOKENS.add("false");

        TOKENS.add("new");
        TOKENS.add("try");
        TOKENS.add("catch");
        TOKENS.add("finally");

        TOKENS.add("strictfp");

        TOKENS.add("const");
        TOKENS.add("goto");
    }

    public java() {
        registerSupportedFiletype("java");
        registerTemplate("EMPTY_CLASS_MAIN", "Java Class with Main method");
        registerTemplate("EMPTY_CLASS","Java Class");
        registerTemplate("EMPTY_INTERFACE", "Java Interface");
        registerTemplate("EMPTY_ENUM", "Java Enum");
        registerTemplate("EXCEPTION", "Java Exception");
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
         * matches multiline comments starting with slash star and ending with star slash
         */
        Matcher matchMultilineComments = Pattern.compile("\\/\\*(?s)(.*?)\\*\\/").matcher(code);
        while(matchMultilineComments.find()) {
            int offset = matchMultilineComments.start();
            formattedCode.add(new ColorInfo(offset, matchMultilineComments.end() - offset, PluginManager.COLOR_COMMENT, 0));
        }

        /**
         * Matches singleline comments starting with //
         */
        Matcher matcherSinglelineComments = Pattern.compile("\\/\\/.*").matcher(code);

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
         * Matches all strings
         */

        Matcher matcherStrings = Pattern.compile("(\"(.*?)\")|(\'(.*?)\')").matcher(code);
        while(matcherStrings.find()){
            int offset = matcherStrings.start();
            formattedCode.add(new ColorInfo(offset, matcherStrings.end() - offset, PluginManager.COLOR_STRING, 1));
        }

        ColorInfo[] codeFinal = new ColorInfo[formattedCode.size()];

        for(int x = 0; x < formattedCode.size(); x++){
            codeFinal[x] = formattedCode.get(x);
        }

        return codeFinal;
    }

    @Override
    public String getName() {
        return "Java";
    }

    @Override
    public String getMainTemplateID() {
        return "EMPTY_CLASS_MAIN";
    }

    @Override
    public String getTemplate(String templateID, Map<String, String> values) {
        if("EMPTY_CLASS_MAIN".equals(templateID)){
            return (values.containsKey("package") ? "package " + values.get("package") + ";\n" : "") +
                    "\n" +
                    "public class " + values.get("filename") + "{\n" +
                    "\n" +
                    "  public static void main(String[] args){\n" +
                    "\n" +
                    "    //TODO implement main code\n" +
                    "\n" +
                    "  }\n" +
                    "}";
        }else if("EMPTY_CLASS".equals(templateID)){
            return (values.containsKey("package") ? "package " + values.get("package") + ";\n" : "") +
                    "\n" +
                    "public class " + values.get("filename") + "{\n" +
                    "\n" +
                    "}";
        }else if("EMPTY_INTERFACE".equals(templateID)){
            return (values.containsKey("package") ? "package " + values.get("package") + ";\n" : "") +
                    "\n" +
                    "public interface " + values.get("filename") + "{\n" +
                    "\n" +
                    "}";
        }else if("EMPTY_ENUM".equals(templateID)){
            return (values.containsKey("package") ? "package " + values.get("package") + ";\n" : "") +
                    "\n" +
                    "public enum " + values.get("filename") + "{\n" +
                    "\n" +
                    "}";
        }else if("EXCEPTION".equals(templateID)){
            return (values.containsKey("package") ? "package " + values.get("package") + ";\n" : "") +
                    "\n" +
                    "public class " + values.get("filename") + " extends Exception{\n" +
                    "\n" +
                    "  public " + values.get("filename") + "() {\n" +
                    "  }\n" +
                    "\n" +
                    "  public " + values.get("filename") + "(String msg) {\n" +
                    "    super(msg);\n" +
                    "  }\n" +
                    "}";
        }
        Log.wtf("Plugin Java", "TemplateID not found");
        return "";
    }

    @Override
    public String getDefaultFileExtension() {
        return "java";
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