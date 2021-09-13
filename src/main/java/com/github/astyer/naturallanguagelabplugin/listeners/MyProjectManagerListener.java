package com.github.astyer.naturallanguagelabplugin.listeners;

import com.github.astyer.naturallanguagelabplugin.services.MyApplicationService;
import com.github.astyer.naturallanguagelabplugin.services.MyProjectService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class MyProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        ProjectManagerListener.super.projectOpened(project);
        MyProjectService myProjectService = project.getService(MyProjectService.class); //how to fetch a project service
        myProjectService.performServiceFunction();

        MyApplicationService myApplicationService = ApplicationManager.getApplication().getService(MyApplicationService.class); //how to fetch an application service
        myApplicationService.incrementProjectsOpened();
        System.out.println("Projects opened: " + myApplicationService.getProjectsOpened());
    }
}
