package com.github.astyer.naturallanguagelabplugin.listeners;

import com.github.astyer.naturallanguagelabplugin.common.IdentifierSuggestionResults;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.ui.IdentifierGrammarToolWindow;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class MyCaretListener implements CaretListener {
    private Project project;
    private PsiDocumentManager psiDocumentManager;
    private ToolWindow toolWindow;
    private IdentifierGrammarToolWindow toolWindowContent;

    public MyCaretListener(Project project) {
        this.project = project;
        psiDocumentManager = PsiDocumentManager.getInstance(project);
        toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Identifier Grammar Pattern Suggestions");
        toolWindowContent = IdentifierGrammarToolWindow.getInstance();
    }

    @Override
    public void caretPositionChanged(@NotNull CaretEvent event) {
        final PsiFile psiFile = psiDocumentManager.getPsiFile(event.getEditor().getDocument());
        if (psiFile instanceof PsiJavaFile) {
            Caret caret = event.getCaret();
            if (caret != null) {
                PsiElement psiElement = psiFile.findElementAt(caret.getOffset());
                if (psiElement instanceof PsiIdentifier) {
                    PsiIdentifier identifier = (PsiIdentifier) psiElement;
                    Result result = IdentifierSuggestionResults.get(identifier);
                    if (result != null) {
                        populateToolWindow(identifier, result);
                        toolWindow.show();
                    }
                }
            }
        }
    }

    private void populateToolWindow(PsiIdentifier identifier, Result result) {
        toolWindowContent.setIdentifierName(identifier.getText());
        toolWindowContent.setSuggestedPattern(result.recommendation);
    }
}
