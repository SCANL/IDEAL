package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import org.tukaani.xz.check.Check;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Rule {
    abstract List<Checkbox> getCheckboxes();
    abstract String getRecommendation();
    abstract int getPriority();
    abstract Pattern getPattern();

    public Optional<Result> runRule(Identifier identifier){
        List<CheckboxResult> results = new ArrayList<>();
        System.out.println("Running rule (" + getRecommendation() +") on " + identifier.getName());
        for(Checkbox cb : getCheckboxes()){
            CheckboxResult r = cb.applyCheckbox(identifier);
            results.add(r);
            System.out.println(cb.name +": " + r.getResult());
        }
        boolean ruleApplies = results.stream().anyMatch(checkboxResult -> checkboxResult.getMatched() && checkboxResult.getResult());
        System.out.println("rule Applies: " + ruleApplies);
        boolean regexMatches = getPattern().matcher(identifier.getPOS()).matches();
        System.out.println("Regex Matches: " + regexMatches);
        if(ruleApplies && !regexMatches){
            return Optional.of(new Result(getRecommendation(), getPriority()));
        }else{
            return Optional.empty();
        }
    }
}
