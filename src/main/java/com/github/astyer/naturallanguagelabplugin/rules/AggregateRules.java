package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.tree.NodeResult;
import com.github.astyer.naturallanguagelabplugin.rules.tree.RuleForest;
import com.kipust.regex.Dfa;

import java.util.*;
import java.util.stream.Collectors;

public class AggregateRules {
    //    Rule[] rules = {new NMNPL(), new NMN(), new V()};//new VNMN()};
    RuleForest forest = RuleForest.getInstance();

    public Result runAll(Identifier i) {
        List<NodeResult> nrs = forest.runIdentifier(i).stream().sorted(
            (NodeResult a, NodeResult b) -> {
                if(a.getDepth() - b.getDepth() == 0){
                    if(a.isIdentifierMatchesRegex() ^ b.isIdentifierMatchesRegex()){
                        if(a.isIdentifierMatchesRegex()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }else{
                        return 0;
                    }
                }else {
                    return a.getDepth() - b.getDepth();
                }
            }
        ).collect(Collectors.toList());
        if(nrs.isEmpty()){
            return new Result(i, new ArrayList<>());
        }
        return new Result(i, nrs.stream().map(
                (nr)->{
                    List<String> nextPosRec = new ArrayList<>();
                    if (nr.getRegexResult() instanceof Dfa.TrashResult){
                        Dfa.TrashResult tr = (Dfa.TrashResult) nr.getRegexResult();
                        nextPosRec = Arrays.asList(tr.getAcceptableOptions());
                    }
                    return new Result.Recommendation(nr.isIdentifierMatchesRegex(), nextPosRec, nr.getExplanation(), nr.getExample());
                }).collect(Collectors.toList())
        );
    }
}
