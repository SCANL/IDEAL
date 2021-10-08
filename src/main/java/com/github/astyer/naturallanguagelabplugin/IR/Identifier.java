package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public interface Identifier {
    String getName();
    String getType();
    PsiElement getPsiObject();
    Optional<Result> accept(RuleVisitor visitor);
    String getPOS();
    Identifier getParent();
    List<Identifier> getChildren();
}
