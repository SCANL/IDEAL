package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMNPL extends RuleVisitor{
    private final String regexStr = "(NM )*NPL";
    private final String resultStr = "NM* NPL";
    private final int priority = 1;
    Pattern p;
    public NMNPL(){
        p = Pattern.compile(regexStr);
    }
    @Override
    public Optional<Result> visitClass(Class c) {
        if(c.getReturnType().equals("Array")){
            Matcher m = p.matcher(c.getPOS());
            if(!m.matches()){
                return Optional.of(new Result(resultStr, priority));
            }
        }
        return Optional.empty();
    }
}
