package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.*;
import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.rules.Checkbox;
import com.github.astyer.naturallanguagelabplugin.rules.CheckboxResult;

import java.util.ArrayList;
import java.util.List;
import com.kipust.regex.Pattern;

public class RuleForest {
    static RuleForest instance = null;
    public static RuleForest getInstance(){
        if(instance == null){
            instance = new RuleForest();
        }
        return instance;
    }

    private RuleForest(){
        //create var
        RuleNode nmn = new RuleNode("NM* N", Pattern.Compile("&(*('NM_'),'N_')"));
        RuleNode vnmn = new RuleNode("V NM* N(PL)", Pattern.Compile("&('V_',&(*('NM_'),|('N_','NPL_')))"));//("V (NM )*(N|NPL)"));
        RuleNode nmnpl = new RuleNode("NM* NPL", Pattern.Compile("&(*('NM_'),'NPL_')"));//("(NM )*NPL"));

        nmn.addBranch(new RuleBranch(
                "Type of Bool",
                vnmn,
                new Checkbox(
                        "Type of Bool",
                        null,
                        null,
                        variable -> new CheckboxResult(variable.getType().equals(IRFactory.IRType.TYPE_BOOLEAN))
                )
        ));

        nmn.addBranch(new RuleBranch(
                "Type of Collection",
                nmnpl,
                new Checkbox(
                        "Type of Collection",
                        null,
                        null,
                        variable -> new CheckboxResult(variable.getType().equals(IRFactory.IRType.TYPE_COLLECTION))
                )
        ));
        this.varTree = nmn;

        //class tree
        this.classTree = new RuleNode("NM* (N|NPL)", Pattern.Compile("&(*('NM_'),|('N_','NPL_'))"));//("(NM )*(N|NPL)"));

        //method tree
        RuleNode methodRoot = new RuleNode("empty", Pattern.AcceptAny());
        RuleNode weirdRulePt1 = new RuleNode("V NM* N(PL)|V+ pt1", Pattern.Compile("|(&('V_',&(*('NM_'),|('N_','NPL_'))),&('V_',*('V_')))"));//("(V (NM )*(N|NPL))|V+"));
        RuleNode weirdRulePt2 = new RuleNode("V NM* N(PL)|V+ pt2", Pattern.Compile("|(&('V_',&(*('NM_'),|('N_','NPL_'))),&('V_',*('V_')))"));//("(V (NM )*(N|NPL))|V+"));
        RuleNode pnmn = new RuleNode("P NM* N(PL)", Pattern.Compile("&('P_',&(*('NM_'),|('N_','NPL_'))))"));//("P (NM )*(N|NPL)"));
        RuleNode vp = new RuleNode("V P NM* N", Pattern.Compile("&('V_',&('P_',&(*('NM_'),'N_')))"));//("V P (NM )*N"));

        methodRoot.addBranch(new RuleBranch("No Void/Generics", weirdRulePt1, new Checkbox("No Void/Generics",null, method -> new CheckboxResult(!method.getType().equals(IRFactory.IRType.TYPE_VOID)), null)));
        methodRoot.addBranch(new RuleBranch("Void/Generics", weirdRulePt2, new Checkbox("Void/Generics", null, method -> new CheckboxResult(method.getType().equals(IRFactory.IRType.TYPE_VOID)), null)));
        weirdRulePt1.addBranch(new RuleBranch("Event Driven Code", pnmn, new Checkbox("Event Driven Code", null, method -> new CheckboxResult(method.performsEventDrivenFunctionality()), null)));
        weirdRulePt1.addBranch(new RuleBranch("Code contains Casting", pnmn, new Checkbox("Code contains Casting", null, method -> new CheckboxResult(method.performsConversion()), null)));
        weirdRulePt1.addBranch(new RuleBranch("Loop in body", vp, new Checkbox("Loop in body", null, null, null))); //todo:check for loops
        this.methodTree = methodRoot;

    }
    private RuleNode varTree;
    private RuleNode classTree;
    private RuleNode methodTree;




    public List<NodeResult> runIdentifier(Identifier id){
        if(id instanceof Variable) {
            List<NodeResult> results = varTree.checkIdentifier(id, 0);
            System.out.println(id.getName() + ": " + id.getPOS());
            for (NodeResult nr : results) {
                System.out.println(nr.getRecommendation() + ": " + nr.getDepth() + ": " + nr.isIdentifierMatchesRegex());
            }
            return results;
        }else if(id instanceof Method) {
            List<NodeResult> results = methodTree.checkIdentifier(id, 0);
            return results;
        }else if(id instanceof Class){
            List<NodeResult> results = classTree.checkIdentifier(id, 0);
            return results;
        }else{
            return new ArrayList<>();
        }
    }
}
