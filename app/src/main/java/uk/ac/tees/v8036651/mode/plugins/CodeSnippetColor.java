package uk.ac.tees.v8036651.mode.plugins;

public class CodeSnippetColor implements CodeSnippet{

    private String color;

    public CodeSnippetColor(String color){
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
