package com.github.astyer.naturallanguagelabplugin.rules.tree;

public class NodeResult {
    boolean identifierMatchesRegex;
    String recommendation;
    int depth;
    public NodeResult(boolean identifierMatchesRegex, String recommendation, int depth){
        this.identifierMatchesRegex = identifierMatchesRegex;
        this.recommendation = recommendation;
        this.depth = depth;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public boolean isIdentifierMatchesRegex() {
        return identifierMatchesRegex;
    }

    public int getDepth() {
        return depth;
    }
}
