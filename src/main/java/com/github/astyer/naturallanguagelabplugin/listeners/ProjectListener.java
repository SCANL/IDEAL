package com.github.astyer.naturallanguagelabplugin.listeners;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
//import org.scanl.plugins.poc.ui.IdentifierListingToolWindow;

//
// THIS CLASS WILL CLEAR THE PLUGIN UI WHEN THE PROJECT IS CLOSED OR A NEW PROJECT IS OPENED IN THE IDE
//
public class ProjectListener implements ProjectManagerListener {

//    @Override
//    public void projectOpened(@NotNull Project project) {
//        // clear the text in the tool window
//        IdentifierListingToolWindow.refreshList(null);
//
//        VirtualFile @NotNull [] ss = FileEditorManager.getInstance(project).getSelectedFiles();
//        VirtualFile @NotNull [] vv = FileEditorManager.getInstance(project).getOpenFiles();
//
//        ProjectManagerListener.super.projectOpened(project);
//    }
//
//    @Override
//    public void projectClosed(@NotNull Project project) {
//        // clear the text in the tool window
//        IdentifierListingToolWindow.refreshList(null);
//
//        ProjectManagerListener.super.projectClosed(project);
//    }
}