package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.*;

public class IRFactory {

    public enum IRType{
        TYPE_VOID,
        TYPE_BOOLEAN,
        TYPE_COLLECTION,
        TYPE_OTHER
    }

    public String IRTypeToString(IRType type, Identifier id){
        switch (type){
            case TYPE_BOOLEAN:
                return "Boolean";
            case TYPE_COLLECTION:
                return "Array";
            case TYPE_VOID:
                return "Void";
            case TYPE_OTHER:
                return id.getCanonicalType();
        }
        return "UNKNOWN_TYPE";
    }

    // maybe move these helper functions into a helper class
    /**
     * Checks if the PsiType is some kind of collection
     * Currently checks for standard arrays and anything that is a Collection or Map
     * @param type
     * @param project
     * @return if the PsiType is some kind of collection
     */
    private static boolean isSomeCollection(PsiType type, Project project) {
        if(type instanceof PsiArrayType) {
            return true;
        }

        PsiElementFactory psiElementFactory = PsiElementFactory.getInstance(project);
        PsiType collectionPsiType = psiElementFactory.createTypeByFQClassName(Collection.class.getName());
        PsiType mapPsiType = psiElementFactory.createTypeByFQClassName(Map.class.getName());

        //check if the type is assignable to Collection or Map
        if(collectionPsiType.isAssignableFrom(type) || mapPsiType.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    private static IRType typeToType(PsiType type, Project project){
        if(type == null) {
            return IRType.TYPE_VOID;
        }
        if(isSomeCollection(type, project)) {
            return IRType.TYPE_COLLECTION;
        }
        String cType = type.getCanonicalText();
        if(cType.equals("boolean") || cType.equals("java.lang.Boolean")){
            return IRType.TYPE_BOOLEAN;
        }
        return IRType.TYPE_OTHER;
    }

    private static String getCanonicalType(PsiType type){
        if(type == null){
            return "null";
        }else{
            return type.getCanonicalText();
        }
    }

    /**
     * Check if psiMethod performs any kind of type conversions
     * Currently only checks for any explicit casting
     * @param psiMethod
     * @return if psiMethod performs any kind of type conversions
     */
    private static boolean performsConversion(PsiMethod psiMethod) {
        PsiTypeCastExpression castExpression = PsiTreeUtil.findChildOfType(psiMethod, PsiTypeCastExpression.class);
        if(castExpression != null) {
            System.out.println("cast type: " + castExpression.getType());
            System.out.println("operand expression: " + castExpression.getOperand().getText());
            return true;
        }
        return false;
    }

    /**
     * Check if psiMethod overrides any methods from a class that is an event listener
     * @param psiMethod
     * @return if psiMethod overrides an event listener method
     */
    private static boolean performsEventDrivenFunctionality(PsiMethod psiMethod) {
        PsiMethod[] superMethods = psiMethod.findSuperMethods();
        if(superMethods.length > 0) {
            PsiElementFactory psiElementFactory = PsiElementFactory.getInstance(psiMethod.getProject());
            PsiType eventListenerPsiType = psiElementFactory.createTypeByFQClassName(EventListener.class.getName());
            for(PsiMethod superMethod: superMethods) {
                PsiClass parentClass = PsiTreeUtil.getParentOfType(superMethod, PsiClass.class);
                if(parentClass != null) {
                    PsiType parentClassType = PsiTypesUtil.getClassType(parentClass);
                    if (eventListenerPsiType.isAssignableFrom(parentClassType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Variable createVariable(PsiVariable psiVariable) {
        IRType type = typeToType(psiVariable.getType(), psiVariable.getProject());
        String canonicalType = getCanonicalType(psiVariable.getType());
        return new Variable(psiVariable.getName(),canonicalType, psiVariable, type);
    }

    public static Method createMethod(PsiMethod psiMethod) {
        IRType type = typeToType(psiMethod.getReturnType(), psiMethod.getProject());
        String typeString = getCanonicalType(psiMethod.getReturnType());

        List<String> params = new ArrayList(); //add params to the name because the POS tagger expects it
        for(PsiParameter param : psiMethod.getParameterList().getParameters()){
            params.add(getCanonicalType(param.getType()));
        }
        String name = psiMethod.getName() + "("+ String.join(",", params) + ")";
        return new Method(name, typeString, performsConversion(psiMethod), performsEventDrivenFunctionality(psiMethod), psiMethod, type);
    }
}
