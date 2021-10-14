package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public class Class implements Identifier {

    private final String name;
    private final String type;
    private final PsiElement psiObject;
    private String pos = null;

    public Class(String name, ClassType type, PsiElement psiObject){
        this.name = name;
        this.type = type.toString();
        this.psiObject = psiObject;
    }

    public String getType() {
        return type;
    }

    @Override
    public PsiElement getPsiObject() {
        return psiObject;
    }

    public String getName() {
        return name;
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
        return "CLASS";
    }


}

