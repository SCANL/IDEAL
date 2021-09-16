package com.github.astyer.naturallanguagelabplugin.actions;

import com.github.astyer.naturallanguagelabplugin.SampleVisitor;
import com.github.astyer.naturallanguagelabplugin.common.Util;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VariableFinder extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiJavaFile psiJavaFile = Util.getPsiJavaFile(e.getProject());
        if(psiJavaFile != null) {
            SampleVisitor sampleVisitor = new SampleVisitor();
            psiJavaFile.accept(sampleVisitor);
            ArrayList<String> variableNames = sampleVisitor.getVariableNames();
            Messages.showInfoMessage("Found " + variableNames.size() + " variables: " + variableNames, "Variable Finder Results");
        }
    }
}
