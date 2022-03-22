package com.kipust.regex;

import java.util.*;

public class Dfa {
    public abstract class DFAResult{
        public abstract boolean success();
    }
    public class AcceptingResult extends DFAResult{
        @Override
        public boolean success() {
            return true;
        }
    }
    public class TrashResult extends DFAResult{
        private String remainingTokens;
        private String[] acceptableOptions;
        private int index;
        public TrashResult(String remainingTokens, int index, String[] acceptableOptions){
            this.remainingTokens = remainingTokens;
            this.acceptableOptions = acceptableOptions;
            this.index = index;
        }

        public String getRemainingTokens(){return remainingTokens;}
        public String[] getAcceptableOptions(){return acceptableOptions;}
        public int getIndex() {return index;}

        @Override
        public boolean success(){
            return false;
        }
    }
    public class NonAcceptingResult extends DFAResult{
        @Override
        public boolean success(){
            return false;
        }
    }

    List<DfaNode> allNodes = new ArrayList<>();
    Set<Const> allConsts;
    DfaNode root;
    String finalStr;
    Boolean anyDfa = false;
    public Dfa(AST regex){
        FindConstantsVisitor fcv = new FindConstantsVisitor();
        regex.accept(fcv);
        allConsts = fcv.constants;
        root = new DfaNode(regex);
        root.createTransitions();
    }

    protected Dfa(){
        this.anyDfa = true;
    }

    public DFAResult run(String str){
        if(anyDfa) {
            return new AcceptingResult();
        }
        return root.run(str, 0);
    }

    public class DfaNode{
        AST regex;
        Map<Const, DfaNode> transitions = new HashMap<>();
        Boolean accepting = false;
        Boolean trash = false;

        public DfaNode(AST regex){
            allNodes.add(this);
            this.regex = regex;
            this.trash = regex instanceof AST.EmptySet;
        }

        public void createTransitions(){
            if(transitions.size() != 0 || this.trash){
                return;
            }
            for(Const cons: allConsts){
                AST derivative = regex.derivative(cons);
                DfaNode otherNode = null;
                for(DfaNode node: allNodes){
                    if(node.regex.equals(derivative)){
                        otherNode = node;
                        break;
                    }
                }
                if(otherNode == null){
                    otherNode = new DfaNode(derivative);
                }
                transitions.put(cons, otherNode);
            }
            for(DfaNode node: transitions.values()){
                node.createTransitions();;
            }
            accepting = regex.acceptsEmpty();
        }

        public DFAResult run(String str, int index){
            finalStr = str;
            if(this.trash){
                return new TrashResult(str, index, transitions.keySet().stream().filter(key -> !transitions.get(key).trash).toArray(String[] ::new));
            }
            if(str.length() == 0){
                return accepting ? new AcceptingResult() : new NonAcceptingResult();
            }
            for(Const key: transitions.keySet()){
                if(key instanceof Const.Value) {
                    String keyVal = ((Const.Value) key).value;
                    if (str.startsWith(keyVal)) {
                        DfaNode next = transitions.get(key);
                        if (!next.trash) {
                            return next.run(str.replaceFirst(keyVal, ""), index + keyVal.length());
                        }
                    }
                }else{
                    DfaNode next = transitions.get(key);
                    if(!next.trash){
                        return next.run(str.substring(1), index + 1);
                    }
                }
            }
            return new TrashResult(str, index, transitions.keySet().stream().filter(key -> !transitions.get(key).trash).map(key -> "\"" + key + "\"").toArray(String[] ::new));
        }
    }

}
