package com.github.astyer.naturallanguagelabplugin.IR;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.*;

public class IRFactory {

    public enum IRType {
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

    private static IRType typeToIRType(PsiType type, Project project){
        if(type == null) {
            return IRType.TYPE_VOID;
        }
        if(isSomeCollection(type, project)) {
            return IRType.TYPE_COLLECTION;
        }
        String cType = type.getCanonicalText();
        if(cType.equals("boolean") || cType.equals("java.lang.Boolean")) {
            return IRType.TYPE_BOOLEAN;
        }
        if(cType.equals("void")) {
            return IRType.TYPE_VOID;
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

    /**
     * Check if psiMethod is a generic method, i.e. one of its parameters resolve to a generic type
     * @param psiMethod
     * @return if psiMethod uses generics
     */
    private static boolean usesGenerics(PsiMethod psiMethod) {
        for(PsiParameter param: psiMethod.getParameterList().getParameters()) {
            PsiType type = param.getType().getDeepComponentType(); //handles the case of generic arrays
            if(type instanceof PsiClassType) {
                PsiClassType classType = (PsiClassType) type;
                PsiClassType.ClassResolveResult result = classType.resolveGenerics();
                if(result.getElement() instanceof PsiTypeParameter) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if psiVariable is the result of some method that performs looping.
     * Specifically, if the method name contains the word "contains" or the word "find".
     * @param psiVariable
     * @return if psiVariable is a loop result
     */
    private static boolean isLoopResult(PsiVariable psiVariable) {
        PsiMethodCallExpression methodCallExp = PsiTreeUtil.findChildOfType(psiVariable, PsiMethodCallExpression.class);
        if(methodCallExp != null) {
            String methodName = methodCallExp.getMethodExpression().getReferenceName().toLowerCase();
            if(methodName.contains("contains") || methodName.contains("find")) {
                System.out.println("variable \"" + psiVariable.getName() + "\" is a loop result from method call \"" + methodName + "\"");
                return true;
            }
        }
        return false;
    }

    /**
     * Check if psiMethod performs any looping by meeting either of two conditions.
     * 1. If it calls a method with a name that contains the word "contains" or the word "find".
     * 2. If the method uses some loop, including for, for each, while, do while, etc.
     * @param psiMethod
     * @return if psiMethod performs looping
     */
    private static boolean performsLooping(PsiMethod psiMethod) {
        PsiMethodCallExpression methodCallExp = PsiTreeUtil.findChildOfType(psiMethod, PsiMethodCallExpression.class);
        if(methodCallExp != null) {
            String methodName = methodCallExp.getMethodExpression().getReferenceName().toLowerCase();
            if(methodName.contains("contains") || methodName.contains("find")) {
                return true;
            }
        }
        PsiStatement loopStatement = PsiTreeUtil.findChildOfType(psiMethod, PsiLoopStatement.class);
        return loopStatement != null;
    }

    public static Variable createVariable(PsiVariable psiVariable) {
        IRType type = typeToIRType(psiVariable.getType(), psiVariable.getProject());
        String canonicalType = getCanonicalType(psiVariable.getType());
        String displayName = psiVariable.getName();
        return new Variable(displayName, displayName, canonicalType, psiVariable, type, isLoopResult(psiVariable));
    }

    public static Method createMethod(PsiMethod psiMethod) {
        IRType type = typeToIRType(psiMethod.getReturnType(), psiMethod.getProject());
        String typeString = getCanonicalType(psiMethod.getReturnType());

        String displayName = psiMethod.getName();
        List<String> params = new ArrayList(); //add params to the name because the POS tagger expects it
        for(PsiParameter param : psiMethod.getParameterList().getParameters()){
            params.add(getCanonicalType(param.getType()));
        }
        String name = displayName + "("+ String.join(",", params) + ")";
        return new Method(name, displayName, typeString, psiMethod, type, performsConversion(psiMethod), performsEventDrivenFunctionality(psiMethod), usesGenerics(psiMethod), performsLooping(psiMethod));
    }

    public static Class createClass(PsiClass psiClass) {
        Class.ClassType type = Class.ClassType.Class; //always Class type for now as we have no rules that need further specification
        String displayName = psiClass.getName();
        return new Class(displayName, displayName, psiClass, type);
    }
}
