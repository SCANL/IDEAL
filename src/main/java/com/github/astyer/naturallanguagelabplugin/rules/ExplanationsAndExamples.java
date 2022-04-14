package com.github.astyer.naturallanguagelabplugin.rules;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

/**
 * A class to hold all the explanations and examples.
 * This should at some point be extracted to a configuration file
 */
public class ExplanationsAndExamples {

    private static final HashMap<Rule, String> explanations = new HashMap<>(Map.ofEntries(
            // variable rules
            entry(Rule.NMN, "We recommend you use a noun phrase for this identifier name. Noun phrases in the software domain are sequences of noun-adjuncts followed by a head-noun. The head-noun is the main entity represented by the identifier name while the noun-adjuncts describe characteristics or properties of the head-noun."),
            entry(Rule.VNMN, "We recommend you use a verb phrase for this identifier name. The first word in the boolean identifier should be an existential verb in most cases. The words to the right of the verb should be a noun phrase, a sequence of noun-adjuncts followed by a head noun. The head-noun is the main entity represented by the identifier name while the noun-adjuncts describe characteristics or properties of the head-noun."),
            entry(Rule.NMNPL, "We recommend you use a plural noun phrase for this identifier name. Plural noun phrases in the software domain are sequences of noun-adjuncts followed by a plural head-noun. The head-noun is the main entity represented by the identifier name and the plurality of this head-noun represents the fact that this entity is a type of collection. The noun-adjuncts describe characteristics or properties of the head-noun."),
            // method rules
            entry(Rule.VV1, "We recommend you use a verb phrase for this identifier name. The first word in the function should be a verb in most cases. The verb is the main action of this function. The words to the right of the verb should be a noun phrase, a sequence of noun-adjuncts followed by a head noun. The head-noun is the main entity represented by the identifier name while the noun-adjuncts describe characteristics or properties of the head-noun."),
            entry(Rule.VV2, "We recommend you use a verb pattern for this identifier name. Generic or void functions may use sequences of verbs without an attached noun phrase to describe one or more generic actions. For example, method names like sort or remove. In this case, these actions are applied to the function parameters or calling object. These parameters take the place of the missing noun phrase."),
            entry(Rule.PNMN, "We recommend you use a prepositional phrase for this identifier name. The preposition helps describe actions like type conversion, event handling, or positioning within space or over time.  The words to the right of the preposition should be a noun phrase, a sequence of noun-adjuncts followed by a head noun. The head-noun is the main entity represented by the identifier name while the noun-adjuncts describe characteristics or properties of the head-noun. You may also consider using a noun phrase or a verb to the left of the preposition."),
            entry(Rule.VDT, "We recommend you use a determiner in this identifier name. The first word in this identifier should be a verb or a determiner. The determiner helps specify the population of interest to this identifier. The words to the right of the determiner should be a noun phrase, a sequence of noun-adjuncts followed by a head noun. The head-noun is the main entity represented by the identifier name while the noun-adjuncts describe characteristics or properties of the head-noun.")
            // class rules
    ));

    private static final HashMap<Rule, String> examples = new HashMap<>(Map.ofEntries(
            // variable rules
            entry(Rule.NMN, "<code>int dynamicTableIndex;</code><br/>The identifier \"dynamicTableIndex\" has grammar pattern NM NM N, where the head noun is singular because <code>int</code> is not a collection type."),
            entry(Rule.VNMN, "<code>boolean isFirstFrame;</code><br/>The identifier \"isFirstFrame\" has grammar pattern V NM N, where a verb precedes the noun phrase because the variable is a <code>boolean</code> type."),
            entry(Rule.NMNPL, "<code>String[] methodNamePrefixes;</code><br/>The identifier \"methodNamePrefixes\" has grammar pattern NM NM NPL, where the head noun is plural because <code>String[]</code> is a collection type."),
            entry(Rule.DT, "<code>boolean containsAnyFile;</code><br/>The identifier \"containsAnyFile\" has grammar pattern V DT N, where the determiner \"Any\" specifies that any file in the population satisfies the constraint."),
            entry(Rule.DTPL, "<code>int[] allOpenIndices;</code><br/>The identifier \"allOpenIndices\" has grammar pattern DT NM NPL, where the determiner \"all\" specifies the population of open indices represented by the array and the head noun is plural because <code>int[]</code> is a collection type."),
            // method rules
            entry(Rule.VV1, "<code> boolean createMetadataArray() {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/> The identifier \"createMetadataArray\" has grammar pattern V NM N, where the verb \"create\" is applied to the noun phrase."),
            entry(Rule.VV2, "<code> void sort() {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/> The identifier \"sort\" has grammar pattern V, where the verb \"sort\" is being applied to an implied noun phrase."),
            entry(Rule.PNMN, "<code> String toString() {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/> The identifier \"toString\" has grammar pattern P N, where the preposition 'to' implies some kind of conversion."),
            entry(Rule.VDT, "<code> boolean matchesAnyParentCategories() {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/> The identifier \"matchesAnyParentCategories\" has grammar pattern V DT NM NPL, where the determiner 'any' specifies what portion of parent categories should be matched."),
            // class rules
            entry(Rule.NMNNPL, "<code>public class DynamicTable {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/>The identifier \"DynamicTable\" has grammar pattern NM N, following the noun phrase pattern because it is a standard class.")
    ));

    public static String getExplanation(Rule rule) {
        return explanations.get(rule);
    }

    public static String getExample(Rule rule) {
        return examples.get(rule);
    }
}
