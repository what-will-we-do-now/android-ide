package uk.ac.tees.v8036651.mode.plugins;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.v8036651.mode.GUI.NumberedTextView;
import uk.ac.tees.v8036651.mode.R;
import uk.ac.tees.v8036651.mode.plugins.languages.ccpp;
import uk.ac.tees.v8036651.mode.plugins.languages.java;
import uk.ac.tees.v8036651.mode.plugins.languages.python;
import uk.ac.tees.v8036651.mode.plugins.languages.xml;

public class PluginManager {

    public static String COLOR_STRING = "#2ECC91";
    public static String COLOR_NUMBER = "#00BFFF";
    public static String COLOR_KEYWORD = "#DBAA21";
    public static String COLOR_COMMENT = "#BFBFBF";

    private static List<Plugin> plugins;

    private static Plugin cachedPlugin;

    private static Context context;

    public static void load(Context context){
        plugins = new ArrayList<>();
        PluginManager.context = context;
        /* PLAN C */

        Plugin pluginJava = new java(context);
        Plugin pluginXML = new xml(context);
        Plugin pluginPython = new python(context);
        Plugin pluginCCpp = new ccpp(context);


        plugins.add(pluginJava);
        plugins.add(pluginXML);
        plugins.add(pluginPython);
        plugins.add(pluginCCpp);

        /* PLAN B
        try{
            File file = context.getFileStreamPath("plugin-java.jar");
            ClassLoader loader = URLClassLoader.newInstance(new URL[] {file.toURI().toURL()});
            Plugin plugin = (Plugin) loader.loadClass("mode.main").getDeclaredConstructor(String.class).newInstance(file.getName());
            plugins.add(plugin);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
         */

        /* PLAN A
        if((new File("plugins")).exists()){
            //the plugins folder exists load all included plugins

            for(File file : (new File("plugins/")).listFiles(
                new FilenameFilter (){
                    @Override
                    public boolean accept(File dir, String name) {
                        return  name.startsWith("plugin") && name.toLowerCase().endsWith(".jar");
                    }
                }
            )){

                try{
                    ClassLoader loader = URLClassLoader.newInstance(new URL[] {file.toURI().toURL()});
                    Plugin plugin = (Plugin) loader.loadClass("mode.main").getDeclaredConstructor(String.class).newInstance(file.getName());
                    plugins.add(plugin);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }else{
            //the plugins folder doesnt exist, we should create it
        }*/
    }

    public static void formatText(View view, File file){
        //check if cached plugin is not defined or if its not matching the file extension
        String fileExtension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();

        if(cachedPlugin == null || !cachedPlugin.getSupportedFiletypes().contains(fileExtension)){
            cachedPlugin = null;
            //find the correct plugin to deal with the file
            for(Plugin plugin : plugins){
                if(plugin.getSupportedFiletypes().contains(fileExtension)){
                    cachedPlugin = plugin;
                    break;
                }
            }

            //there seems to be no valid plugin for this file type, no point in continuing
            if(cachedPlugin == null){
                return;
            }
        }
        //format the text
        ColorInfo[] formattedCode = cachedPlugin.formatText(((NumberedTextView) view).getText().toString());

        Spannable span = new SpannableString(((NumberedTextView) view).getText().toString());
        for(ColorInfo color : formattedCode){
            span.setSpan(new ForegroundColorSpan(Color.parseColor(color.getColor())), color.getOffset(), color.getOffset() + color.getLength(), (color.getPriority() << Spannable.SPAN_PRIORITY_SHIFT) & Spannable.SPAN_PRIORITY);
        }
        /*
        for(CodeSnippet cs : formattedCode){
            if(cs instanceof CodeSnippetString){
                System.out.println("Code format: text: " + cs.toString());
                ((TextView) view).append(cs.toString());
            }else if(cs instanceof  CodeSnippetColor){
                System.out.println("Code format: color: " + cs.toString());
                ((TextView) view).setTextColor(0xFF0000);
                //((TextView) view).setTextColor(Integer.parseInt(cs.toString(), 16));
            }else{
                System.out.println("ERROR UNKNOWN CODE SNIPPPET TYPE");
            }
        }*/
        //get cursor position to prevent resetting it to from
        int curpos = ((NumberedTextView) view).getSelectionStart();
        ((NumberedTextView) view).setText(span);
        ((NumberedTextView) view).setSelection(curpos);
    }
/*
    public static String getDefaultTemplate(String pckg, String filename, String lang){
        if(cachedPlugin != null && cachedPlugin.getSupportedFiletypes().contains(lang)){
            return cachedPlugin.getDefaultTemplate(pckg, filename);
        }else{
            for(Plugin plugin : plugins){
                if(plugin.getSupportedFiletypes().contains(lang)){

                }
            }
        }
        return "";
    }*/

    public static ArrayList<String> getProjectTypes(){
        ArrayList<String> projects = new ArrayList<>();

        for(Plugin plugin : plugins){
            projects.add(plugin.getName());
        }

        return projects;
    }

    public static Map<String, String> getTemplatesFor(String language){
        for(Plugin plugin : plugins){
            if(plugin.getName().equalsIgnoreCase(language)){
                return plugin.getTemplateList();
            }
        }
        Log.wtf("Plugin Manager", "No valid plugin found. This may be a bug. Using Fallback...");
        Map<String, String> fallback = new HashMap<>();
        fallback.put("EMPTY", context.getResources().getString(R.string.plugin_generic_empty));
        return fallback;
    }

    public static String getTemplate(String language, String templateID, Map<String, String> values){
        for(Plugin plugin : plugins){
            if(plugin.getName().equalsIgnoreCase(language)){
                return plugin.getTemplate(templateID, values);
            }
        }
        Log.wtf("Plugin Manager", "Language not found");
        return "";
    }

    public static String getDefaultTemplate(String language, Map<String, String> values){
        for(Plugin plugin : plugins){
            if(plugin.getName().equalsIgnoreCase(language)){
                return plugin.getTemplate(plugin.getMainTemplateID(), values);
            }
        }
        Log.wtf("Plugin Manager", "Language not found");
        return "";
    }

    public static String getDefaultFileExtensionFor(String language){
        for(Plugin plugin : plugins){
            if(plugin.getName().equalsIgnoreCase(language)){
                return plugin.getDefaultFileExtension();
            }
        }
        Log.wtf("Plugin Manager", "Language not found");
        return "";
    }
}