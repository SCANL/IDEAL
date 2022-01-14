package com.github.astyer.naturallanguagelabplugin.rules.Recommendation;

import com.kipust.regex.Dfa;
import com.kipust.regex.Pattern;

import java.util.*;
import java.util.stream.Collectors;

public class Recommendation {
    static class Rec {
        static class Change {
            int start;
            int end;
            public Change(int start, int end){
                this.start = start;
                this.end = end;
            }
        }

        private String recommendation;
        private List<Change> changes;

        public Rec(String recommendation, List<Change> changes){
            this.changes = changes;
            this.recommendation = recommendation;
        }
        public Rec(Rec other, String newRec){
            this.recommendation = newRec;
            this.changes = new ArrayList(other.changes);
        }

        public void addChange(int start, int end){
            this.changes.add(new Change(start, end));
        }

        public String getRecommendation() {
            return recommendation;
        }

        public List<Change> getChanges() {
            return changes;
        }

        @Override
        public String toString() {
            return recommendation;
        }
    }

    Queue<Rec> toTry = new LinkedList<>();
    Pattern pattern;
    int timeout = 500;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Recommendation(Pattern pattern, String str){
        toTry.add(new Rec(str, new ArrayList<>()));
        this.pattern = pattern;

    }

    public Rec getRecommendation(){
        int tries = 0;
        while(!toTry.isEmpty()){
            tries++;
            if(tries > timeout){
                break;
            }
            Rec currentAttempt = toTry.remove();
            System.out.println("trying: " + currentAttempt);
            Dfa.DFAResult result = pattern.match(currentAttempt.getRecommendation());
            if(result.success()){
                return currentAttempt;
            }else{
                if(result instanceof Dfa.TrashResult){
                    Dfa.TrashResult tr = (Dfa.TrashResult) result;
                    List<String> options = Arrays.stream(tr.getAcceptableOptions()).map(option -> option.substring(1,option.length()-2) + "_").collect(Collectors.toList());
                    for (String option : options){
                        for(int replaceIndex = 0; replaceIndex <= tr.getRemainingTokens().split("_").length; replaceIndex++){
                            String preReplace = currentAttempt.getRecommendation().substring(0,tr.getIndex());
                            int index = getActualIndex(currentAttempt.getRecommendation(), tr, replaceIndex);
                            String postReplace = currentAttempt.getRecommendation().substring(index);
                            Rec newAttempt = new Rec(currentAttempt, preReplace + option + postReplace);
                            newAttempt.addChange(tr.getIndex(), index);
                            toTry.add(newAttempt);
                            System.out.println("adding " + newAttempt);
                        }
                    }
                }
            }
        }
        return new Rec("Unable to generate recommendation", new ArrayList<>());
    }

    private int getActualIndex(String currentAttempt, Dfa.TrashResult tr, int replaceIndex) {
        int index = tr.getIndex();
        for(int n = 0; n < replaceIndex; index++){
            if(currentAttempt.charAt(index) == '_'){
                n++;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        Pattern p = Pattern.Compile("&(*('NM_'),'N_')");
        String str = "NM_NM_V_N_V_";
        Recommendation r = new Recommendation(p, str);
        Rec rec = r.getRecommendation();
        for(int i = 0; i<rec.recommendation.length(); i++){
            int finalI = i;
            if(rec.changes.stream().anyMatch(ch -> ch.start <= finalI && finalI <= ch.end)){
                System.err.print(rec.recommendation.charAt(i));
            }else{
                System.out.print(rec.recommendation.charAt(i));
            }
        }
        System.out.println();
    }
}
