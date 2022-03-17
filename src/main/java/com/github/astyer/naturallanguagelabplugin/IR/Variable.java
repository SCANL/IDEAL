package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public class Variable implements Identifier{
    String name, displayName, canonicalType;
    POSTagger.POSResult pos;
    IRFactory.IRType type;
    PsiElement element;

    public Variable(String name, String displayName, String canonicalType, PsiElement element, IRFactory.IRType type){
        this.name = name;
        this.displayName = displayName;
        this.canonicalType = canonicalType;
        this.element = element;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
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
        return pos.getPosTags();
    }

    @Override
    public String getIdentifierSplit() {
        if(pos == null){
            pos = POSTagger.getInstance().tag(this);
        }
        return pos.getId();
    }

    @Override
    public POSTagger.POSResult getPosResult() {
        if(pos == null){
            pos = POSTagger.getInstance().tag(this);
        }
        return this.pos;
    }

    public void setPosResult(POSTagger.POSResult pos){
        this.pos = pos;
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
