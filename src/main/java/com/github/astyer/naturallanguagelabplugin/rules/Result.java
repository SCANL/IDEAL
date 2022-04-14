package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;

import java.util.List;

/**
 * A class to represent the final result of the backend.
 */
public class Result{
    public static class Recommendation {
        // does the identifier match the regex?
        private Boolean regexMatches;
        // what POS tags can be inserted to make the identifier match more
        private List<String> nextPOSRecommendations;
        // the explanation for the rule
        private String explanation;
        // the example for the rule
        private String example;
        // the name of the rule
        private String name;
        // the recommendation that we suggest be applied to the identifier
        private RecommendationAlg.Rec rec;

        public Recommendation(String name, Boolean regexMatches, List<String> nextPOSRecommendations, String explanation, String example, RecommendationAlg.Rec rec){
            this.regexMatches = regexMatches;
            this.nextPOSRecommendations = nextPOSRecommendations;
            this.explanation = explanation;
            this.example = example;
            this.name = name;
            this.rec = rec;
        }

        public String getName(){return name;}

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

        public RecommendationAlg.Rec getRec() {
            return rec;
        }
    }
    private final Identifier id;
    private final List<Recommendation> recommendations;

    public Result(Identifier id, List<Recommendation> recommendations){
        this.id = id;
        this.recommendations = recommendations;
    }

    public Recommendation getTopRecommendation(){
        if(recommendations.size()>0) {
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
