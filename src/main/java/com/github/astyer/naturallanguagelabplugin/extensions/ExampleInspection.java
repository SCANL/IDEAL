package com.github.astyer.naturallanguagelabplugin.extensions;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

public class ExampleInspection extends AbstractBaseJavaLocalInspectionTool {

    private final ExampleQuickFix myQuickFix = new ExampleQuickFix();

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitVariable(PsiVariable variable) {
                super.visitVariable(variable);
                holder.registerProblem(variable, "Variable name '" + variable.getName() + "' may use the wrong grammar pattern", myQuickFix);
            }

            @Override
            public void visitClass(PsiClass aClass) {
                super.visitClass(aClass);
                holder.registerProblem(aClass, "Class name '" + aClass.getName() + "' may use the wrong grammar pattern", myQuickFix);
            }

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                holder.registerProblem(method, "Method name '" + method.getName() + "' may use the wrong grammar pattern", myQuickFix);
            }
        };
    }

    private static class ExampleQuickFix implements LocalQuickFix {
        @Override
        public @IntentionName @NotNull String getName() {
            return "Switch identifier to use suggested grammar pattern";
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
//            PsiElement psiElement = descriptor.getPsiElement();
//            String elementName = PsiTreeUtil.getChildOfType(psiElement, PsiVariable.class).getName();
            System.out.println("Fix on line " + (descriptor.getLineNumber()+1) + " applied!");
        }
    }
}
