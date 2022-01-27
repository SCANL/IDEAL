package com.github.astyer.naturallanguagelabplugin.rules.Recommendation;

import com.kipust.regex.Dfa;
import com.kipust.regex.Pattern;

import java.util.*;
import java.util.stream.Collectors;

public class Recommendation {
    static class Rec {
        interface Change {
            String apply(String original);
        }
        static class Insert implements Rec.Change {
            int index;
            String change;

            public Insert(int index, String change){
                this.index = index;
                this.change = change;
            }

            public String apply(String original){
                return original.substring(0,index) + change + original.substring(index);
            }
        }
        static class Remove implements Rec.Change {
            int index;
            int length;

            public Remove(int index, int length){
                this.index = index;
                this.length = length;
            }

            public String apply(String original){
                return original.substring(0, index) + original.substring(index+length);
            }
        }

        public String debug(){
            StringBuilder sb = new StringBuilder("Starting with \"");
            sb.append(this.original).append("\"\n");
            String current = this.original;
            for(Change change: changes){
                current = change.apply(current);
                if(change instanceof Insert){
                    Insert i = (Insert)change;
                    sb.append("Inserting ").append(i.change).append(" at ").append(i.index);
                }else if(change instanceof Remove){
                    Remove r = (Remove)change;
                    sb.append("Removing ").append(r.length).append(" chars at ").append(r.index);
                }
                sb.append(" resulting in \"").append(current).append("\"");
                sb.append("\n");
            }
            sb.append("Ending up with ").append(current).append("\n");
            return sb.toString();
        }

        private final String original;
        private final List<Change> changes;

        public String getFinal(){
            String result = original;
            for(Change change: changes){
                result = change.apply(result);
            }
            return result;
        }

        public Rec(String original, List<Change> changes){
            this.changes = changes;
            this.original = original;
        }
        public Rec(Rec other){
            this.original = other.original;
            this.changes = new ArrayList(other.changes);
        }

        public void addInsert(int index, String change) {
            this.changes.add(new Insert(index, change));
        }
        public void addRemove(int index, int length) {
            this.changes.add(new Remove(index, length));
        }

        public List<Change> getChanges() {
            return changes;
        }

        public String getOriginal(){
            return original;
        }

        @Override
        public String toString() {
            return getFinal();
        }
    }
    String original;
    Queue<Rec> toTry = new LinkedList<>();
    Pattern pattern;
    int timeout = 500;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Recommendation(Pattern pattern, String str){
        this.original = str;
        toTry.add(new Rec(str, new ArrayList<>()));
        this.pattern = pattern;

    }

    public Rec getRecommendation(){
        HashMap<String, Boolean> seen = new HashMap<>();
//        List<Rec> works = new ArrayList<>();
        int tries = 0;
        while(!toTry.isEmpty()){
            tries++;
            if(tries > timeout){
                System.out.println("timeout");
                break;
            }
            Rec currentAttempt = toTry.remove();
            String currentAttemptStr = currentAttempt.getFinal();
            if(seen.containsKey(currentAttemptStr)){
                continue;
            }else{
                seen.put(currentAttemptStr, true);
            }
            System.out.println("trying: " + currentAttempt);
            Dfa.DFAResult result = pattern.match(currentAttemptStr);
            if(result.success()){
                return currentAttempt;
            }else{
                if(result instanceof Dfa.TrashResult){
                    Dfa.TrashResult tr = (Dfa.TrashResult) result;

                    //remove
                    Rec remove = new Rec(currentAttempt);
                    remove.addRemove(tr.getIndex(), getSizeOfNextTok(currentAttemptStr, tr.getIndex()));
                    toTry.add(remove);

                    //inserts
                    List<String> options = Arrays.stream(tr.getAcceptableOptions()).map(option -> option.substring(1,option.length()-2) + "_").collect(Collectors.toList());
                    for (String option : options){
                        Rec add = new Rec(currentAttempt);
                        add.addInsert(tr.getIndex(), option);
                        toTry.add(add);
                    }
                }
            }
        }
        return new Rec(this.original, new ArrayList<>());
    }

    private int getSizeOfNextTok(String str, int index){
        int i;
        for(i = 0; i >= str.length() || str.charAt(index + i) != '_'; i++){}
        return i+1;
    }

    public static void main(String[] args) {
        //TODO: just additions and deletions then after calc mappings
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        Pattern p = Pattern.Compile("&(*('NM_'),'N_')");
        String str = "NM_NM_NM_NPL_";
        // (black)NM_NM_(green)NM_(black)N_
        Recommendation r = new Recommendation(p, str);
        Rec recs = r.getRecommendation();
        System.out.println(recs.debug());
        // TODO: give back rec with closest lenght
    }
}
