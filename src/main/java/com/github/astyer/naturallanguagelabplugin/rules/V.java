package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class V extends Rule {
    @Override
    List<Checkbox> getCheckboxes() {
        List<Checkbox> result = new ArrayList<>();
//        result.add(Checkbox.PosCheckbox(Pattern.compile("(V )+")));
        result.add(new Checkbox("Return type of void",null, method -> new CheckboxResult(method.getType().equals("void")), null));
        return result;
    }

    @Override
    String getRecommendation() {
        return "V+";
    }

    @Override
    int getPriority() {
        return 2;
    }

    @Override
    Pattern getPattern() {
        return Pattern.compile("V( V)*");
    }
}
