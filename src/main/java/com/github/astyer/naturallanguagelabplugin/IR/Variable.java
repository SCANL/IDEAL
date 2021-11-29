package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public class Variable implements Identifier{
    String name, canonicalType, pos;
    IRFactory.IRType type;
    PsiElement element;
//    TODO: make type enum or class/enum
    public Variable(String name, String canonicalType, PsiElement element,IRFactory.IRType type){
        this.name = name;
        this.canonicalType = canonicalType;
        this.element = element;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IRFactory.IRType getType() {
        return this.type;
    }

    @Override
    public PsiElement getPsiObject() {
        return this.element;
    }

    @Override
    public Optional<Result> accept(RuleVisitor visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public String getPOS() {
        if(pos == null){
            pos = POSTagger.getInstance().tag(this);
        }
        return pos;
    }

    @Override
    public Identifier getParent() {
        return null;
    }

    @Override
    public List<Identifier> getChildren() {
        return null;
    }

    @Override
    public String getContext() {
        return "DECLARATION";
    }

    @Override
    public String getCanonicalType() {
        return canonicalType;
    }
}
