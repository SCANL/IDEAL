package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;

import java.util.*;
import java.util.stream.Collectors;

import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;
import com.kipust.regex.*;


public class RuleNode {
//    private String name;
    private Pattern regex;
    private GetResultAttr getName;
    private GetResultAttr getExplanation;
    private GetResultAttr getExample;
    private List<RuleBranch> branches = new ArrayList<>();
    public RuleNode(GetResultAttr getName, Pattern regex, GetResultAttr getExplanation, GetResultAttr getExample){
        this.getName = getName;
        this.regex = regex;
        this.getExplanation = getExplanation;
        this.getExample = getExample;
    }
    public RuleNode(String name, Pattern regex, String getExplanation, String getExample){
        this.getName = (x)->name;
        this.regex = regex;
        this.getExplanation = (x)->getExplanation;
        this.getExample = (x)->getExample;
    }

    public void addBranch(RuleBranch branch){
        this.branches.add(branch);
    }

    public List<NodeResult> checkIdentifier(Identifier id, int depth){
        List<NodeResult> results = new ArrayList<>();
        RecommendationAlg rec = new RecommendationAlg(regex, id);
        results.add(new NodeResult(regex.match(id.getPOS()), getName.getResultAttr(id), depth, getExplanation.getResultAttr(id), getExample.getResultAttr(id), rec.getRecommendation()));
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
