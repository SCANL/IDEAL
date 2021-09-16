package com.github.astyer.naturallanguagelabplugin.common;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;

public class Util {
    
    public static PsiJavaFile getPsiJavaFile(Project project){
        if (project == null) {
            //("Please select/open a project", project);
            return null;
        }
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            //("Please open the IDE editor and select a Java file", project);
            return null;
        }
        Document currentDoc = editor.getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        if (currentFile == null) {
            //("Please select a Java file", project);
            return null;
        }
        PsiFile currentPsiFile = PsiManager.getInstance(project).findFile(currentFile);
        if (!(currentPsiFile instanceof PsiJavaFile)) {
            //("Please select a Java file", project);
            return null;
        }

       return  (PsiJavaFile) currentPsiFile;
    }
}
