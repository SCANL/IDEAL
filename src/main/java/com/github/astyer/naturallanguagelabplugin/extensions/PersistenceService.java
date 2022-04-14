package com.github.astyer.naturallanguagelabplugin.extensions;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Serves to store project data between sessions, persistently.
 */
@State(name = "persistenceService")
public class PersistenceService implements PersistentStateComponent<PersistenceService.State> {
    static Project project = null;

    public PersistenceService(Project curProject) {
        project = curProject;
    }

    public static PersistenceService getInstance() {
        return project.getService(PersistenceService.class);
    }

    /**
     * Generates a key for an identifier/recommended pattern combo.
     * Used to store and retrieve ignored identifiers.
     * @param name identifier name
     * @param type identifier type string
     * @param context identifier context, i.e. variable, method, or class
     * @param pattern the recommended pattern generic string
     * @return the key used to represent this identifier/recommended pattern combo
     */
    public static String getIgnoreKey(String name, String type, String context, String pattern){
        return name  + "|" + type + "|" + context + "|" + pattern;
    }

    // Store data in this State class and then create helper methods to interact with it
    static class State {
        public HashSet<String> ignoredIdentifiers = new HashSet<>();
    }

    private State myState = new State();

    public State getState() {
        return myState;
    }

    public void loadState(@NotNull State state) {
        myState = state;
    }

    /**
     * Stores key in the State HashSet.
     * @param key the identifier/recommended pattern combo key
     */
    public void ignoreIdentifier(String key) {
        myState.ignoredIdentifiers.add(key);
    }

    /**
     * Removes the key from the State HashSet.
     * @param key the identifier/recommended pattern combo key
     */
    public void unignoreIdentifier(String key) {
        myState.ignoredIdentifiers.remove(key);
    }

    /**
     * Checks if the State HashSet contains the key.
     * @param key the identifier/recommended pattern combo key
     * @return if the identifier is currently ignored
     */
    public boolean identifierIsIgnored(String key) {
        return myState.ignoredIdentifiers.contains(key);
    }
}
