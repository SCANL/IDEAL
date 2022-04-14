package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.Checkbox;

import java.util.Optional;

/**
 * A class for the branches between the rule nodes
 */
public class RuleBranch {
    String description;
    RuleNode endpoint;
    Checkbox checkbox;
    public RuleBranch(String description, RuleNode endpoint, Checkbox checkbox){
        this.description = description;
        this.endpoint = endpoint;
        this.checkbox = checkbox;
    }

    /**
     * checks if the identifier matches against the identifier.
     * @param id
     * @return NONE if the identifier doesn't match and SOME if the identifier does match
     */
    public Optional<RuleNode> checkBranch(Identifier id){
        if(checkbox.applyCheckbox(id).getResult()){
            return Optional.of(endpoint);
        }
        return Optional.empty();
    }
}
