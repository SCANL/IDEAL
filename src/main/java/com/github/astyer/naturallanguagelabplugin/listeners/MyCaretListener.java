package com.github.astyer.naturallanguagelabplugin.listeners;

import com.github.astyer.naturallanguagelabplugin.common.IdentifierSuggestionResults;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.ui.IdentifierGrammarToolWindow;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class MyCaretListener implements CaretListener {
    private final PsiDocumentManager psiDocumentManager;
    private final IdentifierGrammarToolWindow toolWindowContent;

    /**
     * Gathers the project's PsiDocumentManager and the tool window content.
     * @param project the current project
     */
    public MyCaretListener(Project project) {
        psiDocumentManager = PsiDocumentManager.getInstance(project);
        toolWindowContent = IdentifierGrammarToolWindow.getInstance();
    }

    /**
     * Called every time the caret location changes.
     * Detects if the caret is currently over an identifier and updates the tool window if so.
     * @param event the event that moved the caret
     */
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
                        toolWindowContent.setCurrentIdentifier(result, psiFile);
                    }
                }
            }
        }
    }
}
