package com.github.astyer.naturallanguagelabplugin.rules;

import java.util.List;

public class RuleNode {

    String name;
    Rule rule;
    List<Checkbox> checkboxList;

    public RuleNode(String name, Rule rule){
        this.name = name;
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }

    public List<Checkbox> getCheckboxList() {
        return checkboxList;
    }

    public List<Checkbox> runCheckboxes(){
        this.checkboxList = rule.getCheckboxes();
        return checkboxList;
    }

    public String getName(){
        return name;
    }
}
