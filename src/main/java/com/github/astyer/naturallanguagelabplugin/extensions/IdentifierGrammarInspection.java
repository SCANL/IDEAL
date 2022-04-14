package com.github.astyer.naturallanguagelabplugin.extensions;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.IRFactory;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.github.astyer.naturallanguagelabplugin.common.IdentifierSuggestionResults;
import com.github.astyer.naturallanguagelabplugin.rules.AggregateRules;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * This inspection runs whenever a file is edited.
 *
 * It will run each identifier in the file through the rules backend and get recommendations. If the identifier does
 * not match the recommended grammar pattern, and it is not being currently ignored, then it will be registered as a
 * problem and highlighted.
 *
 * Every identifier and its result will be stored in IdentifierSuggestionResults so that other classes have access to
 * that information, such as MyCaretListener.
 */
public class IdentifierGrammarInspection extends AbstractBaseJavaLocalInspectionTool {

    private final ViewExplanationQuickFix myQuickFix = new ViewExplanationQuickFix();
    private static final AggregateRules aggregateRules = new AggregateRules();

    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session, boolean isOnTheFly) {
        IdentifierSuggestionResults.clear();
    }

    /**
     * The visitor built in this method will be used whenever an inspection is run.
     * @param holder every identifier held here will be highlighted by the inspection
     * @param isOnTheFly true for on the fly editor highlighting
     * @return the PsiElementVisitor we want to use
     */
    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            /**
             * Helper method to check if the identifier in question is current being ignored
             * @param project the current project
             * @param id the identifier in question
             * @param recommendationName the recommended grammar pattern name
             * @return if the identifier is being ignored
             */
            private boolean isCurrentlyIgnored(Project project, Identifier id, String recommendationName) {
                PersistenceService persistenceService = project.getService(PersistenceService.class);
                String ignoreKey = PersistenceService.getIgnoreKey(id.getDisplayName(), id.getCanonicalType(), id.getContext(), recommendationName);
                return persistenceService.identifierIsIgnored(ignoreKey);
            }

            /**
             * Visits each variable in the current file and performs the following steps:
             * 1. An IRVariable (Internal Representation Variable) will be created from the PsiVariable.
             * 2. That IRVariable is then run through the rules backend to get a Result containing recommendations.
             * 3. The PsiIdentifier and rules backend Result are then stored in IdentifierSuggestionResults.
             * 4. If the recommendation does not match the current identifier's pattern, and it is not being
             *    ignored, the identifier is registered as a problem for the inspection.
             *
             * These same steps are performed for methods and classes as well in the following visitor methods.
             */
            @Override
            public void visitVariable(PsiVariable variable) {
                Variable IRVariable = IRFactory.createVariable(variable);
                Result result = aggregateRules.runAll(IRVariable);
                PsiIdentifier variableIdentifier = variable.getNameIdentifier();
                IdentifierSuggestionResults.put(variableIdentifier, result);
                Result.Recommendation topRecommendation = result.getTopRecommendation();
                if (!topRecommendation.getRegexMatches() && !isCurrentlyIgnored(variable.getProject(), result.getId(), topRecommendation.getName())) {
                    String description = "Variable name '" + variable.getName() + "' should use grammar pattern " + topRecommendation.getName();
                    holder.registerProblem(variableIdentifier, description, myQuickFix);
                }
//                System.out.println(variable.getName() + " finished parsing at: " + System.currentTimeMillis());
            }

            @Override
            public void visitMethod(PsiMethod method) {
                Method IRMethod = IRFactory.createMethod(method);
                Result result = aggregateRules.runAll(IRMethod);
                PsiIdentifier methodIdentifier = method.getNameIdentifier();
                IdentifierSuggestionResults.put(methodIdentifier, result);
                Result.Recommendation topRecommendation = result.getTopRecommendation();
                if (!topRecommendation.getRegexMatches() && !isCurrentlyIgnored(method.getProject(), result.getId(), topRecommendation.getName())) {
                    String description = "Method name '" + method.getName() + "' should use grammar pattern " + topRecommendation.getName();
                    holder.registerProblem(methodIdentifier, description, myQuickFix);
                }
//                System.out.println(method.getName() + " finished parsing at: " + System.currentTimeMillis());
            }

            @Override
            public void visitClass(PsiClass aClass) {
                Class IRClass = IRFactory.createClass(aClass);
                Result result = aggregateRules.runAll(IRClass);
                PsiIdentifier classIdentifier = aClass.getNameIdentifier();
                IdentifierSuggestionResults.put(classIdentifier, result);
                Result.Recommendation topRecommendation = result.getTopRecommendation();
                if (!topRecommendation.getRegexMatches() && !isCurrentlyIgnored(aClass.getProject(), result.getId(), topRecommendation.getName())) {
                    String description = "Class name '" + aClass.getName() + "' should use grammar pattern " + topRecommendation.getName();
                    holder.registerProblem(classIdentifier, description, myQuickFix);
                }
//                System.out.println(aClass.getName() + " finished parsing at: " + System.currentTimeMillis());
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

        /**
         * When the fix is applied, the cursor moves to the identifier.
         * The cursor being on the identifier triggers the listener to fill out the sidebar.
         * So, all that's left to do is open the tool window.
         */
        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Identifier Grammar Pattern Suggestions");
            if (toolWindow != null) {
                toolWindow.show();
            }
        }
    }
}
