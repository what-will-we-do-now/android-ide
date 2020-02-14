package uk.ac.tees.v8036651.mode.plugins;

import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Plugin {

    private final String name;
    private final ArrayList<String> supportedFiletypes;


    public Plugin(String name){
        this(name, new ArrayList<String>());
    }

    public Plugin(String name, ArrayList<String> supportedFiletypes){
        this.name = name;
        this.supportedFiletypes = supportedFiletypes;
    }



    public abstract void formatText(View codeView, String text);




    public final String getName(){
        return name;
    }

    public final void registerSupportedFiletype(String filetype){
        supportedFiletypes.add(filetype);
    }

    public final List<String> getSupportedFiletypes(){
        return Collections.unmodifiableList(supportedFiletypes);
    }
}