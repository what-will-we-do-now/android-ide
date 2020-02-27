/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.tees.v8036651.mode.plugins.languages;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.v8036651.mode.plugins.CodeSnippet;
import uk.ac.tees.v8036651.mode.plugins.CodeSnippetColor;
import uk.ac.tees.v8036651.mode.plugins.CodeSnippetString;
import uk.ac.tees.v8036651.mode.plugins.ColorInfo;
import uk.ac.tees.v8036651.mode.plugins.Plugin;

/**
 *
 * @author v8073331
 */
public class java extends Plugin{

    private static final ArrayList<String> TOKENS;
    static{
        TOKENS = new ArrayList();
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

    public java(String name) {
        super(name);
        registerSupportedFiletype("java");
        System.out.println("Java plugin registered");
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

        /* pattern for detecting new linux line (\n), windows new line (\r\n), code separator (.) and line end (;) and word boundry
         * for more info see https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
         */

        Matcher matcher = Pattern.compile("\n|\r\n|.|;|\b").matcher(code);

        while(matcher.find()){
            int offset = matcher.start();
            String token = matcher.group();
            if(TOKENS.contains(token)){
                formattedCode.add(new ColorInfo(offset, token.length(), 0x665E48));
            }
        }

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
}