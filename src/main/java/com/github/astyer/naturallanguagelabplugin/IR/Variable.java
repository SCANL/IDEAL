package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

/**
 * represents a variable in the users code
 */
public class Variable implements Identifier{
    String name, displayName, canonicalType;
    POSTagger.POSResult pos;
    IRFactory.IRType type;
    PsiElement element;
    boolean isLoopResult;

    public Variable(String name, String displayName, String canonicalType, PsiElement element, IRFactory.IRType type, boolean isLoopResult){
        this.name = name;
        this.displayName = displayName;
        this.canonicalType = canonicalType;
        this.element = element;
        this.type = type;
        this.isLoopResult = isLoopResult;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    public boolean getIsLoopResult() {
        return isLoopResult;
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
