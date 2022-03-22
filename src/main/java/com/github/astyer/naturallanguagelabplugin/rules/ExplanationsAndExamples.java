package com.github.astyer.naturallanguagelabplugin.rules;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

public class ExplanationsAndExamples {

    private static final HashMap<Rule, String> explanations = new HashMap<>(Map.ofEntries(
            // variable rules
            entry(Rule.NMN, "NM* N is a singular noun phrase, consisting of zero or more noun modifiers followed by a singular head noun. This is the most common naming pattern for variables. It is good practice to be careful in the choice, and number, of noun modifiers to use before the head noun. A good identifier will include only enough noun modifiers to concisely define the concept represented by the head noun."),
            entry(Rule.VNMN, "V NM* N(PL) is a verb phrase, consisting of a verb followed by a noun phrase. The verb in a verb phrase is an action being applied to (or with) the concept embodied by the noun phrase that follows. In some cases, instead of being an action, the verb is an existential quantifier. These are typically identifiers with a boolean type."),
            entry(Rule.NMNPL, "NM* NPL is a plural noun phrase, consisting of zero or more noun modifiers followed by a plural head noun. It is generally considered good practice to match the plurality of an identifier with its type. So, identifiers with a type that represents some kind of collection should have a plural head noun."),

            // method rules
            entry(Rule.VV1, "V NM* N(PL)|V+ pt1 Explanation TODO: update dynamically"),
            entry(Rule.VV2, "V NM* N(PL)|V+ pt2 Explanation TODO: update dynamically"),
            entry(Rule.PNMN, "P NM* N(PL) is a prepositional phrase, consisting of a preposition followed by a noun phrase. The preposition in a prepositional phrase typically explains how the entity (or entities) represented by the accompanying noun or verb-phrase are related in terms of order, space, time (e.g., on_enter), ownership, causality, or representation (e.g., to_string). In the case of this specific grammar pattern, there is oftentimes an un-specified verb on the left-hand-side of the preposition. The un-specified verb is usually an action such as the following: GET, CONVERT (e.g., to string), EXECUTE (e.g., on enter) or some other action. Developers understand the implied action because of experience or domain knowledge, for example, understanding event-driven functions beginning with the preposition 'on'."),
//            entry(Rule.VP, "V P NM* N Explanation"),
            entry(Rule.VDT, "V* DT NM* N(PL) explanation"),
            // class rules
            entry(Rule.NMNNPL, "NM* N(PL) is a noun phrase, consisting of zero or more noun modifiers followed by a head noun. This is the most common naming pattern for classes as they typically represent some entity. It is good practice to be careful in the choice, and number, of noun modifiers to use before the head noun. A good identifier will include only enough noun modifiers to concisely define the concept represented by the head noun.")
    ));

    private static final HashMap<Rule, String> examples = new HashMap<>(Map.ofEntries(
            // variable rules
            entry(Rule.NMN, "<code>int dynamicTableIndex;</code><br/>The identifier \"dynamicTableIndex\" has grammar pattern NM NM N, where the head noun is singular because <code>int</code> is not a collection type."),
            entry(Rule.VNMN, "<code>boolean isFirstFrame;</code><br/>The identifier \"isFirstFrame\" has grammar pattern V NM N, where a verb precedes the noun phrase because the variable is a <code>boolean</code> type."),
            entry(Rule.NMNPL, "<code>String[] methodNamePrefixes;</code><br/>The identifier \"methodNamePrefixes\" has grammar pattern NM NM NPL, where the head noun is plural because <code>String[]</code> is a collection type."),
            // method rules
            entry(Rule.VV1, "V NM* N(PL)|V+ pt1 Example TODO: update dynamically"),
            entry(Rule.VV2, "V NM* N(PL)|V+ pt2 Example TODO: update dynamically"),
            entry(Rule.PNMN, "<code> String toString() {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/> The identifier \"toString\" has grammar pattern P N, where the preposition 'to' implies some kind of conversion."),
//            entry(Rule.VP, "V P NM* N Example"),
            entry(Rule.VDT, "V* DT NM* N(PL) example"),
            // class rules
            entry(Rule.NMNNPL, "<code>public class dynamicTable {<br/><span>&nbsp;&nbsp;</span>. . .<br/>}</code><br/>The identifier \"dynamicTable\" has grammar pattern NM N, following the noun phrase pattern because it is a standard class.")
    ));

    public static String getExplanation(Rule rule) {
        return explanations.get(rule);
    }

    public static String getExample(Rule rule) {
        return examples.get(rule);
    }
}
