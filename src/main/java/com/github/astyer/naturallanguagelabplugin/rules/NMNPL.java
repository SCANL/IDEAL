package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMNPL extends RuleVisitor{
    private final String regexStr = "((N|NM) )*NPL";
    private final String resultStr = "NM* NPL";
    private final int priority = 1;
    Pattern p;

    public NMNPL(){
        p = Pattern.compile(regexStr);
    }

    @Override
    public Optional<Result> visitVariable(Variable v) {
        if(v.getType().equals("Array")){
            Matcher m = p.matcher(v.getPOS());
            if(!m.matches()){
                return Optional.of(new Result(resultStr, priority));
            }
        }
        return Optional.empty();
    }
}
