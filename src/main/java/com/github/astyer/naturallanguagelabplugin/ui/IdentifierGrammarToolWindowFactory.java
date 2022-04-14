package com.github.astyer.naturallanguagelabplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class IdentifierGrammarToolWindowFactory implements ToolWindowFactory {
    /**
     * Uses the instance of IdentifierGrammarToolWindow as tool window content and initializes it by passing in the
     * current project.
     * @param project the current project
     * @param toolWindow the tool window to fill with content
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        IdentifierGrammarToolWindow toolWindowContent = IdentifierGrammarToolWindow.getInstance();
        toolWindowContent.passProject(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolWindowContent.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
