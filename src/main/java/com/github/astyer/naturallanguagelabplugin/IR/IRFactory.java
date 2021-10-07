package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

public class IRFactory {

    public Variable createVariable(PsiVariable psiVariable) {
        PsiType type = psiVariable.getType();
        String typeString = type.toString();
        if (type instanceof PsiArrayType) {
            typeString = "Array";
        }
        return new Variable(psiVariable.getName(), typeString, psiVariable);
    }
}
