package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.tree.NodeResult;
import com.github.astyer.naturallanguagelabplugin.rules.tree.RuleForest;

import java.util.*;
import java.util.stream.Collectors;

public class AggregateRules {
    //    Rule[] rules = {new NMNPL(), new NMN(), new V()};//new VNMN()};
    RuleForest forest = RuleForest.getInstance();

    public List<Result> runAll(Identifier i) {
        List<NodeResult> nr = forest.runIdentifier(i);
        int maxDepth = nr.stream().max(Comparator.comparingInt(a->a.getDepth())).get().getDepth();
        List<NodeResult> maxDepthNRs = nr.stream().filter(r -> r.getDepth() == maxDepth).collect(Collectors.toList());
        Optional<NodeResult> maxDepthViolation =  maxDepthNRs.stream().filter(r -> !r.isIdentifierMatchesRegex()).findFirst();
        if(maxDepthViolation.isEmpty()){
            return new ArrayList<>();
        }else{
            NodeResult violation = maxDepthViolation.get();
            return List.of(new Result(violation.getRecommendation(), violation.getDepth()));
        }
//        return nr.stream().map(nodeResult -> new Result(nodeResult.getRecommendation(), nodeResult.getDepth())).collect(Collectors.toList());
    }
}
