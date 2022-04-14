package com.github.astyer.naturallanguagelabplugin.common;

import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.intellij.psi.PsiIdentifier;

import java.util.HashMap;

/**
 * Stores all the PsiIdentifiers and Results found in IdentifierGrammarInspection in a static HashMap.
 */
public class IdentifierSuggestionResults {
    private final static HashMap<PsiIdentifier, Result> identifierResults = new HashMap<>();

    public static void put(PsiIdentifier identifier, Result result) {
        identifierResults.put(identifier, result);
    }

    public static Result get(PsiIdentifier identifier) {
        return identifierResults.get(identifier);
    }

    public static void clear() {
        identifierResults.clear();
    }
}
