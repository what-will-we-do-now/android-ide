package uk.ac.tees.v8036651.mode.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProgrammingLanguage {

    private final String humanReadableName;
    private final String name;
    private final ArrayList<String> supportedFileNames;
    private final ArrayList<String> templateList;

    public ProgrammingLanguage(String humanReadableName, String name, ArrayList<String> supportedFileNames) {
        this(humanReadableName, name, supportedFileNames, new ArrayList<String>());
    }

    public ProgrammingLanguage(String humanReadableName, String name, ArrayList<String> supportedFileNames, ArrayList<String> templateList) {
        this.humanReadableName = humanReadableName;
        this.name = name;
        this.supportedFileNames = supportedFileNames;
        this.templateList = templateList;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public String getName() {
        return name;
    }

    public List<String> getSupportedFileNames(){
        return Collections.unmodifiableList(supportedFileNames);
    }

    public List<String> getTemplateList(){
        return Collections.unmodifiableList(templateList);
    }
}