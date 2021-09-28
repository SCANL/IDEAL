package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;

import java.util.Optional;

public abstract class RuleVisitor {
    public Optional<Result> visitClass(Class c){
        return Optional.empty();
    }
    public Optional<Result> visitVariable(Variable v){
        return Optional.empty();
    }
}
