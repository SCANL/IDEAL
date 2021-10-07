package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AggregateRules {
    RuleVisitor[] rules = {new NMNPL(), new NMN()};

    public List<Result> runAll(Identifier i){
        List<Result> results = Arrays.stream(rules)
                .parallel()
                .map(i::accept)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return results;
    }

}
