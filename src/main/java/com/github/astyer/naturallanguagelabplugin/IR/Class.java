package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.Optional;

public class Class implements Identifier {

    private final String name;
    private final int lineNumber;
    private final int columnNumber;
    private final String type;
    private final String returnType;
    private final PsiElement psiObject;
    private String pos = null;

    public Class(String name, int lineNumber, int columnNumber, ClassType type, String returnType, PsiElement psiObject){
        this.name = name;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.type = type.toString();
        this.psiObject = psiObject;
        this.returnType = returnType;
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

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getReturnType(){return  returnType;}

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
            pos = POSTagger.getInstance().tag(getName());
        }
        return pos;
    }
}

