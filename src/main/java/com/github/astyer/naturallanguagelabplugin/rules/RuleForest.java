package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;

import java.util.LinkedList;

public class RuleForest {

    private LinkedList<RuleNode> ruleTree;

    public Result buildForest(Identifier identifier){
        if(identifier instanceof Class){
            return classTree(identifier);
        }
        if(identifier instanceof Method){
            return methTree(identifier);
        }
        if(identifier instanceof Variable){
            return varTree(identifier);
        }
        return null;
    }

    public Result varTree(Identifier identifier){
        RuleNode nmn = new RuleNode("NM*N", new NMN());
        ruleTree.add(nmn);
        for (Checkbox cb: nmn.runCheckboxes()) {
            CheckboxResult r = cb.applyCheckbox(identifier);
            if(!r.getMatched() && !r.getResult()){
                return new Result("Unknown", 0);
            }
            else if(r.getMatched() && !r.getResult()) {
                    return new Result(nmn.getName(), 1);
            } else if(r.getMatched() && r.getResult()){
                RuleNode nmnpl = new RuleNode("NM*NPL", new NMNPL());
                ruleTree.add(nmnpl);
                for (Checkbox box: nmn.runCheckboxes()) {
                    CheckboxResult res = box.applyCheckbox(identifier);
                    if(!res.getMatched() && !res.getResult()){
                        return new Result("NM*N", 1);
                    }
                    else if(res.getMatched() && !res.getResult()) {
                        return new Result(nmnpl.getName(), 1);
                    } else if(res.getMatched() && res.getResult()){
                        return new Result();
                    }
                }
            }
        }
        return new Result("Unknown", 0);
    }

    public Result classTree(Identifier identifier){
        RuleNode nmn = new RuleNode("NM*N", new NMN());
        ruleTree.add(nmn);
        for (Checkbox cb: nmn.runCheckboxes()) {
            CheckboxResult r = cb.applyCheckbox(identifier);
            if(!r.getMatched() && !r.getResult()){
                return new Result("Unknown", 0);
            }
            else if(r.getMatched() && !r.getResult()) {
                return new Result(nmn.getName(), 1);
            } else if(r.getMatched() && r.getResult()){
                RuleNode nmnpl = new RuleNode("NM*NPL", new NMNPL());
                ruleTree.add(nmnpl);
                for (Checkbox box: nmn.runCheckboxes()) {
                    CheckboxResult res = box.applyCheckbox(identifier);
                    if(!res.getMatched() && !res.getResult()){
                        return new Result("NM*N", 1);
                    }
                    else if(res.getMatched() && !res.getResult()) {
                        return new Result(nmnpl.getName(), 1);
                    } else if(res.getMatched() && res.getResult()){
                        return new Result();
                    }
                }
            }
        }
        return new Result("Unknown", 0);
    }

    public Result methTree(Identifier identifier){
        RuleNode v = new RuleNode("V", new V());
        ruleTree.add(v);
        for (Checkbox cb: v.runCheckboxes()) {
            CheckboxResult r = cb.applyCheckbox(identifier);
            if(!r.getMatched() && !r.getResult()){
                return new Result("Unknown", 0);
            }
            else if(r.getMatched() && !r.getResult()) {
                return new Result(v.getName(), 1);
            } else if(r.getMatched() && r.getResult()){
                //RuleNode vnmn = new RuleNode("NM*NPL", new VNMN());           //TODO: Check rule vs ruleVisitor
                //ruleTree.add(vnmn);
                return new Result("VNM*N", 1);
            }
        }
        return new Result("Unknown", 0);
    }
}
