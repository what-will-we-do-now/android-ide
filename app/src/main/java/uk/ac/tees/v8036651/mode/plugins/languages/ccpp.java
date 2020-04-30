package uk.ac.tees.v8036651.mode.plugins.languages;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.tees.v8036651.mode.R;
import uk.ac.tees.v8036651.mode.plugins.ColorInfo;
import uk.ac.tees.v8036651.mode.plugins.Plugin;
import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class ccpp extends Plugin {

    private static final ArrayList<String> TOKENS;
    static{
        TOKENS = new ArrayList<>();
        TOKENS.add("alignas");
        TOKENS.add("alignof");
        TOKENS.add("and");
        TOKENS.add("and_eq");
        TOKENS.add("asm");
        TOKENS.add("atomic_cancel");
        TOKENS.add("atomic_commit");
        TOKENS.add("atomic_noexcept");
        TOKENS.add("auto");
        TOKENS.add("bitand");
        TOKENS.add("bitor");
        TOKENS.add("bool");
        TOKENS.add("break");
        TOKENS.add("case");
        TOKENS.add("catch");
        TOKENS.add("char");
        TOKENS.add("char8_t");
        TOKENS.add("char16_t");
        TOKENS.add("char32_t");
        TOKENS.add("class");
        TOKENS.add("compl");
        TOKENS.add("concept");
        TOKENS.add("const");
        TOKENS.add("consteval");
        TOKENS.add("constexpr");
        TOKENS.add("constinit");
        TOKENS.add("const_cast");
        TOKENS.add("continue");
        TOKENS.add("co_await");
        TOKENS.add("co_return");
        TOKENS.add("co_yield");
        TOKENS.add("decltype");
        TOKENS.add("default");
        TOKENS.add("delete");
        TOKENS.add("do");
        TOKENS.add("double");
        TOKENS.add("dynamic_cast");
        TOKENS.add("else");
        TOKENS.add("enum");
        TOKENS.add("explicit");
        TOKENS.add("export");
        TOKENS.add("extern");
        TOKENS.add("false");
        TOKENS.add("float");
        TOKENS.add("for");
        TOKENS.add("friend");
        TOKENS.add("goto");
        TOKENS.add("if");
        TOKENS.add("inline");
        TOKENS.add("int");
        TOKENS.add("long");
        TOKENS.add("mutable");
        TOKENS.add("namespace");
        TOKENS.add("new");
        TOKENS.add("noexcept");
        TOKENS.add("not");
        TOKENS.add("not_eq");
        TOKENS.add("nullptr");
        TOKENS.add("operator");
        TOKENS.add("or");
        TOKENS.add("or_eq");
        TOKENS.add("private");
        TOKENS.add("protected");
        TOKENS.add("public");
        TOKENS.add("reflexpr");
        TOKENS.add("register");
        TOKENS.add("reinterpret_cast");
        TOKENS.add("requires");
        TOKENS.add("return");
        TOKENS.add("short");
        TOKENS.add("signed");
        TOKENS.add("sizeof");
        TOKENS.add("static");
        TOKENS.add("static_assert");
        TOKENS.add("static_cast");
        TOKENS.add("struct");
        TOKENS.add("switch");
        TOKENS.add("synchronized");
        TOKENS.add("template");
        TOKENS.add("this");
        TOKENS.add("thread_local");
        TOKENS.add("throw");
        TOKENS.add("true");
        TOKENS.add("try");
        TOKENS.add("typedef");
        TOKENS.add("typeid");
        TOKENS.add("typename");
        TOKENS.add("union");
        TOKENS.add("unsigned");
        TOKENS.add("using");
        TOKENS.add("virtual");
        TOKENS.add("void");
        TOKENS.add("volatile");
        TOKENS.add("wchar_t");
        TOKENS.add("while");
        TOKENS.add("xor");
        TOKENS.add("xor_eq");
    }

    public ccpp(Context context){
        registerSupportedFiletype("c");
        registerSupportedFiletype("cpp");
        registerSupportedFiletype("h");
        registerTemplate("EMPTY", context.getResources().getString(R.string.plugin_generic_empty));
        registerTemplate("MAIN", context.getResources().getString(R.string.plugin_cpp_main));
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
        return "C/C++";
    }

    @Override
    public String getMainTemplateID() {
        return "MAIN";
    }

    @Override
    public String getTemplate(String templateID, Map<String, String> values) {
        if("EMPTY".equals(templateID)){
            return "";
        }else if("MAIN".equals(templateID)){
            return "int main(int argc, char** argv){\n" +
                    "  //TODO enter code\n" +
                    "  return 0;\n" +
                    "}\n";
        }
        Log.wtf("Plugin C/C++", "TemplateID not found");
        return "";
    }

    @Override
    public String getDefaultFileExtension() {
        return "cpp";
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
