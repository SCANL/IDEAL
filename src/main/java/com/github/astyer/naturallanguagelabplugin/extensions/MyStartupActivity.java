package com.github.astyer.naturallanguagelabplugin.extensions;

import com.github.astyer.naturallanguagelabplugin.listeners.MyCaretListener;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class MyStartupActivity implements StartupActivity, DumbAware {
    @Override
    public void runActivity(@NotNull Project project) {
        final EditorEventMulticaster eventMulticaster = EditorFactory.getInstance().getEventMulticaster();
        eventMulticaster.addCaretListener(new MyCaretListener(project), project);
    }
}
