package com.github.astyer.naturallanguagelabplugin.services;

public class MyApplicationService {

    private int projectsOpened;

    public void incrementProjectsOpened(){
        projectsOpened++;
    }

    public int getProjectsOpened(){
        return projectsOpened;
    }
}
