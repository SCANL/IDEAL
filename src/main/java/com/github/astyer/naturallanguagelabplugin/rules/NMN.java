package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMN extends Rule {
    @Override
    List<Checkbox> getCheckboxes() {
        List<Checkbox> results = new ArrayList<>();
        results.add(new Checkbox("return type not array or boolean",
                null,
                null,
                variable -> new CheckboxResult(!variable.getType().equals("Array") && !variable.getType().equals("boolean"))));
        return results;
    }

    @Override
    String getRecommendation() {
        return "NM* N";
    }

    @Override
    int getPriority() {
        return 1;
    }

    @Override
    Pattern getPattern() {
        return Pattern.compile("((N|MN) )*N");
    }
}
