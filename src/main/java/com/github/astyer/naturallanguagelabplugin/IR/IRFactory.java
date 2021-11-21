package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.*;

public class IRFactory {

    private static HashMap<PsiType, Boolean> isCollectionPsiTypeCache = new HashMap<>();

    // maybe move these helper functions into a helper class
    private static boolean isSomeCollection(PsiType type, Project project) {
        if(type instanceof PsiArrayType) {
            return true;
        }

        Boolean cachedIsCollectionResult = isCollectionPsiTypeCache.get(type);
        if(cachedIsCollectionResult != null) {
            return cachedIsCollectionResult;
        }

        PsiElementFactory psiElementFactory = PsiElementFactory.getInstance(project);
        PsiType collectionPsiType = psiElementFactory.createTypeByFQClassName(Collection.class.getName());
        PsiType mapPsiType = psiElementFactory.createTypeByFQClassName(Map.class.getName());

        //check if the type is assignable to Collection or Map
        if(collectionPsiType.isAssignableFrom(type) || mapPsiType.isAssignableFrom(type)) {
            isCollectionPsiTypeCache.put(type, true);
            return true;
        }
        isCollectionPsiTypeCache.put(type, false);
        return false;
    }

    private static String typeToString(PsiType type, Project project){
        if(type == null) {
            return "null";
        }
        if (isSomeCollection(type, project)) {
            return "Array";
        } else {
            return type.getCanonicalText();
        }
    }

    public static Variable createVariable(PsiVariable psiVariable) {
        String typeString = typeToString(psiVariable.getType(), psiVariable.getProject());
        return new Variable(psiVariable.getName(), typeString, psiVariable);
    }

    public static Method createMethod(PsiMethod psiMethod) {
        String typeString = typeToString(psiMethod.getReturnType(), psiMethod.getProject());
        boolean performsConversion = false; //traverse psi tree to figure out if any casting happens inside the method
        boolean performsEventDrivenFunctionality = false;

        List<String> params = new ArrayList();
        for(PsiParameter param : psiMethod.getParameterList().getParameters()){
            params.add(typeToString(param.getType(), param.getProject()));
        }
        String name = psiMethod.getName() + "("+ String.join(",", params) + ")";
        return new Method(name, typeString, performsConversion, performsEventDrivenFunctionality, psiMethod);
    }
}
