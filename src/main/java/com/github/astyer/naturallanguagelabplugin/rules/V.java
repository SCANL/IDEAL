package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Method;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class V extends RuleVisitor{
    private final String regexStr = "V( V)*";
    private final String resultStr = "V+";
    private final int priority = 2;
    private Pattern p;

    public V(){
        p = Pattern.compile(regexStr);
    }

    @Override
    public Optional<Result> visitMethod(Method m) {
        if(m.getType().equals("void")){
            Matcher match = p.matcher(m.getPOS());
            if(!match.matches()){
                return Optional.of(new Result(resultStr, priority));
            }
        }
        return Optional.empty();
    }
}
