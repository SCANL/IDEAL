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

/**
 * Gets a recommendation for an identifier for a given regex pattern
 * This is why we cant use the standard library for regex
 * We need to be able to figure out exactly where the string failed in addition to
 * being given some options that would make the string match the regex better
 */
public class RecommendationAlg {
    /**
     * A class for a single recommendation.
     */
    public static class Rec {
        /**
         * a single change. will either be insert or remove
         * These indexes rely on previous changes to be applied before applying itself.
         */
        public interface Change {
            /**
             * Apply the change to an identifier
             * @param original
             * @return
             */
            ArrayList<WordPos> apply(ArrayList<WordPos> original);
            Pair<ArrayList<WordPos>, Integer> getColoredId(ArrayList<WordPos> original, int offset);
            Pair<ArrayList<WordPos>, Integer> getColoredPOS(ArrayList<WordPos> original, int offset);
        }

        /**
         * a change that will insert "change" at "index"
         */
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

        /**
         * A change that will remove the word at "index"
         */
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

        /**
         * Print out the debug info for the rec
         * @return
         */
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

        // the original identifier
        private final List<WordPos> original;
        // a list of changes to get to the final identifier
        // note that applying the changes in order is important
        private final List<Change> changes;

        /**
         * get the final identifier by applying the changes
         * @return
         */
        public List<WordPos> getFinal(){
            ArrayList<WordPos> result = new ArrayList<>(original);
            for(Change change: changes){
                result = change.apply(result);
            }
            return result;
        }

        /**
         * create a new Rec from a word and list of changes
         * @param original
         * @param changes
         */
        public Rec(List<WordPos> original, List<Change> changes){
            this.changes = changes;
            this.original = original;
//            this.wordsRemoved = new ArrayList<>();
        }

        /**
         * copy a Rec
         * @param other
         */
        public Rec(Rec other){
            this.original = new ArrayList(other.original);
            this.changes = new ArrayList(other.changes);
        }

        /**
         * Add an insert to the changes
         * @param index the index for the insert
         * @param change the POS to be inserted
         */
        public void addInsert(int index, String change) {
            this.changes.add(new Insert(index, change));
        }

        /**
         * adds a remove to the changes
         * @param index the index to remove
         */
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

    /**
     * a word and POS pair
     */
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

    List<WordPos> original = new ArrayList<>();
    /**
     * A queue to hold eac reccomendations that we are going to try.
     */
    Queue<Rec> toTry = new LinkedList<>();
    Pattern pattern;
    int timeout = 500;

    /**
     * sets the number of tries that we should try before giving up
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Sets up the recommendation algorithem by processing the identifier
     * @param pattern
     * @param id
     */
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

    /**
     * Runs the actual recommendation algorithem
     * It is basicly doing a bredth first search of the binary tree of all the possible changes that we could apply to
     * the identifier. We can either add a new POS tag where the regex fails or we can remove the next word from the identifier
     * where the regex fails. We keep running this until we find a sequence of changes that works.
     * @return
     */
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
}
