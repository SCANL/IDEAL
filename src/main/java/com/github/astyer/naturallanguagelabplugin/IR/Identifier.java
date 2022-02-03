package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public interface Identifier {
    /**
     * @return The identifier name used for the POS tagger (may contain some extra context info)
     */
    String getName();
    /**
     * @return The plain identifier name with no extra context info for display purposes
     */
    String getDisplayName();
    IRFactory.IRType getType();
    PsiElement getPsiObject();
    Optional<Result> accept(RuleVisitor visitor);
    String getPOS();
    String getIdentiferSplit();
    Identifier getParent();
    List<Identifier> getChildren();
    String getContext();
    String getCanonicalType();
    POSTagger.POSResult getPosResult();
}
