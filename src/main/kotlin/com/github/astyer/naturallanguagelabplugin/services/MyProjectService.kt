package com.github.astyer.naturallanguagelabplugin.services

import com.intellij.openapi.project.Project
import com.github.astyer.naturallanguagelabplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
