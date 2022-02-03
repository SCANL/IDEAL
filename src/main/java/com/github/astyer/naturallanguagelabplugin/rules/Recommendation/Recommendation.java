package com.github.astyer.naturallanguagelabplugin.rules.Recommendation;

import com.github.astyer.naturallanguagelabplugin.IR.IRFactory;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.intellij.psi.impl.cache.impl.id.IdIndex;
import com.kipust.regex.Dfa;
import com.kipust.regex.Pattern;

import java.util.*;
import java.util.stream.Collectors;

public class Recommendation {
    static class Rec {
        interface Change {
            String apply(String original);
            String applyId(String original);
        }
        static class Insert implements Rec.Change {
            int index;
            String change;
            int idIndex;

            public Insert(int index, String change, int idIndex){
                this.index = index;
                this.change = change;
                this.idIndex = idIndex;
            }

            public String apply(String original){
                return original.substring(0,index) + change + original.substring(index);
            }

            @Override
            public String applyId(String original) {
                return original.substring(0,idIndex) + "_" + original.substring(idIndex);
            }
        }
        static class Remove implements Rec.Change {
            int index;
            int length;
            int idIndex;
            int idLength;

            public Remove(int index, int length, int idIndex, int idLength){
                this.index = index;
                this.length = length;
                this.idIndex = idIndex;
                this.idLength = idLength;
            }

            public String apply(String original){
                return original.substring(0, index) + original.substring(index+length);
            }

            @Override
            public String applyId(String original) {
                return original.substring(0, idIndex) + original.substring(idIndex+idLength);
            }
        }

        public String debug(){
            StringBuilder sb = new StringBuilder("Starting with \"");
            sb.append(this.original.getPosTags()).append(":").append(this.original.getId()).append("\"\n");
            String current = this.original.getPosTags();
            String currentId = this.original.getId();
            for(Change change: changes){
                current = change.apply(current);
                currentId = change.applyId(currentId);
                if(change instanceof Insert){
                    Insert i = (Insert)change;
                    sb.append("Inserting ").append(i.change).append(" at ").append(i.index);
                }else if(change instanceof Remove){
                    Remove r = (Remove)change;
                    sb.append("Removing ").append(r.length).append(" chars at ").append(r.index).append(" from pos \n");
                    sb.append("Removing ").append(r.idLength).append(" chars at ").append(r.idLength).append(" from identifier\n");

                }
                sb.append("resulting in \"").append(current).append("\":\"").append(currentId).append("\"");
                sb.append("\n");
            }
            sb.append("Ending up with ").append(current).append("\n");
            sb.append("Final POS: ").append(this.getFinal()).append("\n");
            sb.append("Final ID: ").append(this.getResultId()).append("\n");
            sb.append("Word Indexes: ").append(this.getIndexesOfFinalId().stream().map(n -> n.toString()).collect(Collectors.joining(",")));
//            sb.append("Removed words: \n");
//            for(int index : this.wordsRemoved){
//                sb.append("\t").append(original.getId(), index, original.getId().substring(index).indexOf('_'));
//                sb.append(": ").append(index).append("\n");
//            }
            return sb.toString();
        }

        private final POSTagger.POSResult original;
        private final List<Change> changes;

        public String getFinal(){
            String result = original.getPosTags();
            for(Change change: changes){
                result = change.apply(result);
            }
            return result;
        }

        private String getFinalId(){
            String result = original.getId();
            for(Change change: changes){
                result = change.applyId(result);
            }
            return result;
        }

        public String getResultId(){
            return Arrays.stream(getFinalId().split("_")).
                    map(word -> word.substring(0,word.indexOf('.'))).
                    collect(Collectors.joining());
        }

        public List<Integer> getIndexesOfFinalId(){
            return Arrays.stream(getFinalId().split("_"))
                    .map(word -> word.substring(word.indexOf('.')+1))
                    .map(num -> Integer.parseInt(num))
                    .collect(Collectors.toList());
        }

        public Rec(POSTagger.POSResult original, List<Change> changes){
            this.changes = changes;
            this.original = original;
//            this.wordsRemoved = new ArrayList<>();
        }
        public Rec(Rec other){
            this.original = other.original;
            this.changes = new ArrayList(other.changes);
        }

