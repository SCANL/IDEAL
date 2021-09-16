package com.github.astyer.naturallanguagelabplugin;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiVariable;

import java.util.ArrayList;
import java.util.List;

public class SampleVisitor extends JavaRecursiveElementVisitor {
    private ArrayList<String> variableNames = new ArrayList<>();

    @Override
    public void visitVariable(PsiVariable variable) {
        System.out.println("Variable \"" + variable.getName() + "\" found");
        variableNames.add(variable.getName());
        super.visitVariable(variable);
    }

    public ArrayList<String> getVariableNames(){
        return variableNames;
    }
}
