package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;

import java.util.List;

public class Result{
    public static class Recommendation {
        private Boolean regexMatches;
        private List<String> nextPOSRecommendations;
        private String explanation;
        private String example;
        private String name;
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
