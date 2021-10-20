package com.github.astyer.naturallanguagelabplugin.extensions;

import com.github.astyer.naturallanguagelabplugin.IR.IRFactory;
import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.github.astyer.naturallanguagelabplugin.rules.AggregateRules;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.ui.IdentifierGrammarToolWindow;
import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;

public class IdentifierGrammarInspection extends AbstractBaseJavaLocalInspectionTool {

    private final ViewExplanationQuickFix myQuickFix = new ViewExplanationQuickFix();
    private AggregateRules aggregateRules = new AggregateRules();

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitVariable(PsiVariable variable) {
                super.visitVariable(variable);
                Variable IRVariable = IRFactory.createVariable(variable);
                Optional<Result> result = aggregateRules.runAll(IRVariable).stream().max(Comparator.comparingInt(a -> a.priority));
                if (result.isPresent()) {
                    PsiIdentifier variableIdentifier = variable.getNameIdentifier();
                    String description = "Variable name '" + variable.getName() + "' should use grammar pattern " + result.get().recommendation;
                    holder.registerProblem(variableIdentifier, description, myQuickFix);
                }
            }

//            @Override
//            public void visitClass(PsiClass aClass) {
//                super.visitClass(aClass);
//                PsiIdentifier className = aClass.getNameIdentifier();
//                holder.registerProblem(className, "Class name '" + className.getText() + "' may use the wrong grammar pattern", myQuickFix);
//            }

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                Method IRMethod = IRFactory.createMethod(method);
                Optional<Result> result = aggregateRules.runAll(IRMethod).stream().max(Comparator.comparingInt(a -> a.priority));
                if (result.isPresent()) {
                    PsiIdentifier methodIdentifier = method.getNameIdentifier();
                    String description = "Method name '" + method.getName() + "' should use grammar pattern " + result.get().recommendation;
                    holder.registerProblem(methodIdentifier, description, myQuickFix);
                }
            }
        };
    }

    private static class ViewExplanationQuickFix implements LocalQuickFix {
        @Override
        public @IntentionName @NotNull String getName() {
            return "View suggested grammar pattern explanation";
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
//            PsiElement psiElement = descriptor.getPsiElement();
            System.out.println("Viewing grammar pattern explanation for identifier on line " + (descriptor.getLineNumber()+1));
            //IdentifierGrammarToolWindow.setSomething()?
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Identifier Grammar Pattern Suggestions"); //maybe store this toolwindow so we don't keep refetching it?
            toolWindow.show();
        }
    }
}
