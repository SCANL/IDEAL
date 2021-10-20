package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

public class IRFactory {

    public static Variable createVariable(PsiVariable psiVariable) {
        PsiType type = psiVariable.getType();
        String typeString = type.getCanonicalText();
        if (type instanceof PsiArrayType) {
            typeString = "Array";
        }
        return new Variable(psiVariable.getName(), typeString, psiVariable);
    }

    public static Method createMethod(PsiMethod psiMethod) {
        PsiType returnType = psiMethod.getReturnType();
        String typeString = returnType.getCanonicalText();
        boolean performsConversion = false; //traverse psi tree to figure out if any casting happens inside the method
        boolean performsEventDrivenFunctionality = false;
        return new Method(psiMethod.getName(), typeString, performsConversion, performsEventDrivenFunctionality, psiMethod);
    }
}
