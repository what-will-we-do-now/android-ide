/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.tees.v8036651.mode.plugins.languages;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.v8036651.mode.plugins.ColorInfo;
import uk.ac.tees.v8036651.mode.plugins.Plugin;

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
        System.out.println("JAVA-PLUGIN: formatting text");
        /*
        ArrayList<Integer> breakPoints = new ArrayList();

        for(String token : TOKENS){
            String codePart = code;
            while(codePart.contains(token)){
                int breakPoint = codePart.indexOf(token);
                breakPoints.add(breakPoint);
                codePart = codePart.substring(breakPoint);
            }
        }

        */

        ArrayList<ColorInfo> formattedCode = new ArrayList();

        for(String token : TOKENS) {
            ArrayList<Integer> offsets = getOffsetsFor(code, token);
            for(int off : offsets){
                formattedCode.add(new ColorInfo(off, token.length(), "#dbaa21"));
            }
        }

        /* pattern for detecting new linux line (\n), windows new line (\r\n), code separator (.) and line end (;) and word boundry
         * for more info see https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
         */


        /*//https://stackoverflow.com/questions/14735171/regex-to-tokenize-string-in-java-with-space-and-double-quotes
        Matcher matcher = Pattern.compile("\\S").matcher(code);

         while(matcher.find()){
            int offset = matcher.start();
            String token = matcher.group();
            if(TOKENS.contains(token)){
                formattedCode.add(new ColorInfo(offset, token.length(), 0x665E48));
            }
        }*/

        /*
        StringTokenizer tokenized = new StringTokenizer(code);
        while(tokenized.hasMoreTokens()){
            String token = tokenized.nextToken();
            if(TOKENS.contains(token)){
                formattedCode.add(new ColorInfo(tokenized.));
                formattedCode.add(new CodeSnippetColor("665E48"));
                formattedCode.add(new CodeSnippetString(token));
                formattedCode.add(new CodeSnippetColor("FFFFFF"));
            }else{
                formattedCode.add(new CodeSnippetString(token));
            }
        }
        */

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
    public String getTemplate(String templateID, Map<String, String> values) {
        if("EMPTY_CLASS_MAIN".equals(templateID)){
            return "package " + values.get("package") + ";\n" +
                    "\n" +
                    "public class " + values.get("filename") + "{\n" +
                    "\n" +
                    "    public static void main(String[] args){\n" +
                    "\n" +
                    "        //TODO implement main code\n" +
                    "\n" +
                    "    }\n" +
                    "}";
        }else if("EMPTY_CLASS".equals(templateID)){
            return "package " + values.get("package") + ";\n" +
                    "\n" +
                    "public class " + values.get("filename") + "{\n" +
                    "\n" +
                    "}";
        }else if("EMPTY_INTERFACE".equals(templateID)){
            return "package " + values.get("package") + ";\n" +
                    "\n" +
                    "public interface " + values.get("filename") + "{\n" +
                    "\n" +
                    "}";
        }else if("EMPTY_ENUM".equals(templateID)){
            return "package " + values.get("package") + ";\n" +
                    "\n" +
                    "public enum " + values.get("filename") + "{\n" +
                    "\n" +
                    "}";
        }else if("EXCEPTION".equals(templateID)){
            return "package " + values.get("package") + ";\n" +
                    "\n" +
                    "public class " + values.get("filename") + " extends Exception{\n" +
                    "\n" +
                    "    public " + values.get("filename") + "() {\n" +
                    "    }\n" +
                    "\n" +
                    "    public " + values.get("filename") + "(String msg) {\n" +
                    "        super(msg);\n" +
                    "    }\n" +
                    "}";
        }
        return "";
    }

    /*@Override
    public String getDefaultTemplate(String pckg, String filename) {
        return "package " + pckg + ";\n\n\npublic class " + filename + "{\n\n    public static void main(String[] args){\n//TODO implement main code\n\n}\n}";
    }*/

    /*private ArrayList<Integer> getOffsetsFor(String code, String find){
        ArrayList<Integer> offsets = new ArrayList();
        int grandoffset = 0;
        String subcode = code;
        while(true){
            int offset = subcode.indexOf(find);
            if(offset == -1){
                return offsets;
            }else{
                offsets.add(grandoffset + offset);
                grandoffset = offset + find.length();
                subcode = subcode.substring(offset + find.length());
            }
        }
    }*/
    private ArrayList<Integer> getOffsetsFor(String code, String find){
        Matcher matcher = Pattern.compile("\\b" + find + "\\b").matcher(code);
        ArrayList<Integer> offsets = new ArrayList();
        while(matcher.find()){
            int offset = matcher.start();

            offsets.add(offset);
        }
        return offsets;
    }
}