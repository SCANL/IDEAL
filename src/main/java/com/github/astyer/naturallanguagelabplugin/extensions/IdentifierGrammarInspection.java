package com.github.astyer.naturallanguagelabplugin.extensions;

import com.github.astyer.naturallanguagelabplugin.IR.IRFactory;
import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.github.astyer.naturallanguagelabplugin.common.IdentifierSuggestionResults;
import com.github.astyer.naturallanguagelabplugin.rules.AggregateRules;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;

public class IdentifierGrammarInspection extends AbstractBaseJavaLocalInspectionTool {

    private final ViewExplanationQuickFix myQuickFix = new ViewExplanationQuickFix();
    private static AggregateRules aggregateRules = new AggregateRules();

    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session, boolean isOnTheFly) {
        IdentifierSuggestionResults.clear();
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitVariable(PsiVariable variable) {
                Variable IRVariable = IRFactory.createVariable(variable);
                Optional<Result> result = aggregateRules.runAll(IRVariable).stream().max(Comparator.comparingInt(a -> a.priority));
                if (result.isPresent()) {
                    PsiIdentifier variableIdentifier = variable.getNameIdentifier();
                    IdentifierSuggestionResults.put(variableIdentifier, result.get());
                    String description = "Variable name '" + variable.getName() + "' should use grammar pattern " + result.get().recommendation;
                    holder.registerProblem(variableIdentifier, description, myQuickFix);
                }
            }

//            @Override
//            public void visitClass(PsiClass aClass) {
//                PsiIdentifier className = aClass.getNameIdentifier();
//                holder.registerProblem(className, "Class name '" + className.getText() + "' may use the wrong grammar pattern", myQuickFix);
//            }

            @Override
            public void visitMethod(PsiMethod method) {
                Method IRMethod = IRFactory.createMethod(method);
                Optional<Result> result = aggregateRules.runAll(IRMethod).stream().max(Comparator.comparingInt(a -> a.priority));
                if (result.isPresent()) {
                    PsiIdentifier methodIdentifier = method.getNameIdentifier();
                    IdentifierSuggestionResults.put(methodIdentifier, result.get());
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
            // When the fix is applied, the cursor moves to the identifier.
            // The cursor being on the identifier triggers the listener to fill out the sidebar, so we're all done.
        }
    }
}
