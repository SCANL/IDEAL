package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

public class IRFactory {

    private static String typeToString(PsiType type){
        if (type instanceof PsiArrayType) {
            return "Array";
        } else {
            return type.getCanonicalText();
        }
    }

    public static Variable createVariable(PsiVariable psiVariable) {
        String typeString = typeToString(psiVariable.getType());
        return new Variable(psiVariable.getName(), typeString, psiVariable);
    }

    public static Method createMethod(PsiMethod psiMethod) {
        String typeString = typeToString(psiMethod.getReturnType());
        boolean performsConversion = false; //traverse psi tree to figure out if any casting happens inside the method
        boolean performsEventDrivenFunctionality = false;

        List<String> params = new ArrayList();
        for(PsiParameter param : psiMethod.getParameterList().getParameters()){
            params.add(typeToString(param.getType()));
        }
        String name = psiMethod.getName() + "("+ String.join(",", params) + ")";
        return new Method(name, typeString, performsConversion, performsEventDrivenFunctionality, psiMethod);
    }
}
