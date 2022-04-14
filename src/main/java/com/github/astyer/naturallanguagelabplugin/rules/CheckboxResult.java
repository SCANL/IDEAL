package com.github.astyer.naturallanguagelabplugin.rules;

/**
 * the result of checking against a checkbox
 */
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
