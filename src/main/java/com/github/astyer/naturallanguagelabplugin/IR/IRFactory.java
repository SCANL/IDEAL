package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

public class IRFactory {

    public static Variable createVariable(PsiVariable psiVariable) {
        PsiType type = psiVariable.getType();
        String typeString = type.getCanonicalText();
        if (type instanceof PsiArrayType) {
            typeString = "Array";
        }
        return new Variable(psiVariable.getName(), 0, 0, typeString, psiVariable);
    }
}
