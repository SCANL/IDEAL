package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.Optional;

public interface Identifier {
    public String getName();
    public int getLineNumber();
    public int getColumnNumber();
    public String getType();
    public PsiElement getPsiObject();
    public Optional<Result> accept(RuleVisitor visitor);
    public String getPOS();
}
