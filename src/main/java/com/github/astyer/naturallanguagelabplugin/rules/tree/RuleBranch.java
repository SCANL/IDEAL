package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.Checkbox;

import java.util.Optional;

public class RuleBranch {
    String description;
    RuleNode endpoint;
    Checkbox checkbox;
    public RuleBranch(String description, RuleNode endpoint, Checkbox checkbox){
        this.description = description;
        this.endpoint = endpoint;
        this.checkbox = checkbox;
    }

    public Optional<RuleNode> checkBranch(Identifier id){
        if(checkbox.applyCheckbox(id).getResult()){
            return Optional.of(endpoint);
        }
        return Optional.empty();
    }
}
