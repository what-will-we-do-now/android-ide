package uk.ac.tees.v8036651.mode.plugins.languages;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.v8036651.mode.R;
import uk.ac.tees.v8036651.mode.plugins.ColorInfo;
import uk.ac.tees.v8036651.mode.plugins.Plugin;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class php extends Plugin {


    private static final ArrayList<String> TOKENS;
    static {
        //list of keywords from https://www.php.net/manual/en/reserved.keywords.php
        TOKENS = new ArrayList<>();
        TOKENS.add("__halt_compiler");
        TOKENS.add("break");
        TOKENS.add("clone");
        TOKENS.add("die");
        TOKENS.add("empty");
        TOKENS.add("endswitch");
        TOKENS.add("final");
        TOKENS.add("function");
        TOKENS.add("include");
        TOKENS.add("isset");
        TOKENS.add("print");
        TOKENS.add("require_once");
        TOKENS.add("trait");
        TOKENS.add("while");
        TOKENS.add("abstract");
        TOKENS.add("callable");
        TOKENS.add("const");
        TOKENS.add("do");
        TOKENS.add("enddeclare");
        TOKENS.add("endwhile");
        TOKENS.add("finally");
        TOKENS.add("global");
        TOKENS.add("include_once");
        TOKENS.add("list");
        TOKENS.add("private");
        TOKENS.add("return");
        TOKENS.add("try");
        TOKENS.add("xor");
        TOKENS.add("and");
        TOKENS.add("case");
        TOKENS.add("continue");
        TOKENS.add("echo");
        TOKENS.add("endfor");
        TOKENS.add("eval");
        TOKENS.add("fn");
        TOKENS.add("goto");
        TOKENS.add("instanceof");
        TOKENS.add("namespace");
        TOKENS.add("protected");
        TOKENS.add("static");
        TOKENS.add("unset");
        TOKENS.add("yield");
        TOKENS.add("array");
        TOKENS.add("catch");
        TOKENS.add("declare");
        TOKENS.add("else");
        TOKENS.add("endforeach");
        TOKENS.add("exit");
        TOKENS.add("for");
        TOKENS.add("if");
        TOKENS.add("insteadof");
        TOKENS.add("new");
        TOKENS.add("public");
        TOKENS.add("switch");
        TOKENS.add("use");
        TOKENS.add("yield from");
        TOKENS.add("as");
        TOKENS.add("class");
        TOKENS.add("default");
        TOKENS.add("elseif");
        TOKENS.add("endif");
        TOKENS.add("extends");
        TOKENS.add("foreach");
        TOKENS.add("implements");
        TOKENS.add("interface");
        TOKENS.add("or");
        TOKENS.add("require");
        TOKENS.add("throw");
        TOKENS.add("var");
    }

    public php(Context context){
        registerSupportedFiletype("php");
        registerTemplate("EMPTY", context.getResources().getString(R.string.plugin_generic_empty));
        registerTemplate("CLASS", context.getResources().getString(R.string.plugin_php_class));
        registerTemplate("INTERFACE", context.getResources().getString(R.string.plugin_php_interface));
    }

    @Override
    public ColorInfo[] formatText(String code) {

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
         * Matches singleline comments starting with // and ending with end of line or ?>
         */
        Matcher matcherSinglelineComments = Pattern.compile("\\/\\/.*(\\?\\>|$)").matcher(code);

        while(matcherSinglelineComments.find()){
            int offset = matcherSinglelineComments.start();
            formattedCode.add(new ColorInfo(offset, matcherSinglelineComments.end() - offset, PluginManager.COLOR_COMMENT, 0));
        }


        /**
         * Matches singleline comments starting with # and ending with end of line or ?>
         */
        Matcher matcherSinglelineHashComments = Pattern.compile("\\#.*(\\?\\>|$)").matcher(code);

        while(matcherSinglelineHashComments.find()){
            int offset = matcherSinglelineHashComments.start();
            formattedCode.add(new ColorInfo(offset, matcherSinglelineHashComments.end() - offset, PluginManager.COLOR_COMMENT, 0));
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
        return "PHP";
    }

    @Override
    public String getMainTemplateID() {
        return "EMPTY";
    }

    @Override
    public String getTemplate(String templateID, Map<String, String> values) {
        if("EMPTY".equals(templateID)){
            return "<?php\n";
        }else if("CLASS".equals(templateID)){
            return "<?php\n" +
                    "class " + values.get("filename") + "{\n" +
                    "  //TODO enter code\n" +
                    "}\n";
        }else if("INTERFACE".equals(templateID)){
            return "<?php\n" +
                    "interface " + values.get("filename") + "{\n" +
                    "  //TODO enter code\n" +
                    "}\n";
        }else if("WEBPAGE".equals(templateID)){
            return "<html>\n" +
                    "  <head>\n" +
                    "    <title></title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <?php\n" +
                    "    //TODO enter code\n" +
                    "    ?>\n" +
                    "  </body>\n" +
                    "</html>\n";
        }
        return "";
    }

    @Override
    public String getDefaultFileExtension() {
        return "php";
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
