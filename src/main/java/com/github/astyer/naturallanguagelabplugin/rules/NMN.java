package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Variable;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMN extends RuleVisitor {
    private final String regexStr = "(NM )*N";
    private final String resultStr = "NM* N";
    private final int priority = 1;
    Pattern p;

    public NMN(){
        p = Pattern.compile(regexStr);
    }

    @Override
    public Optional<Result> visitVariable(Variable v) {
        if(!v.getType().equals("Array") && !v.getType().equals("Boolean")){
            Matcher m = p.matcher(v.getPOS());
            if(!m.matches()){
                return Optional.of(new Result(resultStr, priority));
            }
        }
        return Optional.empty();
    }
}
