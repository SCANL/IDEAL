package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.google.errorprone.annotations.Var;

import java.util.Optional;
import java.util.regex.Pattern;

public class Checkbox {
    ClassCheckbox ccb;
    MethodCheckbox mcb;
    VariableCheckbox vcb;
    String name;

    public Checkbox(String name, ClassCheckbox ccb, MethodCheckbox mcb, VariableCheckbox vcb){
        this.name = name;
        this.ccb = (ccb == null) ? c -> CheckboxResult.empty() : ccb;
        this.mcb = (mcb == null) ? method -> CheckboxResult.empty() : mcb;
        this.vcb = (vcb == null) ? variable -> CheckboxResult.empty() : vcb;
    }

    public CheckboxResult applyCheckbox(Identifier identifier){
        if(identifier instanceof Class){
            return ccb.visitClass((Class) identifier);
        }
        if(identifier instanceof Method){
            return mcb.visitMethod((Method) identifier);
        }
        if(identifier instanceof Variable){
            return vcb.visitVariable((Variable) identifier);
        }
        return null;
    }

//    static Checkbox PosCheckbox(Pattern p){
//        return new Checkbox("Regex matches " + p.pattern(),
//                c -> new CheckboxResult( p.matcher(c.getPOS()).matches()),
//                method -> new CheckboxResult(p.matcher(method.getPOS()).matches()),
//                variable -> new CheckboxResult(p.matcher(variable.getPOS()).matches()));
//    }
}
