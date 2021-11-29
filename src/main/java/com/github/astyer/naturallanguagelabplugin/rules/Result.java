package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;

import java.util.List;

public class Result{
    public static class Recommendation {
        private Boolean regexMatches;
        private List<String> nextPOSRecommendations;
        private String explanation;
        private String example;
        public Recommendation(Boolean regexMatches, List<String> nextPOSRecommendations, String explanation, String example){
            this.regexMatches = regexMatches;
            this.nextPOSRecommendations = nextPOSRecommendations;
            this.explanation = explanation;
            this.example = example;
        }

        public Boolean getRegexMatches() {
            return regexMatches;
        }

        public List<String> getNextPOSRecommendations() {
            return nextPOSRecommendations;
        }

        public String getExplanation() {
            return explanation;
        }

        public String getExample() {
            return example;
        }
    }
    private final Identifier id;
    private final List<Recommendation> recommendations;

    public Result(Identifier id, List<Recommendation> recommendations){
        this.id = id;
        this.recommendations = recommendations;
    }

    Recommendation getTopRecommendation(){
        if(recommendations.size()>1) {
            return recommendations.get(0);
        }else{
            return null;
        }
    }

    public Identifier getId() {
        return id;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }
}
