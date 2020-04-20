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

public class xml extends Plugin {

    public xml(Context context){
        registerSupportedFiletype("html");
        registerSupportedFiletype("xml");
        registerSupportedFiletype("settings");

        registerTemplate("EMPTY", context.getResources().getString(R.string.plugin_xml_template_empty));
        registerTemplate("WEBSITE", context.getResources().getString(R.string.plugin_xml_template_website));

    }

    @Override
    public ColorInfo[] formatText(String code) {

        ArrayList<ColorInfo> formattedCode = new ArrayList<>();

        /**
         * Match all XML tags names
         */
        Matcher matchTagName = Pattern.compile("\\<(.*?)\\>").matcher(code);
        while(matchTagName.find()){
            int offset = matchTagName.start();
            formattedCode.add(new ColorInfo(offset, matchTagName.end() - offset, PluginManager.COLOR_KEYWORD, 10));
        }

        /**
         * Match all strings
         */

        Matcher matcherStrings = Pattern.compile("(\"(.*?)\")|(\'(.*?)\')").matcher(code);
        while(matcherStrings.find()){
            int offset = matcherStrings.start();
            formattedCode.add(new ColorInfo(offset, matcherStrings.end() - offset, PluginManager.COLOR_STRING, 1));
        }

        /**
         * Matches comments
         *
         */
        Matcher matcherMultiStrings = Pattern.compile("\\<\\!\\-\\-([\\S\\s]*?)\\-\\-\\>").matcher(code);
        while(matcherMultiStrings.find()){
            int offset = matcherMultiStrings.start();
            formattedCode.add(new ColorInfo(offset, matcherMultiStrings.end() - offset, PluginManager.COLOR_COMMENT, 1));
        }

        ColorInfo[] codeFinal = new ColorInfo[formattedCode.size()];

        for(int x = 0; x< formattedCode.size(); x++){
            codeFinal[x] = formattedCode.get(x);
        }

        return codeFinal;
    }

    @Override
    public String getName() {
        return "XML";
    }

    @Override
    public String getMainTemplateID() {
        return "EMPTY";
    }

    @Override
    public String getTemplate(String templateID, Map<String, String> values) {
        if("WEBSITE".equals(templateID)){
            return "<html>\n" +
                    "  <head>\n" +
                    "    <title>Website Title</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "  </body>\n" +
                    "</html>\n";
        }else if("EMPTY".equals(templateID)){
            return "";
        }
        Log.wtf("Plugin XML", "TemplateID not found");
        return "";
    }

    @Override
    public String getDefaultFileExtension() {
        return "xml";
    }
}
