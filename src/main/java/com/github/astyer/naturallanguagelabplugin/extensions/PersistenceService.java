package com.github.astyer.naturallanguagelabplugin.extensions;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;

import java.util.HashSet;

@State(name = "persistenceService")
public class PersistenceService implements PersistentStateComponent<PersistenceService.State> {
    static Project project = null;

    public PersistenceService(Project curProject) {
        project = curProject;
    }

    public static PersistenceService getInstance() {
        return project.getService(PersistenceService.class);
    }

    public static String getIgnoreKey(String name, String type, String context, String pattern){
        return name  + "|" + type + "|" + context + "|" + pattern;
    }

    // Store data in this State class and then create helper methods to interact with it
    static class State {
        public HashSet<String> ignoredValues = new HashSet<>();
    }

    private State myState = new State();

    public State getState() {
        return myState;
    }

    public void loadState(State state) {
        myState = state;
    }

    public void ignoreIdentifier(String key) {
        myState.ignoredValues.add(key);
    }

    public void unignoreIdentifier(String key) {
        myState.ignoredValues.remove(key);
    }

    public boolean identifierIsIgnored(String key) {
        return myState.ignoredValues.contains(key);
    }
}
