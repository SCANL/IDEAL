package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public class Class implements Identifier {
    String name, displayName, canonicalType;
    PsiElement element;
    ClassType type;
    private POSTagger.POSResult pos = null;

    public Class(String name, String displayName, PsiElement element, ClassType type){
        this.name = name;
        this.displayName = displayName;
        this.canonicalType = name;
        this.element = element;
        this.type = type;
    }

    public IRFactory.IRType getType() {
        return IRFactory.IRType.TYPE_OTHER;
    }

    @Override
    public PsiElement getPsiObject() {
        return this.element;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public enum ClassType{
        Class,
        Interface,
        Annotation,
        Record,
        Enum
    }

    public Optional<Result> accept(RuleVisitor v){
        return v.visitClass(this);
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
    public Identifier getParent() {
        return null;
    }

    @Override
    public List<Identifier> getChildren() {
        return null;
    }

    @Override
    public String getContext() {
        return "CLASS";
    }

    @Override
    public String getCanonicalType() {
        return this.canonicalType;
    }

    @Override
    public POSTagger.POSResult getPosResult() {
        if(pos == null){
            pos = POSTagger.getInstance().tag(this);
        }
        return this.pos;
    }


}

