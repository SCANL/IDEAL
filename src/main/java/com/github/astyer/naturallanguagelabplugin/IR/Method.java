package com.github.astyer.naturallanguagelabplugin.IR;

import com.github.astyer.naturallanguagelabplugin.rules.POSTagger;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.RuleVisitor;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Optional;

public class Method implements Identifier{
    String name;
    String canonicalType;
    POSTagger.POSResult pos;
    IRFactory.IRType type;
    boolean performsConversion, performsEventDrivenFunctionality;
    PsiElement element;

    public Method(String name, String canonicalType, boolean performsConversion, boolean performsEventDrivenFunctionality, PsiElement element, IRFactory.IRType type){
        this.name = name;
        this.type = type;
        this.performsConversion = performsConversion;
        this.performsEventDrivenFunctionality = performsEventDrivenFunctionality;
        this.element = element;
        this.canonicalType = canonicalType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public boolean performsConversion() {
        return performsConversion;
    }

    public boolean performsEventDrivenFunctionality() {
        return performsEventDrivenFunctionality;
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
        return visitor.visitMethod(this);
    }

    @Override
    public String getPOS() {
        if(pos == null){
            pos = POSTagger.getInstance().tag(this);
        }
        return pos.getPosTags();
    }

    @Override
    public String getIdentiferSplit() {
        if(pos == null){
            pos = POSTagger.getInstance().tag(this);
        }
        return pos.getId();
    }

    @Override
    public POSTagger.POSResult getPosResult() {
        return this.pos;
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
        return "FUNCTION";
    }

    @Override
    public String getCanonicalType() {
        return this.canonicalType;
    }
}
