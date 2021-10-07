package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public class Variable implements Identifier{
    String name, type, pos;
    int lineNumber, colNumber;
    PsiElement element;
//    TODO: make type enum or class
    public Variable(String name, int lineNumber, int colNumber, String type, PsiElement element){
        this.name = name;
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
        this.type = type;
        this.element = element;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public int getColumnNumber() {
        return this.lineNumber;
    }

    @Override
    public String getType() {
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
            pos = POSTagger.getInstance().tag(getName());
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
}
