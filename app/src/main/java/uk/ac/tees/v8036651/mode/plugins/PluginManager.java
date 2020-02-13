package uk.ac.tees.v8036651.mode.plugins;

import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {
    private static List<Plugin> plugins;

    private static Plugin cachedPlugin;

    static{
        plugins = new ArrayList<>();
        load();
    }

    public static void load(){
        if((new File("plugins")).exists()){
            //the plugins folder exists load all included plugins

            for(File file : (new File("plugins/")).listFiles(
                new FilenameFilter (){
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".jar");
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
        }
    }

    public static void formatText(View view, File file){
        //check if cached plugin is not defined or if its not matching the file extension
        if(cachedPlugin == null || !cachedPlugin.getSupportedFiletypes().contains(file.getName().substring(file.getName().lastIndexOf('.')))){

            cachedPlugin = null;
            //find the correct plugin to deal with the file
            for(Plugin plugin : plugins){
                if(plugin.getSupportedFiletypes().contains(file.getName().substring(file.getName().lastIndexOf('.')))){
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
        cachedPlugin.formatText(view, ((TextView) view).getText().toString());
    }
}