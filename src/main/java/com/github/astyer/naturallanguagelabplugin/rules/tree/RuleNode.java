package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.Checkbox;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleNode {
    private String name;
    private Pattern regex;
    private List<RuleBranch> branches = new ArrayList<>();
    public RuleNode(String name, Pattern regex){
        this.name = name;
        this.regex = regex;
    }

    public void addBranch(RuleBranch branch){
        this.branches.add(branch);
    }

    public List<NodeResult> checkIdentifier(Identifier id, int depth){
        List<NodeResult> results = new ArrayList<>();
        results.add(new NodeResult(regex.matcher(id.getPOS()).matches(), name, depth));
        for(RuleBranch branch: branches){
            Optional<RuleNode> optRuleNode =  branch.checkBranch(id);
            if(optRuleNode.isPresent()){
                RuleNode next = optRuleNode.get();
                results.addAll(next.checkIdentifier(id, depth+1));
            }
        }
        return results.stream().sorted(Comparator.comparingInt(a -> a.depth)).collect(Collectors.toList());
    }
}
