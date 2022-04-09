package com.github.astyer.naturallanguagelabplugin.rules.Recommendation;

import com.github.astyer.naturallanguagelabplugin.IR.IRFactory;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.kipust.regex.Dfa;
import com.kipust.regex.Pattern;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationAlg {
    public static class Rec {
        public interface Change {
            ArrayList<WordPos> apply(ArrayList<WordPos> original);
            Pair<ArrayList<WordPos>, Integer> getColoredId(ArrayList<WordPos> original, int offset);
            Pair<ArrayList<WordPos>, Integer> getColoredPOS(ArrayList<WordPos> original, int offset);
        }
        public static class Insert implements Rec.Change {
            public int index;
            public String change;

            public Insert(int index, String change){
                this.index = index;
                this.change = change;
            }

            @Override
            public ArrayList<WordPos> apply(ArrayList<WordPos> original) {
                ArrayList<WordPos> result = new ArrayList<WordPos>(original);
                result.add(index, new WordPos("", change));
                return result;
            }

            @Override
            public Pair<ArrayList<WordPos>,Integer> getColoredId(ArrayList<WordPos> original, int offset) {
                ArrayList<WordPos> result = new ArrayList<>(original);
                result.add(index + offset, new WordPos( "", change));
                return Pair.with(result, offset);
            }

            @Override
            public Pair<ArrayList<WordPos>, Integer> getColoredPOS(ArrayList<WordPos> original, int offset) {
                ArrayList<WordPos> result = new ArrayList<>(original);
                result.add(index + offset, new WordPos( "", "<b><span style='color: green'>" + change + "</span></b>"));
                return Pair.with(result, offset);
            }

        }
        public static class Remove implements Rec.Change {
            public int index;

            public Remove(int index){
                this.index = index;
            }

            public ArrayList<WordPos> apply(ArrayList<WordPos> original){
                ArrayList<WordPos> result = new ArrayList<>(original);
                result.remove(index);
                return result;
            }

            @Override
            public Pair<ArrayList<WordPos>,Integer> getColoredId(ArrayList<WordPos> original, int offset) {
                ArrayList<WordPos> result = new ArrayList<>(original);
                WordPos item = original.get(index + offset);
                result.set(index + offset, new WordPos( "<b><span style='color: red'>" + item.getWord() + "</span></b>", item.getPos()));
                return Pair.with(result, offset+1);
            }

            @Override
            public Pair<ArrayList<WordPos>,Integer> getColoredPOS(ArrayList<WordPos> original, int offset) {
                ArrayList<WordPos> result = new ArrayList<>(original);
                result.remove(index + offset);
                return Pair.with(result, offset);
            }
        }

        public String debug(){
            StringBuilder sb = new StringBuilder("Starting with \"");
            sb.append(this.original.toString()).append("\"\n");
            ArrayList<WordPos> current = new ArrayList<>(this.original);
            for(Change change: changes){
                current = change.apply(current);
                if(change instanceof Insert){
                    Insert i = (Insert)change;
                    sb.append("Inserting ").append(i.change).append(" at ").append(i.index);
                }else if(change instanceof Remove){
                    Remove r = (Remove)change;
                    sb.append("Removing at ").append(r.index).append("\n");

                }
                sb.append("resulting in \"").append(current.toString()).append("\"");
                sb.append("\n");
            }
            sb.append("Ending up with ").append(current).append("\n");
            return sb.toString();
        }

        private final List<WordPos> original;
        private final List<Change> changes;

        public List<WordPos> getFinal(){
            ArrayList<WordPos> result = new ArrayList<>(original);
            for(Change change: changes){
                result = change.apply(result);
            }
            return result;
        }

        public Rec(List<WordPos> original, List<Change> changes){
            this.changes = changes;
            this.original = original;
//            this.wordsRemoved = new ArrayList<>();
        }
        public Rec(Rec other){
            this.original = new ArrayList(other.original);
            this.changes = new ArrayList(other.changes);
        }

        public void addInsert(int index, String change) {
            this.changes.add(new Insert(index, change));
        }
        public void addRemove(int index) {
            this.changes.add(new Remove(index));
        }

        public List<Change> getChanges() {
            return changes;
        }

        public List<WordPos> getOriginal(){
            return original;
        }

        @Override
        public String toString() {
            return getFinal().stream().map(WordPos::getWord).collect(Collectors.joining()) +
                    ": " +
                    getFinal().stream().map(WordPos::getPos).collect(Collectors.joining());
        }

        public String getFinalId(){
            return getFinal().stream().map(WordPos::getWord).collect(Collectors.joining(""));
        }

        public String getFinalPos(){
            return getFinal().stream().map(WordPos::getPos).collect(Collectors.joining(""));
        }
    }

    public static class WordPos{
        private String word;
        private String pos;
        public WordPos(String word, String pos){
            this.word = word;
            this.pos = pos;
        }

        public String getPos() {
            return pos;
        }
        public String getWord() {
            return word;
        }
    }

    List<WordPos> original = new ArrayList<>();;
    Queue<Rec> toTry = new LinkedList<>();
    Pattern pattern;
    int timeout = 500;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public RecommendationAlg(Pattern pattern, Identifier id){
        String[] poss = id.getPosResult().getPosTags().split("_");
        String[] words = id.getPosResult().getId().split("_");
        int max = Math.max(poss.length, words.length);
        for(int i = 0; i< max; i++){
            String pos = i < poss.length ? poss[i]+"_" : "";
            String word = i < words.length ? words[i]+"_" : "";
            original.add(new WordPos(word, pos));
        }

        toTry.add(new Rec(original, new ArrayList<>()));
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
            if(seen.containsKey(currentAttempt.toString())){
                continue;
            }else{
                seen.put(currentAttempt.toString(), true);
            }
            System.out.println("trying: " + currentAttempt.toString());
            Dfa.DFAResult result = pattern.match(currentAttempt.getFinalPos());
            if(result.success()){
                return currentAttempt;
            }else{
                if(result instanceof Dfa.TrashResult){
                    Dfa.TrashResult tr = (Dfa.TrashResult) result;
                    int failIndex = posIndexToArrIndex(tr.getIndex(), currentAttempt.getFinal());

                    //remove
                    if(failIndex < currentAttempt.getFinal().size()) {
                        Rec remove = new Rec(currentAttempt);
                        remove.addRemove(failIndex);
                        toTry.add(remove);
                    }

                    //inserts
                    List<String> options = Arrays.stream(tr.getAcceptableOptions()).map(option -> option.replaceAll("\"", "")).collect(Collectors.toList());
                    for (String option : options){
                        Rec add = new Rec(currentAttempt);
                        add.addInsert(failIndex, option);
                        toTry.add(add);
                    }
                }
            }
        }
        return null;
    }

    int posIndexToArrIndex(int index, List<WordPos> arr){
        int i = 0;
        while(index > 0){
            index -= arr.get(i).getPos().length();
            i++;
        }
        return i;
    }

    public static void main(String[] args) {
        Pattern p = Pattern.Compile("&(*('NM_'),'NPL_')");
//        String str = "P_NM_V_NM_N_";
        // (black)NM_NM_(green)NM_(black)N_
        Variable v = new Variable("userName","display name", null, null, IRFactory.IRType.TYPE_OTHER, false);
        v.setPosResult(new POSTagger.POSResult("NM_NPL_N_N_", "user_Name_Admin_Name"));
        RecommendationAlg r = new RecommendationAlg(p, v);
        Rec recs = r.getRecommendation();
        System.out.println(recs.debug());
    }
}
