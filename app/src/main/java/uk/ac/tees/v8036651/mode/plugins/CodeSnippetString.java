package uk.ac.tees.v8036651.mode.plugins;

public class CodeSnippetString implements CodeSnippet{
    private String code;

    public CodeSnippetString(String code){
        this.code = code;
    }

    @Override
    public String toString(){
        return code;
    }
}
