package com.github.astyer.naturallanguagelabplugin.rules;

public class CheckboxResult {
    private boolean matched = false;
    private boolean result = false;
    private CheckboxResult(){
    }
    public CheckboxResult(Boolean result){
        this.matched = true;
        this.result = result;
    }

    public boolean getMatched() {
        return matched;
    }

    public boolean getResult() {
        return result;
    }

    static CheckboxResult empty(){
        return new CheckboxResult();
    }
}
