package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;

import java.util.Optional;

public interface RuleVisitor {
    Optional<Result> visitClass(Class c);
}
