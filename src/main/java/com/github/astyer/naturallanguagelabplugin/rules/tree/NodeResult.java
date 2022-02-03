package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;
import com.kipust.regex.Dfa;

public class NodeResult {
    Dfa.DFAResult regexResult;
    String name;
    int depth;
    String explanation;
    String example;
    RecommendationAlg.Rec rec;

    public NodeResult(Dfa.DFAResult regexResult, String name, int depth, String explanation, String example, RecommendationAlg.Rec rec){
        this.regexResult = regexResult;
        this.name = name;
        this.depth = depth;
        this.explanation = explanation;
        this.example = example;
        this.rec = rec;
    }

    public RecommendationAlg.Rec getRec(){
        return this.rec;
    }

    public String getName() {
        return name;
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
