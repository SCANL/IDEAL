package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.kipust.regex.Dfa;

public class NodeResult {
    Dfa.DFAResult regexResult;
    String recommendation;
    int depth;
    String explanation;
    String example;

    public NodeResult(Dfa.DFAResult regexResult, String recommendation, int depth, String explanation, String example){
        this.regexResult = regexResult;
        this.recommendation = recommendation;
        this.depth = depth;
        this.explanation = explanation;
        this.example = example;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public boolean isIdentifierMatchesRegex() {
        return regexResult.success();
    }

    public int getDepth() {
        return depth;
    }
    public Dfa.DFAResult getRegexResult(){
        return regexResult;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getExample() {
        return example;
    }
}
