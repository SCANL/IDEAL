package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VNMN extends RuleVisitor {
    private final String regexStr = "V ((N|MN) )*(N|NPL)";
    private final String resultStr = "V NM* N";
    private final int priority = 1;
    private Pattern p;
    public VNMN(){
        this.p = Pattern.compile(regexStr);
    }

    @Override
    public Optional<Result> visitVariable(Variable v) {
        if(v.getType().equals("boolean")){
            Matcher m = p.matcher(v.getPOS());
            if(!m.matches()){
                return Optional.of(new Result(resultStr, priority));
            }
            else {
                return Optional.empty();
            }
        }
        return null;
    }

    @Override
    public Optional<Result> visitMethod(Method m) {
        if(!m.performsConversion() && !m.performsEventDrivenFunctionality() && !m.getType().equals("void")) {
            Matcher match = p.matcher(m.getPOS());
            if (!match.matches()) {
                return Optional.of(new Result(resultStr, priority));
            }
            else {
                return Optional.empty();
            }
        }
        return null;
    }
}
