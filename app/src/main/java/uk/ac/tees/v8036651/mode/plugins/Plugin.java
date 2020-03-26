package uk.ac.tees.v8036651.mode.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Plugin {

    private final ArrayList<String> supportedFiletypes;
    private final Map<String, String> templateList;

    public Plugin(){
        this.supportedFiletypes = new ArrayList<>();
        this.templateList = new HashMap<>();
    }

    /**
     * @param templateID Represents internally used template ID.
     * @param templateDescription Represents the human readable explanation of what the template is.
     */
    protected final void registerTemplate(String templateID, String templateDescription){
        templateList.put(templateID, templateDescription);
    }

    protected final void registerSupportedFiletype(String filetype){
        supportedFiletypes.add(filetype);
    }

    public final Map<String, String> getTemplateList(){
        return Collections.unmodifiableMap(templateList);
    }

    public final List<String> getSupportedFiletypes(){
        return Collections.unmodifiableList(supportedFiletypes);
    }



    public abstract ColorInfo[] formatText(String code, String type);

    public abstract String getName();

    public abstract String getMainTemplateID();

    /**
     *
     * @param values a list of key-values pairs which should be used to generate the template
     *               Obligatory fields are
     *                - package (the location of the file from src directory)
     *                - filename (the name of the file)
     * @return
     */
    public abstract String getTemplate(String templateID, Map<String, String> values);
}