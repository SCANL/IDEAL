package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;

import java.util.*;
import java.util.stream.Collectors;

import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;
import com.kipust.regex.*;

/**
 * A node in the tree to represent a rule
 */
public class RuleNode {
//    private String name;
    //the regex that this rule matches against
    private Pattern regex;
    //these args are dynamic. They get evaluated when the rule gets run
    private GetResultAttr getName;
    private GetResultAttr getExplanation;
    private GetResultAttr getExample;
    private GetDynamicPattern getRecRegex;

    private List<RuleBranch> branches = new ArrayList<>();

    /**
     * A dynamic constructor. these arguments will be evaluated when the rule gets checked
     * @param getName a function to get the name of the identifier
     * @param regex the regex to match against
     * @param getRecRegex a function to get the regex that is passed into the recommendation algorithm
     * @param getExplanation a function to get the explanation
     * @param getExample a function to get the example
     */
    public RuleNode(GetResultAttr getName, Pattern regex, GetDynamicPattern getRecRegex, GetResultAttr getExplanation, GetResultAttr getExample){
        this.getName = getName;
        this.regex = regex;
        this.getExplanation = getExplanation;
        this.getExample = getExample;
        this.getRecRegex = getRecRegex;
    }

    /**
     * A static constructor. for rules whose values don't change depending on the identifier
     * @param name
     * @param regex
     * @param getExplanation
     * @param getExample
     */
    public RuleNode(String name, Pattern regex, String getExplanation, String getExample){
        this.getName = (x)->name;
        this.regex = regex;
        this.getExplanation = (x)->getExplanation;
        this.getExample = (x)->getExample;
        this.getRecRegex = (x)->regex;
    }

    /**
     * add a branch to the rule
     * @param branch
     */
    public void addBranch(RuleBranch branch){
        this.branches.add(branch);
    }

    /**
     * run the rule against an identifier
     * @param id the identifier to check against
     * @param depth the current depth in the tree
     * @return
     */
    public List<NodeResult> checkIdentifier(Identifier id, int depth){
        List<NodeResult> results = new ArrayList<>();
        RecommendationAlg rec = new RecommendationAlg(this.getRecRegex.getDynamicPattern(id), id);
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
