package com.github.astyer.naturallanguagelabplugin.rules;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class TagNames {

    private static final HashMap<String, String> tagsToNames = new HashMap<>(Map.ofEntries(
            entry("N", "a noun"),
            entry("DT", "a determiner"),
            entry("CJ", "a conjunction"),
            entry("P", "a preposition"),
            entry("NPL", "a plural noun"),
            entry("NM", "a noun modifier"),
            entry("V", "a verb"),
            entry("VM", "a verb modifier"),
            entry("PR", "a pronoun"),
            entry("D", "a digit"),
            entry("PRE", "a preamble")
    ));

    public static String getName(String tag) {
        return tagsToNames.get(tag);
    }
}