        public void addInsert(int index, String change, int idIndex) {
            this.changes.add(new Insert(index, change, idIndex));
        }
        public void addRemove(int index, int length, int idIndex, int idLength) {
            this.changes.add(new Remove(index, length, idIndex, idLength));
//            this.wordsRemoved.add(wordsRemovedIndex);
        }

        public List<Change> getChanges() {
            return changes;
        }

        private POSTagger.POSResult getOriginal(){
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
    Identifier id;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Recommendation(Pattern pattern, Identifier id){
        String labeledId = "";
        String unLabId = id.getPosResult().getId();
        int count = 0;
        for(int j = 0; j < unLabId.length(); j++){
            if(unLabId.charAt(j) == '_'){
                labeledId += "." + count++;
            }
            labeledId += unLabId.charAt(j);
        }
        this.original = labeledId;
        toTry.add(new Rec(new POSTagger.POSResult(id.getPosResult().getPosTags(), labeledId), new ArrayList<>()));
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
            String currentAttemptId = currentAttempt.getFinalId();
            if(seen.containsKey(currentAttemptStr)){
                continue;
            }else{
                seen.put(currentAttemptStr, true);
            }
            System.out.println("trying: " + currentAttemptStr +": "+currentAttemptId);
            Dfa.DFAResult result = pattern.match(currentAttemptStr);
            if(result.success()){
                return currentAttempt;
            }else{
                if(result instanceof Dfa.TrashResult){
                    Dfa.TrashResult tr = (Dfa.TrashResult) result;

                    //remove
                    Rec remove = new Rec(currentAttempt);
                    int idIndex = posIndexToIdIndex(currentAttemptStr, currentAttemptId, tr.getIndex());
                    int idSize = posIndexToIdIndex(currentAttemptStr, currentAttemptId, tr.getIndex()+getSizeOfNextTok(currentAttemptStr, tr.getIndex())) - idIndex;
//                    int wordsRemovedIndex = wordIndexToIdIndex(original, idIndexToWordIndex(currentAttemptId, idIndex) + currentAttempt.wordsRemoved.size());
                    remove.addRemove(tr.getIndex(), getSizeOfNextTok(currentAttemptStr, tr.getIndex()), idIndex, idSize);
                    toTry.add(remove);

                    //inserts
                    List<String> options = Arrays.stream(tr.getAcceptableOptions()).map(option -> option.substring(1,option.length()-2) + "_").collect(Collectors.toList());
                    for (String option : options){
                        Rec add = new Rec(currentAttempt);
                        add.addInsert(tr.getIndex(), option, posIndexToIdIndex(currentAttemptStr, currentAttemptId, tr.getIndex()));
                        toTry.add(add);
                    }
                }
            }
        }
        return null;
    }

    private int idIndexToWordIndex(String id, int idIndex){
        int result = 0;
        for(int i = 0; i< idIndex; i++){
            if(id.charAt(i) == '_'){
                result++;
            }
        }
        return result;
    }

    private int wordIndexToIdIndex(String id, int wordIndex){
        int result = 0;
        while(wordIndex > 0){
            if(id.charAt(result) == '_'){
                wordIndex--;
            }
            result++;
        }
        return result;
    }
    private int posIndexToIdIndex(String pos, String id, int posIndex){
        if(posIndex >= pos.length()){
            return id.length();
        }
        int tokCount = 0;
        for(int i = 0; i<=posIndex;i++){
            if(pos.charAt(i) == '_'){
                tokCount++;
            }
        }
        int idIndex = 0;
        while(tokCount != 0){
            if(id.charAt(idIndex) == '_'){
                tokCount--;
            }
            idIndex++;
        }
        return idIndex;
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
        Pattern p = Pattern.Compile("&('V_',&('P_',&(*('NM_'),'N_')))");
//        String str = "P_NM_V_NM_N_";
        // (black)NM_NM_(green)NM_(black)N_
        Variable v = new Variable("firstUserNameAdminAccount", null, null, IRFactory.IRType.TYPE_OTHER);
        v.setPosResult(new POSTagger.POSResult("V_P_VM_V_", "a_b_c_d_"));
        Recommendation r = new Recommendation(p, v);
        Rec recs = r.getRecommendation();
        System.out.println(recs.debug());
    }
}
